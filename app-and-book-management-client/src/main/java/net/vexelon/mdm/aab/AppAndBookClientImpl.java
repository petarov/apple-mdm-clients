package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.aab.model.response.GetAssetsResponse;
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

	@Nonnull
	@Override
	public GetAssetsResponse fetchAssets(@Nonnull FetchAssetsOptions options, int pageIndex) {
		var params = new ArrayList<String>();
		if (pageIndex > 0) {
			addParam(params, "pageIndex", pageIndex);
		}
		addParam(params, "productType", options.productType());
		addParam(params, "pricingParam", options.pricingParam());
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
}
