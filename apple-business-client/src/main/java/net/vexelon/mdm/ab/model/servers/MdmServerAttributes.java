package net.vexelon.mdm.ab.model.servers;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.util.ParseUtil;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Attributes that describe a device management service resource.
 * <p>
 * All date-time fields use ISO 8601 format (e.g. {@code 2025-05-01T03:21:44.685Z}).
 * All nullable string/list fields default to empty string or empty list when absent or {@code null} in the response.
 *
 * @param serverName             the device management service's name
 * @param serverType             the type of device management service: defaults to {@link ServerType#UNKNOWN} when
 *                               absent or unrecognized. Read only
 * @param status                 the operational status of the device management service: defaults to
 *                               {@link Status#UNKNOWN} when absent or unrecognized. Read only
 * @param defaultProductFamilies the product families that are assigned by default to this device management
 *                               service. Read/update only
 * @param deviceCount            the number of devices currently assigned to this device management service. Read only
 * @param enableMdmDisownFlag    a boolean value that indicates whether the device management service is allowed to
 *                               disown its enrolled devices
 * @param lastConnectedDateTime  the date and time the device management service last connected to Apple's servers.
 *                               Read only
 * @param lastConnectedIp        the IP address from which the device management service last connected to Apple's
 *                               servers. Read only
 * @param createdDateTime        the date and time of the creation of the resource
 * @param updatedDateTime        the date and time of the most-recent update for the resource
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/mdmserver/attributes-data.dictionary">applebusinessapi/mdmserver/attributes-data.dictionary</a>
 * @since Apple Business API 2.2+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MdmServerAttributes(@JsonSetter(nulls = Nulls.AS_EMPTY) String serverName,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) ServerType serverType,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) Status status, @Nonnull @JsonSetter(
		nulls = Nulls.AS_EMPTY) List<ProductFamily> defaultProductFamilies, int deviceCount,
                                  boolean enableMdmDisownFlag,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String lastConnectedDateTime,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String lastConnectedIp,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String createdDateTime,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String updatedDateTime) {

	/**
	 * @return {@link #lastConnectedDateTime()} parsed to {@link OffsetDateTime}, or {@link OffsetDateTime#MIN} if
	 * absent
	 */
	@Nonnull
	public OffsetDateTime lastConnectedDateTimeOffset() {
		return lastConnectedDateTime.isEmpty() ?
				OffsetDateTime.MIN :
				ParseUtil.parseAppleDateTime(lastConnectedDateTime);
	}

	/**
	 * @return {@link #createdDateTime()} parsed to {@link OffsetDateTime}, or {@link OffsetDateTime#MIN} if absent
	 */
	@Nonnull
	public OffsetDateTime createdDateTimeOffset() {
		return createdDateTime.isEmpty() ? OffsetDateTime.MIN : ParseUtil.parseAppleDateTime(createdDateTime);
	}

	/**
	 * @return {@link #updatedDateTime()} parsed to {@link OffsetDateTime}, or {@link OffsetDateTime#MIN} if absent
	 */
	@Nonnull
	public OffsetDateTime updatedDateTimeOffset() {
		return updatedDateTime.isEmpty() ? OffsetDateTime.MIN : ParseUtil.parseAppleDateTime(updatedDateTime);
	}

	/**
	 * The type of device management service.
	 */
	public enum ServerType {
		@JsonEnumDefaultValue UNKNOWN,
		MDM,
		APPLE_CONFIGURATOR,
		APPLE_MDM
	}

	/**
	 * The operational status of a device management service.
	 */
	public enum Status {
		@JsonEnumDefaultValue UNKNOWN,
		ACTIVE,
		INACTIVE,
		DELETED
	}

	/**
	 * A product family that can be assigned as a default to a device management service.
	 */
	public enum ProductFamily {
		@JsonEnumDefaultValue UNKNOWN,
		APPLE_TV,
		IPAD,
		IPHONE,
		IPOD,
		MAC,
		VISION,
		WATCH
	}
}
