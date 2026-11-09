package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * The value to pass for the {@code platform} parameter of the {@code contentMetadataLookup} request.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/clientconfigresponse">devicemanagement/clientconfigresponse</a>
 */
public enum DefaultPlatform {
	/**
	 * For apps in the educational store.
	 */
	VOLUMESTORE,
	/**
	 * For apps in the enterprise store.
	 */
	ENTERPRISESTORE,
	@JsonEnumDefaultValue UNKNOWN
}
