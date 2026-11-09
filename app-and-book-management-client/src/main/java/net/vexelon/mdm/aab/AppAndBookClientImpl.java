package net.vexelon.mdm.aab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.aab.model.*;
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
import java.util.*;

class AppAndBookClientImpl implements AppAndBookClient {

	static final Logger logger = LoggerFactory.getLogger(AppAndBookClientImpl.class);

	static final Duration SERVICE_CONFIG_TTL = Duration.ofMinutes(5);

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
		requestBuilder.setHeader(HttpConsts.HEADER_CONTENT_TYPE, HttpConsts.HEADER_VALUE_APPLICATION_JSON);

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
	public EventResponse associateAssets(@Nonnull Set<RequestAsset> assets, @Nonnull Set<String> clientUserIds,
			@Nonnull Set<String> serialNumbers) {
		var body = manageAssetsBody(assets, clientUserIds, serialNumbers);
		return execute(endpointRequestBuilder("associateAssets", "/assets/associate", List.of()).POST(ofBody(body)),
				EventResponse.class);
	}

	@Nonnull
	@Override
	public EventResponse disassociateAssets(@Nonnull Set<RequestAsset> assets, @Nonnull Set<String> clientUserIds,
			@Nonnull Set<String> serialNumbers) {
		var body = manageAssetsBody(assets, clientUserIds, serialNumbers);
		return execute(
				endpointRequestBuilder("disassociateAssets", "/assets/disassociate", List.of()).POST(ofBody(body)),
				EventResponse.class);
	}

	@Nonnull
	@Override
	public EventResponse revokeAssets(@Nonnull Set<String> clientUserIds, @Nonnull Set<String> serialNumbers) {
		var body = new LinkedHashMap<String, Object>();
		putTargets(body, clientUserIds, serialNumbers);
		return execute(endpointRequestBuilder("revokeAssets", "/assets/revoke", List.of()).POST(ofBody(body)),
				EventResponse.class);
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

	@Nonnull
	@Override
	public EventResponse createUsers(@Nonnull Set<RequestUser> users) {
		return execute(
				endpointRequestBuilder("createUsers", "/users/create", List.of()).POST(ofBody(manageUsersBody(users))),
				EventResponse.class);
	}

	@Nonnull
	@Override
	public EventResponse updateUsers(@Nonnull Set<RequestUser> users) {
		return execute(
				endpointRequestBuilder("updateUsers", "/users/update", List.of()).POST(ofBody(manageUsersBody(users))),
				EventResponse.class);
	}

	@Nonnull
	@Override
	public EventResponse retireUsers(@Nonnull Set<String> clientUserIds) {
		var requestUsers = new ArrayList<Map<String, String>>();
		for (var clientUserId : clientUserIds) {
			requestUsers.add(Map.of("clientUserId", clientUserId));
		}

		var body = new LinkedHashMap<String, Object>();
		body.put("users", requestUsers);
		return execute(endpointRequestBuilder("retireUsers", "/users/retire", List.of()).POST(ofBody(body)),
				EventResponse.class);
	}

	@Nonnull
	@Override
	public StatusResponse fetchEventStatus(@Nonnull String eventId) {
		if (eventId.isBlank()) {
			throw new IllegalArgumentException("eventId must not be blank");
		}

		var params = new ArrayList<String>();
		addParam(params, "eventId", eventId);

		return execute(endpointRequestBuilder("eventStatus", "/status", params).GET(), StatusResponse.class);
	}

	private static Map<String, Object> manageAssetsBody(Set<RequestAsset> assets, Set<String> clientUserIds,
			Set<String> serialNumbers) {
		var requestAssets = new ArrayList<Map<String, String>>();
		for (var asset : assets) {
			requirePricingParam(asset.pricingParam());

			var requestAsset = new LinkedHashMap<String, String>();
			requestAsset.put("adamId", asset.adamId());

			if (asset.pricingParam() != null) {
				requestAsset.put("pricingParam", asset.pricingParam().jsonName());
			}
			requestAssets.add(requestAsset);
		}

		var body = new LinkedHashMap<String, Object>();
		body.put("assets", requestAssets);
		putTargets(body, clientUserIds, serialNumbers);
		return body;
	}

	private static Map<String, Object> manageUsersBody(Set<RequestUser> users) {
		var requestUsers = new ArrayList<Map<String, String>>();
		for (var user : users) {
			var requestUser = new LinkedHashMap<String, String>();
			requestUser.put("clientUserId", user.clientUserId());

			if (user.email() != null) {
				requestUser.put("email", user.email());
			}
			if (user.managedAppleId() != null) {
				requestUser.put("managedAppleId", user.managedAppleId());
			}
			requestUsers.add(requestUser);
		}

		var body = new LinkedHashMap<String, Object>();
		body.put("users", requestUsers);
		return body;
	}

	private static void putTargets(Map<String, Object> body, Set<String> clientUserIds, Set<String> serialNumbers) {
		body.put("clientUserIds", List.copyOf(clientUserIds));
		body.put("serialNumbers", List.copyOf(serialNumbers));
	}
}
