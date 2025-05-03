package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Nonnull;

import java.util.Arrays;

/**
 * The list of setup panes to skip.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/skipkeys">SkipKeys</a>
 */
public enum ProfileSkipItem {
	/**
	 * Skips the Accessibility pane.
	 */
	ACCESSIBILITY("Accessibility", OsType.IOS),

	/**
	 * Skips Apple ID setup.
	 * <p>
	 * Availability: iOS 7.0+, tvOS 10.2+, and macOS 10.9+
	 */
	APPLE_ID("AppleID", OsType.IOS, OsType.MACOS, OsType.TVOS),

	/**
	 * Skips the App Analytics pane.
	 * <p>
	 * Availability: iOS 7.0+, tvOS 10.2+, and macOS 10.9+
	 */
	DIAGNOSTICS("Diagnostics", OsType.IOS, OsType.MACOS, OsType.TVOS),

	/**
	 * Hides and disables the passcode pane.
	 * <p>
	 * Availability: iOS 7.0+
	 */
	PASSCODE("Passcode", OsType.IOS),

	/**
	 * Disables restoring from backup.
	 * <p>
	 * Availability: iOS 7.0+ and macOS 10.9+
	 */
	RESTORE("Restore", OsType.IOS, OsType.MACOS),

	/**
	 * Skips Terms and Conditions.
	 * <p>
	 * Availability: iOS 7.0+, tvOS 10.2+, and macOS 10.9+
	 */
	TOS("TOS", OsType.IOS, OsType.MACOS, OsType.TVOS),

	/**
	 * Skips biometric setup.
	 * <p>
	 * Availability: iOS 8.1+ and macOS 10.12.4+
	 */
	BIOMETRIC("Biometric", OsType.IOS, OsType.MACOS),

	/**
	 * Skips Apple Pay setup.
	 * <p>
	 * Availability: iOS 8.1+ and macOS 10.12.4+
	 */
	PAYMENT("Payment", OsType.IOS, OsType.MACOS),

	/**
	 * Skips zoom setup.
	 * <p>
	 * Availability: iOS 8.3+
	 */
	@Deprecated(since = "iOS 17", forRemoval = true) ZOOM("Zoom", OsType.IOS),

	/**
	 * If the Restore pane is not skipped, removes the Move from Android option in the Restore pane on iOS.
	 * <p>
	 * Availability: iOS 9.0+
	 */
	ANDROID("Android", OsType.IOS),

	/**
	 * Skips the iMessage pane.
	 * <p>
	 * Availability: iOS 10.0+
	 */
	MESSAGING_ACTIVATION_USING_PHONE_NUMBER("MessagingActivationUsingPhoneNumber", OsType.IOS),

	/**
	 * Skip Keyboard pane.
	 * <p>
	 * Availability: iOS 11.0+
	 */
	KEYBOARD("Keyboard", OsType.IOS),

	/**
	 * Skips the screen for watch migration.
	 * <p>
	 * Availability: iOS 11.0+
	 */
	WATCH_MIGRATION("WatchMigration", OsType.IOS),

	/**
	 * Skips the privacy pane.
	 * <p>
	 * Availability: iOS 11.13+, tvOS 11.13+, and macOS 10.13.4+
	 */
	PRIVACY("Privacy", OsType.IOS, OsType.MACOS, OsType.TVOS),

	/**
	 * Skips the iMessage and FaceTime screen in iOS.
	 * <p>
	 * Availability: iOS 12.0+
	 */
	IMESSAGE_AND_FACETIME("iMessageAndFaceTime", OsType.IOS),

	/**
	 * Skips the Screen Time pane.
	 * <p>
	 * Availability: iOS 12.0+ and macOS 10.15+
	 */
	SCREEN_TIME("ScreenTime", OsType.IOS, OsType.MACOS),

	/**
	 * Skips the add cellular plan pane.
	 * <p>
	 * Availability: iOS 12.0+
	 */
	SIM_SETUP("SIMSetup", OsType.IOS),

	/**
	 * Skips the mandatory software update screen in iOS.
	 * <p>
	 * Availability: iOS 12.0+
	 */
	SOFTWARE_UPDATE("SoftwareUpdate", OsType.IOS),

	/**
	 * Skips the Choose Your Look screen.
	 * <p>
	 * Availability: iOS 13.0+ and macOS 10.14+
	 */
	APPEARANCE("Appearance", OsType.IOS, OsType.MACOS),

	/**
	 * Skips Device to Device Migration pane.
	 * <p>
	 * Availability: iOS 13.0+
	 */
	DEVICE_TO_DEVICE_MIGRATION("DeviceToDeviceMigration", OsType.IOS),

	/**
	 * Skips the Dictation pane. This pane isn’t always skippable because it appears before the device retrieves the
	 * Cloud Configuration from the server.
	 * <p>
	 * Availability: iOS 13.0+
	 */
	SPOKEN_LANGUAGE("SpokenLanguage", OsType.IOS),

	/**
	 * Skips the Get Started pane.
	 * <p>
	 * Availability: iOS 13.0+
	 */
	WELCOME("Welcome", OsType.IOS, OsType.MACOS),

	/**
	 * Skips the Restore Completed pane.
	 * <p>
	 * Availability: iOS 14.0+
	 */
	RESTORE_COMPLETED("RestoreCompleted", OsType.IOS),

	/**
	 * Skips the Software Update Complete pane.
	 * <p>
	 * Availability: iOS 14.0+
	 */
	UPDATE_COMPLETED("UpdateCompleted", OsType.IOS),

	/**
	 * Skips the App Store pane.
	 * <p>
	 * Availability: iOS 14.3+ and macOS 11.1+
	 */
	APPSTORE("AppStore", OsType.IOS, OsType.MACOS),

	/**
	 * Skips the Safety pane.
	 * <p>
	 * Availability: iOS 16+
	 */
	SAFETY("Safety", OsType.IOS),

	/**
	 * Skips the Terms of Address pane.
	 * <p>
	 * Availability: iOS 16+, macOS 13+
	 */
	TERMS_OF_ADDRESS("TermsOfAddress", OsType.IOS, OsType.MACOS),

	/**
	 * Skips the Action Button configuration pane.
	 * <p>
	 * Availability: iOS 17.0+
	 */
	ACTION_BUTTON("ActionButton", OsType.IOS),

	/**
	 * Skips the Lockdown Mode pane if an Apple ID is set up.
	 * <p>
	 * Availability: iOS 17.1+ and macOS 14+
	 */
	ENABLE_LOCKDOWN_MODE("EnableLockdownMode", OsType.IOS, OsType.MACOS),

	/**
	 * Skips the Camera Button pane.
	 * <p>
	 * Availability: iOS 18.0+
	 */
	CAMERA_BUTTON("CameraButton", OsType.IOS),

	/**
	 * Skips the Intelligence pane.
	 * <p>
	 * Availability: iOS 18+ and macOS 15+
	 */
	INTELLIGENCE("Intelligence", OsType.IOS, OsType.MACOS),

	/**
	 * Skips web content filtering pane.
	 * <p>
	 * Availability: iOS 18.2+
	 */
	WEB_CONTENT_FILTERING("WebContentFiltering", OsType.IOS),

	/**
	 * Skips Safety and Handling pane
	 * <p>
	 * Availability: iOS 18.4+
	 */
	SAFETY_AND_HANDLING("SafetyAndHandling", OsType.IOS),

	// — non-iOS items

	/**
	 * The key to disable the FileVault Setup Assistant screen in macOS.
	 * <p>
	 * Availability: macOS 10.10+
	 */
	FILE_VAULT("FileVault", OsType.MACOS),

	/**
	 * Skips the iCloud Analytics screen.
	 * <p>
	 * Availability: macOS 10.12.4+
	 */
	ICLOUD_DIAGNOSTICS("iCloudDiagnostics", OsType.MACOS),

	/**
	 * Skips the iCloud Documents and Desktop screen in macOS.
	 * <p>
	 * Availability: macOS 10.13.4+
	 */
	ICLOUD_STORAGE("iCloudStorage", OsType.MACOS),

	/**
	 * Skips the tvOS screen about using aerial screensavers in Apple TV.
	 * <p>
	 * Availability: tvOS 10.2+
	 */
	SCREEN_SAVER("ScreenSaver", OsType.TVOS),

	/**
	 * Skips the Tap To Set Up option in AppleTV about using an iOS device to set up your Apple TV.
	 * <p>
	 * Availability: tvOS 10.2+
	 */
	TAP_TO_SETUP("TapToSetup", OsType.TVOS),

	/**
	 * Skips Apple TV home screen layout sync screen.
	 * <p>
	 * Availability: tvOS 11.0+
	 */
	TV_HOMESCREEN_SYNC("TVHomeScreenSync", OsType.TVOS),

	/**
	 * Skips the TV provider sign in screen.
	 * <p>
	 * Availability: tvOS 11.0+
	 */
	TV_PROVIDER_SIGNIN("TVProviderSignIn", OsType.TVOS),

	/**
	 * Skips the "Where is this Apple TV?" screen in tvOS.
	 * <p>
	 * Availability: tvOS 11.4+
	 */
	TV_ROOM("TVRoom", OsType.TVOS),

	/**
	 * Skips Wallpaper setup.
	 * <p>
	 * Availability: macOS 14.1+
	 */
	WALLPAPER("Wallpaper", OsType.MACOS),

	// ---
	;

	private final String  key;
	private final boolean isDeprecated;
	private final int     os;

	private enum OsType {
		IOS(1),
		MACOS(2),
		TVOS(4);

		private final int mask;

		OsType(int mask) {
			this.mask = mask;
		}

		public int mask() {
			return mask;
		}
	}

	ProfileSkipItem(String key, boolean isDeprecated, @Nonnull OsType... os) {
		this.key = key;
		this.os = Arrays.stream(os).mapToInt(OsType::mask).sum();
		this.isDeprecated = isDeprecated;
	}

	ProfileSkipItem(String key, OsType... os) {
		this(key, false, os);
	}

	@JsonValue
	public String getKey() {
		return key;
	}

	public boolean isDeprecated() {
		return isDeprecated;
	}

	public boolean isIos() {
		return (this.os & OsType.IOS.mask()) == OsType.IOS.mask();
	}

	public boolean isMacOS() {
		return (this.os & OsType.MACOS.mask()) == OsType.MACOS.mask();
	}

	public boolean isTvOS() {
		return (this.os & OsType.TVOS.mask()) == OsType.TVOS.mask();
	}
}
