package com.github.petarov.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * A user within the purchase program.
 *
 * @param clientUserIdStr the identifier supplied by the client when registering a user. An identifier must be unique
 *                        within the organization
 * @param email           the user's email address
 * @param itsIdHash       the hash of the user's iTunes Store ID. The {@code itsIdHash} field is included only when the
 *                        user is associated with an iTunes Store account
 * @param licenses        the list of licenses which have been assigned to this user.
 * @param inviteCode      VPP invitation code
 * @param inviteUrl       VPP user invitation url
 * @param status          the status of the user's account in VPP. Possible values are:<ul>
 *                        <li>{@code Registered}: Indicates the user has been created, but is not yet associated with
 *                        an iTunes account.
 *                        <li>{@code Associated}: Indicates that the user has been associated with an iTunes account.
 *                        When a user is associated
 *                        with an iTunes account, an itsIdHash is generated for the user.
 *                        <li>{@code Retired}: Indicates the user has been retired via Retire a User.
 *                        <li>{@code Deleted}: Indicates that a VPP user is retired and its associated iTunes user has
 *                        since been invited and associated with a new VPP user that shares the same clientUserId.
 *                        Because there are two VPP users with distinct [userId] values but the same [clientUserId]
 *                        value, the `Deleted` status is used to ensure database
 *                        consistency.
 *                        </ul>
 *                        <p>
 *                        A user with a Deleted status will never change status; its sole purpose is to ensure that your software can
 *                        recognize that the userId is no longer associated with the [clientUserId] record, and update any internal
 *                        references appropriately.
 * @param userId          the unique identifier assigned by the VPP when registering the user
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppuser">VppUser</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppUser(@JsonSetter(nulls = Nulls.AS_EMPTY) String clientUserIdStr,
                      @JsonSetter(nulls = Nulls.AS_EMPTY) String email,
                      @JsonSetter(nulls = Nulls.AS_EMPTY) String itsIdHash,
                      @JsonSetter(nulls = Nulls.AS_EMPTY) List<VppLicense> licenses,
                      @JsonSetter(nulls = Nulls.AS_EMPTY) String inviteCode,
                      @JsonSetter(nulls = Nulls.AS_EMPTY) String inviteUrl,
                      @JsonSetter(nulls = Nulls.AS_EMPTY) String status, long userId) {}
