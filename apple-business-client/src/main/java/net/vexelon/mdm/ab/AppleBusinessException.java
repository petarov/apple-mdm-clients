package net.vexelon.mdm.ab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;

/**
 * Runtime exception thrown by the Apple Business Manager API client when the server returns
 * an HTTP error response.
 */
public class AppleBusinessException extends RuntimeException {

	private final int    code;
	private final String statusLine;

	public AppleBusinessException(@Nonnull HttpClientWrapperException exception) {
		super(exception.getMessage(), exception);
		this.code = exception.getStatusCode();
		this.statusLine = exception.getStatusLine();
	}

	/**
	 * @return HTTP status code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return HTTP status line from the server response
	 */
	@Nonnull
	public String getStatusLine() {
		return statusLine;
	}
}
