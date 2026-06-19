package net.vexelon.mdm.ab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.response.device.OrgDeviceResponse;
import net.vexelon.mdm.ab.model.response.device.OrgDevicesResponse;

import java.util.List;

public interface AppleBusinessClient {

	/**
	 * @return new {@link AppleBusinessClientBuilder} instance
	 */
	@Nonnull
	static AppleBusinessClientBuilder newBuilder() {
		return new AppleBusinessClientBuilder();
	}

	/**
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
