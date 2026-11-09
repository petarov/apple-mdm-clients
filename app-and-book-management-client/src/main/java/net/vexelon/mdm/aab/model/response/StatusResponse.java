package net.vexelon.mdm.aab.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.aab.model.ErrorResponse;
import net.vexelon.mdm.aab.model.EventStatus;
import net.vexelon.mdm.aab.model.EventType;
import net.vexelon.mdm.aab.model.MdmInfo;

import java.util.List;

/**
 * The status of an asynchronous event. Compare {@code numCompleted} with {@code numRequested} to track the progress
 * of the event.
 *
 * @param eventStatus         the overall status of the event
 * @param eventType           the type of the event, reflecting the request that created it
 * @param failures            the failures that occurred while the server processed the event
 * @param mdmInfo             the MDM server information set through the client configuration, or {@code null} if
 *                            none is set
 * @param numCompleted        the number of tasks of the request that the server completed
 * @param numRequested        the total number of tasks in the request
 * @param tokenExpirationDate the ISO-8601 expiration date of the token, in UTC
 * @param uId                 the unique identifier of the organization. Use it to detect and skip duplicate
 *                            organizations
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/statusresponse">devicemanagement/statusresponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record StatusResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) EventStatus eventStatus,
                             @JsonSetter(nulls = Nulls.AS_EMPTY) EventType eventType,
                             @JsonSetter(nulls = Nulls.AS_EMPTY) List<ErrorResponse> failures,
                             @Nullable MdmInfo mdmInfo, int numCompleted, int numRequested,
                             @JsonSetter(nulls = Nulls.AS_EMPTY) String tokenExpirationDate,
                             @JsonSetter(nulls = Nulls.AS_EMPTY) String uId) {}
