package com.github.petarov.mdm.aab.legacy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petarov.mdm.aab.legacy.model.response.*;
import com.github.petarov.mdm.shared.http.HttpClientWrapper;
import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import com.github.petarov.mdm.shared.http.HttpConsts;
import com.github.petarov.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpRequest;
import java.util.Set;

class LegacyAppAndBookClientImpl implements LegacyAppAndBookClient {

	static final Logger logger = LoggerFactory.getLogger(LegacyAppAndBookClientImpl.class);

	private static final String HEADER_X_ADM_AUTH_SESSION = "X-ADM-Auth-Session";
	private static final String HEADER_CONTENT_TYPE_VALUE = "application/json;charset=UTF8";

	private final HttpClientWrapper     client;
	private final LegacyAppAndBookToken serverToken;
	private final ObjectMapper          objectMapper;

	public LegacyAppAndBookClientImpl(@Nonnull HttpClientWrapper client, @Nonnull LegacyAppAndBookToken serverToken) {
		this.client = client;
		this.serverToken = serverToken;
		this.objectMapper = JsonUtil.createObjectMapper();
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
		return null;
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
		return null;
	}

	@Nonnull
	@Override
	public VppGetAssignmentsResponse fetchAssignments(String adamIdStr,
			@Nonnull FetchAssignmentsOptions assignmentOptions, String requestId, int pageIndex) {
		return null;
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
