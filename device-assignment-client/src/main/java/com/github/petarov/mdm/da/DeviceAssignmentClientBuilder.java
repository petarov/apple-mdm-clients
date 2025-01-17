package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.config.DeviceAssignmentServerToken;
import com.github.petarov.mdm.shared.config.MdmClientBuilder;
import jakarta.annotation.Nonnull;

public class DeviceAssignmentClientBuilder
		extends MdmClientBuilder<DeviceAssignmentClientBuilder, DeviceAssignmentClient> {

	private final String DEFAULT_APPLE_PRODUCTION_HOST = "https://mdmenrollment.apple.com";
	private final String DEFAULT_USER_AGENT            = "apple-mdm-clients-v1";

	private String                      appleHost;
	private String                      userAgent;
	private DeviceAssignmentServerToken serverToken;

	/**
	 * Sets an Apple host other than the default {@link DeviceAssignmentClientBuilder#DEFAULT_APPLE_PRODUCTION_HOST}.
	 */
	public DeviceAssignmentClientBuilder setAppleHost(String appleHost) {
		this.appleHost = appleHost;
		return this;
	}

	/**
	 * Sets the user-agent to use when sending requests to Apple's servers. Setting a user-agent is recommended.
	 * <p>
	 * Default is {@link DeviceAssignmentClientBuilder#DEFAULT_USER_AGENT}.
	 * </p>
	 */
	@Override
	public DeviceAssignmentClientBuilder setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	/**
	 * Sets the server token issued by Apple Business Manager for an MDM server.
	 */
	public DeviceAssignmentClientBuilder setServerToken(@Nonnull DeviceAssignmentServerToken serverToken) {
		this.serverToken = serverToken;
		return this;
	}

	@Nonnull
	@Override
	public DeviceAssignmentClient build() {
		return null;
	}
}