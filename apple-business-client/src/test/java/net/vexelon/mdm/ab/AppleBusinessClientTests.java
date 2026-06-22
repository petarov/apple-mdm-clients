package net.vexelon.mdm.ab;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.ab.model.devices.AppleCareCoverageAttributes;
import net.vexelon.mdm.ab.model.devices.AppleCareCoverageField;
import net.vexelon.mdm.ab.model.devices.MdmDeviceField;
import net.vexelon.mdm.ab.model.devices.OrgDeviceField;
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
public class AppleBusinessClientTests {

	private static final String CLIENT_ID = "BUSINESSAPI.9703f56c-10ce-4876-8f59-e78e5e23a152";
	private static final String KEY_ID    = "d136aa66-0c3b-4bd4-9892-c20e8db024ab";

	/**
	 * Simulates the base64url cursor Apple returns in meta.paging.nextCursor.
	 */
	private static final String NEXT_CURSOR            = "MDowOjE3NDYxMTM4OTI1OTA=";
	private static final String DEVICE_ID_FOR_COVERAGE = "XABC123X0ABC123X0";

	private static final String TOKEN_RESPONSE = """
			{
			    "access_token": "test-bearer-token-12345",
			    "token_type": "Bearer",
			    "expires_in": 3600,
			    "scope": "business.api"
			}
			""".stripIndent();

	private static final String APPLECARE_COVERAGE_RESPONSE = """
			{
			    "data": [
			        {
			            "type": "appleCareCoverage",
			            "id": "XABC123X0ABC123X0",
			            "attributes": {
			                "contractCancelDateTime": null,
			                "startDateTime": "2025-02-02T00:00:00Z",
			                "isRenewable": false,
			                "isCanceled": false,
			                "description": "Limited Warranty",
			                "agreementNumber": null,
			                "endDateTime": "2026-02-02T00:00:00Z",
			                "status": "ACTIVE",
			                "paymentType": "NONE"
			            }
			        },
			        {
			            "type": "appleCareCoverage",
			            "id": "0000000001",
			            "attributes": {
			                "contractCancelDateTime": null,
			                "startDateTime": "2025-04-17T00:00:00Z",
			                "isRenewable": true,
			                "isCanceled": false,
			                "description": "AppleCare+",
			                "agreementNumber": "0000000001",
			                "endDateTime": "2026-04-17T00:00:00Z",
			                "status": "ACTIVE",
			                "paymentType": "SUBSCRIPTION"
			            }
			        },
			        {
			            "type": "appleCareCoverage",
			            "id": "abe-XABC123X0ABC123X0",
			            "attributes": {
			                "contractCancelDateTime": null,
			                "startDateTime": "2025-04-17T00:00:00Z",
			                "isRenewable": true,
			                "isCanceled": false,
			                "description": "AppleCare+ for Business",
			                "agreementNumber": null,
			                "endDateTime": null,
			                "status": "ACTIVE",
			                "paymentType": "ABE_SUBSCRIPTION"
			            }
			        }
			    ],
			    "links": {
			        "self": "https://api-business.apple.com/v1/orgDevices/XABC123X0ABC123X0/appleCareCoverage"
			    },
			    "meta": {
			        "paging": {
			            "limit": 100
			        }
			    }
			}
			""".stripIndent();

	private static final String MDM_DEVICES_RESPONSE = """
			{
			    "data": [
			        {
			            "type": "mdmDevices",
			            "id": "XABC123X0ABC123X0",
			            "attributes": {
			                "serialNumber": "XABC123X0ABC123X0",
			                "deviceName": "Peter's iPhone",
			                "productFamily": "iPhone",
			                "enrolledUserId": "user-001"
			            }
			        },
			        {
			            "type": "mdmDevices",
			            "id": "YABC456Y0ABC456Y0",
			            "attributes": {
			                "serialNumber": "YABC456Y0ABC456Y0",
			                "deviceName": null,
			                "productFamily": "Mac",
			                "enrolledUserId": null
			            }
			        }
			    ],
			    "links": {
			        "self": "https://api-business.apple.com/v1/mdmDevices"
			    },
			    "meta": {
			        "paging": {
			            "limit": 100,
			            "total": 2
			        }
			    }
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
		var allDevices = new java.util.ArrayList<>();

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

	@Test
	void fetch_with_fields_sends_comma_joined_wire_names_in_declaration_order(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_FIRST_PAGE)));

		// SERIAL_NUMBER and DEVICE_MODEL — declaration order determines the joined string
		createClient(wm).fetchOrgDevices(OrgDeviceField.of(OrgDeviceField.SERIAL_NUMBER, OrgDeviceField.DEVICE_MODEL),
				0, "");

		// WireMock automatically decodes %5B / %5D back to [ / ] when matching query params
		verify(getRequestedFor(urlPathEqualTo("/orgDevices")).withQueryParam("fields[orgDevices]",
				equalTo("serialNumber,deviceModel")));
	}

	@Test
	void fetch_with_empty_fields_omits_fields_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_FIRST_PAGE)));

		createClient(wm).fetchOrgDevices(OrgDeviceField.of(), 0, "");

		verify(getRequestedFor(urlPathEqualTo("/orgDevices")).withoutQueryParam("fields[orgDevices]"));
	}

	@Test
	void fetch_applecare_coverage_uses_correct_path(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(APPLECARE_COVERAGE_RESPONSE)));

		createClient(wm).fetchAppleCareCoverage(DEVICE_ID_FOR_COVERAGE);

		verify(getRequestedFor(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")));
	}

	@Test
	void fetch_applecare_coverage_with_fields_sends_comma_joined_wire_names_in_declaration_order(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(APPLECARE_COVERAGE_RESPONSE)));

		// STATUS and PAYMENT_TYPE — declaration order determines the joined string
		createClient(wm).fetchAppleCareCoverage(DEVICE_ID_FOR_COVERAGE,
				AppleCareCoverageField.of(AppleCareCoverageField.STATUS, AppleCareCoverageField.PAYMENT_TYPE), 0, "");

		// WireMock automatically decodes %5B / %5D back to [ / ] when matching query params
		verify(getRequestedFor(
				urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")).withQueryParam(
				"fields[appleCareCoverage]", equalTo("status,paymentType")));
	}

	@Test
	void fetch_applecare_coverage_with_empty_fields_omits_fields_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(APPLECARE_COVERAGE_RESPONSE)));

		createClient(wm).fetchAppleCareCoverage(DEVICE_ID_FOR_COVERAGE, AppleCareCoverageField.of(), 0, "");

		verify(getRequestedFor(
				urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")).withoutQueryParam(
				"fields[appleCareCoverage]"));
	}

	@Test
	void fetch_applecare_coverage_with_limit_sends_limit_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(APPLECARE_COVERAGE_RESPONSE)));

		createClient(wm).fetchAppleCareCoverage(DEVICE_ID_FOR_COVERAGE, AppleCareCoverageField.of(), 5, "");

		verify(getRequestedFor(
				urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")).withQueryParam("limit",
				equalTo("5")));
	}

	@Test
	void fetch_applecare_coverage_deserializes_status_paymenttype_and_booleans(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE + "/appleCareCoverage")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(APPLECARE_COVERAGE_RESPONSE)));

		var response = createClient(wm).fetchAppleCareCoverage(DEVICE_ID_FOR_COVERAGE);

		assertEquals(3, response.data().size());

		// Limited Warranty — NONE payment, not renewable, null fields mapped to empty string
		var warranty = response.data().get(0).attributes();
		assertEquals(AppleCareCoverageAttributes.Status.ACTIVE, warranty.status());
		assertEquals(AppleCareCoverageAttributes.PaymentType.NONE, warranty.paymentType());
		assertFalse(warranty.isRenewable());
		assertFalse(warranty.isCanceled());
		assertTrue(warranty.agreementNumber().isEmpty(), "null agreementNumber should map to empty string");
		assertTrue(warranty.contractCancelDateTime().isEmpty(),
				"null contractCancelDateTime should map to empty string");

		// AppleCare+ — SUBSCRIPTION, renewable
		var appleCarePlus = response.data().get(1).attributes();
		assertEquals(AppleCareCoverageAttributes.PaymentType.SUBSCRIPTION, appleCarePlus.paymentType());
		assertTrue(appleCarePlus.isRenewable());
		assertEquals("0000000001", appleCarePlus.agreementNumber());

		// AppleCare+ for Business — ABE_SUBSCRIPTION, no endDateTime
		var abe = response.data().get(2).attributes();
		assertEquals(AppleCareCoverageAttributes.PaymentType.ABE_SUBSCRIPTION, abe.paymentType());
		assertTrue(abe.endDateTime().isEmpty(), "null endDateTime should map to empty string");

		assertTrue(response.meta().paging().nextCursor().isEmpty(), "absent nextCursor should map to empty string");
	}

	@Test
	void fetch_mdm_devices_with_fields_sends_comma_joined_wire_names_in_declaration_order(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICES_RESPONSE)));

		// SERIAL_NUMBER and DEVICE_NAME — declaration order determines the joined string
		createClient(wm).fetchMdmDevices(
				MdmDeviceField.of(MdmDeviceField.SERIAL_NUMBER, MdmDeviceField.DEVICE_NAME), 0, "");

		// WireMock automatically decodes %5B / %5D back to [ / ] when matching query params
		verify(getRequestedFor(urlPathEqualTo("/mdmDevices")).withQueryParam("fields[mdmDevices]",
				equalTo("serialNumber,deviceName")));
	}

	@Test
	void fetch_mdm_devices_with_empty_fields_omits_fields_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICES_RESPONSE)));

		createClient(wm).fetchMdmDevices(MdmDeviceField.of(), 0, "");

		verify(getRequestedFor(urlPathEqualTo("/mdmDevices")).withoutQueryParam("fields[mdmDevices]"));
	}

	@Test
	void fetch_mdm_devices_with_limit_sends_limit_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICES_RESPONSE)));

		createClient(wm).fetchMdmDevices(MdmDeviceField.of(), 10, "");

		verify(getRequestedFor(urlPathEqualTo("/mdmDevices")).withQueryParam("limit", equalTo("10")));
	}

	@Test
	void fetch_mdm_devices_deserializes_attributes_and_maps_null_to_empty_string(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICES_RESPONSE)));

		var response = createClient(wm).fetchMdmDevices();

		assertEquals(2, response.data().size());

		var first = response.data().get(0).attributes();
		assertEquals("XABC123X0ABC123X0", first.serialNumber());
		assertEquals("Peter's iPhone", first.deviceName());
		assertEquals("iPhone", first.productFamily());
		assertEquals("user-001", first.enrolledUserId());

		// null fields map to empty string
		var second = response.data().get(1).attributes();
		assertTrue(second.deviceName().isEmpty(), "null deviceName should map to empty string");
		assertTrue(second.enrolledUserId().isEmpty(), "null enrolledUserId should map to empty string");

		assertTrue(response.meta().paging().nextCursor().isEmpty(), "absent nextCursor should map to empty string");
	}

	private AppleBusinessClient createClient(WireMockRuntimeInfo wm) throws Exception {
		var privateKey = AppleBusinessPrivateKey.createFromPEM(
				Objects.requireNonNull(getClass().getResourceAsStream("/test_private_apple_style_key.pem")));

		return AppleBusinessClient.newBuilder().setAppleServiceUrl(wm.getHttpBaseUrl())
				.setAppleAuthUrl(wm.getHttpBaseUrl() + "/auth/oauth2/token").setClientId(CLIENT_ID).setKeyId(KEY_ID)
				.setPrivateKey(privateKey).build();
	}
}
