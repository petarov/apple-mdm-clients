package net.vexelon.mdm.ab.model.response.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.PagingInformation;
import net.vexelon.mdm.ab.model.devices.OrgDevice;

import java.util.List;

/**
 * Response that contains a list of organization device resources.
 *
 * @param data the resource data
 * @param meta paging information for this response
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/orgdevicesresponse">OrgDevicesResponse</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrgDevicesResponse(@Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<OrgDevice> data,
                                 @Nonnull PagingInformation meta) {}
