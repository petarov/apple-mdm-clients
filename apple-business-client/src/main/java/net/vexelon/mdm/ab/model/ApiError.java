package net.vexelon.mdm.ab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * A single error entry from an Apple Business API error response.
 *
 * @param id     the unique ID of a specific instance of an error, request, and response; use this ID when providing
 *               feedback to, or debugging issues with Apple
 * @param status the HTTP status code of the error; this status code usually matches the response's status code.
 *               However, if the request produces multiple errors, these two codes may differ.
 * @param code   a machine-readable code indicating the type of error. The code is a hierarchical value with levels of
 *               specificity separated by a period ({@code .}). This value is parseable for programmatic error handling
 *               in code.
 * @param title  a summary of the error; don’t use this field for programmatic error handling
 * @param detail a detailed explanation of the error; don’t use this field for programmatic error handling.
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/errorresponse">ErrorResponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiError(@JsonSetter(nulls = Nulls.AS_EMPTY) String id, @JsonSetter(nulls = Nulls.AS_EMPTY) String status,
                       @JsonSetter(nulls = Nulls.AS_EMPTY) String code,
                       @JsonSetter(nulls = Nulls.AS_EMPTY) String title,
                       @JsonSetter(nulls = Nulls.AS_EMPTY) String detail) {}
