package net.vexelon.mdm.da.model.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.vexelon.mdm.da.model.SeedBuildToken;

import java.util.List;

/**
 * Provides a list of beta enrollment tokens available for the given organization.
 *
 * @param betaEnrollmentTokens list of beta enrollment tokens available for the given organization
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getseedbuildtokenresponse">GetSeedBuildTokenResponse</a>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record SeedBuildTokenResponse(List<SeedBuildToken> betaEnrollmentTokens) {}
