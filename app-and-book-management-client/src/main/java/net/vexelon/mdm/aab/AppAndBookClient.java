package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;

/**
 * Client interface for Apple's App and Book Management API.
 * <p>
 * Authenticates using a content token (sToken) obtained from Apple Business Manager.
 * <p>
 * The underlying implementation is not thread-safe. Concurrent access from multiple threads requires
 * external synchronization.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/app-and-book-management">App and Book Management</a>
 */
public interface AppAndBookClient {

	/**
	 * Creates a new builder for configuring and constructing an {@link AppAndBookClient}.
	 *
	 * @return a new {@link AppAndBookClientBuilder} instance
	 */
	@Nonnull
	static AppAndBookClientBuilder newBuilder() {
		return new AppAndBookClientBuilder();
	}
}
