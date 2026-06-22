package net.vexelon.mdm.ab;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.ab.model.devices.OrgDeviceField;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.Security;
import java.util.ArrayList;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppleBusinessClientTests {

	private static final String CLIENT_ID   = "BUSINESSAPI.9703f56c-10ce-4876-8f59-e78e5e23a152";
	private static final String KEY_ID      = "d136aa66-0c3b-4bd4-9892-c20e8db024ab";
	private static final String NEXT_CURSOR = "MDowOjE3NDYxMTM4OTI1OTA=";

	private static final String TOKEN_RESPONSE = """
			{
			    "access_token": "test-bearer-token-12345",
			    "token_type": "Bearer",
			    "expires_in": 3600,
			    "scope": "business.api"
			}
			""".stripIndent();

	private static final String ORG_DEVICES_FIRST_PAGE = """
			{
			    "data": [
			        {
			            "type": "orgDevices",
			            "id": "XABC123X0ABC123X0",
			            "attributes": {
			                "serialNumber": "XABC123X0ABC123X0",
			                "deviceModel": "iPhone 15 Pro",
			                "productFamily": "iPhone",
			                "status": "enrolled"
			            }
			        }
			    ],
			    "links": {
			        "self": "https://api-business.apple.com/v1/orgDevices",
			        "next": "https://api-business.apple.com/v1/orgDevices?cursor=MDowOjE3NDYxMTM4OTI1OTA="
			    },
			    "meta": {
			        "paging": {
			            "limit": 1,
			            "total": 2,
			            "nextCursor": "MDowOjE3NDYxMTM4OTI1OTA="
			        }
			    }
			}
			""".stripIndent();

	private static final String ORG_DEVICES_LAST_PAGE = """
			{
			    "data": [
			        {
			            "type": "orgDevices",
			            "id": "YABC456Y0ABC456Y0",
			            "attributes": {
			                "serialNumber": "YABC456Y0ABC456Y0",
			                "deviceModel": "iPad Pro",
			                "productFamily": "iPad",
			                "status": "enrolled"
			            }
			        }
			    ],
			    "links": {
			        "self": "https://api-business.apple.com/v1/orgDevices?cursor=MDowOjE3NDYxMTM4OTI1OTA="
			    },
			    "meta": {
			        "paging": {
			            "limit": 1,
			            "total": 2,
			            "nextCursor": ""
			        }
			    }
			}
			""".stripIndent();

	@BeforeAll
	void init() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@BeforeEach
	void stub_token(WireMockRuntimeInfo wm) {
		stubFor(post(urlPathEqualTo("/auth/oauth2/token")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json").withBody(TOKEN_RESPONSE)));
	}

	@Test
	void fetch_first_page_sends_no_cursor(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_FIRST_PAGE)));

		createClient(wm).fetchOrgDevices();

		verify(getRequestedFor(urlPathEqualTo("/orgDevices")).withoutQueryParam("cursor"));
	}

	@Test
	void fetch_first_page_deserializes_next_cursor(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_FIRST_PAGE)));

		var response = createClient(wm).fetchOrgDevices();

		assertEquals(NEXT_CURSOR, response.meta().paging().nextCursor());
		assertEquals(1, response.data().size());
	}

	@Test
	void fetch_with_cursor_sends_url_encoded_cursor_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_LAST_PAGE)));

		createClient(wm).fetchOrgDevices(OrgDeviceField.of(), 0, NEXT_CURSOR);

		// cursor value contains '=' which must be percent-encoded as %3D
		verify(getRequestedFor(urlPathEqualTo("/orgDevices")).withQueryParam("cursor", equalTo(NEXT_CURSOR)));
	}

	@Test
	void fetch_with_cursor_last_page_has_empty_next_cursor(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_LAST_PAGE)));

		var response = createClient(wm).fetchOrgDevices(OrgDeviceField.of(), 0, NEXT_CURSOR);

		assertTrue(response.meta().paging().nextCursor().isEmpty(), "last page should have an empty nextCursor");
	}

	@Test
	void paging_loop_collects_all_devices(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_FIRST_PAGE)));
		stubFor(get(urlPathEqualTo("/orgDevices")).withQueryParam("cursor", equalTo(NEXT_CURSOR)).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_LAST_PAGE)));

		var client = createClient(wm);
		var allDevices = new ArrayList<>();

		var response = client.fetchOrgDevices();
		while (true) {
			allDevices.addAll(response.data());
			var next = response.meta().paging().nextCursor();
			if (next.isEmpty()) {
				break;
			}
			response = client.fetchOrgDevices(OrgDeviceField.of(), 0, next);
		}

		assertEquals(2, allDevices.size(), "paging loop should collect devices from both pages");
	}

	private AppleBusinessClient createClient(WireMockRuntimeInfo wm) throws Exception {
		var privateKey = AppleBusinessPrivateKey.createFromPEM(
				Objects.requireNonNull(getClass().getResourceAsStream("/test_private_apple_style_key.pem")));

		return AppleBusinessClient.newBuilder().setAppleServiceUrl(wm.getHttpBaseUrl())
				.setAppleAuthUrl(wm.getHttpBaseUrl() + "/auth/oauth2/token").setClientId(CLIENT_ID).setKeyId(KEY_ID)
				.setPrivateKey(privateKey).build();
	}
}
