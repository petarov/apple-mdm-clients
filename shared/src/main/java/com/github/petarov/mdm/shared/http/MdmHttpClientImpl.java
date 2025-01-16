package com.github.petarov.mdm.shared.http;

import com.github.petarov.mdm.shared.config.MdmClientBuilder;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

class MdmHttpClientImpl implements MdmHttpClient {

	private final MdmClientBuilder.MdmBuilderOptions options;
	private       String                             proxyCredentials = "";

	MdmHttpClientImpl(MdmClientBuilder.MdmBuilderOptions options) {
		this.options = options;
		this.proxyCredentials = options.proxyOptions() == null ?
				"" :
				(options.proxyOptions().getUsername().isBlank() ?
						"" :
						"Basic " + Base64.getEncoder().encodeToString(
								(options.proxyOptions().getUsername() + ":" + options.proxyOptions()
										.getPassword()).getBytes(StandardCharsets.UTF_8)));
	}

	public HttpClient.Builder createBuilder() throws Exception {
		var builder = HttpClient.newBuilder();
		builder.followRedirects(HttpClient.Redirect.NORMAL);
		builder.connectTimeout(options.connectTimeout());

		if (options.isSkipSslVerify()) {
			builder.sslContext(SSLContextUtils.newTrustAllSSLContext(new SecureRandom()));
		}

		if (options.proxyOptions() != null) {
			builder.proxy(new ProxyOptionsProxySelectorAdapter(options.proxyOptions()));

			if (!options.proxyOptions().getUsername().isBlank()) {
				builder.authenticator(new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(options.proxyOptions().getUsername(),
								options.proxyOptions().getPassword().toCharArray());
					}
				});
			}
		}

		return builder;
	}

	public HttpRequest.Builder createRequestBuilder(String url) {
		var builder = HttpRequest.newBuilder(URI.create(url));
		builder.setHeader("user-agent", options.userAgent());
		builder.setHeader("accept-encoding", "gzip, deflate");

		if (!proxyCredentials.isBlank()) {
			builder.setHeader("proxy-authorization", proxyCredentials);
		}

		return builder;
	}

	@Nonnull
	protected InputStream getResponseBody(@Nonnull HttpResponse<InputStream> response) throws IOException {
		return switch (response.headers().firstValue("content-encoding").orElse("")) {
			case "gzip" -> new GZIPInputStream(response.body());
			case "deflate" -> new DeflaterInputStream(response.body());
			default -> Objects.requireNonNullElse(response.body(), InputStream.nullInputStream());
		};
	}
}