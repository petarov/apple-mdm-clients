package net.vexelon.mdm.ab.model.devices;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * The set of fields that may be requested via the {@code fields[mdmDeviceDetails]} query parameter
 * when calling the Apple Business Manager MDM device details endpoint.
 *
 * @see MdmDeviceDetailAttributes
 */
public enum MdmDeviceDetailField {
	SERIAL_NUMBER("serialNumber"),
	DEVICE_NAME("deviceName"),
	DEVICE_MODEL("deviceModel"),
	OS_VERSION("osVersion"),
	PLATFORM("platform"),
	IMEI("imei"),
	MEID("meid"),
	WIFI_MAC_ADDRESS("wifiMacAddress"),
	BLUETOOTH_MAC_ADDRESS("bluetoothMacAddress"),
	ETHERNET_MAC_ADDRESS("ethernetMacAddress"),
	LAST_CHECK_IN_DATE_TIME("lastCheckInDateTime"),
	IS_FIREWALL_ENABLED("isFirewallEnabled"),
	IS_FILE_VAULT_ENABLED("isFileVaultEnabled"),
	STORAGE_FREE_CAPACITY("storageFreeCapacity"),
	STORAGE_TOTAL_CAPACITY("storageTotalCapacity"),
	DEVICE_LOCK_STATUS("deviceLockStatus"),
	DEVICE_ERASE_STATUS("deviceEraseStatus"),
	LOST_MODE_STATUS("lostModeStatus"),
	;

	private final String fieldName;

	MdmDeviceDetailField(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return camelCase field name as sent in the {@code fields[mdmDeviceDetails]} query parameter
	 */
	@Nonnull
	public String fieldName() {
		return fieldName;
	}

	/**
	 * Returns an {@link EnumSet} of zero or more specified {@code fields}.
	 *
	 * @return mutable {@link EnumSet} in declaration order; empty when no arguments are given
	 */
	@Nonnull
	public static EnumSet<MdmDeviceDetailField> of(@Nonnull MdmDeviceDetailField... fields) {
		var result = EnumSet.noneOf(MdmDeviceDetailField.class);
		result.addAll(Arrays.asList(fields));
		return result;
	}
}
