package com.github.petarov.mdm.da;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;


public final class TestUtil {

	public static DeviceAssignmentClient createClient(WireMockRuntimeInfo wm) {
		var serverTokenInput = TestUtil.class.getResourceAsStream("/apple-mdm-client-tests-1-server-token.p7m");
		var privateKeyInput = TestUtil.class.getResourceAsStream("/apple-mdm-client-tests-1.der");

		var builder = DeviceAssignmentClient.newBuilder();
		builder.setAppleHost(wm.getHttpBaseUrl());
		builder.setUserAgent("apple-mdm-device-assignment-v1");
		builder.setServerToken(DeviceAssignmentServerToken.create(serverTokenInput,
				DeviceAssignmentPrivateKey.createFromDER(privateKeyInput)));

		return builder.build();
	}

	public static HttpHeaders createDefaultHeaders() {
		return com.github.tomakehurst.wiremock.http.HttpHeaders.noHeaders()
				.plus(HttpHeader.httpHeader("Content-Type", "application/json;charset=UTF8"));
	}
}
