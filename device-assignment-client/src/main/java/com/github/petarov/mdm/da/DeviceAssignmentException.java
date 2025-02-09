package com.github.petarov.mdm.da;

import org.jetbrains.annotations.Nullable;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DeviceAssignmentException extends RuntimeException {

	private final int    code;
	private final String body;

	/**
	 * @param message the exception message
	 * @param cause   the exception cause or {@code null}
	 * @param code    HTTP error code
	 * @param body    OAuth error key returned by the server
	 */
	public DeviceAssignmentException(String message, @Nullable Throwable cause, int code, String body) {
		super(message, cause);
		this.code = code;
		this.body = body;
	}

	public DeviceAssignmentException(String message, int code, String body) {
		this(message, null, code, body);
	}

	public int getCode() {
		return code;
	}

	public String getBody() {
		return body;
	}

	/**
	 * @return the text representation of the OAuth error key
	 */
	public String getErrorFromBody() {
		try {
			return ResourceBundle.getBundle("messages").getString(switch (body) {
				// --- OAuth specific
				case "oauth_problem_adviceBad Request" -> "OAUTH_BAD_REQUEST";
				case "oauth_problem_adviceUnauthorized" -> "OAUTH_UNAUTHORIZED";
				case "token_expiredForbidden" -> "OAUTH_FORBIDDEN";
				default -> body;
			});
		} catch (MissingResourceException e) {
			return body;
		}
	}
}
