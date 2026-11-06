package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * A particular asset with unlimited licenses in the purchase program.
 *
 * @param adamId             the unique identifier for a product in the iTunes Store
 * @param assignedCount      the total number of licenses assigned to a user or a device for the specified
 *                           {@link #adamId} and {@link #pricingParam}
 * @param isDeviceAssignable if {@code true}, a license for the specified {@link #adamId} may be assigned to a device
 * @param pricingParam       the quality of a product in the iTunes Store. Possible values are:<ul>
 *                           <li>{@link PricingParam#STDQ}: Standard quality
 *                           <li>{@link PricingParam#PLUS}: High quality
 *                           </ul>
 * @param productType        the type of product. Possible values are:<ul>
 *                           <li>{@link ProductType#APP}
 *                           <li>{@link ProductType#BOOK}
 *                           </ul>
 * @param isRevocable        if {@code false}, once a license is assigned for the specified {@link #adamId}, the
 *                           license may not be revoked or disassociated
 * @param supportedPlatforms the platforms the asset supports: {@link Platform#IOS}, {@link Platform#MACOS},
 *                           {@link Platform#TVOS}, {@link Platform#WATCHOS}, {@link Platform#VISIONOS}
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/unlimitedresponseasset">devicemanagement/unlimitedresponseasset</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record UnlimitedResponseAsset(@JsonSetter(nulls = Nulls.AS_EMPTY) String adamId, int assignedCount,
                                     @JsonProperty("deviceAssignable") boolean isDeviceAssignable,
                                     @JsonSetter(nulls = Nulls.AS_EMPTY) PricingParam pricingParam,
                                     @JsonSetter(nulls = Nulls.AS_EMPTY) ProductType productType,
                                     @JsonProperty("revocable") boolean isRevocable,
                                     @JsonSetter(nulls = Nulls.AS_EMPTY) List<Platform> supportedPlatforms) {
}
