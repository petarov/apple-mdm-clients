package net.vexelon.mdm.sls.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.vexelon.mdm.sls.model.PlatformReleases;

import java.util.Objects;

/**
 * The software releases Apple publishes, grouped by section and platform. An absent section is
 * {@link PlatformReleases#EMPTY}.
 *
 * @param publicAssetSets                      the latest releases available to the public
 * @param assetSets                            the releases a device management service can push to devices, a
 *                                             subset of the public releases
 * @param publicBackgroundSecurityImprovements the Background Security Improvement releases
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/deploying-software-updates-using-declarative-management">deploying software updates using declarative management</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SoftwareLookupResponse(@JsonProperty("PublicAssetSets") PlatformReleases publicAssetSets,
                                     @JsonProperty("AssetSets") PlatformReleases assetSets, @JsonProperty(
		"PublicBackgroundSecurityImprovements") PlatformReleases publicBackgroundSecurityImprovements) {

	/**
	 * Replaces {@code null} sections with {@link PlatformReleases#EMPTY}.
	 */
	public SoftwareLookupResponse {
		publicAssetSets = Objects.requireNonNullElse(publicAssetSets, PlatformReleases.EMPTY);
		assetSets = Objects.requireNonNullElse(assetSets, PlatformReleases.EMPTY);
		publicBackgroundSecurityImprovements = Objects.requireNonNullElse(publicBackgroundSecurityImprovements,
				PlatformReleases.EMPTY);
	}
}
