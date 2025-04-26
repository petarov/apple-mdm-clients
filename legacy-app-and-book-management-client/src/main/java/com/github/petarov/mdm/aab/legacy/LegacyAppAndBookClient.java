package com.github.petarov.mdm.aab.legacy;

import com.github.petarov.mdm.aab.legacy.model.response.*;
import jakarta.annotation.Nonnull;

import java.util.Set;

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
	 * @return {@link VppClientConfigResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/client-configuration">Client Configuration</a>
	 */
	@Nonnull
	VppClientConfigResponse updateClientConfiguration(String clientContext, String notificationToken);

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
	 * Fetches a list of assignments currently assigned to a user or device.
	 *
	 * @param adamIdStr         the unique identifier for a product in the iTunes Store
	 * @param assignmentOptions see {@link FetchAssignmentsOptions}
	 * @param requestId         a unique ID that is used when making paginated requests
	 * @param pageIndex         the index of the page to lookup. To page through the assignments, use the
	 *                          {@code nextPageIndex} value returned in the previous {@link VppGetAssignmentsResponse}.
	 *                          This must be used in combination with a {@code requestId}, also from the previous
	 *                          response.
	 * @return {@link VppGetAssignmentsResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-assignments-158kc">Get Assignments</a>
	 */
	@Nonnull
	VppGetAssignmentsResponse fetchAssignments(String adamIdStr, @Nonnull FetchAssignmentsOptions assignmentOptions,
			String requestId, int pageIndex);

	/**
	 * Fetches all user and device assignments for specified product {@code adamIdStr}.
	 *
	 * @see #fetchAssignments(String, FetchAssignmentsOptions, String, int)
	 */
	@Nonnull
	default VppGetAssignmentsResponse fetchAssignments(String adamIdStr) {
		return fetchAssignments(adamIdStr, new FetchAssignmentsOptions("", ""), "", 0);
	}

	/**
	 * Fetches next page of assignments given a {@code requestId} and {@code pageIndex}.
	 *
	 * @see #fetchAssignments(String, FetchAssignmentsOptions, String, int)
	 */
	@Nonnull
	default VppGetAssignmentsResponse fetchAssignments(String requestId, int pageIndex) {
		return fetchAssignments("", new FetchAssignmentsOptions("", ""), requestId, pageIndex);
	}

	/**
	 * Associates and disassociates licenses with users.
	 *
	 * @param adamIdStr                 the unique identifier for a product in the iTunes Store
	 * @param associateClientUserIds    a list of client-user IDs to associate licenses with
	 * @param disassociateClientUserIds a list of client-user IDs to disassociate licenses from
	 * @param notifyDisassociation      if {@code true}, sends notifications when licenses are disassociated
	 * @return {@link VppManageLicensesByAdamIdResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/manage-licenses">Manage Licenses</a>
	 */
	@Nonnull
	VppManageLicensesByAdamIdResponse manageUserLicenses(String adamIdStr, @Nonnull Set<String> associateClientUserIds,
			@Nonnull Set<String> disassociateClientUserIds, boolean notifyDisassociation);

	/**
	 * Associates and disassociates licenses with devices.
	 *
	 * @param adamIdStr                 the unique identifier for a product in the iTunes Store
	 * @param associateSerialNumbers    a list of device serial numbers to associate licenses with.
	 * @param disassociateSerialNumbers a list of device serial numbers to disassociate licenses from
	 * @param notifyDisassociation      if {@code true}, sends notifications when licenses are disassociated
	 * @return {@link VppManageLicensesByAdamIdResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/manage-licenses">Manage Licenses</a>
	 */
	@Nonnull
	VppManageLicensesByAdamIdResponse manageDeviceLicenses(String adamIdStr,
			@Nonnull Set<String> associateSerialNumbers, @Nonnull Set<String> disassociateSerialNumbers,
			boolean notifyDisassociation);

	/**
	 * Disassociate licenses by their ids.
	 *
	 * @param adamIdStr            the unique identifier for a product in the iTunes Store
	 * @param licenseIds           a list of license IDs to disassociate from the user ID or device
	 * @param notifyDisassociation if {@code true}, sends notifications when licenses are disassociated
	 * @return {@link VppManageLicensesByAdamIdResponse} object
	 */
	@Nonnull
	VppManageLicensesByAdamIdResponse disassociateLicenses(String adamIdStr, @Nonnull Set<String> licenseIds,
			boolean notifyDisassociation);

	/**
	 * Fetches information about a particular user.
	 *
	 * @param userIdParam the user id. See {@link UserIdParam}.
	 * @return {@link VppGetUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-a-user">Get a User</a>
	 */
	@Nonnull
	VppGetUserResponse fetchUser(@Nonnull UserIdParam userIdParam);

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
	 * @param batchToken a token that signifies which batch is being returned
	 * @param options    additional non-mandatory options
	 * @return {@link VppGetUsersResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-users-5boi1">Get Users</a>
	 */
	@Nonnull
	VppGetUsersResponse fetchUsers(String batchToken, @Nonnull FetchUsersOptions options);

	/**
	 * @see #fetchUsers(String, FetchUsersOptions)
	 */
	@Nonnull
	default VppGetUsersResponse fetchUsers(String batchToken) {
		return fetchUsers(batchToken, new FetchUsersOptions("", false, false));
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
	 * @param clientUserId   required identifier supplied by the client when registering a user: the identifier must be unique within the organization
	 * @param email             the user’s email address
	 * @param managedAppleID the Apple ID associated with the user. This ID's organization must match that of the provided sToken.
	 * @return {@link VppEditUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/register-a-user">Register a User</a>
	 */
	@Nonnull
	VppRegisterUserResponse registerUser(String clientUserId, String email, String managedAppleID);

	/**
	 * @see #registerUser(String, String, String)
	 */
	@Nonnull
	default VppRegisterUserResponse registerUser(String clientUserId, String email) {
		return registerUser(clientUserId, email, "");
	}

	/**
	 * Modifies details about a user.
	 *
	 * @param userIdParam       the user id. See {@link UserIdParam}.
	 * @param email             the user's email address i.e. the email field is updated only if the value is provided in the request
	 * @param managedAppleID the Apple ID associated with the user. This ID's organization must match that of the provided sToken.
	 * @return {@link VppEditUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/edit-a-user">Edit a User</a>
	 */
	@Nonnull
	VppEditUserResponse editUser(@Nonnull UserIdParam userIdParam, String email, String managedAppleID);

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
	 * @param userIdParam the user id. See {@link UserIdParam}.
	 * @return {@link VppRetireUserResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/retire-a-user">Retire a User</a>
	 */
	@Nonnull
	VppRetireUserResponse retireUser(@Nonnull UserIdParam userIdParam);

	/**
	 * Union of fetch assignment parameters. Only one of the two parameters must be set.
	 *
	 * @param clientUserIdStr if specified, returns only assignments assigned to the given client user ID
	 * @param serialNumber    if specified, returns only assignments assigned to the given device serial number
	 */
	record FetchAssignmentsOptions(String clientUserIdStr, String serialNumber) {

		@Nonnull
		public static FetchAssignmentsOptions ofClientUserIdStr(String clientUserIdStr) {
			return new FetchAssignmentsOptions(clientUserIdStr, "");
		}

		@Nonnull
		public static FetchAssignmentsOptions ofSerialNumber(String serialNumber) {
			return new FetchAssignmentsOptions("", serialNumber);
		}
	}

	/**
	 * Union of user id parameters.
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
		public static UserIdParam of(String clientUserIdStr, String itsIdHash) {
			return new UserIdParam(clientUserIdStr, 0, itsIdHash);
		}

		@Nonnull
		public static UserIdParam of(String clientUserIdStr) {
			return of(clientUserIdStr, "");
		}

		@Nonnull
		public static UserIdParam of(long userId) {
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
	record FetchUsersOptions(String sinceModifiedToken, boolean includeRetired, boolean includeRetiredOnly) {

		public FetchUsersOptions(String sinceModifiedToken, boolean includeRetired) {
			this(sinceModifiedToken, includeRetired, false);
		}
	}
}
