package com.github.petarov.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A particular asset in the purchase program.
 *
 * @param adamIdStr          the unique identifier for a product in the iTunes Store
 * @param assignedCount      the total number of licenses assigned to a user or a device for the specified
 *                           {@link #adamIdStr} and {@link #pricingParam}
 * @param availableCount     the total number of licenses available to be assigned for the specified {@link #adamIdStr}
 *                           and {@link #pricingParam}
 * @param isDeviceAssignable if {@code true}, a license for the specified {@link #adamIdStr} may be assigned to a device
 * @param isIrrevocable      if {@code true}, once a license is assigned for specified {@link #adamIdStr}, the license
 *                           may not be revoked or disassociated
 * @param pricingParam       the quality of a product in the iTunes Store. Possible values are:<ul>
 *                           <li>{@code STDQ}: Standard quality
 *                           <li>{@code PLUS}: High quality
 * @param productTypeId      the type of product. Possible values are:<ul>
 *                           <li>{@code 7} = macOS software
 *                           <li>{@code 8} = iOS or macOS app from the App Store
 *                           <li>{@code 10} = Book
 * @param productTypeName    the name of the product type.
 * @param retiredCount       the total number of licenses that have been retired for the specified {@link #adamIdStr}
 *                           and {@link #pricingParam}
 * @param totalCount         the total number of licenses managed by your organization for the specified
 *                           {@link #adamIdStr} and {@link #pricingParam}
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppasset">VppAsset</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppAsset(@JsonSetter(nulls = Nulls.AS_EMPTY) String adamIdStr, int assignedCount, int availableCount,
                       @JsonProperty("deviceAssignable") boolean isDeviceAssignable,
                       @JsonProperty("irrevocable") boolean isIrrevocable,
                       @JsonSetter(nulls = Nulls.AS_EMPTY) String pricingParam, int productTypeId,
                       @JsonSetter(nulls = Nulls.AS_EMPTY) String productTypeName, int retiredCount, int totalCount) {}