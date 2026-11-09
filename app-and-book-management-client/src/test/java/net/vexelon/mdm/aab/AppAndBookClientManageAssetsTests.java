package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.aab.model.PricingParam;
import net.vexelon.mdm.aab.model.RequestAsset;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookClientManageAssetsTests {

	private static final String E1_RESPONSE = """
			{
			    "eventId": "ed3edfc3-e617-465e-b309-a17925266e14",
			    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
			    "uId": "2049025000431439"
			}
			""".stripIndent();

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = TestUtil.createDefaultHeaders();
	}

	@BeforeEach
	void stubServiceConfig(WireMockRuntimeInfo wm) {
		TestUtil.stubServiceConfig(wm);
	}

	private void stubOk(String path, String body) {
		stubFor(post(urlPathEqualTo(path)).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	private static Set<RequestAsset> assets(PricingParam pricingParam) {
		return new LinkedHashSet<>(
				List.of(new RequestAsset("408709785", pricingParam), new RequestAsset("377298193", pricingParam)));
	}

	private static Set<String> set(String... values) {
		return new LinkedHashSet<>(List.of(values));
	}

	@Test
	void test_associate_assets_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim
		stubOk("/assets/associate", E1_RESPONSE);

		var resp = TestUtil.createClient(wm)
				.associateAssets(assets(PricingParam.STDQ), set("client-1", "client-2"), set("serial-1", "serial-2"));

		// Apple's example request, verbatim
		verify(1, postRequestedFor(urlEqualTo("/assets/associate")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
						equalTo("Bearer " + TestUtil.getSToken()))
				.withHeader(HttpConsts.HEADER_CONTENT_TYPE, equalTo("application/json")).withRequestBody(equalToJson("""
						{
						    "assets": [
						        {
						            "adamId": "408709785",
						            "pricingParam": "STDQ"
						        },
						        {
						            "adamId": "377298193",
						            "pricingParam": "STDQ"
						        }
						    ],
						    "clientUserIds": [
						        "client-1",
						        "client-2"
						    ],
						    "serialNumbers": [
						        "serial-1",
						        "serial-2"
						    ]
						}
						""".stripIndent(), true, false)));
		assertEquals("ed3edfc3-e617-465e-b309-a17925266e14", resp.eventId());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertNull(resp.mdmInfo());
	}

	@Test
	void test_disassociate_assets_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim
		stubOk("/assets/disassociate", E1_RESPONSE);

		var resp = TestUtil.createClient(wm).disassociateAssets(assets(PricingParam.STDQ), set("client-1", "client-2"),
				set("serial-1", "serial-2"));

		// Apple's example request, verbatim
		verify(1, postRequestedFor(urlEqualTo("/assets/disassociate")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
						equalTo("Bearer " + TestUtil.getSToken()))
				.withHeader(HttpConsts.HEADER_CONTENT_TYPE, equalTo("application/json")).withRequestBody(equalToJson("""
						{
						    "assets": [
						        {
						            "adamId": "408709785",
						            "pricingParam": "STDQ"
						        },
						        {
						            "adamId": "377298193",
						            "pricingParam": "STDQ"
						        }
						    ],
						    "clientUserIds": [
						        "client-1",
						        "client-2"
						    ],
						    "serialNumbers": [
						        "serial-1",
						        "serial-2"
						    ]
						}
						""".stripIndent(), true, false)));
		verify(0, postRequestedFor(urlEqualTo("/assets/associate")));
		assertEquals("ed3edfc3-e617-465e-b309-a17925266e14", resp.eventId());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertNull(resp.mdmInfo());
	}

	@Test
	void test_revoke_assets_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim
		stubOk("/assets/revoke", E1_RESPONSE);

		var resp = TestUtil.createClient(wm).revokeAssets(set("client-1", "client-2"), set("serial-1", "serial-2"));

		// Apple's example request, verbatim
		verify(1, postRequestedFor(urlEqualTo("/assets/revoke")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
						equalTo("Bearer " + TestUtil.getSToken()))
				.withHeader(HttpConsts.HEADER_CONTENT_TYPE, equalTo("application/json")).withRequestBody(equalToJson("""
						{
						    "clientUserIds": [
						        "client-1",
						        "client-2"
						    ],
						    "serialNumbers": [
						        "serial-1",
						        "serial-2"
						    ]
						}
						""".stripIndent(), true, false)));
		assertEquals("ed3edfc3-e617-465e-b309-a17925266e14", resp.eventId());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertNull(resp.mdmInfo());
	}

	@Test
	void test_associate_assets_without_pricing_param(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim (from the managing-assets article)
		stubOk("/assets/associate", """
				{
				    "eventId": "954910a8-3d9c-4fde-948d-253e5aef431a",
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439"
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm)
				.associateAssets(assets(null), set("client-1", "client-2", "client-3", "client-4"),
						set("serial-1", "serial-2", "serial-3", "serial-4"));

		// Apple's example request, verbatim (from the managing-assets article)
		verify(1, postRequestedFor(urlEqualTo("/assets/associate")).withRequestBody(equalToJson("""
				{
				    "assets": [
				      {
				        "adamId": "408709785"
				      },
				      {
				        "adamId": "377298193"
				      }
				    ],
				     "clientUserIds": [
				      "client-1",
				      "client-2",
				      "client-3",
				      "client-4"
				    ],
				    "serialNumbers": [
				      "serial-1",
				      "serial-2",
				      "serial-3",
				      "serial-4"
				    ]
				}
				""".stripIndent(), true, false)));
		assertEquals("954910a8-3d9c-4fde-948d-253e5aef431a", resp.eventId());
	}

	@Test
	void test_associate_assets_sends_empty_targets(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("/assets/associate", E1_RESPONSE);

		TestUtil.createClient(wm)
				.associateAssets(Set.of(new RequestAsset("408709785", PricingParam.PLUS)), Set.of(), set("serial-1"));

		verify(1, postRequestedFor(urlEqualTo("/assets/associate")).withRequestBody(equalToJson("""
				{"assets": [{"adamId": "408709785", "pricingParam": "PLUS"}], "clientUserIds": [], "serialNumbers": ["serial-1"]}
				""", true, false)));
	}

	@Test
	void test_revoke_assets_sends_empty_targets(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("/assets/revoke", E1_RESPONSE);

		TestUtil.createClient(wm).revokeAssets(Set.of(), Set.of());

		verify(1, postRequestedFor(urlEqualTo("/assets/revoke")).withRequestBody(
				equalToJson("{\"clientUserIds\": [], \"serialNumbers\": []}", true, false)));
	}

	@Test
	void test_associate_assets_rejects_unknown_pricing_param(WireMockRuntimeInfo wm) throws Exception {
		var client = TestUtil.createClient(wm);
		var unknown = Set.of(new RequestAsset("1", PricingParam.UNKNOWN));

		assertThrows(IllegalArgumentException.class, () -> client.associateAssets(unknown, Set.of(), set("serial-1")));
		assertThrows(IllegalArgumentException.class,
				() -> client.disassociateAssets(unknown, Set.of(), set("serial-1")));

		verify(0, anyRequestedFor(anyUrl()));
	}

	@Test
	void test_event_response_with_mdm_info(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("/assets/associate", """
				{
				    "eventId": "ed3edfc3-e617-465e-b309-a17925266e14",
				    "mdmInfo": {"id": "522d5c43-44ca-4f7e-ba7a-53570cf60765", "name": "Apple Configurator 2", "metadata": "2.13.3"},
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439"
				}
				""");

		var resp = TestUtil.createClient(wm).associateAssets(assets(PricingParam.STDQ), set("client-1"), Set.of());

		assertNotNull(resp.mdmInfo());
		assertEquals("522d5c43-44ca-4f7e-ba7a-53570cf60765", resp.mdmInfo().id());
		assertEquals("2.13.3", resp.mdmInfo().metadata());
		assertEquals("Apple Configurator 2", resp.mdmInfo().name());
	}

	@Test
	void test_associate_assets_error(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlPathEqualTo("/assets/associate")).willReturn(aResponse().withStatus(400).withHeaders(headers)
				.withBody("{\"errorNumber\": 9719, \"errorMessage\": \"Either clientUserIds or serialNumbers are "
						+ "required arguments. Change the request to provide assignable users and devices.\"}")));

		var ex = assertThrows(AppAndBookClientException.class,
				() -> TestUtil.createClient(wm).associateAssets(assets(PricingParam.STDQ), Set.of(), Set.of()));

		assertEquals(9719, ex.getCode());
		assertEquals(400, ex.getStatusCode());
	}

	@Test
	void test_manage_assets_resolve_urls(WireMockRuntimeInfo wm) throws Exception {
		var base = wm.getHttpBaseUrl();
		stubFor(get(urlEqualTo("/service/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"urls": {"associateAssets": "%1$s/custom/associate", \
						"disassociateAssets": "%1$s/custom/disassociate", "revokeAssets": "%1$s/custom/revoke"}}
						""".formatted(base))));
		stubOk("/custom/associate", "{}");
		stubOk("/custom/disassociate", "{}");
		stubOk("/custom/revoke", "{}");
		var client = TestUtil.createClient(wm);

		client.associateAssets(assets(PricingParam.STDQ), set("client-1"), Set.of());
		client.disassociateAssets(assets(PricingParam.STDQ), set("client-1"), Set.of());
		client.revokeAssets(set("client-1"), Set.of());

		verify(1, postRequestedFor(urlEqualTo("/custom/associate")));
		verify(1, postRequestedFor(urlEqualTo("/custom/disassociate")));
		verify(1, postRequestedFor(urlEqualTo("/custom/revoke")));
		verify(0, postRequestedFor(urlEqualTo("/assets/associate")));
		verify(0, postRequestedFor(urlEqualTo("/assets/disassociate")));
		verify(0, postRequestedFor(urlEqualTo("/assets/revoke")));
	}
}
