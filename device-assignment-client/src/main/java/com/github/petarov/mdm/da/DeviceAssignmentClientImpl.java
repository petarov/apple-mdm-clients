package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.config.DeviceAssignmentServerToken;
import com.github.petarov.mdm.da.model.AccountDetail;
import com.github.petarov.mdm.da.model.DevicesResponse;
import com.github.petarov.mdm.da.model.FetchDeviceRequest;
import com.github.petarov.mdm.da.model.SessionResponse;
import com.github.petarov.mdm.da.util.OAuth1a;
import com.github.petarov.mdm.shared.http.HttpConsts;
import com.github.petarov.mdm.shared.http.MdmHttpClient;
import com.github.petarov.mdm.shared.util.JsonUtils;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

class DeviceAssignmentClientImpl implements DeviceAssignmentClient {

	private static final String DA_HTTP_X_SERVER_PROTOCOL_VERSION = "3";
	private static final String DA_HTTP_CONTENT_TYPE_VALUE        = "application/json;charset=UTF8";

	private final MdmHttpClient               client;
	private final DeviceAssignmentServerToken serverToken;

	private HttpClient httpClient;
	private String     sessionId = "";

	public DeviceAssignmentClientImpl(@Nonnull MdmHttpClient client, @Nonnull DeviceAssignmentServerToken serverToken) {
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
		authHeaderBuilder.append(
				oauth.generateSignature("GET", client.getOptions().serviceUrl() + "/session", authParams));
		authHeaderBuilder.append("\"");

		try {
			var request = client.createRequestBuilder(client.getOptions().serviceUrl() + "/session").GET()
					.header(HttpConsts.HEADER_AUTHORIZATION, authHeaderBuilder.toString()).build();
			traceReqHeaders(request);
			var resp = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

			if (client.isResponseOk(resp.statusCode())) {
				try (var body = client.getResponseBody(resp)) {
					sessionId = JsonUtils.createObjectMapper().reader().readValue(body, SessionResponse.class)
							.authSessionToken();
				}
			} else {
				throw new DeviceAssignmentException(
						"Error fetching OAuth session token (%d)".formatted(resp.statusCode()), resp.statusCode(),
						new String(resp.body().readAllBytes(), StandardCharsets.UTF_8));
			}
		} catch (IOException e) {
			throw new RuntimeException("Error read/write request", e);
		} catch (InterruptedException e) {
			throw new RuntimeException("Error request-send interrupted", e);
		}
	}

	private String traceReqResp(HttpRequest req, HttpResponse<?> resp) {
		return "%s %s - %d".formatted(req.method(), req.uri(), resp.statusCode());
	}

	private void traceReqHeaders(HttpRequest req) {
		req.headers().map().forEach((k, v) -> System.out.println("HEADER key=" + k + " v=" + v));
	}

	private String execute(HttpRequest.Builder requestBuilder) {
		if (httpClient == null) {
			httpClient = client.createBuilder().build();
		}

		if (sessionId.isBlank()) {
			refreshSessionId();
		}

		requestBuilder.header(HttpConsts.HEADER_CONTENT_TYPE, DA_HTTP_CONTENT_TYPE_VALUE);
		requestBuilder.header("X-Server-Protocol-Version", DA_HTTP_X_SERVER_PROTOCOL_VERSION);
		requestBuilder.header("X-ADM-Auth-Session", sessionId);

		try {
			var req = requestBuilder.build();
			traceReqHeaders(req);

			var resp = httpClient.send(req, HttpResponse.BodyHandlers.ofInputStream());
			if (client.isResponseOk(resp.statusCode())) {
				var body = new String(client.getResponseBody(resp).readAllBytes(), StandardCharsets.UTF_8);
				System.out.println("RESPONSE: " + body);
				return body;
			} else {
				throw new DeviceAssignmentException(traceReqResp(req, resp), resp.statusCode(),
						new String(resp.body().readAllBytes(), StandardCharsets.UTF_8));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Nonnull
	@Override
	public AccountDetail fetchAccount() {
		var result = execute(client.createRequestBuilder(client.getOptions().serviceUrl() + "/account").GET());
		try {
			return JsonUtils.createObjectMapper().reader().readValue(result, AccountDetail.class);
		} catch (IOException e) {
			throw new RuntimeException("Error deserializing json response", e);
		}
	}

	@Nonnull
	@Override
	public DevicesResponse fetchDevices(String cursor, int limit) {
		try {
			var result = execute(client.createRequestBuilder(client.getOptions().serviceUrl() + "/server/devices")
					.POST(HttpRequest.BodyPublishers.ofByteArray(
							JsonUtils.createObjectMapper().writer().writeValueAsBytes(new FetchDeviceRequest()))));
			return JsonUtils.createObjectMapper().reader().readValue(result, DevicesResponse.class);
		} catch (IOException e) {
			throw new RuntimeException("Error deserializing json response", e);
		}
	}
}
