package net.vexelon.mdm.ab.model.response.servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.devices.OrgDevice;
import net.vexelon.mdm.ab.model.servers.MdmServer;

import java.util.List;

/**
 * A response that contains a single device management service resource.
 *
 * @param data     the resource data
 * @param included organization devices included via the {@code devices} relationship, when requested
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/mdmserverresponse">applebusinessapi/mdmserverresponse</a>
 * @since Apple Business API 2.2+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MdmServerResponse(@Nonnull MdmServer data,
                                @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<OrgDevice> included) {}
