package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.aab.model.response.GetAssetsResponse;

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

	String PRICING_PARAM_STANDARD_QUALITY = "STDQ";
	String PRICING_PARAM_HIGH_QUALITY     = "PLUS";

	String PRODUCT_TYPE_APP  = "App";
	String PRODUCT_TYPE_BOOK = "Book";

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
	 * @param productType       the type of product: {@link #PRODUCT_TYPE_APP} or {@link #PRODUCT_TYPE_BOOK}, or empty
	 *                          string {@code ""} to skip this filter
	 * @param pricingParam      the quality of a product: {@link #PRICING_PARAM_STANDARD_QUALITY} or
	 *                          {@link #PRICING_PARAM_HIGH_QUALITY}, or empty string {@code ""} to skip this filter
	 * @param revocable         if set, returns only assets whose licenses are (or are not) revocable
	 * @param deviceAssignable  if set, returns only assets whose licenses can (or cannot) be assigned to a device
	 * @param unlimited         if {@code true}, returns only assets with unlimited licenses (in
	 *                          {@code unlimitedAssets}); if {@code false}, returns only assets that are not unlimited
	 * @param minAvailableCount if set, returns only assets with at least this many available licenses
	 * @param maxAvailableCount if set, returns only assets with at most this many available licenses
	 * @param minAssignedCount  if set, returns only assets with at least this many assigned licenses
	 * @param maxAssignedCount  if set, returns only assets with at most this many assigned licenses
	 */
	record FetchAssetsOptions(String adamId, String productType, String pricingParam, @Nullable Boolean revocable,
	                          @Nullable Boolean deviceAssignable, @Nullable Boolean unlimited,
	                          @Nullable Integer minAvailableCount, @Nullable Integer maxAvailableCount,
	                          @Nullable Integer minAssignedCount, @Nullable Integer maxAssignedCount) {

		@Nonnull
		public static FetchAssetsOptions ofEmpty() {
			return new FetchAssetsOptions("", "", "", null, null, null, null, null, null, null);
		}

		@Nonnull
		public static FetchAssetsOptions ofAdamId(String adamId) {
			return new FetchAssetsOptions(adamId, "", "", null, null, null, null, null, null, null);
		}

		@Nonnull
		public static FetchAssetsOptions ofProductType(String productType) {
			return new FetchAssetsOptions("", productType, "", null, null, null, null, null, null, null);
		}
	}
}
