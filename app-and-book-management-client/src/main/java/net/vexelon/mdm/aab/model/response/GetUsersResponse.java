package net.vexelon.mdm.aab.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.aab.model.ResponseUser;
import net.vexelon.mdm.aab.model.MdmInfo;

import java.util.List;

/**
 * The response to a request for information about a set of users.
 *
 * @param users               the users in the current page
 * @param currentPageIndex    the zero-based index of the current page
 * @param nextPageIndex       the zero-based index of the next page, or {@code 0} when absent, i.e. there is no next
 *                            page
 * @param size                the number of entries in the current page
 * @param totalPages          the total number of pages
 * @param versionId           the version identifier of the user data.
 *                            Pass it as {@code sinceVersionId} to fetch only users modified since this
 *                            response
 * @param mdmInfo             the MDM server information set through the client configuration, or {@code null} if
 *                            none is set
 * @param tokenExpirationDate the ISO-8601 expiration date of the token. Present only when the token expires within
 *                            15 days, otherwise an empty string
 * @param uId                 the unique identifier of the organization
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getusersresponse">devicemanagement/getusersresponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetUsersResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) List<ResponseUser> users,
                               int currentPageIndex, int nextPageIndex, int size, int totalPages,
                               @JsonSetter(nulls = Nulls.AS_EMPTY) String versionId, @Nullable MdmInfo mdmInfo,
                               @JsonSetter(nulls = Nulls.AS_EMPTY) String tokenExpirationDate,
                               @JsonSetter(nulls = Nulls.AS_EMPTY) String uId) {
}
