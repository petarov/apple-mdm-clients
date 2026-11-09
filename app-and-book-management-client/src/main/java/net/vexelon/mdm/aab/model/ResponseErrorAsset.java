package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * A requested product in the store that resulted in an error.
 *
 * @param adamId       the unique identifier for a product in the store
 * @param pricingParam the quality of the product, or {@link PricingParam#UNKNOWN} if absent or unrecognized
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/asset">devicemanagement/asset</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseErrorAsset(@JsonSetter(nulls = Nulls.AS_EMPTY) String adamId,
                                 @JsonSetter(nulls = Nulls.AS_EMPTY) PricingParam pricingParam) {}
