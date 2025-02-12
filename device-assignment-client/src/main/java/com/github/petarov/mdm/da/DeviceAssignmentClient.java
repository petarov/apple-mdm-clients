package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.model.AccountDetail;
import com.github.petarov.mdm.da.model.Device;
import com.github.petarov.mdm.da.model.DeviceListResponse;
import com.github.petarov.mdm.da.model.DevicesResponse;
import jakarta.annotation.Nonnull;

import java.util.Set;

/**
 * Device assignment client interface that abstracts the implementation.
 * <p>
 * The underlying implementation is not thread-safe. If multiple threads access the client concurrently, it <i>must</i>
 * be synchronized externally. It is recommended to use a new client instance per thread.
 */
public interface DeviceAssignmentClient {

	/**
	 * Obtain the details for your account.
	 * <p>
	 * Each Mobile Device Management server must be registered with Apple. This endpoint provides details about the
	 * server entity to identify it uniquely throughout your organization. Each server is identifiable by either its
	 * system-generated unique identifier or by a user-provided name assigned by one of the organization’s users.
	 * Both the identifier and server name must be unique within your organization.
	 * </p>
	 *
	 * @return {@link AccountDetail} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/account-detail">Get Account Detail</a>
	 */
	@Nonnull
	AccountDetail fetchAccount();

	/**
	 * Fetches a list of all devices that are assigned to this MDM server at the time of the request.
	 *
	 * @param cursor optional hex string that represents the starting position for a request. Use this to retrieve
	 *               the list of devices that have been added or removed since a previous request. The string can be up
	 *               to {@code 1000} characters. On the initial request, this should be omitted.
	 * @param limit  optional maximum number of entries to return. Default is {@code 100} and max is {@code 1000}.
	 * @return {@link DevicesResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/fetch-devices">Get a List of Devices</a>
	 */
	@Nonnull
	DevicesResponse fetchDevices(String cursor, int limit);

	/**
	 * @see #fetchDevices(String, int)
	 */
	@Nonnull
	default DevicesResponse fetchDevices() {
		return fetchDevices("", 100);
	}

	/**
	 * @param serialNumbers the serial numbers of the devices that will be fetched
	 * @return {@link DeviceListResponse} object with {@link Device#responseStatus()} with one of the following set:
	 * <ul>
	 *     <li>{@code SUCCESS} - Device was successfully disowned.</li>
	 *     <li>{@code NOT_ACCESSIBLE} - A device with the specified ID was not accessible by this MDM server.</li>
	 *     <li>{@code FAILED} - Disowning the device failed for an unexpected reason. If three retries fail, the user
	 *     should contact Apple support.</li>
	 * </ul>
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/device-details">Get Device Details</a>
	 */
	@Nonnull
	DeviceListResponse fetchDeviceDetails(@Nonnull Set<String> serialNumbers);

	/**
	 * Fetch updates about the list of devices the server manages.
	 * <p>
	 * The sync service depends on a cursor returned by the fetch device service. It returns a list of all modifications
	 * (additions or deletions) since the specified cursor. The cursor passed to this endpoint <i>should not</i> be
	 * older than 7 days.
	 * <p>
	 * This service may return the same device more than once. You must resolve duplicates by matching on the device
	 * serial number and the {@code op_type} and {@code op_date} fields. The record with the latest {@code op_date}
	 * indicates the last known state of the device in DEP.
	 *
	 * @param cursor optional hex string that represents the starting position for a request. Use this to retrieve
	 *               the list of devices that have been added or removed since a previous request. The string can be up
	 *               to {@code 1000} characters. On the initial request, this should be omitted.
	 * @param limit  optional maximum number of entries to return. Default is {@code 100} and max is {@code 1000}.
	 * @return {@link DevicesResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/sync-devices">Sync the List of Devices</a>
	 */
	@Nonnull
	DevicesResponse syncDevices(String cursor, int limit);

	/**
	 * @see #syncDevices(String, int)
	 */
	@Nonnull
	default DevicesResponse syncDevices(String cursor) {
		return syncDevices(cursor, 100);
	}
}
