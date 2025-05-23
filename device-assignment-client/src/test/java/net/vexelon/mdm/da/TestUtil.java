package net.vexelon.mdm.da;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import net.vexelon.mdm.shared.http.HttpConsts;

public final class TestUtil {

	static DeviceAssignmentClient createClient(WireMockRuntimeInfo wm) {
		var serverTokenInput = TestUtil.class.getResourceAsStream("/apple-mdm-client-tests-1-server-token.p7m");
		var privateKeyInput = TestUtil.class.getResourceAsStream("/apple-mdm-client-tests-1.der");

		var builder = DeviceAssignmentClient.newBuilder();
		builder.setAppleHost(wm.getHttpBaseUrl());
		builder.setUserAgent("apple-mdm-device-assignment-v1");
		builder.setServerToken(DeviceAssignmentServerToken.create(serverTokenInput,
				DeviceAssignmentPrivateKey.createFromDER(privateKeyInput)));

		return builder.build();
	}

	static HttpHeaders createDefaultHeaders() {
		return com.github.tomakehurst.wiremock.http.HttpHeaders.noHeaders()
				.plus(HttpHeader.httpHeader(HttpConsts.HEADER_CONTENT_TYPE,
						HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8));
	}

	static String authSessionToken() {
		return """
				{"auth_session_token":"1745786035268O1O789F19CF078867E47DC9D9BF4682D021O75CA72ECB87046A1B2239D9CFA4D6771O420397O11Op1OB123AA978976E390FF7693C640C92D3F8F6FE7F6O81E6CAAC7816AD3E12D531496695CF5A"}
				""".stripIndent();
	}
}
