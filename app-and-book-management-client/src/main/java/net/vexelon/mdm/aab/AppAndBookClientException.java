package net.vexelon.mdm.aab;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.aab.model.ErrorResponse;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import net.vexelon.mdm.shared.util.JsonUtil;

import java.util.Objects;

/**
 * Thrown when the App and Book Management client fails to process its input or an API response.
 */
public class AppAndBookClientException extends RuntimeException {

	private final int    code;
	private final String errorMessage;
	private final int    statusCode;

	public AppAndBookClientException(String message, Throwable cause) {
		super(message, cause);
		this.code = 0;
		this.errorMessage = "";
		this.statusCode = 0;
	}

	public AppAndBookClientException(@Nonnull HttpClientWrapperException exception) {
		super(exception.getMessage(), exception);
		this.statusCode = exception.getStatusCode();
		var body = exception.getStatusLine();
		// avoid scanning large bodies: only the beginning is checked for an error payload
		if (body.substring(0, Math.min(100, body.length())).contains("errorNumber")) {
			try {
				var parsedError = JsonUtil.createObjectMapper().readValue(body, ErrorResponse.class);
				this.code = parsedError.errorNumber();
				this.errorMessage = parsedError.errorMessage();
			} catch (JsonProcessingException e) {
				throw new AppAndBookClientException("Error parsing JSON error message: " + body, e);
			}
		} else {
			this.code = statusCode;
			this.errorMessage = body;
		}
	}

	/**
	 * @return the Apple error number, or the HTTP status code if the response carried no error payload
	 */
	public int getCode() {
		return code;
	}

	@Nonnull
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @return the HTTP status code of the failed response, or {@code 0} if there was none
	 */
	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String getMessage() {
		return Objects.requireNonNullElse(super.getMessage(), "(" + getCode() + ") " + getErrorMessage());
	}
}
