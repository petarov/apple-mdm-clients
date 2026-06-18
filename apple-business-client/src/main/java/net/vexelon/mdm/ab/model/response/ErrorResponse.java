package net.vexelon.mdm.ab.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.ApiError;

import java.util.List;

/**
 * Error response from the Apple Business API.
 *
 * @param errors list of errors; may contain multiple entries for a single failed request
 * @see <a href="https://developer.apple.com/documentation/apple_business_api/errorresponse">ErrorResponse</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorResponse(@Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<ApiError> errors) {}
