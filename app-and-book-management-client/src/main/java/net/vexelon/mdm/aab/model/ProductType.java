package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * The type of product.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 */
public enum ProductType {
	APP,
	BOOK,
	@JsonEnumDefaultValue UNKNOWN
}
