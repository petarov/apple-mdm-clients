package net.vexelon.mdm.aab.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * A product in the store to assign or unassign. An asset is identified by the pair of {@code adamId} and
 * {@code pricingParam}.
 *
 * @param adamId       the unique identifier for a product in the store
 * @param pricingParam the quality of the product: {@link PricingParam#STDQ} or {@link PricingParam#PLUS}, or
 *                     {@code null} to omit it from the request. {@link PricingParam#UNKNOWN} is rejected with an
 *                     {@link IllegalArgumentException}.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/asset">devicemanagement/asset</a>
 */
public record RequestAsset(@Nonnull String adamId, @Nullable PricingParam pricingParam) {}
