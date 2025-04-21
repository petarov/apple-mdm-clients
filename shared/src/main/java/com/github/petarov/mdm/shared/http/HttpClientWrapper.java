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

	private HttpClient   _client;
	private ObjectMapper _objectMapper;

	public HttpClientWrapper(@Nonnull MdmClientBuilder.MdmBuilderOptions options, @Nonnull Logger logger) {
		this.options = options;
		this.logger = logger;
		this.proxyCredentials = options.proxyOptions() == null ?
				"" :
				(options.proxyOptions().getUsername().isBlank() ?
						"" :
						"Basic " + Base64.getEncoder().encodeToString(
								(options.proxyOptions().getUsername() + ":" + options.proxyOptions()
										.getPassword()).getBytes(StandardCharsets.UTF_8)));
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
				throw new RuntimeException("Error creating SSL context", e);
			}
		}

		if (options.proxyOptions() != null) {
			builder.proxy(new ProxyOptionsProxySelectorAdapter(options.proxyOptions()));

			//			if (!options.proxyOptions().getUsername().isBlank()) {
			//				builder.authenticator(new Authenticator() {
			//
			//					@Override
			//					protected PasswordAuthentication getPasswordAuthentication() {
			//						return new PasswordAuthentication(options.proxyOptions().getUsername(),
			//								options.proxyOptions().getPassword().toCharArray());
			//					}
			//				});
			//			}
		}

		return builder;
	}

	protected HttpClient getClient() {
		if (_client == null) {
			_client = createClientBuilder().build();
		}
		return _client;
	}

	protected ObjectMapper getObjectMapper() {
		if (_objectMapper == null) {
			_objectMapper = JsonUtil.createObjectMapper();
		}
		return _objectMapper;
	}

	/**
	 * @param uri junction uri starting with slash, e.g. <i>/session</i>
	 */
	public URI createURI(String uri) {
		if (!uri.startsWith("/")) {
			throw new IllegalArgumentException("uri must start with slash");
		}
		return URI.create(options.serviceUrl() + uri);
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

	private String getRequestResponseLine(HttpRequest req, HttpResponse<?> resp) {
		return "%s %s status=%d".formatted(req.method(), req.uri(), resp.statusCode());
	}

	private void debugReqHeaders(HttpRequest req) {
		var sb = new StringBuilder();
		req.headers().map().forEach((k, v) -> sb.append(k).append("=").append(v).append(" "));
		logger.debug("req: {} {} headers: {}", req.method(), req.uri(), sb);
	}

	private void debugRespHeaders(HttpResponse<?> resp, String body) {
		var sb = new StringBuilder();
		resp.headers().map().forEach((k, v) -> sb.append(k).append("=").append(v).append(" "));

		if (logger.isTraceEnabled()) {
			logger.trace("resp: {} ({}) headers: {} body: {}", resp.uri(), resp.statusCode(), sb,
					body.isBlank() ? "<blank>" : body);
		} else if (logger.isDebugEnabled()) {
			logger.debug("resp: {} ({}) headers: {}", resp.uri(), resp.statusCode(), sb);
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
	public <RESPONSE> RESPONSE send(@Nonnull HttpRequest request, @Nonnull Class<RESPONSE> clazz) {
		HttpResponse<InputStream> response;
		try {
			if (logger.isDebugEnabled()) {
				debugReqHeaders(request);
			}
			response = getClient().send(request, HttpResponse.BodyHandlers.ofInputStream());
		} catch (IOException e) {
			throw new RuntimeException("I/O error while sending request", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("Send request interrupted", e);
		}

		try {
			if (isResponseOk(response.statusCode())) {
				try (var input = decodeResponseBody(response)) {
					var body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
					if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
						debugRespHeaders(response, body);
					}
					return getObjectMapper().reader().readValue(body, clazz);
				}
			} else {
				if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
					debugRespHeaders(response, "");
				}
				try (var input = decodeResponseBody(response)) {
					throw new HttpClientWrapperException(getRequestResponseLine(request, response),
							response.statusCode(), new String(input.readAllBytes(), StandardCharsets.UTF_8),
							response.headers().map().entrySet().stream().collect(
									Collectors.toMap(Map.Entry::getKey, entry -> new ArrayList<>(entry.getValue()))));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Error reading response", e);
		}
	}
}