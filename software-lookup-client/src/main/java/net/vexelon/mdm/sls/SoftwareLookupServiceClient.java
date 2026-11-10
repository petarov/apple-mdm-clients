package net.vexelon.mdm.sls;

import jakarta.annotation.Nonnull;

/**
 * Client interface for Apple's Software Lookup Service.
 * <p>
 * The service requires no authentication.
 */
public interface SoftwareLookupServiceClient {

	/**
	 * Creates a new builder for configuring and constructing a {@link SoftwareLookupServiceClient}.
	 *
	 * @return a new {@link SoftwareLookupServiceClientBuilder} instance
	 */
	@Nonnull
	static SoftwareLookupServiceClientBuilder newBuilder() {
		return new SoftwareLookupServiceClientBuilder();
	}
}
