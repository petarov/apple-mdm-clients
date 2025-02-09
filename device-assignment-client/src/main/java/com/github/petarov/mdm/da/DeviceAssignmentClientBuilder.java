package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.config.DeviceAssignmentServerToken;
import com.github.petarov.mdm.shared.config.MdmClientBuilder;
import com.github.petarov.mdm.shared.http.MdmHttpClient;
import jakarta.annotation.Nonnull;

import java.time.Duration;
import java.util.Objects;

public class DeviceAssignmentClientBuilder
		extends MdmClientBuilder<DeviceAssignmentClientBuilder, DeviceAssignmentClient> {

	private final String   DEFAULT_APPLE_PRODUCTION_HOST = "https://mdmenrollment.apple.com";
	private final String   DEFAULT_USER_AGENT            = "apple-mdm-clients-v1";
	private final Duration DEFAULT_CONNECT_TIMEOUT       = Duration.ofSeconds(10);
	private final Duration DEFAULT_READ_TIMEOUT          = Duration.ofSeconds(20);

	private String                      appleHost;
	private DeviceAssignmentServerToken serverToken;

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

		return new DeviceAssignmentClient(new MdmHttpClient(
				new MdmBuilderOptions(serviceUrl, userAgent, skipSslVerify, connectTimeout, readTimeout, proxyOptions)),
				serverToken);
	}
}