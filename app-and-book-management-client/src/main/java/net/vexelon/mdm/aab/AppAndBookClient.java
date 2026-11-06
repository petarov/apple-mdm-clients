package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.aab.model.PricingParam;
import net.vexelon.mdm.aab.model.ProductType;
import net.vexelon.mdm.aab.model.response.GetAssetsResponse;
import net.vexelon.mdm.aab.model.response.GetAssignmentsResponse;
import net.vexelon.mdm.aab.model.response.GetUsersResponse;

/**
 * Client interface for Apple's App and Book Management API.
 * <p>
 * Authenticates using a content token (sToken) obtained from Apple Business Manager.
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
}
