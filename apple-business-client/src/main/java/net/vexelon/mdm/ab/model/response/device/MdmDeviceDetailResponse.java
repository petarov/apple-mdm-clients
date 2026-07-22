package net.vexelon.mdm.ab.model.response.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.devices.MdmDeviceDetail;

/**
 * A response that contains a single MDM device detail resource.
 *
 * @param data the resource data
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/mdmdevicedetailresponse">applebusinessapi/mdmdevicedetailresponse</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MdmDeviceDetailResponse(@Nonnull MdmDeviceDetail data) {}
