package com.github.petarov.mdm.da;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petarov.mdm.da.config.DeviceAssignmentServerToken;
import com.github.petarov.mdm.da.model.*;
import com.github.petarov.mdm.da.util.OAuth1a;
import com.github.petarov.mdm.shared.http.HttpClientWrapper;
import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import com.github.petarov.mdm.shared.http.HttpConsts;
import com.github.petarov.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;

import java.net.http.HttpRequest;
import java.util.Optional;
import java.util.Set;

class DeviceAssignmentClientImpl implements DeviceAssignmentClient {

	private static final String HEADER_X_ADM_AUTH_SESSION              = "X-ADM-Auth-Session";
	private static final String HEADER_X_SERVER_PROTOCOL_VERSION       = "X-Server-Protocol-Version";
	private static final String HEADER_X_SERVER_PROTOCOL_VERSION_VALUE = "3";
	private static final String HEADER_CONTENT_TYPE_VALUE              = "application/json;charset=UTF8";

	private final HttpClientWrapper           client;
	private final DeviceAssignmentServerToken serverToken;
	private final ObjectMapper                objectMapper;

	private String sessionId;

	public DeviceAssignmentClientImpl(@Nonnull HttpClientWrapper client,
			@Nonnull DeviceAssignmentServerToken serverToken) {
		this.client = client;
		this.serverToken = serverToken;
		this.objectMapper = JsonUtil.createObjectMapper();
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
							.POST(HttpRequest.BodyPublishers.ofByteArray(
									objectMapper.writer().writeValueAsBytes(new FetchDeviceRequest(cursor, limit)))),
					DevicesResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public DeviceListResponse fetchDeviceDetails(@Nonnull Set<String> serialNumbers) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/devices"))
							.POST(HttpRequest.BodyPublishers.ofByteArray(
									objectMapper.writer().writeValueAsBytes(new DeviceListRequest(serialNumbers)))),
					DeviceListResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public DevicesResponse syncDevices(String cursor, int limit) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/devices/sync"))
							.POST(HttpRequest.BodyPublishers.ofByteArray(
									objectMapper.writer().writeValueAsBytes(new FetchDeviceRequest(cursor, limit)))),
					DevicesResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public DeviceStatusResponse disownDevices(@Nonnull Set<String> serialNumbers) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/devices/disown"))
							.POST(HttpRequest.BodyPublishers.ofByteArray(
									objectMapper.writer().writeValueAsBytes(new DeviceListRequest(serialNumbers)))),
					DeviceStatusResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public ProfileDevicesResponse createProfile(@Nonnull Profile profile) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/profile"))
							.POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writer().writeValueAsBytes(profile))),
					ProfileDevicesResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public Optional<Profile> fetchProfile(String profileUuid) {
		try {
			return Optional.of(
					execute(client.createRequestBuilder(client.createURI("/profile?profile_uuid=" + profileUuid)).GET(),
							Profile.class));
		} catch (DeviceAssignmentException e) {
			if (e.getCode() == HttpConsts.STATUS_NOT_FOUND) {
				return Optional.empty();
			}
			throw e;
		}
	}

	@Nonnull
	@Override
	public ProfileDevicesResponse assignProfile(String profileUuid, @Nonnull Set<String> serialNumbers) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/profile/devices"))
							.POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writer()
									.writeValueAsBytes(new ProfileDevicesRequest(profileUuid, serialNumbers)))),
					ProfileDevicesResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public ProfileDevicesResponse unassignProfile(String profileUuid, @Nonnull Set<String> serialNumbers) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/profile/devices")).method("DELETE",
							HttpRequest.BodyPublishers.ofByteArray(objectMapper.writer()
									.writeValueAsBytes(new ProfileDevicesRequest(profileUuid, serialNumbers)))),
					ProfileDevicesResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public ActivationLockStatusResponse enableActivationLock(String serialNumber, String escrowKey,
			String lostMessage) {
		try {
			return execute(client.createRequestBuilder(client.createURI("/device/activationlock"))
							.POST(HttpRequest.BodyPublishers.ofByteArray(objectMapper.writer()
									.writeValueAsBytes(new ActivationLockRequest(serialNumber, escrowKey, lostMessage)))),
					ActivationLockStatusResponse.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public SeedBuildTokenResponse fetchBetaEnrollmentTokens() {
		return execute(client.createRequestBuilder(client.createURI("/os-beta-enrollment/tokens")).GET(),
				SeedBuildTokenResponse.class);
	}
}
