package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.config.MdmClientBuilder;
import net.vexelon.mdm.shared.http.HttpClientWrapper;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.Objects;

public class AppAndBookClientBuilder extends MdmClientBuilder<AppAndBookClientBuilder, AppAndBookClient> {

	private static final String DEFAULT_APPLE_PRODUCTION_HOST = "https://vpp.itunes.apple.com/mdm/v2";

	private AppAndBookToken serverToken;
	private Clock           clock;

	AppAndBookClientBuilder() {
	}

	/**
	 * Sets an Apple host other than the default {@link AppAndBookClientBuilder#DEFAULT_APPLE_PRODUCTION_HOST}.
	 * The service configuration is fetched from this URL. Endpoint URLs are then taken from it, falling back to this
	 * URL when an endpoint is missing.
	 */
	@Nonnull
	public AppAndBookClientBuilder setAppleServiceUrl(String serviceUrl) {
		return this.setServiceUrl(serviceUrl);
	}

	/**
	 * Sets the server token issued by Apple Business Manager for an MDM server.
	 * <p>
	 * See <a href="https://developer.apple.com/documentation/devicemanagement/getting-started-with-the-management-api#Authenticate">Authentication</a>.
	 */
	@Nonnull
	public AppAndBookClientBuilder setServerToken(@Nonnull AppAndBookToken serverToken) {
		this.serverToken = serverToken;
		return this;
	}

	@Nonnull
	AppAndBookClientBuilder setClock(@Nonnull Clock clock) {
		this.clock = clock;
		return this;
	}

	@Nonnull
	@Override
	public AppAndBookClient build() {
		setServiceUrl(Objects.requireNonNullElse(serviceUrl, DEFAULT_APPLE_PRODUCTION_HOST));
		setUserAgent(Objects.requireNonNullElse(userAgent, DEFAULT_USER_AGENT));
		setConnectTimeout(Objects.requireNonNullElse(connectTimeout, DEFAULT_CONNECT_TIMEOUT));
		setReadTimeout(Objects.requireNonNullElse(readTimeout, DEFAULT_READ_TIMEOUT));
		setRandom(Objects.requireNonNullElseGet(secureRandom, SecureRandom::new));

		return new AppAndBookClientImpl(new HttpClientWrapper(
				new MdmBuilderOptions(serviceUrl, userAgent, skipSslVerify, connectTimeout, readTimeout, proxyOptions,
						secureRandom), AppAndBookClientImpl.logger), serverToken,
				Objects.requireNonNullElseGet(clock, Clock::systemUTC));
	}
}

