package com.github.petarov.mdm.aab.legacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petarov.mdm.aab.legacy.model.response.*;
import com.github.petarov.mdm.shared.http.HttpClientWrapper;
import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import com.github.petarov.mdm.shared.http.HttpConsts;
import com.github.petarov.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

class LegacyAppAndBookClientImpl implements LegacyAppAndBookClient {

	static final Logger logger = LoggerFactory.getLogger(LegacyAppAndBookClientImpl.class);

	private final HttpClientWrapper                  client;
	private final LegacyAppAndBookToken              serverToken;
	private final ObjectMapper                       objectMapper;
	private final Supplier<VppServiceConfigResponse> serviceConfigSupplier;

	public LegacyAppAndBookClientImpl(@Nonnull HttpClientWrapper client, @Nonnull LegacyAppAndBookToken serverToken) {
		this.client = client;
		this.serverToken = serverToken;
		this.objectMapper = JsonUtil.createObjectMapper();
		this.serviceConfigSupplier = new Supplier<>() {

			private VppServiceConfigResponse resp;

			@Override
			public VppServiceConfigResponse get() {
				if (resp == null) {
					resp = fetchServiceConfiguration();
				}
				return resp;
			}
		};
	}

	private Map<String, Object> params(@Nullable String k1, @Nullable Object v1, @Nullable String k2,
			@Nullable Object v2, @Nullable String k3, @Nullable Object v3, @Nullable String k4, @Nullable Object v4) {
		var result = new HashMap<String, Object>();
		if (k4 != null) {
			result.put(k4, v4);
		}
		if (k3 != null) {
			result.put(k3, v3);
		}
		if (k2 != null) {
			result.put(k2, v2);
		}
		if (k1 != null) {
			result.put(k1, v1);
		}
		result.put("sToken", serverToken.sToken());
		return result;
	}

	private Map<String, Object> params(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
		return params(k1, v1, k2, v2, k3, v3, null, null);
	}

	private Map<String, Object> params(String k1, Object v1, String k2, Object v2) {
		return params(k1, v1, k2, v2, null, null);
	}

	private Map<String, Object> params(String k, Object v) {
		return params(k, v, null, null);
	}

	private Map<String, Object> params() {
		return params(null, null);
	}

	private <T> T execute(HttpRequest.Builder requestBuilder, Class<T> clazz) {
		requestBuilder.setHeader(HttpConsts.HEADER_CONTENT_TYPE, HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8);

		try {
			return client.send(requestBuilder.build(), clazz);
		} catch (HttpClientWrapperException e) {
			// Retry-After behavior
			// See - https://developer.apple.com/documentation/devicemanagement/app_and_book_management/managing_apps_and_books_through_web_services#3230015
			if (e.getStatusCode() == HttpConsts.STATUS_SERVICE_UNAVAILABLE) {
				throw new LegacyAppAndBookClientRetryAfterException(e);
			}

			throw new LegacyAppAndBookClientException(e);
		}
	}

	private <T> HttpRequest.BodyPublisher ofBody(T obj) {
		try {
			return HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(obj));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error serializing to json: " + obj.getClass().getName(), e);
		}
	}

	@Nonnull
	@Override
	public VppServiceConfigResponse fetchServiceConfiguration() {
		return execute(client.createRequestBuilder(client.createURI("/VPPServiceConfigSrv")).GET(),
				VppServiceConfigResponse.class);
	}

	@Nonnull
	@Override
	public VppClientConfigResponse updateClientConfiguration(String clientContext, String notificationToken) {
		return null;
	}

	@Nonnull
	@Override
	public VppGetAssetResponse fetchAssets(boolean includeLicenseCounts, String pricingParam) {
		var params = params("includeLicenseCounts", includeLicenseCounts);
		if (!pricingParam.isBlank()) {
			params.put("pricingParam", pricingParam);
		}
		return execute(client.createRequestBuilder(URI.create(serviceConfigSupplier.get().getVPPAssetsSrvUrl()))
				.POST(ofBody(params)), VppGetAssetResponse.class);
	}

	@Nonnull
	@Override
	public VppGetAssignmentsResponse fetchAssignments(String adamIdStr,
			@Nonnull FetchAssignmentsOptions assignmentOptions, String requestId, int pageIndex) {
		var params = params();
		if (!adamIdStr.isBlank()) {
			params.put("adamIdStr", adamIdStr);
		}

		if (!assignmentOptions.clientUserIdStr().isBlank()) {
			params.put("clientUserIdStr", assignmentOptions.clientUserIdStr());
		} else if (!assignmentOptions.serialNumber().isBlank()) {
			params.put("serialNumber", assignmentOptions.serialNumber());
		}

		if (!requestId.isBlank()) {
			params.put("requestId", requestId);
		}

		if (pageIndex > 0) {
			params.put("pageIndex", pageIndex);
		}

		return execute(client.createRequestBuilder(URI.create(serviceConfigSupplier.get().getAssignmentsSrvUrl()))
				.POST(ofBody(params)), VppGetAssignmentsResponse.class);
	}

	@Nonnull
	@Override
	public VppManageLicensesByAdamIdResponse manageUserLicenses(String adamIdStr,
			@Nonnull Set<String> associateClientUserIdStrs, @Nonnull Set<String> disassociateClientUserIdStrs,
			boolean notifyDisassociation) {
		return null;
	}

	@Nonnull
	@Override
	public VppManageLicensesByAdamIdResponse manageDeviceLicenses(String adamIdStr,
			@Nonnull Set<String> associateSerialNumbers, @Nonnull Set<String> disassociateSerialNumbers,
			boolean notifyDisassociation) {
		return null;
	}

	@Nonnull
	@Override
	public VppManageLicensesByAdamIdResponse disassociateLicenses(String adamIdStr,
			@Nonnull Set<String> disassociateLicenseIdStrs, boolean notifyDisassociation) {
		return null;
	}

	@Nonnull
	@Override
	public VppGetUserResponse fetchUser(@Nonnull UserIdParam userIdParam) {
		var params = params();
		if (!userIdParam.clientUserIdStr().isBlank()) {
			params.put("clientUserIdStr", userIdParam.clientUserIdStr());
		} else {
			params.put("userId", userIdParam.userId());
		}

		if (!userIdParam.itsIdHash().isBlank()) {
			params.put("itsIdHash", userIdParam.itsIdHash());
		}

		return execute(client.createRequestBuilder(URI.create(serviceConfigSupplier.get().getUserSrvUrl()))
				.POST(ofBody(params)), VppGetUserResponse.class);
	}

	@Nonnull
	@Override
	public VppGetUsersResponse fetchUsers(String batchToken, @Nonnull FetchUsersOptions options) {
		var params = params();
		if (!batchToken.isBlank()) {
			params.put("batchToken", batchToken);
		}

		if (!options.sinceModifiedToken().isBlank()) {
			params.put("sinceModifiedToken", options.sinceModifiedToken());
		}

		if (options.includeRetiredOnly()) {
			params.put("includeRetiredOnly", true);
		} else if (options.includeRetired()) {
			params.put("includeRetired", true);
		}

		return execute(client.createRequestBuilder(URI.create(serviceConfigSupplier.get().getUsersSrvUrl()))
				.POST(ofBody(params)), VppGetUsersResponse.class);
	}

	@Nonnull
	@Override
	public VppRegisterUserResponse registerUser(String clientUserIdStr, String email, String managedAppleIDStr) {
		return null;
	}

	@Nonnull
	@Override
	public VppEditUserResponse editUser(@Nonnull UserIdParam userIdParam, String email, String managedAppleIDStr) {
		return null;
	}

	@Nonnull
	@Override
	public VppRetireUserResponse retireUser(@Nonnull UserIdParam userIdParam) {
		return null;
	}
}
