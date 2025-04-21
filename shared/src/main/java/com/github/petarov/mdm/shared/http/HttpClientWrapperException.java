package com.github.petarov.mdm.shared.http;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

public class HttpClientWrapperException extends RuntimeException {

	private final int    statusCode;
	private final String statusLine;

	/**
	 * @param message    the exception message
	 * @param cause      the exception cause or {@code null}
	 * @param statusCode HTTP error code
	 * @param statusLine HTTP status line
	 */
	public HttpClientWrapperException(String message, @Nullable Throwable cause, int statusCode, String statusLine) {
		super(message, cause);
		this.statusCode = statusCode;
		this.statusLine = Objects.toString(statusLine, "");
	}

	public HttpClientWrapperException(String message, int statusCode, String statusLine) {
		this(message, null, statusCode, statusLine);
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Nonnull
	public String getStatusLine() {
		return statusLine;
	}
}
