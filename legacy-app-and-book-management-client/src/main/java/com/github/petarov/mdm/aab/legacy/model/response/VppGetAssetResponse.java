package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppAsset;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * The response with the asset.
 *
 * @param response   {@link VppResponse}
 * @param totalCount the total number of assets that will be returned
 * @param assets     the list of assets managed by the provided sToken
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getvppassetresponse">GetVppAssetResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppGetAssetResponse(@JsonUnwrapped VppResponse response, int totalCount,
                                  @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull List<VppAsset> assets) {}

