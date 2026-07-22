package net.vexelon.mdm.ab.model.response.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.devices.OrgDevice;

/**
 * A response that contains a single organization device resource.
 *
 * @param data the resource data
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/orgdeviceresponse">applebusinessapi/orgdeviceresponse</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrgDeviceResponse(@Nonnull OrgDevice data) {}
