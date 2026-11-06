package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.aab.model.ProductType;
import net.vexelon.mdm.aab.model.PricingParam;
import net.vexelon.mdm.aab.model.response.GetAssetsResponse;
import net.vexelon.mdm.aab.model.response.GetAssignmentsResponse;
import net.vexelon.mdm.aab.model.response.GetUsersResponse;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class AppAndBookClientImpl implements AppAndBookClient {

	static final Logger logger = LoggerFactory.getLogger(AppAndBookClientImpl.class);

	private final HttpClientWrapper client;
	private final AppAndBookToken   serverToken;

	AppAndBookClientImpl(@Nonnull HttpClientWrapper client, @Nonnull AppAndBookToken serverToken) {
		this.client = client;
		this.serverToken = serverToken;
	}

	private <T> T execute(HttpRequest.Builder requestBuilder, Class<T> clazz) {
		requestBuilder.setHeader(HttpConsts.HEADER_CONTENT_TYPE, HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8);
		requestBuilder.setHeader(HttpConsts.HEADER_AUTHORIZATION, "Bearer " + serverToken.sToken());

		try {
			return client.send(requestBuilder.build(), clazz);
		} catch (HttpClientWrapperException e) {
			// Retry-After behavior
			// See - https://developer.apple.com/documentation/devicemanagement/handling-error-responses#Handle-retry-after-headers
			if (e.getStatusCode() / 100 == 5 && AppAndBookClientRetryAfterException.findRetryAfter(e.headers())
					.isPresent()) {
				throw new AppAndBookClientRetryAfterException(e);
			}

			throw new AppAndBookClientException(e);
		}
	}

	private static void addParam(List<String> params, String name, String value) {
		if (value != null && !value.isBlank()) {
			params.add(name + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8));
		}
	}

	private static void addParam(List<String> params, String name, Object value) {
		if (value != null) {
			params.add(name + "=" + value);
		}
	}

	private static void requirePricingParam(PricingParam pricingParam) {
		if (pricingParam == PricingParam.UNKNOWN) {
			throw new IllegalArgumentException("pricingParam must not be UNKNOWN");
		}
	}

	private static void addPricingParam(List<String> params, PricingParam pricingParam) {
		if (pricingParam != null) {
			addParam(params, "pricingParam", pricingParam.jsonName());
		}
	}

	@Nonnull
	@Override
	public GetAssetsResponse fetchAssets(@Nonnull FetchAssetsOptions options, int pageIndex) {
		if (options.productType() == ProductType.UNKNOWN) {
			throw new IllegalArgumentException("productType must not be UNKNOWN");
		}
		requirePricingParam(options.pricingParam());

		var params = new ArrayList<String>();
		if (pageIndex > 0) {
			addParam(params, "pageIndex", pageIndex);
		}
		if (options.productType() != null) {
			addParam(params, "productType", options.productType().jsonName());
		}
		addPricingParam(params, options.pricingParam());
		addParam(params, "revocable", options.revocable());
		addParam(params, "deviceAssignable", options.deviceAssignable());
		addParam(params, "maxAvailableCount", options.maxAvailableCount());
		addParam(params, "minAvailableCount", options.minAvailableCount());
		addParam(params, "maxAssignedCount", options.maxAssignedCount());
		addParam(params, "minAssignedCount", options.minAssignedCount());
		addParam(params, "adamId", options.adamId());
		addParam(params, "unlimited", options.unlimited());

		var path = "/assets" + (params.isEmpty() ? "" : "?" + String.join("&", params));
		return execute(client.createRequestBuilder(client.complementURI(path)).GET(), GetAssetsResponse.class);
	}

	@Nonnull
	@Override
	public GetAssignmentsResponse fetchAssignments(String adamId, @Nonnull FetchAssignmentsOptions options,
			int pageIndex) {
		requirePricingParam(options.pricingParam());

		var params = new ArrayList<String>();
		if (pageIndex > 0) {
			addParam(params, "pageIndex", pageIndex);
		}
		addParam(params, "adamId", adamId);
		if (!options.clientUserId().isBlank()) {
			addParam(params, "clientUserId", options.clientUserId());
		} else if (!options.serialNumber().isBlank()) {
			addParam(params, "serialNumber", options.serialNumber());
		}
		addPricingParam(params, options.pricingParam());
		addParam(params, "excludeInactiveUsers", options.excludeInactiveUsers());
		addParam(params, "includeUserState", options.includeUserState());
		addParam(params, "sinceVersionId", options.sinceVersionId());

		var path = "/assignments" + (params.isEmpty() ? "" : "?" + String.join("&", params));
		return execute(client.createRequestBuilder(client.complementURI(path)).GET(), GetAssignmentsResponse.class);
	}

	@Nonnull
	@Override
	public GetUsersResponse fetchUsers(@Nonnull FetchUsersOptions options, int pageIndex) {
		var params = new ArrayList<String>();
		if (pageIndex > 0) {
			addParam(params, "pageIndex", pageIndex);
		}
		addParam(params, "clientUserId", options.clientUserId());
		addParam(params, "activeOnly", options.activeOnly());
		addParam(params, "retiredOnly", options.retiredOnly());
		addParam(params, "sinceVersionId", options.sinceVersionId());

		var path = "/users" + (params.isEmpty() ? "" : "?" + String.join("&", params));
		return execute(client.createRequestBuilder(client.complementURI(path)).GET(), GetUsersResponse.class);
	}
}
