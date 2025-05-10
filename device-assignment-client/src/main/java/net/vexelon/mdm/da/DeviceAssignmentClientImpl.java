package net.vexelon.mdm.da;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.da.model.*;
import net.vexelon.mdm.da.util.OAuth1a;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import net.vexelon.mdm.shared.http.HttpConsts;
import net.vexelon.mdm.shared.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class DeviceAssignmentClientImpl implements DeviceAssignmentClient {

	static final Logger logger = LoggerFactory.getLogger(DeviceAssignmentClientImpl.class);

	private static final String HEADER_X_ADM_AUTH_SESSION              = "X-ADM-Auth-Session";
	private static final String HEADER_X_SERVER_PROTOCOL_VERSION       = "X-Server-Protocol-Version";
	private static final String HEADER_X_SERVER_PROTOCOL_VERSION_VALUE = "3";

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
		authHeaderBuilder.append(
				oauth.generateSignature("GET", client.complementURI("/session").toString(), authParams));
		authHeaderBuilder.append("\"");

		var request = client.createRequestBuilder(client.complementURI("/session")).GET()
				.setHeader(HttpConsts.HEADER_AUTHORIZATION, authHeaderBuilder.toString()).build();
		var sessionResponse = client.send(request, SessionResponse.class);

		sessionId = sessionResponse.authSessionToken();
	}

	private <T> T execute(HttpRequest.Builder requestBuilder, Class<T> clazz) {
		if (sessionId == null) {
			refreshSessionId();
		}

		requestBuilder.setHeader(HttpConsts.HEADER_CONTENT_TYPE, HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8);
		requestBuilder.setHeader(HEADER_X_SERVER_PROTOCOL_VERSION, HEADER_X_SERVER_PROTOCOL_VERSION_VALUE);

		boolean retry = false;
		for (; ; ) {
			try {
				requestBuilder.setHeader(HEADER_X_ADM_AUTH_SESSION, sessionId);
				return client.send(requestBuilder.build(), clazz);
			} catch (HttpClientWrapperException e) {
				if (client.isResponseUnauthorized(e.getStatusCode()) && !retry) {
					logger.info("Refreshing expired auth token ...");
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

	private <T> HttpRequest.BodyPublisher ofBody(T obj) {
		try {
			return HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(obj));
		} catch (JsonProcessingException e) {
			throw new HttpClientWrapperException("Error serializing to json: " + obj.getClass().getName(), e);
		}
	}

	@Nonnull
	@Override
	public AccountDetail fetchAccount() {
		return execute(client.createRequestBuilder(client.complementURI("/account")).GET(), AccountDetail.class);
	}

	@Nonnull
	@Override
	public DevicesResponse fetchDevices(String cursor, int limit) {
		return execute(client.createRequestBuilder(client.complementURI("/server/devices"))
						.POST(ofBody(cursor.isBlank() ? Map.of("limit", limit) : Map.of("cursor", cursor, "limit", limit))),
				DevicesResponse.class);
	}

	@Nonnull
	@Override
	public DeviceListResponse fetchDeviceDetails(@Nonnull Set<String> serialNumbers) {
		return execute(client.createRequestBuilder(client.complementURI("/devices"))
				.POST(ofBody(Map.of("devices", serialNumbers))), DeviceListResponse.class);
	}

	@Nonnull
	@Override
	public DevicesResponse syncDevices(String cursor, int limit) {
		return execute(client.createRequestBuilder(client.complementURI("/devices/sync"))
				.POST(ofBody(Map.of("cursor", cursor, "limit", limit))), DevicesResponse.class);
	}

	@Nonnull
	@Override
	public DeviceStatusResponse disownDevices(@Nonnull Set<String> serialNumbers) {
		return execute(client.createRequestBuilder(client.complementURI("/devices/disown"))
				.POST(ofBody(Map.of("devices", serialNumbers))), DeviceStatusResponse.class);
	}

	@Nonnull
	@Override
	public ProfileDevicesResponse createProfile(@Nonnull Profile profile) {
		return execute(client.createRequestBuilder(client.complementURI("/profile")).POST(ofBody(profile)),
				ProfileDevicesResponse.class);
	}

	@Nonnull
	@Override
	public Optional<Profile> fetchProfile(String profileUuid) {
		try {
			return Optional.of(
					execute(client.createRequestBuilder(client.complementURI("/profile?profile_uuid=" + profileUuid))
							.GET(), Profile.class));
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
		return execute(client.createRequestBuilder(client.complementURI("/profile/devices"))
						.POST(ofBody(Map.of("profile_uuid", profileUuid, "devices", serialNumbers))),
				ProfileDevicesResponse.class);
	}

	@Nonnull
	@Override
	public ProfileDevicesResponse unassignProfile(String profileUuid, @Nonnull Set<String> serialNumbers) {
		return execute(client.createRequestBuilder(client.complementURI("/profile/devices"))
						.method("DELETE", ofBody(Map.of("profile_uuid", profileUuid, "devices", serialNumbers))),
				ProfileDevicesResponse.class);
	}

	@Nonnull
	@Override
	public ActivationLockStatusResponse enableActivationLock(String serialNumber, String escrowKey,
			String lostMessage) {
		var params = new HashMap<String, String>();
		params.put("device", serialNumber);

		if (!escrowKey.isBlank()) {
			params.put("escrow_key", escrowKey);
		}

		if (!lostMessage.isBlank()) {
			params.put("lost_message", lostMessage);
		}

		return execute(client.createRequestBuilder(client.complementURI("/device/activationlock")).POST(ofBody(params)),
				ActivationLockStatusResponse.class);
	}

	@Nonnull
	@Override
	public SeedBuildTokenResponse fetchBetaEnrollmentTokens() {
		return execute(client.createRequestBuilder(client.complementURI("/os-beta-enrollment/tokens")).GET(),
				SeedBuildTokenResponse.class);
	}
}
