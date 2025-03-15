package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppUser;
import jakarta.annotation.Nonnull;

/**
 * The response from the user details service.
 *
 * @param response {@link VppResponse}
 * @param user     the requested user
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getvppuserresponse">GetVppUserResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppGetUserResponse(@JsonUnwrapped VppResponse response, @Nonnull VppUser user) {}
