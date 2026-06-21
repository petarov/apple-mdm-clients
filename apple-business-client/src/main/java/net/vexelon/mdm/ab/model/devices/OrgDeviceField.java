package net.vexelon.mdm.ab.model.devices;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * The set of fields that may be requested via the {@code fields[orgDevices]} query parameter when
 * calling the Apple Business Manager Org Devices endpoints.
 * <p>
 * <b>Known limitation:</b> {@code assignedServer} and {@code appleCareCoverage} are valid API fields but are not yet
 * modeled in {@link OrgDeviceAttributes}; the server returns relationship links for these fields, which are silently
 * ignored during deserialization.
 *
 * @see OrgDeviceAttributes
 */
public enum OrgDeviceField {
	SERIAL_NUMBER("serialNumber"),
	ADDED_TO_ORG_DATE_TIME("addedToOrgDateTime"),
	RELEASED_FROM_ORG_DATE_TIME("releasedFromOrgDateTime"),
	RELEASER_ID("releaserId"),
	RELEASER_ENTITY_TYPE("releaserEntityType"),
	UPDATED_DATE_TIME("updatedDateTime"),
	DEVICE_MODEL("deviceModel"),
	PRODUCT_FAMILY("productFamily"),
	PRODUCT_TYPE("productType"),
	DEVICE_CAPACITY("deviceCapacity"),
	PART_NUMBER("partNumber"),
	ORDER_NUMBER("orderNumber"),
	COLOR("color"),
	STATUS("status"),
	ORDER_DATE_TIME("orderDateTime"),
	IMEI("imei"),
	MEID("meid"),
	EID("eid"),
	PURCHASE_SOURCE_ID("purchaseSourceId"),
	PURCHASE_SOURCE_TYPE("purchaseSourceType"),
	WIFI_MAC_ADDRESS("wifiMacAddress"),
	BLUETOOTH_MAC_ADDRESS("bluetoothMacAddress"),
	ETHERNET_MAC_ADDRESS("ethernetMacAddress"),
	;

	private final String fieldName;

	OrgDeviceField(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return camelCase field name as sent in the {@code fields[orgDevices]} query parameter
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
	public static EnumSet<OrgDeviceField> of(@Nonnull OrgDeviceField... fields) {
		var result = EnumSet.noneOf(OrgDeviceField.class);
		result.addAll(Arrays.asList(fields));
		return result;
	}
}
