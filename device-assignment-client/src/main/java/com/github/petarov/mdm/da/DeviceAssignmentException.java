package com.github.petarov.mdm.da;

import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import jakarta.annotation.Nonnull;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DeviceAssignmentException extends RuntimeException {

	private final int    code;
	private final String statusLine;

	public DeviceAssignmentException(@Nonnull HttpClientWrapperException exception) {
		super(exception.getMessage(), exception);
		this.code = exception.getStatusCode();
		this.statusLine = exception.getStatusLine();
	}

	public int getCode() {
		return code;
	}

	public String getStatusLine() {
		return statusLine;
	}

	@Override
	public String toString() {
		return super.toString() + ": " + getErrorFromStatus();
	}

	/**
	 * @return the text representation of the OAuth error key
	 */
	public String getErrorFromStatus() {
		try {
			return ResourceBundle.getBundle("messages").getString(switch (statusLine) {
				// --- OAuth specific
				case "oauth_problem_adviceBad Request" -> "OAUTH_BAD_REQUEST";
				case "oauth_problem_adviceUnauthorized" -> "OAUTH_UNAUTHORIZED";
				case "token_expiredForbidden" -> "OAUTH_FORBIDDEN";
				default -> statusLine;
			});
		} catch (MissingResourceException e) {
			return statusLine;
		}
	}
}
