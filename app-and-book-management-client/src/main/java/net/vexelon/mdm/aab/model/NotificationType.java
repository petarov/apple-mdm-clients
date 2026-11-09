package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.annotation.Nonnull;

/**
 * The type of a notification.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/subscribing-to-notifications">devicemanagement/subscribing-to-notifications</a>
 */
public enum NotificationType {
	/**
	 * Sent when notifications are configured with Client Config. Can only be requested; never in a subscribed or supported set.
	 */
	TEST_NOTIFICATION("TEST_NOTIFICATION"),
	/**
	 * Sent when an asset is associated or disassociated.
	 */
	ASSET_MANAGEMENT("ASSET_MANAGEMENT"),
	/**
	 * Sent when a user is created, updated or retired.
	 */
	USER_MANAGEMENT("USER_MANAGEMENT"),
	/**
	 * Sent when a user accepts an invitation.
	 */
	USER_ASSOCIATED("USER_ASSOCIATED"),
	/**
	 * Sent when the total asset count changes, e.g. through a purchase, transfer or refund.
	 */
	ASSET_COUNT("ASSET_COUNT"),
	/**
	 * Sent when a subscription is associated or disassociated.
	 */
	SUBSCRIPTION_MANAGEMENT("SUBSCRIPTION_MANAGEMENT"),
	/**
	 * Sent when the subscription seat inventory changes.
	 */
	SUBSCRIPTION_COUNT("SUBSCRIPTION_COUNT"),
	/**
	 * Sent when the set of unlimited-license assets changes.
	 */
	UNLIMITED_ASSET("UNLIMITED_ASSET"),
	@JsonEnumDefaultValue UNKNOWN("");

	private final String jsonName;

	NotificationType(String jsonName) {
		this.jsonName = jsonName;
	}

	/**
	 * @return the value as sent in the {@code notificationTypes} request field, e.g. {@code ASSET_COUNT}
	 */
	@Nonnull
	public String jsonName() {
		return jsonName;
	}
}
