package net.vexelon.mdm.da;

import net.vexelon.mdm.shared.config.MdmClientBuilder;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import jakarta.annotation.Nonnull;

import java.security.SecureRandom;
import java.util.Objects;

public class DeviceAssignmentClientBuilder
		extends MdmClientBuilder<DeviceAssignmentClientBuilder, DeviceAssignmentClient> {

	private static final String DEFAULT_APPLE_PRODUCTION_HOST = "https://mdmenrollment.apple.com";

	private String                      appleHost;
	private DeviceAssignmentServerToken serverToken;

	DeviceAssignmentClientBuilder() {
	}

	/**
	 * Sets an Apple host other than the default {@link DeviceAssignmentClientBuilder#DEFAULT_APPLE_PRODUCTION_HOST}.
	 */
	@Nonnull
	public DeviceAssignmentClientBuilder setAppleHost(String appleHost) {
		return this.setServiceUrl(appleHost);
	}

	/**
	 * Sets the server token issued by Apple Business Manager for an MDM server.
	 */
	@Nonnull
	public DeviceAssignmentClientBuilder setServerToken(@Nonnull DeviceAssignmentServerToken serverToken) {
		this.serverToken = serverToken;
		return this;
	}

	@Nonnull
	@Override
	public DeviceAssignmentClient build() {
		setServiceUrl(Objects.requireNonNullElse(serviceUrl, DEFAULT_APPLE_PRODUCTION_HOST));
		setUserAgent(Objects.requireNonNullElse(userAgent, DEFAULT_USER_AGENT));
		setConnectTimeout(Objects.requireNonNullElse(connectTimeout, DEFAULT_CONNECT_TIMEOUT));
		setReadTimeout(Objects.requireNonNullElse(readTimeout, DEFAULT_READ_TIMEOUT));
		setRandom(Objects.requireNonNullElseGet(secureRandom, SecureRandom::new));

		return new DeviceAssignmentClientImpl(new HttpClientWrapper(
				new MdmBuilderOptions(serviceUrl, userAgent, skipSslVerify, connectTimeout, readTimeout, proxyOptions,
						secureRandom), DeviceAssignmentClientImpl.logger), serverToken);
	}
}