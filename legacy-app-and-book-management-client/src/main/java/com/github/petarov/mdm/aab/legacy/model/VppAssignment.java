package com.github.petarov.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * An assignment’s properties and their values.
 *
 * @param adamIdStr       the unique identifier for a product in the iTunes Store
 * @param clientUserIdStr the client user ID of the user that the device is currently assigned to
 * @param pricingParam    the quality of a product in the iTunes Store. Possible values are:<ul>
 *                        <li>{@code STDQ}: Standard quality
 *                        <li>{@code PLUS}: High quality
 * @param serialNumber    the device's serial number that the license is currently assigned to
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppassignment">VppAssignment</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record VppAssignment(@JsonSetter(nulls = Nulls.AS_EMPTY) String adamIdStr,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String clientUserIdStr,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String pricingParam,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber) {}
