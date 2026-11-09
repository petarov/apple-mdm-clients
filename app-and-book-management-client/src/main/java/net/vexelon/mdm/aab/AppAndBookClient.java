package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.aab.model.MdmInfo;
import net.vexelon.mdm.aab.model.NotificationType;
import net.vexelon.mdm.aab.model.PricingParam;
import net.vexelon.mdm.aab.model.ProductType;
import net.vexelon.mdm.aab.model.RequestAsset;
import net.vexelon.mdm.aab.model.response.*;

import java.util.Set;

/**
 * Client interface for Apple's App and Book Management API.
 * <p>
 * Authenticates using a content token (sToken) obtained from Apple Business Manager.
 * <p>
 * Endpoint URLs are resolved from the service configuration ({@link #fetchServiceConfiguration()}), which is
 * fetched on first use and cached for {@code 5 minutes}, as Apple recommends. If the configuration lacks an endpoint's
 * URL, the client falls back to the endpoint's fixed path on the configured service URL. A failure to fetch the
 * service configuration fails the call.
 * <p>
 * The underlying implementation is not thread-safe. Concurrent access from multiple threads requires
 * external synchronization.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/app-book-and-subscription-management">App, Book, and Subscription Management</a>
 */
public interface AppAndBookClient {

	/**
	 * Creates a new builder for configuring and constructing an {@link AppAndBookClient}.
	 *
	 * @return a new {@link AppAndBookClientBuilder} instance
	 */
	@Nonnull
	static AppAndBookClientBuilder newBuilder() {
		return new AppAndBookClientBuilder();
	}

	/**
	 * Fetches the full list of service URLs, supported notification types, request limits and possible error codes.
	 * <p>
	 * This is the only call that does not authenticate with the server token. It always contacts the server and
	 * also refreshes the cached service configuration used to resolve endpoint URLs.
	 *
	 * @return {@link ServiceConfigResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/service-config">Service Config</a>
	 */
	@Nonnull
	ServiceConfigResponse fetchServiceConfiguration();

	/**
	 * Stores client-specific information on the server.
	 * <p>
	 * Sets the MDM information returned in all subsequent responses (to detect another MDM managing the same
	 * organization) and configures notifications. When notifications are configured, the server sends a
	 * {@link NotificationType#TEST_NOTIFICATION} to the notification URL and rejects the configuration if delivery
	 * fails. Apple recommends checking the client configuration regularly; see
	 * {@link #fetchClientConfiguration()}.
	 *
	 * @param options see {@link ClientConfigOptions}
	 * @return {@link ClientConfigResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/client-config-4szk1">Client Config</a>
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/subscribing-to-notifications">Subscribing to notifications</a>
	 */
	@Nonnull
	ClientConfigResponse updateClientConfiguration(@Nonnull ClientConfigOptions options);

	/**
	 * Fetches the client-specific information stored on the server.
	 * <p>
	 * Returns the current MDM information and notification settings, as set through
	 * {@link #updateClientConfiguration(ClientConfigOptions)}, without changing them. Apple recommends checking the
	 * client configuration regularly to verify the expected values.
	 *
	 * @return {@link ClientConfigResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/client-config-4szk1">Client Config</a>
	 */
	@Nonnull
	ClientConfigResponse fetchClientConfiguration();

	/**
	 * Fetches the set of assets that your organization manages.
	 * <p>
	 * Results are paginated. Pass the {@code nextPageIndex} of the previous {@link GetAssetsResponse} as
	 * {@code pageIndex} until the response no longer contains a next page.
	 * <p>
	 * See <a href="https://developer.apple.com/documentation/devicemanagement/using-paginated-endpoints">Using paginated endpoints</a>
	 *
	 * @param options   asset filters. See {@link FetchAssetsOptions}.
	 * @param pageIndex the zero-based index of the page to fetch
	 * @return {@link GetAssetsResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-assets-4ski1">Get Assets</a>
	 */
	@Nonnull
	GetAssetsResponse fetchAssets(@Nonnull FetchAssetsOptions options, int pageIndex);

	/**
	 * Fetches the first page of assets matching the given filters.
	 *
	 * @see #fetchAssets(FetchAssetsOptions, int)
	 */
	@Nonnull
	default GetAssetsResponse fetchAssets(@Nonnull FetchAssetsOptions options) {
		return fetchAssets(options, 0);
	}

	/**
	 * Fetches the given page of all assets.
	 *
	 * @see #fetchAssets(FetchAssetsOptions, int)
	 */
	@Nonnull
	default GetAssetsResponse fetchAssets(int pageIndex) {
		return fetchAssets(FetchAssetsOptions.ofEmpty(), pageIndex);
	}

	/**
	 * Fetches the first page of all assets.
	 *
	 * @see #fetchAssets(FetchAssetsOptions, int)
	 */
	@Nonnull
	default GetAssetsResponse fetchAssets() {
		return fetchAssets(FetchAssetsOptions.ofEmpty(), 0);
	}

	/**
	 * Asset filters. Empty strings and {@code null} values are omitted from the request.
	 *
	 * @param adamId            the unique identifier for a product in the iTunes Store, or empty string {@code ""} to
	 *                          skip this filter
	 * @param productType       the type of product: {@link ProductType#APP} or {@link ProductType#BOOK}, or
	 *                          {@code null} to skip this filter. {@link ProductType#UNKNOWN} is rejected with an
	 *                          {@link IllegalArgumentException}.
	 * @param pricingParam      the quality of a product: {@link PricingParam#STDQ} or {@link PricingParam#PLUS}, or
	 *                          {@code null} to skip this filter. {@link PricingParam#UNKNOWN} is rejected with an
	 *                          {@link IllegalArgumentException}.
	 * @param revocable         if set, returns only assets whose licenses are (or are not) revocable
	 * @param deviceAssignable  if set, returns only assets whose licenses can (or cannot) be assigned to a device
	 * @param unlimited         if {@code true}, returns only assets with unlimited licenses (in
	 *                          {@code unlimitedAssets}); if {@code false}, returns only assets that are not unlimited
	 * @param minAvailableCount if set, returns only assets with at least this many available licenses
	 * @param maxAvailableCount if set, returns only assets with at most this many available licenses
	 * @param minAssignedCount  if set, returns only assets with at least this many assigned licenses
	 * @param maxAssignedCount  if set, returns only assets with at most this many assigned licenses
	 */
	record FetchAssetsOptions(String adamId, @Nullable ProductType productType, @Nullable PricingParam pricingParam,
	                          @Nullable Boolean revocable, @Nullable Boolean deviceAssignable,
	                          @Nullable Boolean unlimited, @Nullable Integer minAvailableCount,
	                          @Nullable Integer maxAvailableCount, @Nullable Integer minAssignedCount,
	                          @Nullable Integer maxAssignedCount) {

		@Nonnull
		public static FetchAssetsOptions ofEmpty() {
			return new FetchAssetsOptions("", null, null, null, null, null, null, null, null, null);
		}

		@Nonnull
		public static FetchAssetsOptions ofAdamId(String adamId) {
			return new FetchAssetsOptions(adamId, null, null, null, null, null, null, null, null, null);
		}

		@Nonnull
		public static FetchAssetsOptions ofProductType(@Nonnull ProductType productType) {
			return new FetchAssetsOptions("", productType, null, null, null, null, null, null, null, null);
		}
	}

	/**
	 * Fetches the set of current assignments for users or devices.
	 * <p>
	 * Results are paginated. Pass the {@code nextPageIndex} of the previous {@link GetAssignmentsResponse} as
	 * {@code pageIndex}, with the same {@code adamId} and options, until the response no longer contains a next page.
	 * To fetch only assignments modified since a previous fetch, pass that response's {@code versionId} as
	 * {@link FetchAssignmentsOptions#sinceVersionId()}.
	 * <p>
	 * See <a href="https://developer.apple.com/documentation/devicemanagement/using-paginated-endpoints">Using paginated endpoints</a>
	 *
	 * @param adamId    the unique identifier for a product in the store, or empty string {@code ""} for all products
	 * @param options   see {@link FetchAssignmentsOptions}
	 * @param pageIndex the zero-based index of the page to fetch
	 * @return {@link GetAssignmentsResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-assignments-9wv1e">Get Assignments</a>
	 */
	@Nonnull
	GetAssignmentsResponse fetchAssignments(String adamId, @Nonnull FetchAssignmentsOptions options, int pageIndex);

	/**
	 * Fetches the first page of assignments matching the given product and filters.
	 *
	 * @see #fetchAssignments(String, FetchAssignmentsOptions, int)
	 */
	@Nonnull
	default GetAssignmentsResponse fetchAssignments(String adamId, @Nonnull FetchAssignmentsOptions options) {
		return fetchAssignments(adamId, options, 0);
	}

	/**
	 * Fetches the first page of assignments for the given product.
	 *
	 * @see #fetchAssignments(String, FetchAssignmentsOptions, int)
	 */
	@Nonnull
	default GetAssignmentsResponse fetchAssignments(String adamId) {
		return fetchAssignments(adamId, FetchAssignmentsOptions.ofEmpty(), 0);
	}

	/**
	 * Fetches information about a set of users.
	 * <p>
	 * Results are paginated. Pass the {@code nextPageIndex} of the previous {@link GetUsersResponse} as
	 * {@code pageIndex}, with the same options, until the response no longer contains a next page. To fetch only users
	 * modified since a previous fetch, pass that response's {@code versionId} as
	 * {@link FetchUsersOptions#sinceVersionId()}.
	 * <p>
	 * See <a href="https://developer.apple.com/documentation/devicemanagement/using-paginated-endpoints">Using paginated endpoints</a>
	 *
	 * @param options   see {@link FetchUsersOptions}
	 * @param pageIndex the zero-based index of the page to fetch
	 * @return {@link GetUsersResponse} object
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/get-users-4mwln">Get Users</a>
	 */
	@Nonnull
	GetUsersResponse fetchUsers(@Nonnull FetchUsersOptions options, int pageIndex);

	/**
	 * Fetches the first page of users matching the given filters.
	 *
	 * @see #fetchUsers(FetchUsersOptions, int)
	 */
	@Nonnull
	default GetUsersResponse fetchUsers(@Nonnull FetchUsersOptions options) {
		return fetchUsers(options, 0);
	}

	/**
	 * Fetches the given page of all users.
	 *
	 * @see #fetchUsers(FetchUsersOptions, int)
	 */
	@Nonnull
	default GetUsersResponse fetchUsers(int pageIndex) {
		return fetchUsers(FetchUsersOptions.ofEmpty(), pageIndex);
	}

	/**
	 * Fetches the first page of all users.
	 *
	 * @see #fetchUsers(FetchUsersOptions, int)
	 */
	@Nonnull
	default GetUsersResponse fetchUsers() {
		return fetchUsers(FetchUsersOptions.ofEmpty(), 0);
	}

	/**
	 * Assigns assets to users and devices.
	 * <p>
	 * The request is asynchronous: install the assets on a device only after the server sends a successful
	 * notification. Request size limits are dynamic; see {@link ServiceConfigResponse#limits()}
	 * ({@code maxAssets}, {@code maxClientUserIds}, {@code maxSerialNumbers}).
	 *
	 * @param assets        the assets to assign. Must not be empty
	 * @param clientUserIds the client user IDs to assign the assets to, or an empty set (sent as {@code []})
	 * @param serialNumbers the device serial numbers to assign the assets to, or an empty set (sent as {@code []}).
	 *                      At least one of {@code clientUserIds} and {@code serialNumbers} must be non-empty
	 * @return {@link EventResponse} object, whose {@code eventId} identifies the asynchronous event
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/associate-assets">Associate Assets</a>
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/managing-assets">Managing assets</a>
	 */
	@Nonnull
	EventResponse associateAssets(@Nonnull Set<RequestAsset> assets, @Nonnull Set<String> clientUserIds,
			@Nonnull Set<String> serialNumbers);

	/**
	 * Unassigns the given assets from users and devices.
	 * <p>
	 * The request is asynchronous: remove the assets from a device only after the server sends a successful
	 * notification. An unassigned asset that remains installed on a device becomes unusable after 30 days. Request
	 * size limits are the same as for {@link #associateAssets(Set, Set, Set)}.
	 *
	 * @param assets        the assets to unassign. Must not be empty
	 * @param clientUserIds the client user IDs to unassign the assets from, or an empty set (sent as {@code []})
	 * @param serialNumbers the device serial numbers to unassign the assets from, or an empty set (sent as
	 *                      {@code []}). At least one of {@code clientUserIds} and {@code serialNumbers} must be
	 *                      non-empty
	 * @return {@link EventResponse} object, whose {@code eventId} identifies the asynchronous event
	 * @see #revokeAssets(Set, Set)
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/disassociate-assets">Disassociate Assets</a>
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/managing-assets">Managing assets</a>
	 */
	@Nonnull
	EventResponse disassociateAssets(@Nonnull Set<RequestAsset> assets, @Nonnull Set<String> clientUserIds,
			@Nonnull Set<String> serialNumbers);

	/**
	 * Unassigns all assigned revocable assets from users and devices.
	 * <p>
	 * Unlike {@link #disassociateAssets(Set, Set, Set)}, no assets are specified: every revocable asset currently
	 * assigned to the given users and devices is unassigned. The request is asynchronous: remove the assets from a
	 * device only after the server sends a successful notification. Request size limits are dynamic; see
	 * {@link ServiceConfigResponse#limits()} ({@code maxRevokeClientUserIds}, {@code maxRevokeSerialNumbers}).
	 *
	 * @param clientUserIds the client user IDs to revoke all assets from, or an empty set (sent as {@code []})
	 * @param serialNumbers the device serial numbers to revoke all assets from, or an empty set (sent as
	 *                      {@code []}). At least one of {@code clientUserIds} and {@code serialNumbers} must be
	 *                      non-empty
	 * @return {@link EventResponse} object, whose {@code eventId} identifies the asynchronous event
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/revoke-assets">Revoke Assets</a>
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/managing-assets">Managing assets</a>
	 */
	@Nonnull
	EventResponse revokeAssets(@Nonnull Set<String> clientUserIds, @Nonnull Set<String> serialNumbers);

	/**
	 * Fetches the status of an asynchronous event, such as associating, disassociating or revoking assets.
	 * <p>
	 * Compare {@link StatusResponse#numCompleted()} with {@link StatusResponse#numRequested()} to track the progress
	 * of the event; {@link StatusResponse#failures()} describes each failure. Apple recommends using this call to
	 * verify that an event is no longer pending when its notifications are missing.
	 *
	 * @param eventId the unique identifier of the event, as returned in {@link EventResponse#eventId()}. Must not be
	 *                blank
	 * @return {@link StatusResponse} object
	 * @throws IllegalArgumentException if {@code eventId} is blank
	 * @see <a href="https://developer.apple.com/documentation/devicemanagement/events-status">Event Status</a>
	 */
	@Nonnull
	StatusResponse fetchEventStatus(@Nonnull String eventId);

	/**
	 * Union of fetch assignment parameters. Only one of the two parameters {@code clientUserId} and
	 * {@code serialNumber} must be set; if both are set, {@code clientUserId} takes precedence and
	 * {@code serialNumber} is ignored. Empty strings and {@code null} values are omitted from the request.
	 *
	 * @param clientUserId         if specified, returns only assignments assigned to the given client user ID
	 * @param serialNumber         if specified, returns only assignments assigned to the given device serial number
	 * @param pricingParam         if set, returns only assignments of the given product quality:
	 *                             {@link PricingParam#STDQ} or {@link PricingParam#PLUS}.
	 *                             {@link PricingParam#UNKNOWN} is rejected with an {@link IllegalArgumentException}.
	 * @param excludeInactiveUsers when {@code true}, excludes assignments from retired or inactive users
	 * @param includeUserState     when {@code true}, includes {@code userStatus} and {@code idHash} fields in each
	 *                             assignment. Otherwise {@link net.vexelon.mdm.aab.model.Assignment#userStatus()} is
	 *                             {@link net.vexelon.mdm.aab.model.UserStatus#UNKNOWN}
	 * @param sinceVersionId       if specified, returns only assignments modified since the given {@code versionId}
	 *                             of a previous response
	 */
	record FetchAssignmentsOptions(String clientUserId, String serialNumber, @Nullable PricingParam pricingParam,
	                               @Nullable Boolean excludeInactiveUsers, @Nullable Boolean includeUserState,
	                               String sinceVersionId) {

		@Nonnull
		public static FetchAssignmentsOptions ofEmpty() {
			return new FetchAssignmentsOptions("", "", null, null, null, "");
		}

		@Nonnull
		public static FetchAssignmentsOptions ofClientUserId(String clientUserId) {
			return new FetchAssignmentsOptions(clientUserId, "", null, null, null, "");
		}

		@Nonnull
		public static FetchAssignmentsOptions ofSerialNumber(String serialNumber) {
			return new FetchAssignmentsOptions("", serialNumber, null, null, null, "");
		}

		@Nonnull
		public static FetchAssignmentsOptions ofSinceVersionId(String sinceVersionId) {
			return new FetchAssignmentsOptions("", "", null, null, null, sinceVersionId);
		}
	}

	/**
	 * User filters. Empty strings and {@code null} values are omitted from the request.
	 *
	 * @param clientUserId   if specified, returns only users with the given client user ID. Several users may match,
	 *                       as a retired user may have been re-created with the same ID
	 * @param sinceVersionId if specified, returns only users modified since the given {@code versionId} of a
	 *                       previous response
	 * @param activeOnly     when {@code true}, returns only active (nonretired) users
	 * @param retiredOnly    when {@code true}, returns only retired users. If both {@code activeOnly} and
	 *                       {@code retiredOnly} are {@code true}, they are passed through unchanged and the result is
	 *                       up to the server
	 */
	record FetchUsersOptions(String clientUserId, String sinceVersionId, @Nullable Boolean activeOnly,
	                         @Nullable Boolean retiredOnly) {

		@Nonnull
		public static FetchUsersOptions ofEmpty() {
			return new FetchUsersOptions("", "", null, null);
		}

		@Nonnull
		public static FetchUsersOptions ofClientUserId(String clientUserId) {
			return new FetchUsersOptions(clientUserId, "", null, null);
		}

		@Nonnull
		public static FetchUsersOptions ofSinceVersionId(String sinceVersionId) {
			return new FetchUsersOptions("", sinceVersionId, null, null);
		}
	}

	/**
	 * Client configuration. {@code null} values are omitted from the request; empty strings and empty sets are sent
	 * as-is. Apple does not document whether omitted fields keep their stored value.
	 *
	 * @param mdmInfo               the MDM information; Apple requires all of {@code id}, {@code name} and
	 *                              {@code metadata} when set (error {@code 9723}), although its documentation
	 *                              marks them optional
	 * @param notificationUrl       the HTTPS URL to which subscribed notifications POST; host and path only.
	 *                              Apple requires it together with {@code notificationAuthToken}
	 * @param notificationAuthToken the bearer token the server sends in the {@code Authorization} header of
	 *                              notifications (shared secret)
	 * @param notificationTypes     the complete set of notification types to subscribe to; must not contain
	 *                              {@link NotificationType#UNKNOWN}
	 */
	record ClientConfigOptions(@Nullable MdmInfo mdmInfo, @Nullable String notificationUrl,
	                           @Nullable String notificationAuthToken,
	                           @Nullable Set<NotificationType> notificationTypes) {

		@Nonnull
		public static ClientConfigOptions ofMdmInfo(@Nonnull MdmInfo mdmInfo) {
			return new ClientConfigOptions(mdmInfo, null, null, null);
		}

		@Nonnull
		public static ClientConfigOptions ofNotifications(@Nonnull String notificationUrl,
				@Nonnull String notificationAuthToken, @Nonnull Set<NotificationType> notificationTypes) {
			return new ClientConfigOptions(null, notificationUrl, notificationAuthToken, notificationTypes);
		}
	}
}
