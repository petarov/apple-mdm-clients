package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.config.DeviceAssignmentServerToken;
import com.github.petarov.mdm.shared.config.MdmClientBuilder;
import com.github.petarov.mdm.shared.http.MdmHttpClient;
import jakarta.annotation.Nonnull;

public class DeviceAssignmentClientBuilder
		extends MdmClientBuilder<DeviceAssignmentClientBuilder, DeviceAssignmentClient> {

	private final String DEFAULT_APPLE_PRODUCTION_HOST = "https://mdmenrollment.apple.com";
	private final String DEFAULT_USER_AGENT            = "apple-mdm-clients-v1";

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
		return new DeviceAssignmentClient(new MdmHttpClient(
				new MdmBuilderOptions(serviceUrl, userAgent, skipSslVerify, connectTimeout, readTimeout, proxyOptions)),
				serverToken);
	}
}