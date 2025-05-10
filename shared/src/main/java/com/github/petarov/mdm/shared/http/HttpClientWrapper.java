package com.github.petarov.mdm.shared.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petarov.mdm.shared.config.MdmClientBuilder;
import com.github.petarov.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

public class HttpClientWrapper {

	private final MdmClientBuilder.MdmBuilderOptions options;
	private final Logger                             logger;
	private final String                             proxyCredentials;

	private HttpClient   httpClient;
	private ObjectMapper objectMapper;

	public HttpClientWrapper(@Nonnull MdmClientBuilder.MdmBuilderOptions options, @Nonnull Logger logger) {
		this.options = options;
		this.logger = logger;

		if (options.proxyOptions() == null || options.proxyOptions().getUsername().isBlank()) {
			this.proxyCredentials = "";
		} else {
			this.proxyCredentials = "Basic " + Base64.getEncoder().encodeToString(
					(options.proxyOptions().getUsername() + ":" + options.proxyOptions().getPassword()).getBytes(
							StandardCharsets.UTF_8));
		}
	}

	@Nonnull
	public MdmClientBuilder.MdmBuilderOptions getOptions() {
		return options;
	}

	public boolean isResponseOk(int code) {
		return code / 100 == 2;
	}

	public boolean isResponseUnauthorized(int code) {
		return code == HttpConsts.STATUS_UNAUTHORIZED;
	}

	private HttpClient.Builder createClientBuilder() {
		var builder = HttpClient.newBuilder();
		builder.followRedirects(HttpClient.Redirect.NORMAL);
		builder.connectTimeout(options.connectTimeout());

		if (options.isSkipSslVerify()) {
			try {
				builder.sslContext(SSLContextUtil.newTrustAllSSLContext(options.random()));
			} catch (Exception e) {
				throw new HttpClientWrapperException("Error creating SSL context", e);
			}
		}

		if (options.proxyOptions() != null) {
			builder.proxy(new ProxyOptionsProxySelectorAdapter(options.proxyOptions()));
		}

		return builder;
	}

	protected HttpClient getClient() {
		if (httpClient == null) {
			httpClient = createClientBuilder().build();
		}
		return httpClient;
	}

	protected ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = JsonUtil.createObjectMapper();
		}
		return objectMapper;
	}

	/**
	 * @param resourceUri junction uri starting with slash, e.g. <i>/session</i>
	 * @return the complete url, i.e., the {@code resourceUri} prefixed with the configured service url
	 */
	@Nonnull
	public URI complementURI(String resourceUri) {
		if (!resourceUri.startsWith("/")) {
			throw new IllegalArgumentException("uri must start with slash");
		}
		return URI.create(options.serviceUrl() + resourceUri);
	}

	@Nonnull
	public HttpRequest.Builder createRequestBuilder(@Nonnull URI uri) {
		var builder = HttpRequest.newBuilder(uri);
		builder.timeout(options.readTimeout());
		builder.setHeader(HttpConsts.HEADER_USER_AGENT, options.userAgent());
		builder.setHeader(HttpConsts.HEADER_ACCEPT_ENCODING, "gzip, deflate");

		if (!proxyCredentials.isBlank()) {
			builder.setHeader(HttpConsts.HEADER_PROXY_AUTHORIZATION, proxyCredentials);
		}

		return builder;
	}

	/**
	 * @param resourceUrl complete url to the resource
	 */
	@Nonnull
	public HttpRequest.Builder createRequestBuilder(String resourceUrl) {
		return createRequestBuilder(URI.create(resourceUrl));
	}

	private String getRequestResponseLine(HttpRequest req, HttpResponse<?> resp) {
		return "%s %s status=%d".formatted(req.method(), req.uri(), resp.statusCode());
	}

	private void debugReqHeaders(HttpRequest req) {
		if (logger.isDebugEnabled()) {
			var sb = new StringBuilder();
			req.headers().map().forEach((k, v) -> sb.append(k).append("=").append(v).append(" "));
			logger.debug("req: {} {} headers: {}", req.method(), req.uri(), sb);
		}
	}

	private void debugRespHeaders(HttpResponse<?> resp, String body) {
		if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
			var sb = new StringBuilder();
			resp.headers().map().forEach((k, v) -> sb.append(k).append("=").append(v).append(" "));

			if (logger.isTraceEnabled()) {
				logger.trace("resp: {} ({}) headers: {} body: {}", resp.uri(), resp.statusCode(), sb,
						body.isBlank() ? "<blank>" : body);
			} else if (logger.isDebugEnabled()) {
				logger.debug("resp: {} ({}) headers: {}", resp.uri(), resp.statusCode(), sb);
			}
		}
	}

	private InputStream decodeResponseBody(HttpResponse<InputStream> response) throws IOException {
		return switch (response.headers().firstValue(HttpConsts.HEADER_CONTENT_ENCODING).orElse("")) {
			case "gzip" -> new GZIPInputStream(response.body());
			case "deflate" -> new DeflaterInputStream(response.body());
			default -> Objects.requireNonNullElse(response.body(), InputStream.nullInputStream());
		};
	}

	@Nonnull
	public <T> T send(@Nonnull HttpRequest request, @Nonnull Class<T> clazz) {
		HttpResponse<InputStream> response;
		try {
			debugReqHeaders(request);
			response = getClient().send(request, HttpResponse.BodyHandlers.ofInputStream());
		} catch (IOException e) {
			throw new HttpClientWrapperException("I/O error while sending request", e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new HttpClientWrapperException("Send request interrupted", e);
		}

		try {
			if (isResponseOk(response.statusCode())) {
				try (var input = decodeResponseBody(response)) {
					var body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
					debugRespHeaders(response, body);
					return getObjectMapper().reader().readValue(body, clazz);
				}
			} else {
				debugRespHeaders(response, "");
				try (var input = decodeResponseBody(response)) {
					throw new HttpClientWrapperException(getRequestResponseLine(request, response),
							response.statusCode(), new String(input.readAllBytes(), StandardCharsets.UTF_8),
							response.headers().map().entrySet().stream().collect(
									Collectors.toMap(Map.Entry::getKey, entry -> new ArrayList<>(entry.getValue()))));
				}
			}
		} catch (IOException e) {
			throw new HttpClientWrapperException("Error reading response", e);
		}
	}
}