package net.vexelon.mdm.ab;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.ab.model.devices.AppleCareCoverageAttributes;
import net.vexelon.mdm.ab.model.devices.AppleCareCoverageField;
import net.vexelon.mdm.ab.model.devices.MdmDeviceDetailAttributes;
import net.vexelon.mdm.ab.model.devices.MdmDeviceDetailField;
import net.vexelon.mdm.ab.model.devices.MdmDeviceField;
import net.vexelon.mdm.ab.model.devices.OrgDeviceField;
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
public class AppleBusinessClientDevicesTests {

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

	private static final String ORG_DEVICES_RESPONSE = """
			{
			    "data": [
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
			        "self": "https://api-business.apple.com/v1/orgDevices"
			    },
			    "meta": {
			        "paging": {
			            "limit": 100,
			            "total": 1
			        }
			    }
			}
			""".stripIndent();

	private static final String ORG_DEVICE_RESPONSE = """
			{
			    "data": {
			        "type": "orgDevices",
			        "id": "XABC123X0ABC123X0",
			        "attributes": {
			            "serialNumber": "XABC123X0ABC123X0",
			            "deviceModel": "iPhone 15 Pro",
			            "productFamily": "iPhone",
			            "status": "ASSIGNED",
			            "color": "Black",
			            "imei": ["356938035643809"]
			        }
			    }
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

	private static final String MDM_DEVICE_DETAIL_RESPONSE = """
			{
			    "data": {
			        "type": "mdmDeviceDetails",
			        "id": "XABC123X0ABC123X0",
			        "attributes": {
			            "serialNumber": "XABC123X0ABC123X0",
			            "deviceName": "Peter's Mac",
			            "deviceModel": "MacBook Pro (16-inch, M4 Max)",
			            "osVersion": "15.4.1",
			            "platform": "macOS",
			            "wifiMacAddress": "aa:bb:cc:dd:ee:ff",
			            "bluetoothMacAddress": "aa:bb:cc:dd:ee:00",
			            "ethernetMacAddress": null,
			            "lastCheckInDateTime": "2025-06-01T12:00:00Z",
			            "imei": ["356938035643809", "356938035643810"],
			            "meid": null,
			            "isFirewallEnabled": true,
			            "isFileVaultEnabled": true,
			            "storageFreeCapacity": 123456789012,
			            "storageTotalCapacity": 1000000000000,
			            "deviceLockStatus": "UNLOCKED",
			            "deviceEraseStatus": "NOT_ERASED",
			            "lostModeStatus": "DISABLED"
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

	@BeforeAll
	void init() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@BeforeEach
	void stub_token(WireMockRuntimeInfo wm) {
		stubFor(post(urlPathEqualTo("/auth/oauth2/token")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json").withBody(TOKEN_RESPONSE)));
	}

	// --- fetchOrgDevices ---

	@Test
	void fetch_org_devices_with_fields_sends_comma_joined_wire_names_in_declaration_order(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_RESPONSE)));

		// SERIAL_NUMBER and DEVICE_MODEL — declaration order determines the joined string
		createClient(wm).fetchOrgDevices(OrgDeviceField.of(OrgDeviceField.SERIAL_NUMBER, OrgDeviceField.DEVICE_MODEL),
				0, "");

		// WireMock automatically decodes %5B / %5D back to [ / ] when matching query params
		verify(getRequestedFor(urlPathEqualTo("/orgDevices")).withQueryParam("fields[orgDevices]",
				equalTo("serialNumber,deviceModel")));
	}

	@Test
	void fetch_org_devices_with_empty_fields_omits_fields_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_RESPONSE)));

		createClient(wm).fetchOrgDevices(OrgDeviceField.of(), 0, "");

		verify(getRequestedFor(urlPathEqualTo("/orgDevices")).withoutQueryParam("fields[orgDevices]"));
	}

	@Test
	void fetch_org_devices_deserializes_attributes(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICES_RESPONSE)));

		var response = createClient(wm).fetchOrgDevices();

		assertEquals(1, response.data().size());
		var attrs = response.data().getFirst().attributes();
		assertEquals("XABC123X0ABC123X0", attrs.serialNumber());
		assertEquals("iPhone 15 Pro", attrs.deviceModel());
		assertEquals("iPhone", attrs.productFamily());
	}

	// --- fetchOrgDevice ---

	@Test
	void fetch_org_device_uses_correct_path(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE)).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICE_RESPONSE)));

		createClient(wm).fetchOrgDevice(DEVICE_ID_FOR_COVERAGE);

		verify(getRequestedFor(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE)));
	}

	@Test
	void fetch_org_device_deserializes_attributes(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/orgDevices/" + DEVICE_ID_FOR_COVERAGE)).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(ORG_DEVICE_RESPONSE)));

		var response = createClient(wm).fetchOrgDevice(DEVICE_ID_FOR_COVERAGE);

		var attrs = response.data().attributes();
		assertEquals("XABC123X0ABC123X0", attrs.serialNumber());
		assertEquals("Black", attrs.color());
		assertEquals(1, attrs.imei().size());
		assertEquals("356938035643809", attrs.imei().getFirst());
	}

	// --- fetchAppleCareCoverage ---

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
		var warranty = response.data().getFirst().attributes();
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

	// --- fetchMdmDevices ---

	@Test
	void fetch_mdm_devices_with_fields_sends_comma_joined_wire_names_in_declaration_order(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICES_RESPONSE)));

		// SERIAL_NUMBER and DEVICE_NAME — declaration order determines the joined string
		createClient(wm).fetchMdmDevices(MdmDeviceField.of(MdmDeviceField.SERIAL_NUMBER, MdmDeviceField.DEVICE_NAME), 0,
				"");

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

		var first = response.data().getFirst().attributes();
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

	// --- fetchMdmDeviceDetail ---

	@Test
	void fetch_mdm_device_detail_uses_correct_path(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices/" + DEVICE_ID_FOR_COVERAGE + "/details")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICE_DETAIL_RESPONSE)));

		createClient(wm).fetchMdmDeviceDetail(DEVICE_ID_FOR_COVERAGE);

		verify(getRequestedFor(urlPathEqualTo("/mdmDevices/" + DEVICE_ID_FOR_COVERAGE + "/details")));
	}

	@Test
	void fetch_mdm_device_detail_with_fields_sends_comma_joined_wire_names_in_declaration_order(WireMockRuntimeInfo wm)
			throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices/" + DEVICE_ID_FOR_COVERAGE + "/details")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICE_DETAIL_RESPONSE)));

		// SERIAL_NUMBER and DEVICE_NAME — declaration order determines the joined string
		createClient(wm).fetchMdmDeviceDetail(DEVICE_ID_FOR_COVERAGE,
				MdmDeviceDetailField.of(MdmDeviceDetailField.SERIAL_NUMBER, MdmDeviceDetailField.DEVICE_NAME));

		// WireMock automatically decodes %5B / %5D back to [ / ] when matching query params
		verify(getRequestedFor(urlPathEqualTo("/mdmDevices/" + DEVICE_ID_FOR_COVERAGE + "/details")).withQueryParam(
				"fields[mdmDeviceDetails]", equalTo("serialNumber,deviceName")));
	}

	@Test
	void fetch_mdm_device_detail_with_empty_fields_omits_fields_param(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices/" + DEVICE_ID_FOR_COVERAGE + "/details")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICE_DETAIL_RESPONSE)));

		createClient(wm).fetchMdmDeviceDetail(DEVICE_ID_FOR_COVERAGE, MdmDeviceDetailField.of());

		verify(getRequestedFor(urlPathEqualTo("/mdmDevices/" + DEVICE_ID_FOR_COVERAGE + "/details")).withoutQueryParam(
				"fields[mdmDeviceDetails]"));
	}

	@Test
	void fetch_mdm_device_detail_deserializes_attributes(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/mdmDevices/" + DEVICE_ID_FOR_COVERAGE + "/details")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json")
						.withBody(MDM_DEVICE_DETAIL_RESPONSE)));

		var response = createClient(wm).fetchMdmDeviceDetail(DEVICE_ID_FOR_COVERAGE);

		var attrs = response.data().attributes();
		assertEquals("XABC123X0ABC123X0", attrs.serialNumber());
		assertEquals("Peter's Mac", attrs.deviceName());
		assertEquals("MacBook Pro (16-inch, M4 Max)", attrs.deviceModel());
		assertEquals("macOS", attrs.platform());
		// enums
		assertEquals(MdmDeviceDetailAttributes.DeviceLockStatus.UNLOCKED, attrs.deviceLockStatus());
		assertEquals(MdmDeviceDetailAttributes.DeviceEraseStatus.NOT_ERASED, attrs.deviceEraseStatus());
		assertEquals(MdmDeviceDetailAttributes.LostModeStatus.DISABLED, attrs.lostModeStatus());
		// booleans
		assertTrue(attrs.isFirewallEnabled());
		assertTrue(attrs.isFileVaultEnabled());
		// long capacities
		assertEquals(123456789012L, attrs.storageFreeCapacity());
		assertEquals(1000000000000L, attrs.storageTotalCapacity());
		// lists — imei populated, null meid → empty list
		assertEquals(2, attrs.imei().size());
		assertEquals("356938035643809", attrs.imei().getFirst());
		assertTrue(attrs.meid().isEmpty(), "null meid should map to empty list");
		// null ethernetMacAddress → empty string
		assertTrue(attrs.ethernetMacAddress().isEmpty(), "null ethernetMacAddress should map to empty string");
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

		// absent fields (e.g. Apple's own sample response omits status) stay null, not UNKNOWN
		var third = response.data().get(2).attributes();
		assertNull(third.serverType(), "absent serverType should stay null");
		assertNull(third.status(), "absent status should stay null");

		// explicit JSON null also stays null, not UNKNOWN
		var fourth = response.data().get(3).attributes();
		assertNull(fourth.serverType(), "null serverType should stay null");
		assertNull(fourth.status(), "null status should stay null");

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
	void fetch_mdm_service_detail_with_fields_sends_comma_joined_wire_names_in_declaration_order(
			WireMockRuntimeInfo wm) throws Exception {
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

	private AppleBusinessClient createClient(WireMockRuntimeInfo wm) throws Exception {
		var privateKey = AppleBusinessPrivateKey.createFromPEM(
				Objects.requireNonNull(getClass().getResourceAsStream("/test_private_apple_style_key.pem")));

		return AppleBusinessClient.newBuilder().setAppleServiceUrl(wm.getHttpBaseUrl())
				.setAppleAuthUrl(wm.getHttpBaseUrl() + "/auth/oauth2/token").setClientId(CLIENT_ID).setKeyId(KEY_ID)
				.setPrivateKey(privateKey).build();
	}
}
