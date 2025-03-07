package com.github.petarov.mdm.aab.legacy;

import com.github.petarov.mdm.aab.legacy.model.response.VppServiceConfigResponse;
import jakarta.annotation.Nonnull;

public interface LegacyAppAndBookClient {

	/**
	 * @return new {@link LegacyAppAndBookClientBuilder} instance
	 */
	@Nonnull
	static LegacyAppAndBookClientBuilder newBuilder() {
		return new LegacyAppAndBookClientBuilder();
	}

	/**
	 * Fetches the full list of web service URLs and a list of possible error numbers.
	 *
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/service-configuration">Service Configuration</a>
	 */
	@Nonnull
	VppServiceConfigResponse fetchServiceConfiguration();
}
