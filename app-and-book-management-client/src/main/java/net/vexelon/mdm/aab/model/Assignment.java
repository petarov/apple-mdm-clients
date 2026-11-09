package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A license assignment to a user or a device.
 * <p>
 * Either {@code clientUserId} or {@code serialNumber} is set; the other one is an empty string.
 *
 * @param adamId       the unique identifier for a product in the store
 * @param clientUserId the unique identifier of the user the license is assigned to
 * @param idHash       the hash of the user's unique store identifier. Empty string unless the request set
 *                     {@code includeUserState=true} and the user has an associated Apple Account
 * @param pricingParam the quality of the product
 * @param serialNumber the serial number of the device the license is assigned to
 * @param userStatus   the status of the user. {@link UserStatus#UNKNOWN} unless the request set
 *                     {@code includeUserState=true}
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/assignment">devicemanagement/assignment</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Assignment(@JsonSetter(nulls = Nulls.AS_EMPTY) String adamId,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String clientUserId,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String idHash,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) PricingParam pricingParam,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) UserStatus userStatus) {
}
