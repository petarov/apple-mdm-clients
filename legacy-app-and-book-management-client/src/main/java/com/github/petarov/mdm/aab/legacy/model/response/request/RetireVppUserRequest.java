package com.github.petarov.mdm.aab.legacy.model.response.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * The request to retire a user.
 *
 * @param sToken          required the authentication token.
 *                        For more information, see <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
 * @param clientUserIdStr the identifier supplied by the client when registering a user.
 *                        Either {@link #clientUserIdStr()} or {@link #userId()} is required.
 *                        If both {@code clientUserIdStr} and {@code userId} are supplied, {@code userId} takes precedence.
 * @param userId          the unique identifier assigned by the VPP when registering the user.
 *                        Either {@link #clientUserIdStr()} or {@link #userId()} is required.
 *                        If both {@code clientUserIdStr} and {@code userId} are supplied, {@code userId} takes precedence.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/retirevppuserrequest">RetireVppUserRequest</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record RetireVppUserRequest(String sToken, String clientUserIdStr, long userId) {}
