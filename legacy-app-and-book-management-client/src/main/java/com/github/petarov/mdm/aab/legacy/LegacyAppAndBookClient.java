package com.github.petarov.mdm.aab.legacy;

import com.github.petarov.mdm.aab.legacy.model.response.VppClientConfigResponse;
import com.github.petarov.mdm.aab.legacy.model.response.VppEditUserResponse;
import com.github.petarov.mdm.aab.legacy.model.response.VppRetireUserResponse;
import com.github.petarov.mdm.aab.legacy.model.response.VppServiceConfigResponse;
import jakarta.annotation.Nonnull;

public interface LegacyAppAndBookClient {

	/**
	 * @return new {@link LegacyAppAndBookClientBuilder} instance
	 */
	@Nonnull
	static LegacyAppAndBookClientBuilder newBuilder() {
		return new LegacyAppAndBookClientBuilder();
	}

	/**
	 * Fetches the full list of web service URLs and a list of possible error numbers.
	 *
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/service-configuration">Service Configuration</a>
	 */
	@Nonnull
	VppServiceConfigResponse fetchServiceConfiguration();

	/**
	 * Stores client-specific information on the server.
	 *
	 * @param clientContext     any JSON string under 256 bytes. The server stores the value of this field, and this value
	 *                          is returned in all responses. To clear the field’s value, provide an empty string as the
	 *                          input value {@code ""}.
	 * @param notificationToken the token to use when sending notifications through {@code notificationURL}
	 * @param sToken            required authentication token. See <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 * @return {@link VppClientConfigResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/client-configuration">Client Configuration</a>
	 */
	@Nonnull
	VppClientConfigResponse updateClientConfiguration(String clientContext, String notificationToken, String sToken);

	/**
	 * Retires a user account.
	 * <p>
	 * This service disassociates a VPP user from its iTunes account and releases the revocable licenses associated
	 * with the VPP user. The revoked licenses can then be assigned to other users in the organization.
	 * <p>
	 * Currently, ebook licenses are irrevocable.
	 * <p>A retired VPP user can be reregistered, in the same organization, using the
	 * <a href="https://developer.apple.com/documentation/devicemanagement/register-a-user">Register a User</a>
	 * endpoint.
	 *
	 * @param sToken          required the authentication token.
	 *                        For more information, see <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 * @param clientUserIdStr the identifier supplied by the client when registering a user.
	 *                        Either {@code clientUserIdStr} or {@code userId} is required.
	 *                        If both {@code clientUserIdStr} and {@code userId} are supplied, {@code userId} takes precedence.
	 * @param userId          the unique identifier assigned by the VPP when registering the user.
	 *                        Either {@code clientUserIdStr} or {@code userId} is required.
	 *                        If both {@code clientUserIdStr} and {@code userId} are supplied, {@code userId} takes precedence.
	 * @return {@link VppRetireUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/retire-a-user">Retire a User</a>
	 */
	@Nonnull
	VppRetireUserResponse retireUser(String sToken, String clientUserIdStr, long userId);

	/**
	 * Modifies details about a user.
	 *
	 * @param sToken
	 * @param clientUserIdStr
	 * @param userId
	 * @return {@link VppEditUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/edit-a-user">Edit a User</a>
	 */
	@Nonnull
	VppEditUserResponse editUser(String sToken, String clientUserIdStr, String email, String itsIdHash,
			String managedAppleIDStr, long userId); // TODO: improve params
}
