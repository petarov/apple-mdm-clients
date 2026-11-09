package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * The status of a user.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/managing-users">devicemanagement/managing-users</a>
 */
public enum UserStatus {
	/**
	 * The user was created, but has no Apple Account yet.
	 */
	REGISTERED,
	/**
	 * The user has an Apple Account; an {@code idHash} is generated.
	 */
	ASSOCIATED,
	/**
	 * The user was retired.
	 */
	RETIRED,
	/**
	 * Deprecated state: the user was retired and the Apple Account moved to a new user with the same
	 * {@code clientUserId}.
	 */
	DELETED,
	@JsonEnumDefaultValue UNKNOWN
}
