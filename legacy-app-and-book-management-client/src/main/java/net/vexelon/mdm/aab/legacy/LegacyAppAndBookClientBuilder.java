package net.vexelon.mdm.aab.legacy;

import net.vexelon.mdm.shared.config.MdmClientBuilder;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import jakarta.annotation.Nonnull;

import java.security.SecureRandom;
import java.util.Objects;

public class LegacyAppAndBookClientBuilder
		extends MdmClientBuilder<LegacyAppAndBookClientBuilder, LegacyAppAndBookClient> {

	private static final String DEFAULT_APPLE_PRODUCTION_HOST = "https://vpp.itunes.apple.com/mdm";

	private String                appleHost;
	private LegacyAppAndBookToken serverToken;

	LegacyAppAndBookClientBuilder() {
	}

	/**
	 * Sets an Apple host other than the default {@link LegacyAppAndBookClientBuilder#DEFAULT_APPLE_PRODUCTION_HOST}.
	 */
	@Nonnull
	public LegacyAppAndBookClientBuilder setAppleHost(String appleHost) {
		return this.setServiceUrl(appleHost);
	}

	/**
	 * Sets the server token issued by Apple Business Manager for an MDM server.
	 * <p>
	 * See <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 */
	@Nonnull
	public LegacyAppAndBookClientBuilder setServerToken(@Nonnull LegacyAppAndBookToken serverToken) {
		this.serverToken = serverToken;
		return this;
	}

	@Nonnull
	@Override
	public LegacyAppAndBookClient build() {
		setServiceUrl(Objects.requireNonNullElse(serviceUrl, DEFAULT_APPLE_PRODUCTION_HOST));
		setUserAgent(Objects.requireNonNullElse(userAgent, DEFAULT_USER_AGENT));
		setConnectTimeout(Objects.requireNonNullElse(connectTimeout, DEFAULT_CONNECT_TIMEOUT));
		setReadTimeout(Objects.requireNonNullElse(readTimeout, DEFAULT_READ_TIMEOUT));
		setRandom(Objects.requireNonNullElseGet(secureRandom, SecureRandom::new));

		return new LegacyAppAndBookClientImpl(new HttpClientWrapper(
				new MdmBuilderOptions(serviceUrl, userAgent, skipSslVerify, connectTimeout, readTimeout, proxyOptions,
						secureRandom), LegacyAppAndBookClientImpl.logger), serverToken);
	}
}

