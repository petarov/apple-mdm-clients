package net.vexelon.mdm.aab.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.aab.model.MdmInfo;

/**
 * The response to an asynchronous request, such as associating, disassociating or revoking assets.
 *
 * @param eventId             the unique identifier of the asynchronous event
 * @param mdmInfo             the MDM server information set through the client configuration, or {@code null} if
 *                            none is set
 * @param tokenExpirationDate the ISO-8601 expiration date of the token, in UTC
 * @param uId                 the unique identifier of the organization. Use it to detect and skip duplicate
 *                            organizations
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/eventresponse">devicemanagement/eventresponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String eventId, @Nullable MdmInfo mdmInfo,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String tokenExpirationDate,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String uId) {}
