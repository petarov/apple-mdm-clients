package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppUser;
import jakarta.annotation.Nonnull;

/**
 * The response from retiring a user.
 * <p>
 * If the user passes the {@code userId} value for an already-retired user, this request returns an error that indicates
 * the user has already been retired.
 *
 * @param response {@link VppResponse}
 * @param user     the retired user
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/retirevppuserresponse">RetireVppUserResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppRetireUserResponse(@JsonUnwrapped VppResponse response, @Nonnull VppUser user) {}
