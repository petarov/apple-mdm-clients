package net.vexelon.mdm.ab.model;

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
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String status,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String orderDateTime,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> imei,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> meid,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String eid,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String purchaseSourceUid,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String purchaseSourceType,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String wifiMacAddress,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String bluetoothMacAddress,
                                  @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> ethernetMacAddress) {}
