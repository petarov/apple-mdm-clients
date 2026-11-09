package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A user managed through the purchase program.
 *
 * @param clientUserId the unique identifier of the user, as assigned by the MDM server
 * @param email        the email address of the user
 * @param idHash       the hash of the user's unique store identifier. Empty string unless the user has an associated
 *                     Apple Account
 * @param inviteCode   the code used to associate an Apple Account to the user
 * @param status       the status of the user
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/responseuser">devicemanagement/responseuser</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseUser(@JsonSetter(nulls = Nulls.AS_EMPTY) String clientUserId,
                           @JsonSetter(nulls = Nulls.AS_EMPTY) String email,
                           @JsonSetter(nulls = Nulls.AS_EMPTY) String idHash,
                           @JsonSetter(nulls = Nulls.AS_EMPTY) String inviteCode,
                           @JsonSetter(nulls = Nulls.AS_EMPTY) UserStatus status) {
}
