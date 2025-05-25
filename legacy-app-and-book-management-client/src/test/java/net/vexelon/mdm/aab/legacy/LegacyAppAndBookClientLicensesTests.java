package net.vexelon.mdm.aab.legacy;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LegacyAppAndBookClientLicensesTests {

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = TestUtil.createDefaultHeaders();
	}

	@BeforeEach
	void initServiceConfig(WireMockRuntimeInfo wm) {
		TestUtil.createServiceConfig(wm.getHttpBaseUrl(), headers);
	}

	@Test
	void test_fetch_assets(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/mdm/getVPPAssetsSrv")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "uId": "9397866",
						    "status": 0,
						    "assets": [
						        {
						            "productTypeName": "Application",
						            "productTypeId": 8,
						            "pricingParam": "STDQ",
						            "adamIdStr": "343555245",
						            "isIrrevocable": false,
						            "deviceAssignable": true
						        },
						        {
						            "productTypeName": "Application",
						            "productTypeId": 8,
						            "pricingParam": "STDQ",
						            "adamIdStr": "389801252",
						            "isIrrevocable": false,
						            "deviceAssignable": true
						        },
						        {
						            "productTypeName": "Application",
						            "productTypeId": 8,
						            "pricingParam": "STDQ",
						            "adamIdStr": "544007664",
						            "isIrrevocable": false,
						            "deviceAssignable": true
						        },
						        {
						            "productTypeName": "Application",
						            "productTypeId": 8,
						            "pricingParam": "STDQ",
						            "adamIdStr": "983156458",
						            "isIrrevocable": false,
						            "deviceAssignable": true
						        },
						        {
						            "productTypeName": "Application",
						            "productTypeId": 8,
						            "pricingParam": "STDQ",
						            "adamIdStr": "1135064690",
						            "isIrrevocable": false,
						            "deviceAssignable": true
						        }
						    ],
						    "clientContext": "{\\"id\\":\\"somesever8910\\"}\\n",
						    "expirationMillis": 1776630125812,
						    "totalCount": 5,
						    "location": {
						        "locationName": "Petarov GmbH - Test",
						        "locationId": 1106700002122099
						    }
						}
						""".stripIndent())));

		var assets = TestUtil.createClient(wm).fetchAssets(false);

		assertEquals(5, assets.totalCount());
		assertEquals(5, assets.assets().size());

		assertEquals("Application", assets.assets().getFirst().productTypeName());
		assertEquals(8, assets.assets().getFirst().productTypeId());
		assertEquals("STDQ", assets.assets().getFirst().pricingParam());
		assertEquals("343555245", assets.assets().getFirst().adamIdStr());
		assertFalse(assets.assets().getFirst().isIrrevocable());
		assertTrue(assets.assets().getFirst().isDeviceAssignable());

		assertEquals("Application", assets.assets().getLast().productTypeName());
		assertEquals(8, assets.assets().getLast().productTypeId());
		assertEquals("STDQ", assets.assets().getLast().pricingParam());
		assertEquals("1135064690", assets.assets().getLast().adamIdStr());
		assertFalse(assets.assets().getLast().isIrrevocable());
		assertTrue(assets.assets().getLast().isDeviceAssignable());

		assertEquals("{\"id\":\"somesever8910\"}\n", assets.getResponse().clientContext());
		assertEquals(1776630125812L, assets.getResponse().expirationMillis());
		assertEquals("Petarov GmbH - Test", assets.getResponse().location().locationName());
		assertEquals(1106700002122099L, assets.getResponse().location().locationId());
		assertEquals("9397866", assets.getResponse().uId());
		assertEquals(0, assets.getResponse().status());


		stubFor(post(urlEqualTo("/mdm/getVPPAssetsSrv")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "uId": "9397866",
						    "status": 0,
						    "assets": [
							  {
							      "assignedCount": 0,
							      "productTypeName": "Application",
							      "productTypeId": 8,
							      "availableCount": 150,
							      "pricingParam": "STDQ",
							      "retiredCount": 0,
							      "adamIdStr": "343555245",
							      "totalCount": 150,
							      "isIrrevocable": false,
							      "deviceAssignable": true
							  },
							  {
							      "assignedCount": 2,
							      "productTypeName": "Application",
							      "productTypeId": 8,
							      "availableCount": 16,
							      "pricingParam": "STDQ",
							      "retiredCount": 0,
							      "adamIdStr": "389801252",
							      "totalCount": 18,
							      "isIrrevocable": false,
							      "deviceAssignable": true
							  }
						    ],
						    "clientContext": "{\\"id\\":\\"somesever8910\\"}\\n",
						    "expirationMillis": 1776630125812,
						    "totalCount": 2,
						    "location": {
						        "locationName": "Petarov GmbH - Test",
						        "locationId": 1106700002122099
						    }
						}
						""".stripIndent())));

		var assetsAndCounts = TestUtil.createClient(wm).fetchAssets(true);

		assertEquals(2, assetsAndCounts.totalCount());
		assertEquals(2, assetsAndCounts.assets().size());

		assertEquals(0, assetsAndCounts.assets().getFirst().assignedCount());
		assertEquals("Application", assetsAndCounts.assets().getFirst().productTypeName());
		assertEquals(8, assetsAndCounts.assets().getFirst().productTypeId());
		assertEquals(150, assetsAndCounts.assets().getFirst().availableCount());
		assertEquals("STDQ", assetsAndCounts.assets().getFirst().pricingParam());
		assertEquals(0, assetsAndCounts.assets().getFirst().retiredCount());
		assertEquals("343555245", assetsAndCounts.assets().getFirst().adamIdStr());
		assertEquals(150, assetsAndCounts.assets().getFirst().totalCount());
		assertFalse(assetsAndCounts.assets().getFirst().isIrrevocable());
		assertTrue(assetsAndCounts.assets().getFirst().isDeviceAssignable());
	}

	@Test
	void test_fetch_assignments(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/mdm/getAssignments")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "assignments": [
						        {
						            "adamIdStr": "389801252",
						            "pricingParam": "STDQ",
						            "serialNumber": "B9FPP3Q6GMK7"
						        },
						        {
						            "adamIdStr": "389801252",
						            "pricingParam": "STDQ",
						            "serialNumber": "BNPT0GHHM7252"
						        }
						    ],
						    "assignmentsInCurrentPage": 2,
						    "currentPageIndex": 0,
						    "clientContext": "{\\"id\\":\\"somesever8910\\"}\\n",
						    "expirationMillis": 1776630125812,
						    "location": {
						        "locationName": "Petarov GmbH - Test",
						        "locationId": 1106700002122099
						    },
						    "requestId": "c302ccde-1395-46cd-1276-d6b7aac9c2e5",
						    "status": 0,
						    "totalAssignments": 2,
						    "totalPages": 1,
						    "uId": "9397866"
						}
						""".stripIndent())));

		var assignments = TestUtil.createClient(wm).fetchAssignments("389801252");

		// verify the request body has the correct sToken
		verify(postRequestedFor(urlEqualTo("/mdm/getAssignments")).withRequestBody(equalToJson("""
				{"adamIdStr": "389801252", "sToken": "%s"}
				""".stripIndent().formatted(TestUtil.getSToken()), true, false)));

		assertEquals(2, assignments.assignmentsInCurrentPage());
		assertEquals(2, assignments.totalAssignments());
		assertEquals(0, assignments.currentPageIndex());
		assertEquals(1, assignments.totalPages());
		assertEquals("c302ccde-1395-46cd-1276-d6b7aac9c2e5", assignments.requestId());

		assertEquals(2, assignments.assignments().size());
		assertEquals("389801252", assignments.assignments().getFirst().adamIdStr());
		assertEquals("STDQ", assignments.assignments().getFirst().pricingParam());
		assertEquals("B9FPP3Q6GMK7", assignments.assignments().getFirst().serialNumber());
		assertEquals("389801252", assignments.assignments().getLast().adamIdStr());
		assertEquals("STDQ", assignments.assignments().getLast().pricingParam());
		assertEquals("BNPT0GHHM7252", assignments.assignments().getLast().serialNumber());

		// ---

		stubFor(post(urlEqualTo("/mdm/getAssignments")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "assignments": [
						        {
						            "adamIdStr": "389801252",
						            "pricingParam": "STDQ",
						            "serialNumber": "B9FPP3Q6GMK7"
						        }
						    ],
						    "assignmentsInCurrentPage": 1,
						    "currentPageIndex": 0,
						    "clientContext": "{\\"id\\":\\"somesever8910\\"}\\n",
						    "expirationMillis": 1776630125812,
						    "location": {
						        "locationName": "Petarov GmbH - Test",
						        "locationId": 1106700002122099
						    },
						    "requestId": "c302ccde-1395-46cd-1276-d6b7aac9c2e5",
						    "status": 0,
						    "totalAssignments": 1,
						    "totalPages": 1,
						    "uId": "9397866"
						}
						""".stripIndent())));

		var serialAssignments = TestUtil.createClient(wm).fetchAssignments("389801252",
				LegacyAppAndBookClient.FetchAssignmentsOptions.ofSerialNumber("B9FPP3Q6GMK7"));

		// verify the request body has the correct sToken
		verify(postRequestedFor(urlEqualTo("/mdm/getAssignments")).withRequestBody(equalToJson("""
				{"adamIdStr": "389801252", "serialNumber": "B9FPP3Q6GMK7", "sToken": "%s"}
				""".stripIndent().formatted(TestUtil.getSToken()), true, false)));

		assertEquals(1, serialAssignments.assignmentsInCurrentPage());
		assertEquals(1, serialAssignments.totalAssignments());
		assertEquals(0, serialAssignments.currentPageIndex());
		assertEquals(1, serialAssignments.totalPages());
		assertEquals("c302ccde-1395-46cd-1276-d6b7aac9c2e5", assignments.requestId());

		assertEquals(2, assignments.assignments().size());
		assertEquals("389801252", assignments.assignments().getFirst().adamIdStr());
		assertEquals("STDQ", assignments.assignments().getFirst().pricingParam());
		assertEquals("B9FPP3Q6GMK7", assignments.assignments().getFirst().serialNumber());
	}

	@Test
	void test_manageUserLicenses_associate(WireMockRuntimeInfo wm) {
		String adamId = "389801252";

		String expectedRequestBody = String.format("""
				{
				    "adamIdStr": "%s",
				    "associateClientUserIdStrs": ["RZMSGlDjVbEoiajPy/gND9xXaevk3nILRo6NrAS25Jg=", "K7hgLqg3iBmEvEVEduqbweqjmkMYYGG+D0zwBmrh610="],
				    "disassociateClientUserIdStrs": [],
				    "notifyDisassociation": %b,
				    "sToken": "%s"
				}""", adamId, false, TestUtil.getSToken());

		String mockResponseBody = """
				{
				    "status": 0,
				    "uId": "9397866",
				    "eventId": "someEventId",
				    "clientContext": "{\\"id\\":\\"somesever8910\\"}",
				    "expirationMillis": 1776630125812,
				    "location": {"locationId": 12345, "locationName": "Test Location"},
				    "associations": [
				        {"clientUserIdStr": "RZMSGlDjVbEoiajPy/gND9xXaevk3nILRo6NrAS25Jg=", "licenseIdStr": "992d9d47b0f0b0d1"},
				        {"clientUserIdStr": "K7hgLqg3iBmEvEVEduqbweqjmkMYYGG+D0zwBmrh610=", "licenseIdStr": "78e6ca16523eb83f"}
				    ],
				    "disassociations": []
				}""";

		stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
						equalToJson(expectedRequestBody, true, true))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(mockResponseBody)));

		var response = TestUtil.createClient(wm).manageUserLicenses(adamId,
				Set.of("RZMSGlDjVbEoiajPy/gND9xXaevk3nILRo6NrAS25Jg=", "K7hgLqg3iBmEvEVEduqbweqjmkMYYGG+D0zwBmrh610="),
				Set.of(), false);

		assertNotNull(response);
		assertEquals(0, response.getResponse().status());
		assertEquals("9397866", response.getResponse().uId());
		assertEquals(2, response.getAssociations().size());
		assertEquals("RZMSGlDjVbEoiajPy/gND9xXaevk3nILRo6NrAS25Jg=",
				response.getAssociations().getFirst().clientUserIdStr());
		assertEquals("992d9d47b0f0b0d1", response.getAssociations().getFirst().licenseIdStr());
		assertTrue(response.getDisassociations().isEmpty());

		verify(postRequestedFor(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
				equalToJson(expectedRequestBody, true, true)));
	}

	@Test
	void test_manageUserLicenses_disassociate(WireMockRuntimeInfo wm) {
		String adamId = "389801252";

		String expectedRequestBody = String.format("""
				{
				    "adamIdStr": "%s",
				    "associateClientUserIdStrs": [],
				    "disassociateClientUserIdStrs": ["JSdHlZBo3QR9qJfpfEM4hE9G2Z+RXBH5ANOwEpDq+YI=", "cVQbeIAB5InRWYFuVHiEeEE01k/C9IX3D80mLyS3q+g="],
				    "notifyDisassociation": %b,
				    "sToken": "%s"
				}""", adamId, true, TestUtil.getSToken());

		String mockResponseBody = """
				{
				    "status": 0,
				    "uId": "9397866",
				    "associations": [],
				    "disassociations": [
				        {"clientUserIdStr": "JSdHlZBo3QR9qJfpfEM4hE9G2Z+RXBH5ANOwEpDq+YI="},
				        {"clientUserIdStr": "cVQbeIAB5InRWYFuVHiEeEE01k/C9IX3D80mLyS3q+g="}
				    ]
				}""";

		stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
						equalToJson(expectedRequestBody, true, true))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(mockResponseBody)));

		var response = TestUtil.createClient(wm).manageUserLicenses(adamId, Set.of(),
				Set.of("JSdHlZBo3QR9qJfpfEM4hE9G2Z+RXBH5ANOwEpDq+YI=", "cVQbeIAB5InRWYFuVHiEeEE01k/C9IX3D80mLyS3q+g="),
				true);

		assertNotNull(response);
		assertEquals(0, response.getResponse().status());
		assertEquals("9397866", response.getResponse().uId());
		assertTrue(response.getAssociations().isEmpty());
		assertEquals(2, response.getDisassociations().size());
		assertEquals("JSdHlZBo3QR9qJfpfEM4hE9G2Z+RXBH5ANOwEpDq+YI=",
				response.getDisassociations().getFirst().clientUserIdStr());

		verify(postRequestedFor(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
				equalToJson(expectedRequestBody, true, true)));
	}

	@Test
	void test_manageUserLicenses_error(WireMockRuntimeInfo wm) {
		String adamId = "389801252";

		String expectedRequestBody = String.format("""
				{
				    "adamIdStr": "%s",
				    "associateClientUserIdStrs": ["unknownUser"],
				    "disassociateClientUserIdStrs": [],
				    "notifyDisassociation": %b,
				    "sToken": "%s"
				}""", adamId, false, TestUtil.getSToken());

		String mockResponseBody = """
				{
				    "status": -1,
				    "errorMessage": "Unable to find the registered user.",
				    "errorNumber": 9609
				}""";

		stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
						equalToJson(expectedRequestBody, true, true))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(mockResponseBody)));

		var t = assertThrows(LegacyAppAndBookClientException.class,
				() -> TestUtil.createClient(wm).manageUserLicenses(adamId, Set.of("unknownUser"), Set.of(), false));

		assertEquals(9609, t.getCode());
		assertEquals("Unable to find the registered user.", t.getErrorMessage());
		assertEquals("(9609) Unable to find the registered user.", t.getMessage());
		assertEquals(
				"net.vexelon.mdm.aab.legacy.LegacyAppAndBookClientException: (9609) Unable to find the registered user.",
				t.toString());
	}

	@Test
	void test_manageDeviceLicenses_associate(WireMockRuntimeInfo wm) {
		String adamId = "389801252";

		String expectedRequestBody = String.format("""
				{
				    "adamIdStr": "%s",
				    "associateSerialNumbers": ["B9FPP3Q6GMK7", "BNPT0GHHM7252"],
				    "disassociateSerialNumbers": [],
				    "notifyDisassociation": %b,
				    "sToken": "%s"
				}""", adamId, false, TestUtil.getSToken());

		String mockResponseBody = """
				{
				    "status": 0,
				    "uId": "9397866",
				    "associations": [
				        {"serialNumber": "B9FPP3Q6GMK7", "licenseIdStr": "8d35653c121c831c"},
				        {"serialNumber": "BNPT0GHHM7252", "licenseIdStr": "26e67830a6218950"}
				    ],
				    "disassociations": []
				}""";

		stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
						equalToJson(expectedRequestBody, true, true))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(mockResponseBody)));

		var response = TestUtil.createClient(wm)
				.manageDeviceLicenses(adamId, Set.of("B9FPP3Q6GMK7", "BNPT0GHHM7252"), Set.of(), false);

		assertNotNull(response);
		assertEquals(0, response.getResponse().status());
		assertEquals(2, response.getAssociations().size());
		assertEquals("B9FPP3Q6GMK7", response.getAssociations().getFirst().serialNumber());
		assertEquals("8d35653c121c831c", response.getAssociations().getFirst().licenseIdStr());
		assertTrue(response.getDisassociations().isEmpty());

		verify(postRequestedFor(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
				equalToJson(expectedRequestBody, true, true)));
	}

	@Test
	void test_manageDeviceLicenses_disassociate(WireMockRuntimeInfo wm) {
		String adamId = "389801252";

		String expectedRequestBody = String.format("""
				{
				    "adamIdStr": "%s",
				    "associateSerialNumbers": [],
				    "disassociateSerialNumbers": ["B9FPP3Q6GMK7", "BNPT0GHHM7252"],
				    "notifyDisassociation": %b,
				    "sToken": "%s"
				}""", adamId, true, TestUtil.getSToken());

		String mockResponseBody = """
				{
				    "status": 0,
				    "uId": "9397866",
				    "associations": [],
				    "disassociations": [
				        {"serialNumber": "B9FPP3Q6GMK7"},
				        {"serialNumber": "BNPT0GHHM7252"}
				    ]
				}""";

		stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
						equalToJson(expectedRequestBody, true, true))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(mockResponseBody)));

		var response = TestUtil.createClient(wm)
				.manageDeviceLicenses(adamId, Set.of(), Set.of("B9FPP3Q6GMK7", "BNPT0GHHM7252"), true);

		assertNotNull(response);
		assertEquals(0, response.getResponse().status());
		assertTrue(response.getAssociations().isEmpty());
		assertEquals(2, response.getDisassociations().size());
		assertEquals("B9FPP3Q6GMK7", response.getDisassociations().getFirst().serialNumber());
		assertEquals("BNPT0GHHM7252", response.getDisassociations().getLast().serialNumber());

		verify(postRequestedFor(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
				equalToJson(expectedRequestBody, true, true)));
	}

	@Test
	void test_manageDeviceLicenses_error(WireMockRuntimeInfo wm) {
		String adamId = "389801252";

		String expectedRequestBody = String.format("""
				{
				    "adamIdStr": "%s",
				    "associateSerialNumbers": ["INELIGIBLE_SERIAL"],
				    "disassociateSerialNumbers": [],
				    "notifyDisassociation": %b,
				    "sToken": "%s"
				}""", adamId, false, TestUtil.getSToken());

		String mockResponseBody = """
				{
				    "status": -1,
				    "errorMessage": "License not eligible for device assignment.",
				    "errorNumber": 9628
				}""";

		stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
						equalToJson(expectedRequestBody, true, true))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(mockResponseBody)));

		var t = assertThrows(LegacyAppAndBookClientException.class, () -> TestUtil.createClient(wm)
				.manageDeviceLicenses(adamId, Set.of("INELIGIBLE_SERIAL"), Set.of(), false));

		assertEquals(9628, t.getCode());
		assertEquals("License not eligible for device assignment.", t.getErrorMessage());
		assertEquals("(9628) License not eligible for device assignment.", t.getMessage());
		assertEquals(
				"net.vexelon.mdm.aab.legacy.LegacyAppAndBookClientException: (9628) License not eligible for device assignment.",
				t.toString());
	}

	@Test
	void test_disassociateLicenses_success(WireMockRuntimeInfo wm) {
		String adamId = "389801252";

		String expectedRequestBody = String.format("""
				{
				    "adamIdStr": "%s",
				    "disassociateLicenseIdStrs": ["992d9d47b0f0b0d1", "78e6ca16523eb83f"],
				    "notifyDisassociation": %b,
				    "sToken": "%s"
				}""", adamId, true, TestUtil.getSToken());

		String mockResponseBody = """
				{
				    "status": 0,
				    "uId": "9397866",
				    "eventId": "anotherEventId",
				    "clientContext": "{\\"id\\":\\"somesever8910\\"}",
				    "expirationMillis": 1776630125812,
				    "location": {"locationId": 12345, "locationName": "Test Location"},
				    "associations": [],
				    "disassociations": [
				        {"licenseIdStr": "992d9d47b0f0b0d1"},
				        {"licenseIdStr": "78e6ca16523eb83f"}
				    ]
				}""";

		stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
						equalToJson(expectedRequestBody, true, true))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(mockResponseBody)));

		var response = TestUtil.createClient(wm)
				.disassociateLicenses(adamId, Set.of("992d9d47b0f0b0d1", "78e6ca16523eb83f"), true);

		assertNotNull(response);
		assertEquals(0, response.getResponse().status());
		assertEquals("9397866", response.getResponse().uId());
		assertTrue(response.getAssociations().isEmpty());
		assertEquals(2, response.getDisassociations().size());
		assertEquals("992d9d47b0f0b0d1", response.getDisassociations().getFirst().licenseIdStr());
		assertEquals("78e6ca16523eb83f", response.getDisassociations().getLast().licenseIdStr());

		verify(postRequestedFor(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
				equalToJson(expectedRequestBody, true, true)));
	}

	@Test
	void test_disassociateLicenses_error(WireMockRuntimeInfo wm) {
		String adamId = "389801252";

		String expectedRequestBody = String.format("""
				{
				    "adamIdStr": "%s",
				    "disassociateLicenseIdStrs": ["nonExistentLicId"],
				    "notifyDisassociation": %b,
				    "sToken": "%s"
				}""", adamId, false, TestUtil.getSToken());

		String mockResponseBody = """
				{
				    "status": -1,
				    "errorMessage": "License not found.",
				    "errorNumber": 9610
				}""";

		stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv")).withRequestBody(
						equalToJson(expectedRequestBody, true, true))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(mockResponseBody)));

		var t = assertThrows(LegacyAppAndBookClientException.class,
				() -> TestUtil.createClient(wm).disassociateLicenses(adamId, Set.of("nonExistentLicId"), false));

		assertEquals(9610, t.getCode());
		assertEquals("License not found.", t.getErrorMessage());
		assertEquals("(9610) License not found.", t.getMessage());
		assertEquals("net.vexelon.mdm.aab.legacy.LegacyAppAndBookClientException: (9610) License not found.",
				t.toString());
	}
}