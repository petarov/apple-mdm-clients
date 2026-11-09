package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nullable;

/**
 * The error payload returned by the service for failed requests, and for each failure of an asynchronous event.
 *
 * @param errorNumber  the error code
 * @param errorMessage the error description
 * @param errorInfo    the request-specific information about the failure, or {@code null} if absent
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/errorresponse">devicemanagement/errorresponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorResponse(int errorNumber, @JsonSetter(nulls = Nulls.AS_EMPTY) String errorMessage,
                            @Nullable ResponseErrorInfo errorInfo) {}
