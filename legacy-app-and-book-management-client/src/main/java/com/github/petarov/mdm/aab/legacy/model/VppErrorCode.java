package com.github.petarov.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * An error code.
 *
 * @param errorMessage The human-readable explanation of the error.
 * @param errorNumber  The numeric code of the error.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vpperrorcode">VppErrorCode</a>
 */
public record VppErrorCode(@JsonSetter(nulls = Nulls.AS_EMPTY) String errorMessage, int errorNumber) {}
