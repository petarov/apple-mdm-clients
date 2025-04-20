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

	private static final String HEADER_X_ADM_AUTH_SESSION = "X-ADM-Auth-Session";
	private static final String HEADER_CONTENT_TYPE_VALUE = "application/json;charset=UTF8";

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

	private <K, V> Map<K, V> params(@Nonnull K k1, @Nonnull V v1, @Nullable K k2, @Nullable V v2, @Nullable K k3,
			@Nullable V v3, @Nullable K k4, @Nullable V v4) {
		var result = new HashMap<K, V>();
		if (k4 != null) {
			result.put(k4, v4);
		}
		if (k3 != null) {
			result.put(k3, v3);
		}
		if (k2 != null) {
			result.put(k2, v2);
		}
		result.put(k1, v1);
		return result;
	}

	private <K, V> Map<K, V> params(K k1, V v1, K k2, V v2, K k3, V v3) {
		return params(k1, v1, k2, v2, k3, v3, null, null);
	}

	private <K, V> Map<K, V> params(K k1, V v1, K k2, V v2) {
		return params(k1, v1, k2, v2, null, null);
	}

	private <K, V> Map<K, V> params(K k, V v) {
		return params(k, v, null, null);
	}

	private <T> T execute(HttpRequest.Builder requestBuilder, Class<T> clazz) {
		requestBuilder.setHeader(HttpConsts.HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);

		try {
			return client.send(requestBuilder.build(), clazz);
		} catch (HttpClientWrapperException e) {
			// TODO:
			throw e;
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
	public VppClientConfigResponse updateClientConfiguration(String sToken, String clientContext,
			String notificationToken) {
		return null;
	}

	@Nonnull
	@Override
	public VppGetAssetResponse fetchAssets(boolean includeLicenseCounts, String pricingParam) {
		var params = params("sToken", serverToken.sToken(), "includeLicenseCounts", includeLicenseCounts);
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
		Map<String, Object> params = params("sToken", serverToken.sToken());
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

		//		Request(
		//				Method.POST,
		//				serviceConfig.getAssignmentsSrvUrl
		//		).body(
		//				with(StringBuilder()) {
		//			append("""{ "sToken": "${token.sToken}"""")
		//			if (adamIdStr.isNotBlank()) {
		//				append(""", "adamIdStr": "$adamIdStr"""")
		//			}
		//			if (clientUserIdStr.isNotBlank()) {
		//				append(""", "clientUserIdStr": $clientUserIdStr""")
		//			}
		//			if (serialNumber.isNotBlank()) {
		//				append(""", "serialNumber": "$serialNumber"""")
		//			}
		//			if (requestId.isNotBlank()) {
		//				append(""", "requestId": "$requestId"""")
		//			}
		//			if (pageIndex > 0) {
		//				append(""", "pageIndex": $pageIndex""")
		//			}
		//			append(" }")
		//		}.toString()
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
	public VppGetUserResponse fetchUser(String sToken, @Nonnull UserIdParam userIdParam) {
		return null;
	}

	@Nonnull
	@Override
	public VppGetUsersResponse fetchUsers(String sToken, String batchToken, @Nonnull FetchUsersOptions options) {
		return null;
	}

	@Nonnull
	@Override
	public VppRegisterUserResponse registerUser(String sToken, String clientUserIdStr, String email,
			String managedAppleIDStr) {
		return null;
	}

	@Nonnull
	@Override
	public VppEditUserResponse editUser(String sToken, @Nonnull UserIdParam userIdParam, String email,
			String managedAppleIDStr) {
		return null;
	}

	@Nonnull
	@Override
	public VppRetireUserResponse retireUser(String sToken, @Nonnull UserIdParam userIdParam) {
		return null;
	}
}
