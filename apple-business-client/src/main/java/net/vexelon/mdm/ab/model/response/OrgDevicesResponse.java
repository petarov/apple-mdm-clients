package net.vexelon.mdm.ab.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.OrgDevice;
import net.vexelon.mdm.ab.model.OrgDevicesMeta;

import java.util.List;

/**
 * Response that contains a list of organization device resources.
 *
 * @param data the resource data
 * @param meta paging information
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/orgdevicesresponse">OrgDevicesResponse</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrgDevicesResponse(@Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<OrgDevice> data,
                                 @Nonnull OrgDevicesMeta meta) {}
