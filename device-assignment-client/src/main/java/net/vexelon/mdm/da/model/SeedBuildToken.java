package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.annotation.Nonnull;

/**
 * Describes a beta enrollment token available for the given organization.
 *
 * @param os    the platform related to beta build: defaults to {@link OS#UNKNOWN} when absent or unrecognized
 * @param title the public facing name, like <i>"iOS 17 Public Beta"</i>
 * @param token the token to use when requesting the beta build
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/seedbuildtoken">devicemanagement/seedbuildtoken</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record SeedBuildToken(@Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) OS os, String title, String token) {

	/**
	 * The platform related to a beta build.
	 */
	public enum OS {
		@JsonEnumDefaultValue UNKNOWN,
		HOMEPODOS,
		IOS,
		OSX,
		TVOS,
		VISIONOS,
		WATCHOS
	}
}
