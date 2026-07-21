package net.vexelon.mdm.ab.model.servers;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * The set of fields that may be requested via the {@code fields[mdmServers]} query parameter when
 * calling the Apple Business Manager MDM Servers endpoint.
 * <p>
 * {@code devices} is a valid API field but is not modeled in {@link MdmServerAttributes}; the server returns
 * relationship links for this field instead of populating an attribute. Requesting it may populate the response's
 * {@code included} array rather than being silently ignored.
 *
 * @see MdmServerAttributes
 */
public enum MdmServerField {
	SERVER_NAME("serverName"),
	ENABLE_MDM_DISOWN_FLAG("enableMdmDisownFlag"),
	DEFAULT_PRODUCT_FAMILIES("defaultProductFamilies"),
	STATUS("status"),
	SERVER_TYPE("serverType"),
	DEVICE_COUNT("deviceCount"),
	LAST_CONNECTED_DATE_TIME("lastConnectedDateTime"),
	LAST_CONNECTED_IP("lastConnectedIp"),
	CREATED_DATE_TIME("createdDateTime"),
	UPDATED_DATE_TIME("updatedDateTime"),
	DEVICES("devices"),
	;

	private final String fieldName;

	MdmServerField(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return camelCase field name as sent in the {@code fields[mdmServers]} query parameter
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
	public static EnumSet<MdmServerField> of(@Nonnull MdmServerField... fields) {
		var result = EnumSet.noneOf(MdmServerField.class);
		result.addAll(Arrays.asList(fields));
		return result;
	}
}
