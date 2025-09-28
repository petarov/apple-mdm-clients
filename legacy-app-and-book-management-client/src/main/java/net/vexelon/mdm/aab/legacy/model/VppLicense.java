package net.vexelon.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A license for a product in the purchase program.
 *
 * @param adamId          the unique identifier for a product in the iTunes Store
 * @param adamIdStr       the unique identifier for a product in the iTunes Store
 * @param clientUserIdStr the client user ID for the user to whom this license is assigned
 * @param isIrrevocable   if {@code true}, once a license is assigned for specified adamId, the license may not be
 *                        revoked or disassociated
 * @param itsIdHash       the hash of the iTunes Store ID for the user to whom this license is assigned.
 *                        The {@code itsIdHash} field is included only when the user is associated with an iTunes Store
 *                        account.
 * @param licenseIdStr    the identifier of the assigned license
 * @param pricingParam    the quality of a product in the iTunes Store. Possible values are:<ul>
 *                        <li>{@code STDQ}: Standard quality
 *                        <li>{@code PLUS}: High quality
 *                        </ul>
 * @param productTypeId   the type of product. Possible values are:<ul>
 *                        <li>{@code 7} = macOS software
 *                        <li>{@code 8} = iOS or macOS app from the App Store
 *                        <li>{@code 10} = Book
 *                        </ul>
 * @param productTypeName the name of the product type
 * @param serialNumber    the device serial number to which this license is assigned
 * @param status          the current state of the license. Possible values are:<ul>
 *                        <li>{@code Associated}
 *                        <li>{@code Available}
 *                        <li>{@code Refunded}
 *                        </ul>
 * @param userId          the unique identifier assigned by the VPP for the user to whom this license is assigned
 * @param userIdStr       the string representation of the unique identifier assigned by the VPP for the user to whom
 *                        this license is assigned
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vpplicense">VppLicense</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record VppLicense(long adamId, @JsonSetter(nulls = Nulls.AS_EMPTY) String adamIdStr,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String clientUserIdStr,
                         @JsonProperty("irrevocable") boolean isIrrevocable,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String itsIdHash,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String licenseIdStr,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String pricingParam, int productTypeId,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String productTypeName,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String status, long userId,
                         @JsonSetter(nulls = Nulls.AS_EMPTY) String userIdStr) {}
