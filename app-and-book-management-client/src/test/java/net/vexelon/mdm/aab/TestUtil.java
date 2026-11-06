package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import net.vexelon.mdm.shared.http.HttpConsts;

public final class TestUtil {

	static String getSToken() {
		return "eyJ0b2tlbiI6IlZHaHBjeUJwY3lCaElITmhiWEJzWlNCMFpYaDBJSGRvYVdOb0lIZGhjeUIxYzJWa0lIUnZJR055WldGMFpTQjBhR1VnYzJsdGRXeGhkRzl5SUhSdmEyVnVDZz09IiwiZXhwRGF0ZSI6IjIwMjAtMTItMzFUMTM6NTc6MTkrMDI6MDAiLCJvcmdOYW1lIjoiYmxhYmxhIEdtYkgifQ==";
	}

	static AppAndBookClient createClient(WireMockRuntimeInfo wm) {
		var sTokenInput = TestUtil.class.getResourceAsStream("/apple-mdm-client-tests-stoken-1.stoken");

		var builder = AppAndBookClient.newBuilder();
		builder.setAppleServiceUrl(wm.getHttpBaseUrl());
		builder.setUserAgent("apple-mdm-aab-v1");
		builder.setServerToken(AppAndBookToken.create(sTokenInput));

		return builder.build();
	}

	static HttpHeaders createDefaultHeaders() {
		return HttpHeaders.noHeaders().plus(HttpHeader.httpHeader(HttpConsts.HEADER_CONTENT_TYPE,
				HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8));
	}
}
