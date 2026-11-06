package net.vexelon.mdm.aab;

/**
 * Thrown when the App and Book Management client fails to process its input or an API response.
 */
public class AppAndBookClientException extends RuntimeException {

	public AppAndBookClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
