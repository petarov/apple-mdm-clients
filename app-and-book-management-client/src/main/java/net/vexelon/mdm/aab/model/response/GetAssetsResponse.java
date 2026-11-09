package net.vexelon.mdm.aab.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.aab.model.MdmInfo;
import net.vexelon.mdm.aab.model.ResponseAsset;
import net.vexelon.mdm.aab.model.UnlimitedResponseAsset;

import java.util.List;

/**
 * The response to a request for the set of assets that your organization manages.
 *
 * @param assets              the assets with a limited number of licenses
 * @param unlimitedAssets     the assets with unlimited licenses
 * @param currentPageIndex    the zero-based index of the current page
 * @param nextPageIndex       the zero-based index of the next page, or {@code 0} when absent, i.e. there is no next
 *                            page
 * @param size                the number of assets in the current page
 * @param totalPages          the total number of pages
 * @param versionId           the version identifier of the asset data
 * @param mdmInfo             the MDM server information set through the client configuration, or {@code null} if
 *                            none is set
 * @param tokenExpirationDate the ISO-8601 expiration date of the token. Present only when the token expires within
 *                            15 days, otherwise an empty string
 * @param uId                 the unique identifier of the organization
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getassetsresponse">devicemanagement/getassetsresponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetAssetsResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) List<ResponseAsset> assets,
                                @JsonSetter(nulls = Nulls.AS_EMPTY) List<UnlimitedResponseAsset> unlimitedAssets,
                                int currentPageIndex, int nextPageIndex, int size, int totalPages,
                                @JsonSetter(nulls = Nulls.AS_EMPTY) String versionId, @Nullable MdmInfo mdmInfo,
                                @JsonSetter(nulls = Nulls.AS_EMPTY) String tokenExpirationDate,
                                @JsonSetter(nulls = Nulls.AS_EMPTY) String uId) {
}
