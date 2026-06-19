package net.vexelon.mdm.ab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.response.device.OrgDeviceResponse;
import net.vexelon.mdm.ab.model.response.device.OrgDevicesResponse;

import java.util.List;

/**
 * Client interface for the Apple Business Manager API.
 * <p>
 * Automates device management activities, retrieves device information, and manages users and user
 * groups in Apple Business Manager. Authenticates using OAuth 2.0 client credentials with a JWT
 * client assertion signed with an EC private key (ES256).
 * <p>
 * The underlying implementation is not thread-safe. Concurrent access from multiple threads requires
 * external synchronization.
 *
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi">Apple Business API</a>
 */
public interface AppleBusinessClient {

	/**
	 * Creates a new builder for configuring and constructing an {@link AppleBusinessClient}.
	 *
	 * @return a new {@link AppleBusinessClientBuilder} instance
	 */
	@Nonnull
	static AppleBusinessClientBuilder newBuilder() {
		return new AppleBusinessClientBuilder();
	}

	/**
	 * Fetches all organization devices using the server default field set and page size.
	 *
	 * @return response that contains a list of organization device resources
	 * @see #fetchOrgDevices(List, int)
	 */
	@Nonnull
	default OrgDevicesResponse fetchOrgDevices() {
		return fetchOrgDevices(List.of(), 0);
	}

	/**
	 * Fetches a list of devices in an organization that enroll using Automated Device Enrollment.
	 *
	 * @param fields the fields to return for included related types; pass an empty list to receive all fields
	 * @param limit  maximum number of devices per page (1–1000); pass {@code 0} to use the server default
	 * @return response that contains a list of organization device resources
	 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/get-org-devices">Get Organization Devices</a>
	 */
	@Nonnull
	OrgDevicesResponse fetchOrgDevices(@Nonnull List<String> fields, int limit);

	/**
	 * Fetches information about a single device using the server default field set.
	 *
	 * @param id the unique identifier of the device
	 * @return response that contains a single organization device resource
	 * @see #fetchOrgDevice(String, List)
	 */
	@Nonnull
	default OrgDeviceResponse fetchOrgDevice(@Nonnull String id) {
		return fetchOrgDevice(id, List.of());
	}

	/**
	 * Fetches information about a single device in an organization.
	 *
	 * @param id     the unique identifier of the device
	 * @param fields the fields to return for included related types; pass an empty list to receive all fields
	 * @return response that contains a single organization device resource
	 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/get-orgdevice-information">Get Device Information</a>
	 */
	@Nonnull
	OrgDeviceResponse fetchOrgDevice(@Nonnull String id, @Nonnull List<String> fields);
}
