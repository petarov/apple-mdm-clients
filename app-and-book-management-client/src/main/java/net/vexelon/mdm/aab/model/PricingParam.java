package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.annotation.Nonnull;

/**
 * The quality of a product in the iTunes Store.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 */
public enum PricingParam {
	STDQ("STDQ"),
	PLUS("PLUS"),
	@JsonEnumDefaultValue UNKNOWN("");

	private final String jsonName;

	PricingParam(String jsonName) {
		this.jsonName = jsonName;
	}

	/**
	 * @return the value as sent in the {@code pricingParam} query parameter, e.g. {@code STDQ}
	 */
	@Nonnull
	public String jsonName() {
		return jsonName;
	}
}
