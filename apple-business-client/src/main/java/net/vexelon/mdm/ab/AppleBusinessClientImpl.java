package net.vexelon.mdm.ab;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.devices.OrgDeviceField;
import net.vexelon.mdm.ab.model.response.device.OrgDeviceResponse;
import net.vexelon.mdm.ab.model.response.device.OrgDevicesResponse;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import net.vexelon.mdm.shared.http.HttpConsts;
import net.vexelon.mdm.shared.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.stream.Collectors;

class AppleBusinessClientImpl implements AppleBusinessClient {

	static final Logger logger = LoggerFactory.getLogger(AppleBusinessClientImpl.class);

	private final HttpClientWrapper       client;
	private final AppleBusinessClientAuth auth;
	private final ObjectMapper            objectMapper;

	AppleBusinessClientImpl(@Nonnull HttpClientWrapper client, @Nonnull AppleBusinessClientAuth auth) {
		this.client = client;
		this.auth = auth;
		this.objectMapper = JsonUtil.createObjectMapper();
	}

	private <T> T executeFn(HttpRequest.Builder requestBuilder, Function<HttpRequest, T> sender) {
		requestBuilder.setHeader(HttpConsts.HEADER_CONTENT_TYPE, HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8);

		boolean retry = false;
		for (; ; ) {
			try {
				requestBuilder.setHeader(HttpConsts.HEADER_AUTHORIZATION, "Bearer " + auth.getAccessToken());
				return sender.apply(requestBuilder.build());
			} catch (HttpClientWrapperException e) {
				if (client.isResponseUnauthorized(e.getStatusCode()) && !retry) {
					logger.info("Refreshing expired access token ...");
					auth.refreshAccessToken();
					retry = true;
				} else {
					throw new AppleBusinessException(e);
				}
			}
		}
	}

	<T> T execute(HttpRequest.Builder requestBuilder, Class<T> clazz) {
		return executeFn(requestBuilder, req -> client.send(req, clazz));
	}

	void execute(HttpRequest.Builder requestBuilder) {
		executeFn(requestBuilder, req -> {
			client.send(req);
			return null;
		});
	}

	@Nonnull
	@Override
	public OrgDevicesResponse fetchOrgDevices(@Nonnull EnumSet<OrgDeviceField> fields, int limit, String cursor) {
		var path = new StringBuilder("/orgDevices");
		var params = new ArrayList<String>();
		if (!fields.isEmpty()) {
			// fields[orgDevices] — brackets must be percent-encoded in the URI
			params.add("fields%5BorgDevices%5D=" + fields.stream()
					.map(f -> URLEncoder.encode(f.fieldName(), StandardCharsets.UTF_8))
					.collect(Collectors.joining(",")));
		}
		if (limit > 0) {
			params.add("limit=" + limit);
		}
		if (cursor != null && !cursor.isEmpty()) {
			params.add("cursor=" + URLEncoder.encode(cursor, StandardCharsets.UTF_8));
		}
		if (!params.isEmpty()) {
			path.append("?").append(String.join("&", params));
		}
		return execute(client.createRequestBuilder(client.complementURI(path.toString())).GET(),
				OrgDevicesResponse.class);
	}

	@Nonnull
	@Override
	public OrgDeviceResponse fetchOrgDevice(@Nonnull String id, @Nonnull EnumSet<OrgDeviceField> fields) {
		var path = new StringBuilder("/orgDevices/").append(URLEncoder.encode(id, StandardCharsets.UTF_8));
		if (!fields.isEmpty()) {
			path.append("?fields%5BorgDevices%5D=")
					.append(fields.stream().map(f -> URLEncoder.encode(f.fieldName(), StandardCharsets.UTF_8))
							.collect(Collectors.joining(",")));
		}
		return execute(client.createRequestBuilder(client.complementURI(path.toString())).GET(),
				OrgDeviceResponse.class);
	}

	<T> HttpRequest.BodyPublisher ofBody(T obj) {
		try {
			return HttpRequest.BodyPublishers.ofByteArray(objectMapper.writeValueAsBytes(obj));
		} catch (JsonProcessingException e) {
			throw new HttpClientWrapperException("Error serializing to json: " + obj.getClass().getName(), e);
		}
	}
}
