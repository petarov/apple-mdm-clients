package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.da.DeviceAssignmentClient;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

/**
 * @param assetTag                the device's asset tag
 * @param color                   the color of the device
 * @param description             a description of the device
 * @param deviceAssignedBy        the email of the person who assigned the device
 * @param deviceAssignedDate      a time stamp in ISO 8601 format that indicates when the device was assigned to the MDM server
 * @param deviceFamily            the device's Apple product family: defaults to {@link DeviceFamily#UNKNOWN} when absent or
 *                                unrecognized. This key is valid in <i>X-Server-Protocol-Version 2</i> and later.
 * @param model                   the model name
 * @param opDate                  a time stamp in ISO 8601 format that indicates when the device was added, updated, or
 *                                deleted. If the value of {@link #opType()} is added, this is the same as
 *                                {@link #deviceAssignedDate()}. This field is only applicable with the
 *                                <i>Sync the List of Devices</i> call - {@link DeviceAssignmentClient#syncDevices(String, int)}.
 * @param opType                  indicates whether the device was added (assigned to the MDM server), modified, or deleted:
 *                                defaults to {@link OpType#UNKNOWN} when absent or unrecognized. This field is only
 *                                applicable with the <i>Sync the List of Devices</i> call -
 *                                {@link DeviceAssignmentClient#syncDevices(String, int)}.
 * @param os                      the device's operating system: defaults to {@link DeviceOs#UNKNOWN} when absent or unrecognized.
 *                                This key is valid in <i>X-Server-Protocol-Version 2</i> and later.
 * @param profileAssignTime       a time stamp in ISO 8601 format that indicates when a profile was assigned to the device
 * @param profilePushTime         a time stamp in ISO 8601 format that indicates when a profile was pushed to the device
 * @param profileStatus           the status of profile installation: defaults to {@link ProfileStatus#UNKNOWN} when absent
 *                                or unrecognized.
 * @param profileUuid             the unique ID of the assigned profile
 * @param serialNumber            the device's serial number
 * @param responseStatus          a string indicating whether a particular device's data could be retrieved, either
 *                                {@code SUCCESS}, {@code NOT_ACCESSIBLE} or {@code FAILED}. Available after calling
 *                                {@link DeviceAssignmentClient#fetchDeviceDetails(Set)}.
 * @param mdmMigrationDeadline    a time stamp in ISO 8601 format that indicates the MDM migration deadline.
 *                                This key is valid with <i>X-Server-Protocol-Version 8</i> and later.
 * @param eid                     the Embedded Identity Document (EID), sometimes known as the CSN, that uniquely identifies
 *                                the eSIM chip built into the device. This key is valid in
 *                                <i>X-Server-Protocol-Version 10</i> and later.
 * @param ethernetMacAddress      the device's Ethernet MAC address.
 *                                This key is valid in <i>X-Server-Protocol-Version 10</i> and later.
 * @param wifiMacAddress          the device's Wi-Fi MAC address.
 *                                This key is valid in <i>X-Server-Protocol-Version 10</i> and later.
 * @param bluetoothMacAddress     the device's Bluetooth MAC address.
 *                                This key is valid in <i>X-Server-Protocol-Version 10</i> and later.
 * @param imei                    the International Mobile Equipment Identity (IMEI) numbers that identify the device.
 *                                This key is valid in <i>X-Server-Protocol-Version 10</i> and later.
 * @param meid                    the Mobile Equipment Identifier (MEID) numbers that identify CDMA-based mobile devices.
 *                                This key is valid in <i>X-Server-Protocol-Version 10</i> and later.
 * @param isReplacementDevice     if {@code true}, the device is a replacement for another device.
 *                                This key is valid in <i>X-Server-Protocol-Version 10</i> and later.
 * @param isReleasedByReplacement if {@code true}, the device was released from the MDM server because it was replaced by
 *                                another device. Only present with <i>Sync the List of Devices</i> when {@link #opType()}
 *                                is {@link OpType#DELETED}.
 *                                This key is valid in <i>X-Server-Protocol-Version 10</i> and later.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Device(@JsonSetter(nulls = Nulls.AS_EMPTY) String assetTag,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String color,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String description,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceAssignedBy,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceAssignedDate,
                     @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) DeviceFamily deviceFamily,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String model,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String opDate,
                     @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) OpType opType,
                     @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) DeviceOs os,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String profileAssignTime,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String profilePushTime,
                     @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) ProfileStatus profileStatus,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String profileUuid,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String responseStatus,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String mdmMigrationDeadline,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String eid,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String ethernetMacAddress,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String wifiMacAddress,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String bluetoothMacAddress,
                     @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> imei,
                     @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> meid, boolean isReplacementDevice,
                     @JsonProperty("released_by_replacement") boolean isReleasedByReplacement) {

	/**
	 * @return an empty {@link Device} with empty field values
	 */
	@Nonnull
	public static Device ofEmpty() {
		return new Device("", "", "", "", "", DeviceFamily.UNKNOWN, "", "", OpType.UNKNOWN, DeviceOs.UNKNOWN, "", "",
				ProfileStatus.UNKNOWN, "", "", "", "", "", "", "", "", List.of(), List.of(), false, false);
	}

	/**
	 * @return parsed {@link #deviceAssignedDate()} or {@link OffsetDateTime#MIN} if no date-time is available
	 */
	@Nonnull
	public OffsetDateTime deviceAssignedDateTime() {
		return deviceAssignedDate.isBlank() ? OffsetDateTime.MIN : OffsetDateTime.parse(deviceAssignedDate);
	}

	/**
	 * @return parsed {@link #opDate()} or {@link OffsetDateTime#MIN} if no date-time is available
	 */
	@Nonnull
	public OffsetDateTime opDateTime() {
		return opDate.isBlank() ? OffsetDateTime.MIN : OffsetDateTime.parse(opDate);
	}

	/**
	 * @return parsed {@link #profileAssignTime()} or {@link OffsetDateTime#MIN} if no date-time is available
	 */
	@Nonnull
	public OffsetDateTime profileAssignDateTime() {
		return profileAssignTime.isBlank() ? OffsetDateTime.MIN : OffsetDateTime.parse(profileAssignTime);
	}

	/**
	 * @return parsed {@link #profilePushTime()} or {@link OffsetDateTime#MIN} if no date-time is available
	 */
	@Nonnull
	public OffsetDateTime profilePushDateTime() {
		return profilePushTime.isBlank() ? OffsetDateTime.MIN : OffsetDateTime.parse(profilePushTime);
	}

	/**
	 * @return parsed {@link #mdmMigrationDeadline()} or {@link OffsetDateTime#MIN} if no date-time is available
	 */
	@Nonnull
	public OffsetDateTime mdmMigrationDeadlineDateTime() {
		return mdmMigrationDeadline.isBlank() ? OffsetDateTime.MIN : OffsetDateTime.parse(mdmMigrationDeadline);
	}

	/**
	 * The device's Apple product family.
	 */
	public enum DeviceFamily {
		@JsonEnumDefaultValue UNKNOWN,
		IPAD,
		IPHONE,
		IPOD,
		MAC,
		APPLETV,
		VISION
	}

	/**
	 * The device's operating system.
	 */
	public enum DeviceOs {
		@JsonEnumDefaultValue UNKNOWN,
		IOS,
		IPADOS,
		OSX,
		TVOS,
		VISIONOS
	}

	/**
	 * Indicates whether the device was added, modified, or deleted during a sync.
	 */
	public enum OpType {
		@JsonEnumDefaultValue UNKNOWN,
		ADDED,
		MODIFIED,
		DELETED
	}

	/**
	 * The status of profile installation on a device.
	 */
	public enum ProfileStatus {
		@JsonEnumDefaultValue UNKNOWN,
		EMPTY,
		ASSIGNED,
		PUSHED,
		REMOVED
	}
}
