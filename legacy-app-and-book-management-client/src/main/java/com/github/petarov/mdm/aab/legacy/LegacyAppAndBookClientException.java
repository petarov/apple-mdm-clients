package com.github.petarov.mdm.aab.legacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.petarov.mdm.aab.legacy.model.VppErrorCode;
import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import com.github.petarov.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;

public class LegacyAppAndBookClientException extends RuntimeException {

	private final int    code;
	private final String errorMessage;

	public LegacyAppAndBookClientException(int code, @Nonnull String errorMessage) {
		this.code = code;
		this.errorMessage = errorMessage;
	}

	public LegacyAppAndBookClientException(@Nonnull HttpClientWrapperException exception) {
		super(exception.getMessage(), exception);
		/*
		 * Translate VPP service errors. In case of errors, the HTTP service (much to our misfortune) returns 200 OK
		 * with a VppError JSON payload. Scanning every successful response may cost CPU time, so to counter scanning
		 * large responses, this just checks the first 100 characters for an error occurrence. A better solution may
		 * be needed here.
		 */
		if (exception.getStatusLine().substring(0, Math.min(100, exception.getStatusLine().length()))
				.contains("errorNumber")) {
			try {
				var parsedError = JsonUtil.createObjectMapper()
						.readValue(exception.getStatusLine(), VppErrorCode.class);
				this.code = parsedError.errorNumber();
				this.errorMessage = parsedError.errorMessage();
			} catch (JsonProcessingException e) {
				throw new RuntimeException("Error parsing JSON error message: " + exception.getStatusLine(), e);
			}
		} else {
			this.code = exception.getStatusCode();
			this.errorMessage = exception.getStatusLine();
		}
	}

	public int getCode() {
		return code;
	}

	@Nonnull
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return super.toString() + ": (" + getCode() + ") " + getErrorMessage();
	}
}
