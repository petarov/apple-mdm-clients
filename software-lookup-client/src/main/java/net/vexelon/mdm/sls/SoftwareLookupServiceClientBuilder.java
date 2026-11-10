package net.vexelon.mdm.sls;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.config.MdmClientBuilder;
import net.vexelon.mdm.shared.http.HttpClientWrapper;

import java.security.SecureRandom;
import java.util.Objects;

public class SoftwareLookupServiceClientBuilder
		extends MdmClientBuilder<SoftwareLookupServiceClientBuilder, SoftwareLookupServiceClient> {

	private static final String DEFAULT_APPLE_PRODUCTION_HOST = "https://gdmf.apple.com/v2";

	SoftwareLookupServiceClientBuilder() {
	}

	/**
	 * Sets an Apple host other than the default {@link SoftwareLookupServiceClientBuilder#DEFAULT_APPLE_PRODUCTION_HOST}.
	 */
	@Nonnull
	public SoftwareLookupServiceClientBuilder setAppleServiceUrl(String serviceUrl) {
		return this.setServiceUrl(serviceUrl);
	}

	@Nonnull
	@Override
	public SoftwareLookupServiceClient build() {
		setServiceUrl(Objects.requireNonNullElse(serviceUrl, DEFAULT_APPLE_PRODUCTION_HOST));
		setUserAgent(Objects.requireNonNullElse(userAgent, DEFAULT_USER_AGENT));
		setConnectTimeout(Objects.requireNonNullElse(connectTimeout, DEFAULT_CONNECT_TIMEOUT));
		setReadTimeout(Objects.requireNonNullElse(readTimeout, DEFAULT_READ_TIMEOUT));
		setRandom(Objects.requireNonNullElseGet(secureRandom, SecureRandom::new));

		return new SoftwareLookupServiceClientImpl(new HttpClientWrapper(
				new MdmBuilderOptions(serviceUrl, userAgent, skipSslVerify, connectTimeout, readTimeout, proxyOptions,
						secureRandom), SoftwareLookupServiceClientImpl.logger));
	}
}
