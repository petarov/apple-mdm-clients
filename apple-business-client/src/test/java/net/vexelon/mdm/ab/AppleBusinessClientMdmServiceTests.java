package net.vexelon.mdm.ab;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.ab.model.servers.MdmServerAttributes;
import net.vexelon.mdm.ab.model.servers.MdmServerField;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.Security;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppleBusinessClientMdmServiceTests {

	private static final String CLIENT_ID = "BUSINESSAPI.9703f56c-10ce-4876-8f59-e78e5e23a152";
	private static final String KEY_ID    = "d136aa66-0c3b-4bd4-9892-c20e8db024ab";

	private static final String DEVICE_ID_FOR_COVERAGE = "XABC123X0ABC123X0";

	private static final String TOKEN_RESPONSE = """
			{
			    "access_token": "test-bearer-token-12345",
			    "token_type": "Bearer",
			    "expires_in": 3600,
			    "scope": "business.api"
			}
			""".stripIndent();

	private static final String MDM_SERVERS_RESPONSE = """
			{
			    "data": [
			        {
			            "type": "mdmServers",
			            "id": "1F97349736CF4614A94F624E705841AD",
			            "attributes": {
			                "serverName": "Test Device Management Service",
			                "serverType": "MDM",
			                "status": "ACTIVE",
			                "defaultProductFamilies": ["IPHONE", "IPAD"],
			                "deviceCount": 42,
			                "enableMdmDisownFlag": true,
			                "lastConnectedDateTime": "2025-05-01T03:22:00.000Z",
			                "lastConnectedIp": "192.0.2.1",
			                "createdDateTime": "2025-05-01T03:21:44.685Z",
			                "updatedDateTime": "2025-05-01T03:21:46.284Z"
			            }
			        },
			        {
			            "type": "mdmServers",
			            "id": "2F97349736CF4614A94F624E705841AE",
			            "attributes": {
			                "serverName": null,
			                "serverType": "FUTURE_SERVER_TYPE",
			                "status": "FUTURE_STATUS",
			                "defaultProductFamilies": null,
			                "deviceCount": 0,
			                "enableMdmDisownFlag": false,
			                "lastConnectedDateTime": null,
			                "lastConnectedIp": null,
			                "createdDateTime": null,
			                "updatedDateTime": null
			            }
			        },
			        {
			            "type": "mdmServers",
			            "id": "3F97349736CF4614A94F624E705841AF",
			            "attributes": {
			                "serverName": "Minimal Service",
			                "createdDateTime": "2025-05-01T03:21:44.685Z",
			                "updatedDateTime": "2025-05-01T03:21:46.284Z"
			            }
			        },
			        {
			            "type": "mdmServers",
			            "id": "4F97349736CF4614A94F624E705841BF",
			            "attributes": {
			                "serverName": "Explicit Null Enums",
			                "serverType": null,
			                "status": null,
			                "createdDateTime": "2025-05-01T03:21:44.685Z",
			                "updatedDateTime": "2025-05-01T03:21:46.284Z"
			            }
			        }
			    ],
			    "included": [
			        {
			            "type": "orgDevices",
			            "id": "XABC123X0ABC123X0",
			            "attributes": {
			                "serialNumber": "XABC123X0ABC123X0",
			                "deviceModel": "iPhone 15 Pro",
			                "productFamily": "iPhone",
			                "status": "ASSIGNED"
			            }
			        }
			    ],
			    "links": {
			        "self": "https://api-business.apple.com/v1/mdmServers"
			    },
			    "meta": {
			        "paging": {
			            "limit": 100,
			            "total": 4
			        }
			    }
			}
			""".stripIndent();

	private static final String MDM_SERVER_RESPONSE = """
			{
			    "data": {
			        "type": "mdmServers",
			        "id": "1F97349736CF4614A94F624E705841AD",
			        "attributes": {
			            "serverName": "Production MDM",
			            "serverType": "MDM",
			            "status": "ACTIVE",
			            "defaultProductFamilies": ["IPAD", "IPHONE", "MAC"],
			            "deviceCount": 128,
			            "enableMdmDisownFlag": false,
			            "lastConnectedDateTime": "2026-06-01T08:14:23Z",
			            "lastConnectedIp": "203.0.113.5",
			            "createdDateTime": "2025-05-01T03:21:44Z",
			            "updatedDateTime": "2026-05-12T11:02:18Z"
			        }
			    },
			    "included": [
			        {
			            "type": "orgDevices",
			            "id": "XABC123X0ABC123X0",
			            "attributes": {
			                "serialNumber": "XABC123X0ABC123X0",
			                "deviceModel": "iPhone 15 Pro",
			                "productFamily": "iPhone",
			                "status": "ASSIGNED"
			            }
			        }
			    ],
			    "links": {
			        "self": "https://api-business.apple.com/v1/mdmServers/1F97349736CF4614A94F624E705841AD"
			    }
			}
			""".stripIndent();

	private static final String MDM_SERVER_RESPONSE_SERVER_TYPE_OMITTED = """
			{
			    "data": {
			        "type": "mdmServers",
			        "id": "5F97349736CF4614A94F624E705841CF",
			        "attributes": {
			            "serverName": "Server Without Type",
			            "status": "ACTIVE",
			            "createdDateTime": "2025-05-01T03:21:44.685Z",
			            "updatedDateTime": "2025-05-01T03:21:46.284Z"
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

	// --- fetchMdmServices ---

	@Test
	void fetch_mdm_services_with_fields_sends_comma_joined_wire_names_in_declaration_order(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVERS_RESPONSE)));

		// SERVER_NAME and STATUS — declaration order determines the joined string
		createClient(wm).fetchMdmServices(MdmServerField.of(MdmServerField.SERVER_NAME, MdmServerField.STATUS), 0, "");

		// WireMock automatically decodes %5B / %5D back to [ / ] when matching query params
		verify(getRequestedFor(urlPathEqualTo("/mdmServers")).withQueryParam("fields[mdmServers]",
				equalTo("serverName,status")));
	}

	@Test
	void fetch_mdm_services_with_empty_fields_omits_fields_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVERS_RESPONSE)));

		createClient(wm).fetchMdmServices(MdmServerField.of(), 0, "");

		verify(getRequestedFor(urlPathEqualTo("/mdmServers")).withoutQueryParam("fields[mdmServers]"));
	}

	@Test
	void fetch_mdm_services_with_limit_sends_limit_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVERS_RESPONSE)));

		createClient(wm).fetchMdmServices(MdmServerField.of(), 10, "");

		verify(getRequestedFor(urlPathEqualTo("/mdmServers")).withQueryParam("limit", equalTo("10")));
	}

	@Test
	void fetch_mdm_services_with_cursor_sends_cursor_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVERS_RESPONSE)));

		createClient(wm).fetchMdmServices(MdmServerField.of(), 0, "next-page-cursor");

		verify(getRequestedFor(urlPathEqualTo("/mdmServers")).withQueryParam("cursor", equalTo("next-page-cursor")));
	}

	@Test
	void fetch_mdm_services_deserializes_attributes_and_included(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVERS_RESPONSE)));

		var response = createClient(wm).fetchMdmServices();

		assertEquals(4, response.data().size());

		var first = response.data().getFirst().attributes();
		assertEquals("Test Device Management Service", first.serverName());
		assertEquals(MdmServerAttributes.ServerType.MDM, first.serverType());
		assertEquals(MdmServerAttributes.Status.ACTIVE, first.status());
		assertEquals(2, first.defaultProductFamilies().size());
		assertEquals(MdmServerAttributes.ProductFamily.IPHONE, first.defaultProductFamilies().getFirst());
		assertEquals(42, first.deviceCount());
		assertTrue(first.enableMdmDisownFlag());

		// null/unrecognized fields map to empty string / empty list / UNKNOWN
		var second = response.data().get(1).attributes();
		assertTrue(second.serverName().isEmpty(), "null serverName should map to empty string");
		assertEquals(MdmServerAttributes.ServerType.UNKNOWN, second.serverType(),
				"unrecognized serverType should map to UNKNOWN");
		assertEquals(MdmServerAttributes.Status.UNKNOWN, second.status(), "unrecognized status should map to UNKNOWN");
		assertTrue(second.defaultProductFamilies().isEmpty(), "null defaultProductFamilies should map to empty list");

		// absent fields (e.g. Apple's own sample response omits status) also map to UNKNOWN, not null
		var third = response.data().get(2).attributes();
		assertEquals(MdmServerAttributes.ServerType.UNKNOWN, third.serverType(),
				"absent serverType should map to UNKNOWN");
		assertEquals(MdmServerAttributes.Status.UNKNOWN, third.status(), "absent status should map to UNKNOWN");

		// explicit JSON null also maps to UNKNOWN, not Java null
		var fourth = response.data().get(3).attributes();
		assertEquals(MdmServerAttributes.ServerType.UNKNOWN, fourth.serverType(),
				"null serverType should map to UNKNOWN");
		assertEquals(MdmServerAttributes.Status.UNKNOWN, fourth.status(), "null status should map to UNKNOWN");

		assertEquals(1, response.included().size());
		assertEquals("XABC123X0ABC123X0", response.included().getFirst().attributes().serialNumber());

		assertTrue(response.meta().paging().nextCursor().isEmpty(), "absent nextCursor should map to empty string");
	}

	// --- fetchMdmServiceDetail ---

	@Test
	void fetch_mdm_service_detail_uses_correct_path(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers/" + DEVICE_ID_FOR_COVERAGE)).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVER_RESPONSE)));

		createClient(wm).fetchMdmService(DEVICE_ID_FOR_COVERAGE);

		verify(getRequestedFor(urlPathEqualTo("/mdmServers/" + DEVICE_ID_FOR_COVERAGE)));
	}

	@Test
	void fetch_mdm_service_detail_with_fields_sends_comma_joined_wire_names_in_declaration_order(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers/" + DEVICE_ID_FOR_COVERAGE)).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVER_RESPONSE)));

		// SERVER_NAME and STATUS — declaration order determines the joined string
		createClient(wm).fetchMdmService(DEVICE_ID_FOR_COVERAGE,
				MdmServerField.of(MdmServerField.SERVER_NAME, MdmServerField.STATUS));

		// WireMock automatically decodes %5B / %5D back to [ / ] when matching query params
		verify(getRequestedFor(urlPathEqualTo("/mdmServers/" + DEVICE_ID_FOR_COVERAGE)).withQueryParam(
				"fields[mdmServers]", equalTo("serverName,status")));
	}

	@Test
	void fetch_mdm_service_detail_with_empty_fields_omits_fields_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers/" + DEVICE_ID_FOR_COVERAGE)).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVER_RESPONSE)));

		createClient(wm).fetchMdmService(DEVICE_ID_FOR_COVERAGE, MdmServerField.of());

		verify(getRequestedFor(urlPathEqualTo("/mdmServers/" + DEVICE_ID_FOR_COVERAGE)).withoutQueryParam(
				"fields[mdmServers]"));
	}

	@Test
	void fetch_mdm_service_detail_deserializes_attributes_and_included(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers/" + DEVICE_ID_FOR_COVERAGE)).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVER_RESPONSE)));

		var response = createClient(wm).fetchMdmService(DEVICE_ID_FOR_COVERAGE);

		var attrs = response.data().attributes();
		assertEquals("Production MDM", attrs.serverName());
		assertEquals(MdmServerAttributes.ServerType.MDM, attrs.serverType());
		assertEquals(MdmServerAttributes.Status.ACTIVE, attrs.status());
		assertEquals(3, attrs.defaultProductFamilies().size());
		assertEquals(MdmServerAttributes.ProductFamily.IPAD, attrs.defaultProductFamilies().getFirst());
		assertEquals(128, attrs.deviceCount());
		assertFalse(attrs.enableMdmDisownFlag());
		assertEquals("203.0.113.5", attrs.lastConnectedIp());

		assertEquals(1, response.included().size());
		assertEquals("XABC123X0ABC123X0", response.included().getFirst().attributes().serialNumber());
	}

	@Test
	void fetch_mdm_service_detail_with_omitted_server_type_maps_to_unknown(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmServers/" + DEVICE_ID_FOR_COVERAGE)).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_SERVER_RESPONSE_SERVER_TYPE_OMITTED)));

		var response = createClient(wm).fetchMdmService(DEVICE_ID_FOR_COVERAGE);

		assertEquals(MdmServerAttributes.ServerType.UNKNOWN, response.data().attributes().serverType(),
				"omitted serverType should map to UNKNOWN");
	}

	private AppleBusinessClient createClient(WireMockRuntimeInfo wm) throws Exception {
		var privateKey = AppleBusinessPrivateKey.createFromPEM(
				Objects.requireNonNull(getClass().getResourceAsStream("/test_private_apple_style_key.pem")));

		return AppleBusinessClient.newBuilder().setAppleServiceUrl(wm.getHttpBaseUrl())
				.setAppleAuthUrl(wm.getHttpBaseUrl() + "/auth/oauth2/token").setClientId(CLIENT_ID).setKeyId(KEY_ID)
				.setPrivateKey(privateKey).build();
	}
}
