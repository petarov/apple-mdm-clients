package com.github.petarov.mdm.da;

import org.jetbrains.annotations.Nullable;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DeviceAssignmentException extends Exception {

	private final int    code;
	private final String errorKey;

	/**
	 * @param message  the exception message
	 * @param cause    the exception cause or {@code null}
	 * @param code     HTTP error code
	 * @param errorKey OAuth error key returned by the server
	 */
	public DeviceAssignmentException(String message, @Nullable Throwable cause, int code, String errorKey) {
		super(message, cause);
		this.code = code;
		this.errorKey = errorKey;
	}

	public DeviceAssignmentException(String message, int code, String errorKey) {
		this(message, null, code, errorKey);
	}

	public int getCode() {
		return code;
	}

	public String getErrorKey() {
		return errorKey;
	}

	/**
	 * @return the text representation of the OAuth error key
	 */
	public String getErrorMessage() {
		try {
			return ResourceBundle.getBundle("messages").getString(switch (errorKey) {
				// --- OAuth specific
				case "oauth_problem_adviceBad Request" -> "OAUTH_BAD_REQUEST";
				case "oauth_problem_adviceUnauthorized" -> "OAUTH_UNAUTHORIZED";
				case "token_expiredForbidden" -> "OAUTH_FORBIDDEN";
				default -> errorKey;
			});
		} catch (MissingResourceException e) {
			System.err.println(e.getMessage());
			return errorKey;
		}
	}
}
