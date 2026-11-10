package net.vexelon.mdm.sls.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.List;

/**
 * Software releases grouped by platform. Release lists keep the order and duplicates of the response.
 *
 * @param ios      the releases for iOS, which also covers iPadOS, tvOS, watchOS and HomePod
 * @param macos    the releases for macOS
 * @param visionos the releases for visionOS
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlatformReleases(
		@JsonProperty("iOS") @JsonSetter(nulls = Nulls.AS_EMPTY, contentNulls = Nulls.SKIP) List<SoftwareRelease> ios,
		@JsonProperty("macOS") @JsonSetter(nulls = Nulls.AS_EMPTY,
				contentNulls = Nulls.SKIP) List<SoftwareRelease> macos,
		@JsonProperty("visionOS") @JsonSetter(nulls = Nulls.AS_EMPTY,
				contentNulls = Nulls.SKIP) List<SoftwareRelease> visionos) {

	public static final PlatformReleases EMPTY = new PlatformReleases(List.of(), List.of(), List.of());
}
