package com.github.petarov.mdm.aab.legacy.model.response;

import jakarta.annotation.Nonnull;

public interface VppHasResponse {

	/**
	 * @return {@link VppResponse}
	 */
	@Nonnull
	VppResponse getResponse();
}
