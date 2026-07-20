package net.vexelon.mdm.ab.model.response.servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.PagingInformation;
import net.vexelon.mdm.ab.model.devices.OrgDevice;
import net.vexelon.mdm.ab.model.servers.MdmServer;

import java.util.List;

/**
 * A response that contains a list of device management service resources.
 *
 * @param data     the resource data
 * @param included organization devices included via the {@code devices} relationship, when requested
 * @param meta     paging information for this response
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/mdmserversresponse">MdmServersResponse</a>
 * @since Apple Business API 2.2+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MdmServersResponse(@Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<MdmServer> data,
                                 @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<OrgDevice> included,
                                 @Nonnull PagingInformation meta) {}
