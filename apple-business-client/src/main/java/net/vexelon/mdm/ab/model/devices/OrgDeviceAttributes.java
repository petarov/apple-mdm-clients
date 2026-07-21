package net.vexelon.mdm.ab.model.devices;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * Attributes that describe an organization device resource.
 *
 * <p>All date-time fields use ISO 8601 format (e.g. {@code 2025-04-30T22:05:14.192Z}).
 * All nullable fields default to empty string or empty list when absent or {@code null} in the response.
 *
 * @param serialNumber            the device's serial number
 * @param addedToOrgDateTime      date/time the device was added to the organization
 * @param releasedFromOrgDateTime date/time the device was released from the organization; empty if
 *                                not released. Only single-device queries are supported for this property
 * @param releaserId              identifier of the entity that released the device
 * @param releaserEntityType      type of the entity that released the device
 * @param updatedDateTime         date/time of the most-recent update for the device
 * @param deviceModel             the model name
 * @param productFamily           Apple product family: iPhone, iPad, Mac, AppleTV, Watch, or Vision
 * @param productType             product type (e.g. iPhone14,3, iPad13,4, MacBookPro14,2)
 * @param deviceCapacity          the capacity of the device
 * @param partNumber              the part number of the device
 * @param orderNumber             the order number of the device
 * @param color                   the color of the device
 * @param status                  device status: defaults to {@link Status#UNKNOWN} when absent or unrecognized
 * @param orderDateTime           date/time the device's order was placed
 * @param imei                    the device's IMEI(s), if available
 * @param meid                    the device's MEID(s), if available
 * @param eid                     the device's EID, if available
 * @param purchaseSourceId        unique ID of the purchase source (Apple Customer Number or Reseller Number)
 * @param purchaseSourceType      purchase source type; defaults to {@link PurchaseSourceType#UNKNOWN} when absent or
 *                                unrecognized
 * @param wifiMacAddress          the device's Wi-Fi MAC address
 * @param bluetoothMacAddress     the device's Bluetooth MAC address
 * @param ethernetMacAddress      the device's built-in Ethernet MAC address(es)
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/orgdevice/attributes-data.dictionary">OrgDevice.Attributes</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrgDeviceAttributes(@JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String addedToOrgDateTime,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String releasedFromOrgDateTime,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String releaserId,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String releaserEntityType,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String updatedDateTime,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceModel,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String productFamily,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String productType,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceCapacity,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String partNumber,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String orderNumber,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String color,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) Status status,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String orderDateTime,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> imei,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> meid,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String eid,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String purchaseSourceId,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) PurchaseSourceType purchaseSourceType,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String wifiMacAddress,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String bluetoothMacAddress,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> ethernetMacAddress) {

	/**
	 * The devices status for an organization device.
	 */
	enum Status {
		@JsonEnumDefaultValue UNKNOWN,
		APPLE,
		ASSIGNED,
		UNASSIGNED
	}

	/**
	 * Purchase source type for an organization device.
	 */
	enum PurchaseSourceType {
		@JsonEnumDefaultValue UNKNOWN,
		APPLE,
		RESELLER,
		MANUALLY_ADDED
	}
}
