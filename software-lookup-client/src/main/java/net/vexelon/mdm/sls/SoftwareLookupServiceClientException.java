package net.vexelon.mdm.sls;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;

/**
 * Thrown when the Software Lookup Service client fails to process its input or an API response.
 */
public class SoftwareLookupServiceClientException extends RuntimeException {

	private final int statusCode;

	public SoftwareLookupServiceClientException(String message, Throwable cause) {
		super(message, cause);
		this.statusCode = 0;
	}

	public SoftwareLookupServiceClientException(@Nonnull HttpClientWrapperException exception) {
		super(exception.getMessage(), exception);
		this.statusCode = exception.getStatusCode();
	}

	/**
	 * @return the HTTP status code of the failed response, or {@code 0} if there was none
	 */
	public int getStatusCode() {
		return statusCode;
	}
}
