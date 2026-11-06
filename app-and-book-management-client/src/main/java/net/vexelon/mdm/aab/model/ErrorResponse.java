package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * The error payload returned by the service for failed requests.
 *
 * @param errorNumber  the error code
 * @param errorMessage the error description
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/errorresponse">devicemanagement/errorresponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorResponse(int errorNumber, @JsonSetter(nulls = Nulls.AS_EMPTY) String errorMessage) {
}
