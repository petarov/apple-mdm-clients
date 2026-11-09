package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import net.vexelon.mdm.shared.http.HttpConsts;

import java.time.Clock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public final class TestUtil {

	static String getSToken() {
		return "eyJ0b2tlbiI6IlZHaHBjeUJwY3lCaElITmhiWEJzWlNCMFpYaDBJSGRvYVdOb0lIZGhjeUIxYzJWa0lIUnZJR055WldGMFpTQjBhR1VnYzJsdGRXeGhkRzl5SUhSdmEyVnVDZz09IiwiZXhwRGF0ZSI6IjIwMjAtMTItMzFUMTM6NTc6MTkrMDI6MDAiLCJvcmdOYW1lIjoiYmxhYmxhIEdtYkgifQ==";
	}

	static AppAndBookClient createClient(WireMockRuntimeInfo wm) {
		return createClient(wm, Clock.systemUTC());
	}

	static AppAndBookClient createClient(WireMockRuntimeInfo wm, Clock clock) {
		var sTokenInput = TestUtil.class.getResourceAsStream("/apple-mdm-client-tests-stoken-1.stoken");

		var builder = AppAndBookClient.newBuilder();
		builder.setAppleServiceUrl(wm.getHttpBaseUrl());
		builder.setUserAgent("apple-mdm-aab-v1");
		builder.setServerToken(AppAndBookToken.create(sTokenInput));
		builder.setClock(clock);

		return builder.build();
	}

	static HttpHeaders createDefaultHeaders() {
		return HttpHeaders.noHeaders().plus(HttpHeader.httpHeader(HttpConsts.HEADER_CONTENT_TYPE,
				HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8));
	}

	/**
	 * Stubs GET /service/config with urls pointing at the WireMock server. NOT an Apple example payload.
	 */
	static void stubServiceConfig(WireMockRuntimeInfo wm) {
		wm.getWireMock().register(get(urlEqualTo("/service/config")).willReturn(
				aResponse().withStatus(200).withHeaders(createDefaultHeaders())
						.withBody(serviceConfigJson(wm.getHttpBaseUrl()))));
	}

	static String serviceConfigJson(String baseUrl) {
		return """
				{"urls": {"getAssets": "%1$s/assets", "getAssignments": "%1$s/assignments", \
				"getUsers": "%1$s/users", "clientConfig": "%1$s/client/config"}}
				""".formatted(baseUrl);
	}
}
