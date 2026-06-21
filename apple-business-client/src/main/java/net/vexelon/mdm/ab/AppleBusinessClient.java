package net.vexelon.mdm.ab;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.ab.model.PagingInformation;
import net.vexelon.mdm.ab.model.devices.AppleCareCoverageField;
import net.vexelon.mdm.ab.model.devices.OrgDeviceField;
import net.vexelon.mdm.ab.model.response.device.AppleCareCoverageResponse;
import net.vexelon.mdm.ab.model.response.device.OrgDeviceResponse;
import net.vexelon.mdm.ab.model.response.device.OrgDevicesResponse;

import java.util.EnumSet;

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
	 * Fetches the first page of organization devices using the server default field set and page size.
	 *
	 * @see #fetchOrgDevices(EnumSet, int, String)
	 */
	@Nonnull
	default OrgDevicesResponse fetchOrgDevices() {
		return fetchOrgDevices(OrgDeviceField.of(), 0, "");
	}

	/**
	 * Fetches the first page of organization devices for the given fields and page size.
	 *
	 * @see #fetchOrgDevices(EnumSet, int, String)
	 */
	@Nonnull
	default OrgDevicesResponse fetchOrgDevices(@Nonnull EnumSet<OrgDeviceField> fields, int limit) {
		return fetchOrgDevices(fields, limit, "");
	}

	/**
	 * Fetches a list of devices in an organization that enroll using Automated Device Enrollment.
	 *
	 * @param fields the fields to return for included related types; pass an empty set to receive all fields
	 * @param limit  maximum number of devices per page (1–1000); pass {@code 0} to use the server default
	 * @param cursor the pagination cursor from a previous response's
	 *               {@link PagingInformation.Paging#nextCursor()}; pass {@code ""} or {@code null} for the
	 *               first page
	 * @return response that contains a list of organization device resources
	 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/get-org-devices">Get Organization Devices</a>
	 */
	@Nonnull
	OrgDevicesResponse fetchOrgDevices(@Nonnull EnumSet<OrgDeviceField> fields, int limit, @Nullable String cursor);

	/**
	 * Fetches information about a single device using the server default field set.
	 *
	 * @param id the unique identifier of the device
	 * @return response that contains a single organization device resource
	 * @see #fetchOrgDevice(String, EnumSet)
	 */
	@Nonnull
	default OrgDeviceResponse fetchOrgDevice(@Nonnull String id) {
		return fetchOrgDevice(id, OrgDeviceField.of());
	}

	/**
	 * Fetches information about a single device in an organization.
	 *
	 * @param id     the unique identifier of the device
	 * @param fields the fields to return for included related types; pass an empty set to receive all fields
	 * @return response that contains a single organization device resource
	 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/get-orgdevice-information">Get Device Information</a>
	 */
	@Nonnull
	OrgDeviceResponse fetchOrgDevice(@Nonnull String id, @Nonnull EnumSet<OrgDeviceField> fields);

	/**
	 * Fetches the first page of AppleCare coverage resources for a device using the server default field set and
	 * page size.
	 *
	 * @see #fetchAppleCareCoverage(String, EnumSet, int, String)
	 */
	@Nonnull
	default AppleCareCoverageResponse fetchAppleCareCoverage(@Nonnull String id) {
		return fetchAppleCareCoverage(id, AppleCareCoverageField.of(), 0, "");
	}

	/**
	 * Fetches the first page of AppleCare coverage resources for a device for the given fields and page size.
	 *
	 * @see #fetchAppleCareCoverage(String, EnumSet, int, String)
	 */
	@Nonnull
	default AppleCareCoverageResponse fetchAppleCareCoverage(@Nonnull String id,
			@Nonnull EnumSet<AppleCareCoverageField> fields, int limit) {
		return fetchAppleCareCoverage(id, fields, limit, "");
	}

	/**
	 * Fetches a list of AppleCare coverage resources for an organization device.
	 *
	 * @param id     the unique identifier of the device (e.g. its serial number)
	 * @param fields the fields to return for included related types; pass an empty set to receive all fields
	 * @param limit  maximum number of resources per page (1–1000); pass {@code 0} to use the server default
	 * @param cursor the pagination cursor from a previous response's
	 *               {@link PagingInformation.Paging#nextCursor()}; pass {@code ""} or {@code null} for the
	 *               first page
	 * @return response that contains a list of AppleCare coverage resources for the device
	 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/get-orgdevice-applecarecoverage">Get AppleCare Coverage Information for a Device</a>
	 */
	@Nonnull
	AppleCareCoverageResponse fetchAppleCareCoverage(@Nonnull String id,
			@Nonnull EnumSet<AppleCareCoverageField> fields, int limit, @Nullable String cursor);
}
