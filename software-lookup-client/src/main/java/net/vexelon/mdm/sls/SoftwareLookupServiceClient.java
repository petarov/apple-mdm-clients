package net.vexelon.mdm.sls;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.sls.model.response.SoftwareLookupResponse;

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

	/**
	 * Fetches the software releases Apple publishes for its platforms.
	 *
	 * @return the releases grouped by section and platform; sections or platforms missing from the response are
	 * empty, never {@code null}
	 * @throws SoftwareLookupServiceClientException if the request fails or the response cannot be parsed
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/integrating-declarative-management-of-software-updates">Integrating declarative management of software updates</a>
	 */
	@Nonnull
	SoftwareLookupResponse fetchSoftwareReleases();
}
