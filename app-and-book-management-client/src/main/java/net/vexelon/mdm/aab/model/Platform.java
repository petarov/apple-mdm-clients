package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * A platform supported by an asset.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 */
public enum Platform {
	IOS,
	MACOS,
	TVOS,
	WATCHOS,
	VISIONOS,
	@JsonEnumDefaultValue UNKNOWN
}
