package net.vexelon.mdm.ab.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * OAuth 2.0 access token response from the Apple authorization server.
 *
 * @param accessToken the bearer token to attach on every API request
 * @param tokenType   always {@code Bearer}
 * @param expiresIn   token TTL in seconds
 * @param scope       granted scope, e.g. {@code business.api}
 * @see <a href="https://developer.apple.com/documentation/apple-school-and-business-manager-api/implementing-oauth-for-the-apple-school-manager-and-apple-business-api#Request-an-access-token">
 * Implementing OAuth for the Apple School Manager and Apple Business API - Request an access token</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AccessTokenResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String accessToken,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String tokenType, long expiresIn,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) String scope) {}
