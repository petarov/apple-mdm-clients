package com.github.petarov.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * An error code.
 *
 * @param errorMessage The human-readable explanation of the error.
 * @param errorNumber  The numeric code of the error.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vpperrorcode">VppErrorCode</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record VppErrorCode(@JsonSetter(nulls = Nulls.AS_EMPTY) String errorMessage, int errorNumber) {}
