package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * An error number and its human-readable explanation, as listed by the service configuration.
 *
 * @param errorMessage the human-readable explanation of the error
 * @param errorNumber  the error number
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/responseerrorcode">devicemanagement/responseerrorcode</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseErrorCode(@JsonSetter(nulls = Nulls.AS_EMPTY) String errorMessage, int errorNumber) {}
