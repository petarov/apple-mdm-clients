package com.github.petarov.mdm.shared.http;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpClientWrapperException extends RuntimeException {

	private final int                       statusCode;
	private final String                    statusLine;
	private final Map<String, List<String>> headers;

	/**
	 * @param message    the exception message
	 * @param cause      the exception cause or {@code null}
	 * @param statusCode HTTP error code
	 * @param statusLine HTTP status line
	 * @param headers    HTTP response headers
	 */
	public HttpClientWrapperException(String message, @Nullable Throwable cause, int statusCode, String statusLine,
			Map<String, List<String>> headers) {
		super(message, cause);
		this.statusCode = statusCode;
		this.statusLine = Objects.toString(statusLine, "");
		this.headers = headers;
	}

	public HttpClientWrapperException(String message, int statusCode, String statusLine,
			Map<String, List<String>> headers) {
		this(message, null, statusCode, statusLine, headers);
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Nonnull
	public String getStatusLine() {
		return statusLine;
	}

	@Nonnull
	public Map<String, List<String>> headers() {
		return headers;
	}
}
