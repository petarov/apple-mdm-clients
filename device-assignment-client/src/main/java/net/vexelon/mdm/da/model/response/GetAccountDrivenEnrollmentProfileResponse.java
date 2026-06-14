package net.vexelon.mdm.da.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @param mdmServiceDiscoveryUrl  the MDM Service Discovery URL that the MDM server sets for redirection during
 *                                account-driven enrollment
 * @param lastUpdatedTimestamp    the timestamp of the most-recent update for the MDM Service Discovery URL
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getaccountdrivenenrollmentprofileresponse">GetAccountDrivenEnrollmentProfileResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetAccountDrivenEnrollmentProfileResponse(
		@JsonSetter(nulls = Nulls.AS_EMPTY) String mdmServiceDiscoveryUrl,
		@JsonSetter(nulls = Nulls.AS_EMPTY) String lastUpdatedTimestamp) {}
