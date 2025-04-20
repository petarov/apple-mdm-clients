package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppErrorCode;

import java.util.List;

/**
 * The response with the service configuration.
 *
 * @param associateLicenseSrvUrl           the URL for the Associate License endpoint. Note the Associate License
 *                                         endpoint has been deprecated. Use <a href="https://developer.apple.com/documentation/devicemanagement/manage-licenses">Manage Licenses</a> instead.
 * @param clientConfigSrvUrl               the URL for the Client Configuration endpoint
 * @param contentMetadataLookupUrl         the URL that returns metadata about a product in the iTunes Store.
 *                                         See <a href="https://developer.apple.com/documentation/devicemanagement/getting-app-and-book-information-legacy">Getting app and book information</a>, for more information.
 * @param disassociateLicenseSrvUrl        The URL for the Disassociate License endpoint. Note the Disassociate License
 *                                         endpoint has been deprecated. Use Manage Licenses instead.
 * @param editUserSrvUrl                   the URL for the Edit a User endpoint
 * @param errorCodes                       list of possible error numbers and their human-readable explanations
 * @param errorMessage                     the human-readable explanation of the error
 * @param errorNumber                      the numeric code of the error
 * @param getLicensesSrvUrl                the URL for the Get Licenses endpoint
 * @param getUserSrvUrl                    the URL for the Get a User endpoint
 * @param getUsersSrvUrl                   the URL for the Get Users endpoint
 * @param getVPPAssetsSrvUrl               the URL for the Get Assets endpoint
 * @param invitationEmailUrl               the URL template for inviting users to an organization
 * @param manageVPPLicensesByAdamIdSrvUrl  the URL for the Manage Licenses endpoint
 * @param maxBatchAssociateLicenseCount    the maximum number of entries allowed in the arrays for associating licenses
 *                                         with Manage Licenses. The MDM server should check this value every 5 minutes, because it could change without notice.
 * @param maxBatchDisassociateLicenseCount the maximum number of entries allowed in the arrays for disassociating
 *                                         licenses from Manage Licenses. The MDM server should check this value every
 *                                         5 minutes, because it could change without notice.
 * @param registerUserSrvUrl               the URL for the Register a User endpoint
 * @param retireUserSrvUrl                 the URL for the Retire a User endpoint
 * @param status                           the status code for the response. Possible values are:<ul>
 *                                         <li>{@code 0}: Success
 *                                         <li>{@code –1}: Failure
 * @param vppWebsiteUrl                    the URL for the VPP website
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppserviceconfigresponse">VppServiceConfigResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record VppServiceConfigResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String associateLicenseSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String clientConfigSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String contentMetadataLookupUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String disassociateLicenseSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String editUserSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String getAssignmentsSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) List<VppErrorCode> errorCodes,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String errorMessage, int errorNumber,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String getLicensesSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String getUserSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String getUsersSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String getVPPAssetsSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String invitationEmailUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String manageVPPLicensesByAdamIdSrvUrl,
                                       int maxBatchAssociateLicenseCount, int maxBatchDisassociateLicenseCount,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String registerUserSrvUrl,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String retireUserSrvUrl, int status,
                                       @JsonSetter(nulls = Nulls.AS_EMPTY) String vppWebsiteUrl) {}
