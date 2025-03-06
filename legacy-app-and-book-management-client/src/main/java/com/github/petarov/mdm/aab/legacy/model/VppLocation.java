package com.github.petarov.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * A location used for managing purchases.
 *
 * @param locationId   the identifier for the location
 * @param locationName the name of the location
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vpplocation">VppLocation</a>
 */
public record VppLocation(long locationId, @JsonSetter(nulls = Nulls.AS_EMPTY) String locationName) {}
