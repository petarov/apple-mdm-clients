package net.vexelon.mdm.aab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.aab.model.NotificationType;
import net.vexelon.mdm.aab.model.PricingParam;
import net.vexelon.mdm.aab.model.ProductType;
import net.vexelon.mdm.aab.model.response.*;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import net.vexelon.mdm.shared.http.HttpConsts;
import net.vexelon.mdm.shared.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

class AppAndBookClientImpl implements AppAndBookClient {

	static final Logger logger = LoggerFactory.getLogger(AppAndBookClientImpl.class);

	static final Duration SERVICE_CONFIG_TTL = Duration.ofMinutes(5);

	private static final String CONTENT_TYPE_JSON = "application/json";

	private final HttpClientWrapper client;
	private final AppAndBookToken   serverToken;
	private final Clock             clock;
	private final ObjectMapper      objectMapper = JsonUtil.createObjectMapper();

	private ServiceConfigResponse serviceConfig;
	private Instant               serviceConfigFetchedAt;

	AppAndBookClientImpl(@Nonnull HttpClientWrapper client, @Nonnull AppAndBookToken serverToken,
			@Nonnull Clock clock) {
		this.client = client;
		this.serverToken = serverToken;
		this.clock = clock;
	}

	private <T> T execute(HttpRequest.Builder requestBuilder, Class<T> clazz) {
		requestBuilder.setHeader(HttpConsts.HEADER_AUTHORIZATION, "Bearer " + serverToken.sToken());
		return send(requestBuilder, clazz);
	}

	private <T> T send(HttpRequest.Builder requestBuilder, Class<T> clazz) {
		requestBuilder.setHeader(HttpConsts.HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);

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

	@Nonnull
	@Override
	public ServiceConfigResponse fetchServiceConfiguration() {
		var response = send(client.createRequestBuilder(client.complementURI("/service/config")).GET(),
				ServiceConfigResponse.class);
		serviceConfig = response;
		serviceConfigFetchedAt = clock.instant();
		return response;
	}

	private ServiceConfigResponse cachedServiceConfig() {
		if (serviceConfig == null || !clock.instant().isBefore(serviceConfigFetchedAt.plus(SERVICE_CONFIG_TTL))) {
			return fetchServiceConfiguration();
		}
		return serviceConfig;
	}

	/**
	 * Creates a request builder for the endpoint URL stored under {@code urlKey} in the cached service
	 * configuration, or for {@code fallbackPath} on the configured service URL if the key is missing or blank.
	 * A failure to fetch the service configuration propagates.
	 */
	private HttpRequest.Builder endpointRequestBuilder(String urlKey, String fallbackPath, List<String> params) {
		var query = params.isEmpty() ? "" : "?" + String.join("&", params);
		var url = cachedServiceConfig().urls().get(urlKey);
		if (url == null || url.isBlank()) {
			return client.createRequestBuilder(client.complementURI(fallbackPath + query));
		}
		return client.createRequestBuilder(url + query);
	}

	private HttpRequest.BodyPublisher ofBody(Object obj) {
		try {
			System.out.println("\n\nREQUEST: " + objectMapper.writeValueAsString(obj) + "\n");
			return HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(obj));
		} catch (JsonProcessingException e) {
			throw new AppAndBookClientException("Error serializing to json: " + obj.getClass().getName(), e);
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
	public ClientConfigResponse updateClientConfiguration(@Nonnull ClientConfigOptions options) {
		var types = options.notificationTypes();
		if (types != null && types.contains(NotificationType.UNKNOWN)) {
			throw new IllegalArgumentException("notificationTypes must not contain UNKNOWN");
		}

		var body = new LinkedHashMap<String, Object>();
		if (options.mdmInfo() != null) {
			body.put("mdmInfo", options.mdmInfo());
		}
		if (options.notificationAuthToken() != null) {
			body.put("notificationAuthToken", options.notificationAuthToken());
		}
		if (options.notificationUrl() != null) {
			body.put("notificationUrl", options.notificationUrl());
		}
		if (types != null) {
			body.put("notificationTypes", types.stream().map(NotificationType::jsonName).toList());
		}

		return execute(endpointRequestBuilder("clientConfig", "/client/config", List.of()).POST(ofBody(body)),
				ClientConfigResponse.class);
	}

	@Nonnull
	@Override
	public ClientConfigResponse fetchClientConfiguration() {
		return execute(endpointRequestBuilder("clientConfig", "/client/config", List.of()).GET(),
				ClientConfigResponse.class);
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

		return execute(endpointRequestBuilder("getAssets", "/assets", params).GET(), GetAssetsResponse.class);
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

		return execute(endpointRequestBuilder("getAssignments", "/assignments", params).GET(),
				GetAssignmentsResponse.class);
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

		return execute(endpointRequestBuilder("getUsers", "/users", params).GET(), GetUsersResponse.class);
	}
}
