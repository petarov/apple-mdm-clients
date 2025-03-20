package com.github.petarov.mdm.aab.legacy;

import com.github.petarov.mdm.aab.legacy.model.response.*;
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
	 * @param sToken            required authentication token. See <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 * @param clientContext     any JSON string under 256 bytes. The server stores the value of this field, and this value
	 *                          is returned in all responses. To clear the field’s value, provide an empty string as the
	 *                          input value {@code ""}.
	 * @param notificationToken the token to use when sending notifications through {@code notificationURL}
	 * @return {@link VppClientConfigResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/client-configuration">Client Configuration</a>
	 */
	@Nonnull
	VppClientConfigResponse updateClientConfiguration(String sToken, String clientContext, String notificationToken);

	/**
	 * Fetches the set of assets managed by your organization.
	 *
	 * @param includeLicenseCounts if {@code true}, returns the total number of licenses, the number of assigned
	 *                             licenses, and the number of unassigned licenses in the response for each asset.
	 * @param pricingParam         the quality of a product in the iTunes Store. If a pricing parameter is specified,
	 *                             only records with that parameter are included in the results. Possible values are:
	 *                             <ul>
	 *                             <li>{@code STDQ}: Standard quality
	 *                             <li>{@code PLUS}: High quality
	 * @return {@link VppGetAssetResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-assets-44p83">Get Assets</a>
	 */
	@Nonnull
	VppGetAssetResponse fetchAssets(boolean includeLicenseCounts, String pricingParam);

	/**
	 * @see #fetchAssets(boolean, String)
	 */
	@Nonnull
	default VppGetAssetResponse fetchAssets(boolean includeLicenseCounts) {
		return fetchAssets(includeLicenseCounts, "");
	}

	/**
	 * Fetches information about a particular user.
	 *
	 * @param sToken      required authentication token. See <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 * @param userIdParam the user id. See {@link UserIdParam}.
	 * @return {@link VppGetUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-a-user">Get a User</a>
	 */
	@Nonnull
	VppGetUserResponse fetchUser(String sToken, @Nonnull UserIdParam userIdParam);

	/**
	 * Fetches information about a set of users.
	 * <p>
	 * For an initial request that doesn't include {@code batchToken} or {@code sinceModifiedToken},
	 * a batchToken value is returned if the number of results exceeds a server-controlled limit. Subsequent requests
	 * must include batchToken. As long as additional batches remain, the server returns a new batchToken value in
	 * its response.
	 * <p>
	 * See <a href="https://developer.apple.com/documentation/devicemanagement/retrieving-a-large-record-set#Batched-Responses">Batched Responses</a>
	 *
	 * @param sToken     required authentication token. See <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 * @param batchToken a token that signifies which batch is being returned
	 * @param options    additional non-mandatory options
	 * @return {@link VppGetUsersResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-users-5boi1">Get Users</a>
	 */
	@Nonnull
	VppGetUsersResponse fetchUsers(String sToken, String batchToken, @Nonnull UsersRequestOptions options);

	/**
	 * @see #fetchUsers(String, String, UsersRequestOptions)
	 */
	@Nonnull
	default VppGetUsersResponse fetchUsers(String sToken, String batchToken) {
		return fetchUsers(sToken, batchToken, new UsersRequestOptions("", false, false));
	}

	/**
	 * Registers a user with the volume-purchase program.
	 * <p>
	 * The {@code clientUserIdStr} must be unique within the organization and may not be changed once a user is
	 * registered. It should not, for example, be an email address, because an email address might be reused by a
	 * future user. It can be, for example, the <i>GUID</i> of the user.
	 * <p>
	 * When a user is first registered, the user's initial status is <i>Registered</i>.
	 * <ul>
	 * <li>If the user has already been registered, as identified by {@code clientUserIdStr}, the following occurs:
	 * <li>If the user's status is <i>Registered</i> or <i>Associated</i>, that active user account is returned.
	 * <li>If the user's status is <i>Retired</i> and the user has never been assigned to an iTunes account, the account's status is changed to <i>Registered</i> and the existing user is returned.
	 * <li>If the user's status is <i>Retired</i> and the user has previously been assigned to an iTunes account, a new account is created.
	 *
	 * @param sToken            required authentication token. See <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 * @param clientUserIdStr   required identifier supplied by the client when registering a user: the identifier must be unique within the organization
	 * @param email             the user’s email address
	 * @param managedAppleIDStr the Apple ID associated with the user. This ID's organization must match that of the provided sToken.
	 * @return {@link VppEditUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/register-a-user">Register a User</a>
	 */
	@Nonnull
	VppRegisterUserResponse registerUser(String sToken, String clientUserIdStr, String email, String managedAppleIDStr);

	/**
	 * Modifies details about a user.
	 *
	 * @param sToken            required authentication token. See <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 * @param userIdParam       the user id. See {@link UserIdParam}.
	 * @param email             the user's email address i.e. the email field is updated only if the value is provided in the request
	 * @param managedAppleIDStr the Apple ID associated with the user. This ID's organization must match that of the provided sToken.
	 * @return {@link VppEditUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/edit-a-user">Edit a User</a>
	 */
	@Nonnull
	VppEditUserResponse editUser(String sToken, @Nonnull UserIdParam userIdParam, String email,
			String managedAppleIDStr);

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
	 * @param sToken      required the authentication token.
	 *                    For more information, see <a href="https://developer.apple.com/documentation/devicemanagement/managing-apps-and-books-through-web-services-legacy#Authentication">Authentication</a>.
	 * @param userIdParam the user id. See {@link UserIdParam}.
	 * @return {@link VppRetireUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/retire-a-user">Retire a User</a>
	 */
	@Nonnull
	VppRetireUserResponse retireUser(String sToken, @Nonnull UserIdParam userIdParam);


	/**
	 * Union of user id parameters. Only one of the two parameters must be set.
	 * <p>
	 * Either {@code clientUserIdStr} or {@code userId} is required. If {@code clientUserIdStr} is used, an
	 * {@code itsIdHash} (iTunes Store ID hash) value may be included, but it is optional. If {@code userId} has a
	 * value, only that value is used, and {@code clientUserIdStr} and {@code itsIdHash} are ignored.
	 * <p>
	 * To obtain a retired user record previously associated with an iTunes Store account, your MDM server can pass
	 * either the {@code userId} for that record or the {@code clientUserIdStr} and {@code itsIdHash}  for that record.
	 *
	 * @param clientUserIdStr the identifier supplied by the client when registering a user.
	 *                        Either {@code clientUserIdStr} or {@code userId} is required.
	 *                        If both {@code clientUserIdStr} and {@code userId} are supplied, {@code userId} takes precedence.
	 * @param userId          the unique identifier assigned by the VPP when registering the user.
	 *                        Either {@code clientUserIdStr} or {@code userId} is required.
	 * @param itsIdHash       (optional) the hash of the user's iTunes Store ID
	 */
	record UserIdParam(String clientUserIdStr, long userId, String itsIdHash) {

		@Nonnull
		static UserIdParam of(String clientUserIdStr, String itsIdHash) {
			return new UserIdParam(clientUserIdStr, 0, itsIdHash);
		}

		@Nonnull
		static UserIdParam of(String clientUserIdStr) {
			return of(clientUserIdStr, "");
		}

		@Nonnull
		static UserIdParam of(long userId) {
			return new UserIdParam("", userId, "");
		}
	}

	/**
	 * The request for the users' details service.
	 *
	 * @param sinceModifiedToken a token that marks a place in time or empty string {@code ""} to skip this parameter.
	 *                           After all records have been returned for a request, the server includes a
	 *                           {@code sinceModifiedToken} value in the response. Use it in
	 *                           subsequent requests to get licenses that have been modified since the token was
	 *                           generated.
	 * @param includeRetired     if {@code true}, returns retired users in the results
	 * @param includeRetiredOnly if {@code true}, returns only retired users in the results
	 */
	record UsersRequestOptions(String sinceModifiedToken, boolean includeRetired, boolean includeRetiredOnly) {

		public UsersRequestOptions(String sinceModifiedToken, boolean includeRetired) {
			this(sinceModifiedToken, includeRetired, false);
		}
	}
}
