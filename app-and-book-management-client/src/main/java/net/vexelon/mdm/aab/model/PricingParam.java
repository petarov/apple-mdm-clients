package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * The quality of a product in the iTunes Store.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 */
public enum PricingParam {
	STDQ,
	PLUS,
	@JsonEnumDefaultValue UNKNOWN
}
