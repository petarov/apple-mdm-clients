package com.github.petarov.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * A location used for managing purchases.
 *
 * @param locationId   the identifier for the location
 * @param locationName the name of the location
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vpplocation">VppLocation</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppLocation(long locationId, @JsonSetter(nulls = Nulls.AS_EMPTY) String locationName) {}
