package net.vexelon.mdm.aab.legacy;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.Set; // Added import
import net.vexelon.mdm.aab.legacy.model.response.VppManageLicensesByAdamIdResponse; // Added import

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

    // New test methods to be added:

    @Test
    void test_manageUserLicenses_associateSuccess(WireMockRuntimeInfo wm) {
        LegacyAppAndBookClient client = TestUtil.createClient(wm); 
        String sToken = TestUtil.getSToken(); 

        String adamId = "testAdamId123";
        Set<String> usersToAssociate = Set.of("user1", "user2");
        Set<String> usersToDisassociate = Set.of();
        boolean notify = false;

        String expectedRequestBody = String.format("""
                {
                    "adamIdStr": "%s",
                    "associateClientUserIdStrs": ["user1", "user2"],
                    "disassociateClientUserIdStrs": [],
                    "notifyDisassociation": %b,
                    "sToken": "%s"
                }""", adamId, notify, sToken);

        String mockResponseBody = String.format("""
                {
                    "status": 0,
                    "uId": "uniqueResponseId_UserAssoc",
                    "eventId": "someEventId",
                    "clientContext": "{\\"id\\":\\"somesever8910\\"}",
                    "expirationMillis": 1776630125812,
                    "location": {"locationId": 12345, "locationName": "Test Location"},
                    "associations": [
                        {"clientUserIdStr": "user1", "licenseIdStr": "lic1", "adamIdStr": "%s"},
                        {"clientUserIdStr": "user2", "licenseIdStr": "lic2", "adamIdStr": "%s"}
                    ],
                    "disassociations": []
                }""", adamId, adamId);

        stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeaders(headers) 
                        .withBody(mockResponseBody)));

        VppManageLicensesByAdamIdResponse response = client.manageUserLicenses(
                adamId,
                usersToAssociate,
                usersToDisassociate,
                notify
        );

        assertNotNull(response);
        assertEquals(0, response.getResponse().status());
        assertNotNull(response.getResponse().uId());
        assertEquals("uniqueResponseId_UserAssoc", response.getResponse().uId());
        assertEquals(2, response.associations().size());
        assertEquals("user1", response.associations().getFirst().clientUserIdStr());
        assertEquals("lic1", response.associations().getFirst().licenseIdStr());
        assertTrue(response.disassociations().isEmpty());

        verify(postRequestedFor(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true)));
    }

    @Test
    void test_manageUserLicenses_disassociateSuccess(WireMockRuntimeInfo wm) {
        LegacyAppAndBookClient client = TestUtil.createClient(wm);
        String sToken = TestUtil.getSToken();

        String adamId = "testAdamId456";
        Set<String> usersToAssociate = Set.of();
        Set<String> usersToDisassociate = Set.of("user3", "user4");
        boolean notify = true;

        String expectedRequestBody = String.format("""
                {
                    "adamIdStr": "%s",
                    "associateClientUserIdStrs": [],
                    "disassociateClientUserIdStrs": ["user3", "user4"],
                    "notifyDisassociation": %b,
                    "sToken": "%s"
                }""", adamId, notify, sToken);

        String mockResponseBody = String.format("""
                {
                    "status": 0,
                    "uId": "uniqueResponseId_UserDisassoc",
                    "associations": [],
                    "disassociations": [
                        {"clientUserIdStr": "user3", "licenseIdStr": "lic3", "adamIdStr": "%s"},
                        {"clientUserIdStr": "user4", "licenseIdStr": "lic4", "adamIdStr": "%s"}
                    ]
                }""", adamId, adamId);

        stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeaders(headers)
                        .withBody(mockResponseBody)));

        VppManageLicensesByAdamIdResponse response = client.manageUserLicenses(
                adamId,
                usersToAssociate,
                usersToDisassociate,
                notify
        );

        assertNotNull(response);
        assertEquals(0, response.getResponse().status());
        assertNotNull(response.getResponse().uId());
        assertTrue(response.associations().isEmpty());
        assertEquals(2, response.disassociations().size());
        assertEquals("user3", response.disassociations().getFirst().clientUserIdStr());
    }

    @Test
    void test_manageUserLicenses_apiError(WireMockRuntimeInfo wm) {
        LegacyAppAndBookClient client = TestUtil.createClient(wm);
        String sToken = TestUtil.getSToken();

        String adamId = "testAdamId789";
        Set<String> usersToAssociate = Set.of("unknownUser");
        Set<String> usersToDisassociate = Set.of();
        boolean notify = false;

        String expectedRequestBody = String.format("""
                {
                    "adamIdStr": "%s",
                    "associateClientUserIdStrs": ["unknownUser"],
                    "disassociateClientUserIdStrs": [],
                    "notifyDisassociation": %b,
                    "sToken": "%s"
                }""", adamId, notify, sToken);

        String mockResponseBody = """
                {
                    "status": 9609,
                    "errorMessage": "Unable to find the registered user.",
                    "errorNumber": 9609,
                    "uId": "uniqueErrorResponseId_User"
                }""";

        stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true))
                .willReturn(aResponse()
                        .withStatus(200) 
                        .withHeaders(headers)
                        .withBody(mockResponseBody)));

        VppManageLicensesByAdamIdResponse response = client.manageUserLicenses(
                adamId,
                usersToAssociate,
                usersToDisassociate,
                notify
        );

        assertNotNull(response);
        assertEquals(9609, response.getResponse().status());
        assertEquals("Unable to find the registered user.", response.getResponse().errorMessage());
        assertEquals(9609, response.getResponse().errorNumber());
    }

    // New test methods for manageDeviceLicenses:

    @Test
    void test_manageDeviceLicenses_associateSuccess(WireMockRuntimeInfo wm) {
        LegacyAppAndBookClient client = TestUtil.createClient(wm);
        String sToken = TestUtil.getSToken();

        String adamId = "appAdamId789";
        Set<String> devicesToAssociate = Set.of("SERIAL1A", "SERIAL2B");
        Set<String> devicesToDisassociate = Set.of();
        boolean notify = false;

        String expectedRequestBody = String.format("""
                {
                    "adamIdStr": "%s",
                    "associateSerialNumbers": ["SERIAL1A", "SERIAL2B"],
                    "disassociateSerialNumbers": [],
                    "notifyDisassociation": %b,
                    "sToken": "%s"
                }""", adamId, notify, sToken);

        String mockResponseBody = String.format("""
                {
                    "status": 0,
                    "uId": "uniqueResponseId_DeviceAssoc",
                    "associations": [
                        {"serialNumber": "SERIAL1A", "licenseIdStr": "licDev1", "adamIdStr": "%s"},
                        {"serialNumber": "SERIAL2B", "licenseIdStr": "licDev2", "adamIdStr": "%s"}
                    ],
                    "disassociations": []
                }""", adamId, adamId);

        stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeaders(headers)
                        .withBody(mockResponseBody)));

        VppManageLicensesByAdamIdResponse response = client.manageDeviceLicenses(
                adamId,
                devicesToAssociate,
                devicesToDisassociate,
                notify
        );

        assertNotNull(response);
        assertEquals(0, response.getResponse().status());
        assertEquals(2, response.associations().size());
        assertEquals("SERIAL1A", response.associations().getFirst().serialNumber());
        assertTrue(response.disassociations().isEmpty());

        verify(postRequestedFor(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true)));
    }

    @Test
    void test_manageDeviceLicenses_disassociateSuccess(WireMockRuntimeInfo wm) {
        LegacyAppAndBookClient client = TestUtil.createClient(wm);
        String sToken = TestUtil.getSToken();

        String adamId = "appAdamIdABC";
        Set<String> devicesToAssociate = Set.of();
        Set<String> devicesToDisassociate = Set.of("SERIAL3C", "SERIAL4D");
        boolean notify = true;

        String expectedRequestBody = String.format("""
                {
                    "adamIdStr": "%s",
                    "associateSerialNumbers": [],
                    "disassociateSerialNumbers": ["SERIAL3C", "SERIAL4D"],
                    "notifyDisassociation": %b,
                    "sToken": "%s"
                }""", adamId, notify, sToken);

        String mockResponseBody = String.format("""
                {
                    "status": 0,
                    "uId": "uniqueResponseId_DeviceDisassoc",
                    "associations": [],
                    "disassociations": [
                        {"serialNumber": "SERIAL3C", "licenseIdStr": "licDev3", "adamIdStr": "%s"},
                        {"serialNumber": "SERIAL4D", "licenseIdStr": "licDev4", "adamIdStr": "%s"}
                    ]
                }""", adamId, adamId);

        stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeaders(headers)
                        .withBody(mockResponseBody)));

        VppManageLicensesByAdamIdResponse response = client.manageDeviceLicenses(
                adamId,
                devicesToAssociate,
                devicesToDisassociate,
                notify
        );

        assertNotNull(response);
        assertEquals(0, response.getResponse().status());
        assertTrue(response.associations().isEmpty());
        assertEquals(2, response.disassociations().size());
        assertEquals("SERIAL3C", response.disassociations().getFirst().serialNumber());
    }

    @Test
    void test_manageDeviceLicenses_apiError(WireMockRuntimeInfo wm) {
        LegacyAppAndBookClient client = TestUtil.createClient(wm);
        String sToken = TestUtil.getSToken();

        String adamId = "appAdamIdXYZ";
        Set<String> devicesToAssociate = Set.of("INELIGIBLE_SERIAL");
        Set<String> devicesToDisassociate = Set.of();
        boolean notify = false;

        String expectedRequestBody = String.format("""
                {
                    "adamIdStr": "%s",
                    "associateSerialNumbers": ["INELIGIBLE_SERIAL"],
                    "disassociateSerialNumbers": [],
                    "notifyDisassociation": %b,
                    "sToken": "%s"
                }""", adamId, notify, sToken);

        String mockResponseBody = """
                {
                    "status": 9628, 
                    "errorMessage": "License not eligible for device assignment.",
                    "errorNumber": 9628,
                    "uId": "uniqueErrorResponseId_Device"
                }""";

        stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeaders(headers)
                        .withBody(mockResponseBody)));

        VppManageLicensesByAdamIdResponse response = client.manageDeviceLicenses(
                adamId,
                devicesToAssociate,
                devicesToDisassociate,
                notify
        );

        assertNotNull(response);
        assertEquals(9628, response.getResponse().status());
        assertEquals("License not eligible for device assignment.", response.getResponse().errorMessage());
        assertEquals(9628, response.getResponse().errorNumber());
    }

    // New test methods for disassociateLicenses:

    @Test
    void test_disassociateLicenses_success(WireMockRuntimeInfo wm) {
        LegacyAppAndBookClient client = TestUtil.createClient(wm);
        String sToken = TestUtil.getSToken();

        String adamId = "bookAdamId001";
        Set<String> licenseIdsToDisassociate = Set.of("licId001", "licId002");
        boolean notify = true;

        String expectedRequestBody = String.format("""
                {
                    "adamIdStr": "%s",
                    "disassociateLicenseIdStrs": ["licId001", "licId002"],
                    "notifyDisassociation": %b,
                    "sToken": "%s"
                }""", adamId, notify, sToken);

        String mockResponseBody = String.format("""
                {
                    "status": 0,
                    "uId": "uniqueResponseId_LicIdDisassoc",
                    "eventId": "anotherEventId",
                    "clientContext": "{\\"id\\":\\"somesever8910\\"}",
                    "expirationMillis": 1776630125812,
                    "location": {"locationId": 12345, "locationName": "Test Location"},
                    "associations": [], 
                    "disassociations": [
                        {"licenseIdStr": "licId001", "adamIdStr": "%s"},
                        {"licenseIdStr": "licId002", "adamIdStr": "%s"}
                    ]
                }""", adamId, adamId);

        stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeaders(headers)
                        .withBody(mockResponseBody)));

        VppManageLicensesByAdamIdResponse response = client.disassociateLicenses(
                adamId,
                licenseIdsToDisassociate,
                notify
        );

        assertNotNull(response);
        assertEquals(0, response.getResponse().status());
        assertNotNull(response.getResponse().uId());
        assertTrue(response.associations().isEmpty());
        assertEquals(2, response.disassociations().size());
        assertEquals("licId001", response.disassociations().getFirst().licenseIdStr());

        verify(postRequestedFor(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true)));
    }

    @Test
    void test_disassociateLicenses_apiError(WireMockRuntimeInfo wm) {
        LegacyAppAndBookClient client = TestUtil.createClient(wm);
        String sToken = TestUtil.getSToken();

        String adamId = "bookAdamId002";
        Set<String> licenseIdsToDisassociate = Set.of("nonExistentLicId");
        boolean notify = false;

        String expectedRequestBody = String.format("""
                {
                    "adamIdStr": "%s",
                    "disassociateLicenseIdStrs": ["nonExistentLicId"],
                    "notifyDisassociation": %b,
                    "sToken": "%s"
                }""", adamId, notify, sToken);

        String mockResponseBody = """
                {
                    "status": 9610, 
                    "errorMessage": "License not found.",
                    "errorNumber": 9610,
                    "uId": "uniqueErrorResponseId_LicId"
                }""";

        stubFor(post(urlEqualTo("/mdm/manageVPPLicensesByAdamIdSrv"))
                .withRequestBody(equalToJson(expectedRequestBody, true, true))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeaders(headers)
                        .withBody(mockResponseBody)));

        VppManageLicensesByAdamIdResponse response = client.disassociateLicenses(
                adamId,
                licenseIdsToDisassociate,
                notify
        );

        assertNotNull(response);
        assertEquals(9610, response.getResponse().status());
        assertEquals("License not found.", response.getResponse().errorMessage());
        assertEquals(9610, response.getResponse().errorNumber());
    }
}