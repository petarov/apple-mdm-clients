package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * Device Assignment Profile.
 *
 * @param anchorCerts             each string should contain a DER-encoded (Distinguished Encoding Rules) certificate
 *                                converted to Base64 encoding. If provided, these certificates are used as trusted
 *                                anchor certificates when evaluating the trust of the connection to the MDM server URL.
 *                                Otherwise, the built-in root certificates are used.
 * @param isAutoAdvanceSetup      if set to {@code true}, the device will tell tvOS Setup Assistant to automatically
 *                                advance though its screens. This key is valid in <i>X-Server-Protocol-Version 2</i>
 *                                and later.
 * @param isAwaitDeviceConfigured if {@code true}, the device will not continue in Setup Assistant until the MDM server
 *                                sends a command that states the device is configured. This key is valid in
 *                                <i>X-Server-Protocol-Version 2</i> and later.</p>
 * @param configurationWebUrl     the URL that the clients load into a web view during setup. This site provides the
 *                                appropriate UI to authenticate the user, and when satisfied, initiates the download of
 *                                the MDM enrollment profile. <p>To provide the MDM enrollment profile, the web view
 *                                looks for a page with MIME type {@code application/x-apple-aspen-config}. While the
 *                                user is allowed to navigate to any site/host during authentication, the MDM enrollment
 *                                profile must originate from the same host as specified in this field.</p>
 * @param department              the user-defined department or location name
 * @param devices                 array of strings that contains device serial numbers (may be empty)
 * @param isMdmRemovable          if {@code false}, the MDM payload delivered by the configuration URL cannot be removed
 *                                by the user via the user interface on the device; that is, the MDM payload is locked
 *                                onto the device.
 * @param isMultiUser             if {@code true}, tells the device to configure for Shared iPad. This key is valid only
 *                                for Apple School Manager or Apple Business Manager organizations using
 *                                <i>X-Server-Protocol-Version 2</i> and later.<p>Devices that do not meet the Shared
 *                                iPad minimum requirements do not honor this command. With iOS devices,
 *                                {@code com.apple.mdm.per-user-connections} must be added to the MDM enrollment
 *                                profile's Server Capabilities.</p>
 * @param language                a language designator is a code that represents a language. ISO 639-1 or ISO 639-2
 *                                standard. Available on <i>tvOS</i>.<p>Example two-letter: {@code en}, {@code fr},
 *                                {@code ja}</p><p>Example three-letter: {@code eng}, {@code fre}, {@code jpn},
 *                                {@code haw}</p>
 * @param orgMagic                uniquely identifies various services that are managed by a single organization
 * @param profileName             human-readable name for the profile
 * @param region                  a region designator is a code that represents a country. Available on <i>tvOS</i>.
 *                                <p>Use the ISO 3166-1 standard, a two-letter, capitalized code. Examples: {@code US},
 *                                {@code GB}, {@code AU}</p>
 * @param skipSetupItems          a list of {@link ProfileSkipItem} setup panes to skip
 * @param supervisingHostCerts    each string contains a DER-encoded certificate converted to Base64 encoding.
 *                                If provided, the device will continue to pair with a host that possesses one of these
 *                                certificates.
 * @param supportEmailAddress     a support email address for the organization. This key is valid in
 *                                <i>X-Server-Protocol-Version 2</i> and later.
 * @param supportPhoneNumber      a support phone number for the organization
 * @param url                     the URL of the MDM server
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/profile">Profile</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Profile(@Nonnull List<String> anchorCerts, @JsonProperty("auto_advance_setup") boolean isAutoAdvanceSetup,
                      @JsonProperty("await_device_configured") boolean isAwaitDeviceConfigured,
                      String configurationWebUrl, String department, @Nonnull List<String> devices,
                      boolean isMdmRemovable, boolean isMultiUser, String language, String orgMagic, String profileName,
                      String region, @Nonnull List<ProfileSkipItem> skipSetupItems,
                      @Nonnull List<String> supervisingHostCerts, String supportEmailAddress, String supportPhoneNumber,
                      String url) {

	/**
	 * Profile builder.
	 */
	public static class ProfileBuilder {

		private List<String>          anchorCerts             = List.of();
		private boolean               isAutoAdvanceSetup      = false;
		private boolean               isAwaitDeviceConfigured = false;
		private String                configurationWebUrl;
		private String                department;
		private List<String>          devices                 = List.of();
		private boolean               isMdmRemovable          = true;
		private boolean               isMultiUser             = false;
		private String                language;
		private String                orgMagic;
		private String                profileName;
		private String                region;
		private List<ProfileSkipItem> skipSetupItems          = List.of();
		private List<String>          supervisingHostCerts    = List.of();
		private String                supportEmailAddress;
		private String                supportPhoneNumber;
		private String                url;

		public ProfileBuilder setAnchorCerts(List<String> anchorCerts) {
			this.anchorCerts = anchorCerts;
			return this;
		}

		public ProfileBuilder setAutoAdvanceSetup(boolean autoAdvanceSetup) {
			isAutoAdvanceSetup = autoAdvanceSetup;
			return this;
		}

		public ProfileBuilder setAwaitDeviceConfigured(boolean awaitDeviceConfigured) {
			isAwaitDeviceConfigured = awaitDeviceConfigured;
			return this;
		}

		public ProfileBuilder setConfigurationWebUrl(String configurationWebUrl) {
			this.configurationWebUrl = configurationWebUrl;
			return this;
		}

		public ProfileBuilder setDepartment(String department) {
			this.department = department;
			return this;
		}

		public ProfileBuilder setDevices(List<String> devices) {
			this.devices = devices;
			return this;
		}

		public ProfileBuilder setMdmRemovable(boolean mdmRemovable) {
			isMdmRemovable = mdmRemovable;
			return this;
		}

		public ProfileBuilder setMultiUser(boolean multiUser) {
			isMultiUser = multiUser;
			return this;
		}

		public ProfileBuilder setLanguage(String language) {
			this.language = language;
			return this;
		}

		public ProfileBuilder setOrgMagic(String orgMagic) {
			this.orgMagic = orgMagic;
			return this;
		}

		public ProfileBuilder setProfileName(String profileName) {
			this.profileName = profileName;
			return this;
		}

		public ProfileBuilder setRegion(String region) {
			this.region = region;
			return this;
		}

		public ProfileBuilder setSkipSetupItems(List<ProfileSkipItem> skipSetupItems) {
			this.skipSetupItems = skipSetupItems;
			return this;
		}

		public ProfileBuilder setSupervisingHostCerts(List<String> supervisingHostCerts) {
			this.supervisingHostCerts = supervisingHostCerts;
			return this;
		}

		public ProfileBuilder setSupportEmailAddress(String supportEmailAddress) {
			this.supportEmailAddress = supportEmailAddress;
			return this;
		}

		public ProfileBuilder setSupportPhoneNumber(String supportPhoneNumber) {
			this.supportPhoneNumber = supportPhoneNumber;
			return this;
		}

		public ProfileBuilder setUrl(String url) {
			this.url = url;
			return this;
		}

		@Nonnull
		public Profile build() {
			if (!configurationWebUrl.isBlank() && !configurationWebUrl.startsWith("https://")) {
				throw new IllegalArgumentException("configurationWebUrl must be an HTTPS url");
			}

			if (!url.isBlank() && !url.startsWith("https://")) {
				throw new IllegalArgumentException("url must be an HTTPS url");
			}

			return new Profile(anchorCerts, isAutoAdvanceSetup, isAwaitDeviceConfigured, configurationWebUrl,
					department, devices, isMdmRemovable, isMultiUser, language, orgMagic, profileName, region,
					skipSetupItems, supervisingHostCerts, supportEmailAddress, supportPhoneNumber, url);
		}
	}
}
