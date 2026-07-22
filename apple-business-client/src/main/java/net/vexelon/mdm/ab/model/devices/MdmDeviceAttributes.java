package net.vexelon.mdm.ab.model.devices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * Attributes for a device enrolled in Apple device management service.
 * <p>
 * All nullable fields default to empty string when absent or {@code null} in the response.
 *
 * @param serialNumber   the serial number of the device
 * @param deviceName     the name of the device
 * @param productFamily  the product family of the device (e.g. iPhone, iPad, Mac)
 * @param enrolledUserId the unique identifier of the user enrolled with the device
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/mdmdevice/attributes-data.dictionary">mdmdevice/attributes-data.dictionary</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MdmDeviceAttributes(@JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceName,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String productFamily,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String enrolledUserId) {}
