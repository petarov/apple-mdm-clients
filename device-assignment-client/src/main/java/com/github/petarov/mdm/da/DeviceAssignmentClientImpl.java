package com.github.petarov.mdm.da;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.petarov.mdm.da.config.DeviceAssignmentServerToken;
import com.github.petarov.mdm.da.model.*;
import com.github.petarov.mdm.da.util.OAuth1a;
import com.github.petarov.mdm.shared.http.HttpClientWrapper;
import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import com.github.petarov.mdm.shared.http.HttpConsts;
import com.github.petarov.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;

import java.net.http.HttpRequest;
import java.util.Set;

class DeviceAssignmentClientImpl implements DeviceAssignmentClient {

	private static final String HEADER_X_ADM_AUTH_SESSION              = "X-ADM-Auth-Session";
	private static final String HEADER_X_SERVER_PROTOCOL_VERSION       = "X-Server-Protocol-Version";
	private static final String HEADER_X_SERVER_PROTOCOL_VERSION_VALUE = "3";
	private static final String HEADER_CONTENT_TYPE_VALUE              = "application/json;charset=UTF8";

	private final HttpClientWrapper           client;
	private final DeviceAssignmentServerToken serverToken;

	private String sessionId;

	public DeviceAssignmentClientImpl(@Nonnull HttpClientWrapper client,
			@Nonnull DeviceAssignmentServerToken serverToken) {
		this.client = client;
		this.serverToken = serverToken;
	}

	private void refreshSessionId() {
		var authHeaderBuilder = new StringBuilder();
		authHeaderBuilder.append("OAuth ");

		var oauth = new OAuth1a(serverToken);
		var authParams = oauth.getAuthParams(client.getOptions().random());
		authParams.forEach((k, v) -> authHeaderBuilder.append(k).append("=").append("\"").append(v).append("\","));
		authHeaderBuilder.append("oauth_signature=\"");
		authHeaderBuilder.append(oauth.generateSignature("GET", client.createURI("/session").toString(), authParams));
		authHeaderBuilder.append("\"");

		var request = client.createRequestBuilder(client.createURI("/session")).GET()
				.setHeader(HttpConsts.HEADER_AUTHORIZATION, authHeaderBuilder.toString()).build();
		var sessionResponse = client.send(request, SessionResponse.class);

		sessionId = sessionResponse.authSessionToken();
	}

	private <T> T execute(HttpRequest.Builder requestBuilder, Class<T> clazz) {
		if (sessionId == null) {
			refreshSessionId();
		}

		requestBuilder.setHeader(HttpConsts.HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
		requestBuilder.setHeader(HEADER_X_SERVER_PROTOCOL_VERSION, HEADER_X_SERVER_PROTOCOL_VERSION_VALUE);

		boolean retry = false;
		for (; ; ) {
			try {
				requestBuilder.setHeader(HEADER_X_ADM_AUTH_SESSION, sessionId);
				return client.send(requestBuilder.build(), clazz);
			} catch (HttpClientWrapperException e) {
				if (client.isResponseUnauthorized(e.getStatusCode()) && !retry) {
					System.out.println("REFRESHING TOKEN ..."); // TODO
					// New session id may be needed
					refreshSessionId();
					// Retry only once
					retry = true;
				} else {
					throw new DeviceAssignmentException(e);
				}
			}
		}
	}

	@Nonnull
	@Override
	public AccountDetail fetchAccount() {
		return execute(client.createRequestBuilder(client.createURI("/account")).GET(), AccountDetail.class);
	}

	@Nonnull
	@Override
	public DevicesResponse fetchDevices(String cursor, int limit) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/server/devices"))
					.POST(HttpRequest.BodyPublishers.ofByteArray(JsonUtil.createObjectMapper().writer()
							.writeValueAsBytes(new FetchDeviceRequest(cursor, limit)))), DevicesResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public DeviceListResponse fetchDeviceDetails(@Nonnull Set<String> serialNumbers) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/devices"))
					.POST(HttpRequest.BodyPublishers.ofByteArray(JsonUtil.createObjectMapper().writer()
							.writeValueAsBytes(new DeviceListRequest(serialNumbers)))), DeviceListResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
