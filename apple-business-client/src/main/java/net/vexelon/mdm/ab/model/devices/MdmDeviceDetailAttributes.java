package net.vexelon.mdm.ab.model.devices;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * Attributes that describe a detailed MDM device resource.
 *
 * <ul>
 *     <li>All date-time fields use ISO 8601 format (e.g. {@code 2025-06-01T12:00:00Z}).
 *     <li>All nullable string fields default to empty string when absent or {@code null} in the response.
 *     <li>All nullable list fields default to an empty list when absent or {@code null} in the response.
 *
 * @param serialNumber         the device's serial number
 * @param deviceName           the device name assigned by the user
 * @param deviceModel          the device model name
 * @param osVersion            the operating system version
 * @param platform             the device platform (e.g. {@code macOS}, {@code iOS})
 * @param wifiMacAddress       the device's Wi-Fi MAC address
 * @param bluetoothMacAddress  the device's Bluetooth MAC address
 * @param ethernetMacAddress   the device's built-in Ethernet MAC address
 * @param lastCheckInDateTime  UTC date and time the device last checked in
 * @param imei                 the device's IMEI(s), if available
 * @param meid                 the device's MEID(s), if available
 * @param isFirewallEnabled    {@code true} if the device firewall is enabled
 * @param isFileVaultEnabled   {@code true} if FileVault disk encryption is enabled
 * @param storageFreeCapacity  free storage capacity in bytes
 * @param storageTotalCapacity total storage capacity in bytes
 * @param deviceLockStatus     the device lock status; defaults to {@link DeviceLockStatus#UNKNOWN} when absent
 *                             or unrecognized
 * @param deviceEraseStatus    the device erase status; defaults to {@link DeviceEraseStatus#UNKNOWN} when absent
 *                             or unrecognized
 * @param lostModeStatus       the lost mode status; defaults to {@link LostModeStatus#UNKNOWN} when absent
 *                             or unrecognized
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/mdmdevicedetail/attributes-data.dictionary">MdmDeviceDetail.Attributes</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MdmDeviceDetailAttributes(@JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceName,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceModel,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) String osVersion,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) String platform,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) String wifiMacAddress,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) String bluetoothMacAddress,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) String ethernetMacAddress,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) String lastCheckInDateTime,
                                        @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> imei,
                                        @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> meid,
                                        boolean isFirewallEnabled, boolean isFileVaultEnabled, long storageFreeCapacity,
                                        long storageTotalCapacity,
                                        @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) DeviceLockStatus deviceLockStatus,
                                        @Nonnull @JsonSetter(
		                                        nulls = Nulls.AS_EMPTY) DeviceEraseStatus deviceEraseStatus,
                                        @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) LostModeStatus lostModeStatus) {

	/**
	 * The lock status of an MDM-enrolled device.
	 */
	public enum DeviceLockStatus {
		@JsonEnumDefaultValue UNKNOWN,
		LOCKED,
		UNLOCKED
	}

	/**
	 * The erase status of an MDM-enrolled device.
	 */
	public enum DeviceEraseStatus {
		@JsonEnumDefaultValue UNKNOWN,
		NOT_ERASED,
		ERASED
	}

	/**
	 * The lost mode status of an MDM-enrolled device.
	 */
	public enum LostModeStatus {
		@JsonEnumDefaultValue UNKNOWN,
		ENABLED,
		DISABLED
	}
}
