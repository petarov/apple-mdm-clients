package com.github.petarov.mdm.da.model;

public enum ProfileSkipItem {

	KEYBOARD("Keyboard", OsType.IOS.mask()),
	DEVICE_TO_DEVICE_MIGRATION("DeviceToDeviceMigration", OsType.IOS.mask()),
	SIM_SETUP("SIMSetup", OsType.IOS.mask()),
	PRIVACY("Privacy", OsType.IOS.mask() + OsType.MACOS.mask() + OsType.TVOS.mask()),
	PASSCODE("Passcode", OsType.IOS.mask()),
	BIOMETRIC("Biometric", OsType.IOS.mask() + OsType.MACOS.mask()),
	PAYMENT("Payment", OsType.IOS.mask() + OsType.MACOS.mask()),
	RESTORE("Restore", OsType.IOS.mask() + OsType.MACOS.mask()),
	// TODO ...
	// ---
	;

	private String  value;
	private int     os;
	private boolean isDeprecated;

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

	ProfileSkipItem(String value, int os, boolean isDeprecated) {
		this.value = value;
		this.os = os;
		this.isDeprecated = isDeprecated;
	}

	ProfileSkipItem(String value, int os) {
		this(value, os, false);
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
