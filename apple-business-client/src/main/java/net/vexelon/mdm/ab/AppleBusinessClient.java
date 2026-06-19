package net.vexelon.mdm.ab;

import jakarta.annotation.Nonnull;
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
	 * Returns a paginated list of devices enrolled in the organization via Automated Device Enrollment.
	 *
	 * @param fields sparse field set - when non-empty only the listed attribute names are returned;
	 *               pass an empty list to receive all fields
	 * @param limit  maximum number of devices per page (1–1000); pass {@code 0} to use the server default
	 * @return the response containing the device list and paging metadata
	 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/get-org-devices">Get Organization Devices</a>
	 */
	@Nonnull
	OrgDevicesResponse fetchOrgDevices(@Nonnull List<String> fields, int limit);
}
