package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.model.Profile;
import com.github.petarov.mdm.da.model.ProfileSkipItem;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.Security;
import java.util.EnumSet;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceAssignmentProfileManagementTests {

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		Security.addProvider(new BouncyCastleProvider());
		headers = TestUtil.createDefaultHeaders();
	}

	@BeforeEach
	void test_session() {
		stubFor(get(urlEqualTo("/session")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody(TestUtil.authSessionToken())));
	}

	@Test
	void test_create_profile(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/profile")).withRequestBody(equalToJson("""
						{
						    "auto_advance_setup": false,
						    "await_device_configured": true,
						    "department": "Sales",
						    "devices": [
						        "A9C1R3Q8KJA9",
						        "B112R4L8KJC7"
						    ],
						    "is_mandatory": false,
						    "is_mdm_removable": true,
						    "is_multi_user": false,
						    "profile_name": "mdm-server-01-sales-profile",
						    "skip_setup_items": [
						        "TapToSetup",
						        "iCloudDiagnostics",
						        "EnableLockdownMode",
						        "iCloudStorage"
						    ],
						    "support_email_address": "sales-it@example.org",
						    "support_phone_number": "555-555-555",
						    "url": "https://mdm-server-01.local"
						}
						""".stripIndent(), true, false))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "profile_uuid": "F3325E3D2646895BC4261BDAC42D4708",
						    "devices": {
						        "A9C1R3Q8KJA9": "NOT_ACCESSIBLE",
						        "B112R4L8KJC7": "SUCCESS"
						    }
						}
						""".stripIndent())));

		var response = TestUtil.createClient(wm).createProfile(
				new Profile.ProfileBuilder().setProfileName("mdm-server-01-sales-profile")
						.setUrl("https://mdm-server-01.local").setDepartment("Sales").setAwaitDeviceConfigured(true)
						.setMdmRemovable(true).setSupportPhoneNumber("555-555-555")
						.setSupportEmailAddress("sales-it@example.org").setSkipSetupItems(
								EnumSet.of(ProfileSkipItem.ENABLE_LOCKDOWN_MODE, ProfileSkipItem.TAP_TO_SETUP,
										ProfileSkipItem.ICLOUD_DIAGNOSTICS, ProfileSkipItem.ICLOUD_STORAGE))
						.setDevices(Set.of("A9C1R3Q8KJA9", "B112R4L8KJC7")).build());

		assertEquals("F3325E3D2646895BC4261BDAC42D4708", response.profileUuid());
		assertEquals(2, response.devices().size());
		assertEquals("NOT_ACCESSIBLE", response.devices().get("A9C1R3Q8KJA9"));
		assertEquals("SUCCESS", response.devices().get("B112R4L8KJC7"));
	}

	@Test
	void test_fetch_profile(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/profile?profile_uuid=95C2189CB0EFB3192BC7B3C555091D22")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "support_phone_number": "077 555 555",
						    "support_email_address": "support@petarov.net",
						    "org_magic": "petarov GmbH",
						    "url": "https://node.petarov.net/srv/intra",
						    "is_supervised": true,
						    "allow_pairing": true,
						    "is_mandatory": true,
						    "is_mdm_removable": true,
						    "await_device_configured": true,
						    "is_multi_user": true,
						    "is_return_to_service": false,
						    "do_not_use_profile_from_backup": false,
						    "auto_advance_setup": true,
						    "skip_setup_items": [
								"Accessibility",
								"ActionButton",
								"Android",
								"Appearance",
								"AppleID",
								"AppStore",
								"Biometric",
								"CameraButton",
								"DeviceToDeviceMigration",
								"Diagnostics",
								"EnableLockdownMode",
								"FileVault",
								"iCloudDiagnostics",
								"iCloudStorage",
								"iMessageAndFaceTime",
								"Intelligence",
								"Keyboard",
								"Location",
								"MessagingActivationUsingPhoneNumber",
								"Passcode",
								"Payment",
								"Privacy",
								"Restore",
								"RestoreCompleted",
								"Safety",
								"ScreenSaver",
								"ScreenTime",
								"SIMSetup",
								"Siri",
								"SoftwareUpdate",
								"SpokenLanguage",
								"TapToSetup",
								"TermsOfAddress",
								"TOS",
								"TVHomeScreenSync",
								"TVProviderSignIn",
								"TVRoom",
								"UpdateCompleted",
								"Wallpaper",
								"WatchMigration",
								"WebContentFiltering",
								"Welcome",
								"SafetyAndHandling"
						    ],
						    "profile_uuid": "95C2189CB0EFB3192BC7B3C555091D22",
						    "profile_name": "MDM petarov"
						}""".stripIndent())));

		var wrappedResponse = TestUtil.createClient(wm).fetchProfile("95C2189CB0EFB3192BC7B3C555091D22");
		assertTrue(wrappedResponse.isPresent());

		var response = wrappedResponse.orElseThrow();
		assertTrue(response.devices().isEmpty());
		assertEquals("077 555 555", response.supportPhoneNumber());
		assertEquals("support@petarov.net", response.supportEmailAddress());
		assertEquals("petarov GmbH", response.orgMagic());
		assertEquals("https://node.petarov.net/srv/intra", response.url());
		assertTrue(response.isMandatory());
		assertTrue(response.isMdmRemovable());
		assertTrue(response.isAwaitDeviceConfigured());
		assertTrue(response.isMultiUser());
		assertTrue(response.isAutoAdvanceSetup());
		assertEquals(43, response.skipSetupItems().size());
		assertEquals("95C2189CB0EFB3192BC7B3C555091D22", response.profileUuid());
		assertEquals("MDM petarov", response.profileName());

		var wrappedResponse2 = TestUtil.createClient(wm).fetchProfile("DOESNOTEXIST");
		assertTrue(wrappedResponse2.isEmpty());
	}

	@Test
	void test_assign_profile(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/profile/devices")).withRequestBody(equalToJson("""
						{"profile_uuid": "95C2189CA0EFB3272AC8B3C66201F33", "devices": ["C112342756", "B222342AF8"]}
						""".stripIndent(), true, false))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"profile_uuid":"95C2189CB0EFB3192BC7B3C555091D22","devices":{"C112342756":"SUCCESS","B222342AF8":"NOT_ACCESSIBLE"}}
						""".stripIndent())));

		var response = TestUtil.createClient(wm)
				.assignProfile("95C2189CA0EFB3272AC8B3C66201F33", Set.of("C112342756", "B222342AF8"));

		assertEquals(2, response.devices().size());
		assertEquals("SUCCESS", response.devices().get("C112342756"));
		assertEquals("NOT_ACCESSIBLE", response.devices().get("B222342AF8"));
	}

	@Test
	void test_unassign_profile(WireMockRuntimeInfo wm) throws Exception {
		stubFor(delete(urlEqualTo("/profile/devices")).withRequestBody(equalToJson("""
						{"profile_uuid": "95C2189CA0EFB3272AC8B3C66201F33", "devices": ["C112342756", "B222342AF8"]}
						""".stripIndent(), true, false))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"devices":{"C112342756":"SUCCESS","B222342AF8":"NOT_ACCESSIBLE"}}
						""".stripIndent())));

		var response = TestUtil.createClient(wm)
				.unassignProfile("95C2189CA0EFB3272AC8B3C66201F33", Set.of("C112342756", "B222342AF8"));

		assertEquals(2, response.devices().size());
		assertEquals("SUCCESS", response.devices().get("C112342756"));
		assertEquals("NOT_ACCESSIBLE", response.devices().get("B222342AF8"));
	}
}
