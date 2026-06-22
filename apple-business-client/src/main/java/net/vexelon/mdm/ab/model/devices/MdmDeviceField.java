package net.vexelon.mdm.ab.model.devices;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * The set of fields that may be requested via the {@code fields[mdmDevices]} query parameter when
 * calling the Apple Business Manager MDM Devices endpoint.
 * <p>
 * <b>Known limitation:</b> {@code details} is a valid API field but is not modeled in
 * {@link MdmDeviceAttributes}; the server returns relationship data for this field, which is silently
 * ignored during deserialization.
 *
 * @see MdmDeviceAttributes
 */
public enum MdmDeviceField {
	SERIAL_NUMBER("serialNumber"),
	DEVICE_NAME("deviceName"),
	PRODUCT_FAMILY("productFamily"),
	ENROLLED_USER_ID("enrolledUserId"),
	;

	private final String fieldName;

	MdmDeviceField(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return camelCase field name as sent in the {@code fields[mdmDevices]} query parameter
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
	public static EnumSet<MdmDeviceField> of(@Nonnull MdmDeviceField... fields) {
		var result = EnumSet.noneOf(MdmDeviceField.class);
		result.addAll(Arrays.asList(fields));
		return result;
	}
}
