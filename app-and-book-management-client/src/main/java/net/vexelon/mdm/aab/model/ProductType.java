package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.annotation.Nonnull;

/**
 * The type of product.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 */
public enum ProductType {
	APP("App"),
	BOOK("Book"),
	@JsonEnumDefaultValue UNKNOWN("");

	private final String jsonName;

	ProductType(String jsonName) {
		this.jsonName = jsonName;
	}

	/**
	 * @return the value as sent in the {@code productType} query parameter, e.g. {@code App}
	 */
	@Nonnull
	public String jsonName() {
		return jsonName;
	}
}
