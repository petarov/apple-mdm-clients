package com.github.petarov.mdm.aab.legacy;

import com.github.petarov.mdm.shared.config.MdmClientBuilder;
import jakarta.annotation.Nonnull;

public class LegacyAppAndBookClientBuilder
		extends MdmClientBuilder<LegacyAppAndBookClientBuilder, LegacyAppAndBookClient> {

	private final String DEFAULT_APPLE_PRODUCTION_HOST = "https://vpp.itunes.apple.com/mdm";
	private final String DEFAULT_USER_AGENT            = "apple-mdm-clients-v1";

	LegacyAppAndBookClientBuilder() {
	}

	@Nonnull
	@Override
	public LegacyAppAndBookClient build() {
		return null;
	}
}

