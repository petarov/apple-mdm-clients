package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.aab.AppAndBookClient.ClientConfigOptions;
import net.vexelon.mdm.aab.model.DefaultPlatform;
import net.vexelon.mdm.aab.model.MdmInfo;
import net.vexelon.mdm.aab.model.NotificationType;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookClientClientConfigTests {

	private static final MdmInfo APPLE_MDM_INFO = new MdmInfo("522d5c43-44ca-4f7e-ba7a-53570cf60765", "2.13.3",
			"Apple Configurator 2");

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = TestUtil.createDefaultHeaders();
	}

	@BeforeEach
	void stubServiceConfig(WireMockRuntimeInfo wm) {
		TestUtil.stubServiceConfig(wm);
	}

	private void stubOk(String body) {
		stubFor(post(urlPathEqualTo("/client/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	private void stubGetOk(String body) {
		stubFor(get(urlPathEqualTo("/client/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	@Test
	void test_update_client_configuration_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim
		stubOk("""
				{
				    "countryISO2ACode": "US",
				    "defaultPlatform": "volumestore",
				    "locationName": "PS01",
				    "mdmInfo": {
				        "id": "522d5c43-44ca-4f7e-ba7a-53570cf60765", 
				        "name": "Apple Configurator 2", 
				        "metadata": "2.13.3"
				    },
				    "notificationAuthToken": "SUp3rS3Cr3t",
				    "notificationUrl": "https://www.next.com/notification",
				    "subscribedNotificationTypes": [
				        "ASSET_COUNT",
				        "ASSET_MANAGEMENT",
				        "USER_MANAGEMENT",
				        "USER_ASSOCIATED"
				    ],
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439",
				    "websiteURL": "https://school.apple.com"
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).updateClientConfiguration(
				new ClientConfigOptions(APPLE_MDM_INFO, "https://www.next.com/notification", "SUp3rS3Cr3t",
						EnumSet.of(NotificationType.ASSET_COUNT, NotificationType.ASSET_MANAGEMENT,
								NotificationType.USER_MANAGEMENT, NotificationType.USER_ASSOCIATED)));

		// Apple's example request, verbatim
		verify(postRequestedFor(urlEqualTo("/client/config")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
						equalTo("Bearer " + TestUtil.getSToken()))
				.withHeader(HttpConsts.HEADER_CONTENT_TYPE, equalTo("application/json")).withRequestBody(equalToJson("""
						{
						    "mdmInfo": {
						        "id": "522d5c43-44ca-4f7e-ba7a-53570cf60765",
						        "name": "Apple Configurator 2",
						        "metadata": "2.13.3"
						    },
						    "notificationAuthToken": "SUp3rS3Cr3t",
						    "notificationUrl": "https://www.next.com/notification",
						    "notificationTypes": [
						        "ASSET_COUNT",
						        "ASSET_MANAGEMENT",
						        "USER_MANAGEMENT",
						        "USER_ASSOCIATED"
						    ]
						}
						""".stripIndent(), true, false)));
		assertEquals("US", resp.countryISO2ACode());
		assertEquals(DefaultPlatform.VOLUMESTORE, resp.defaultPlatform());
		assertEquals("PS01", resp.locationName());
		assertEquals(APPLE_MDM_INFO, resp.mdmInfo());
		assertEquals("SUp3rS3Cr3t", resp.notificationAuthToken());
		assertEquals("https://www.next.com/notification", resp.notificationUrl());
		assertEquals(List.of(NotificationType.ASSET_COUNT, NotificationType.ASSET_MANAGEMENT,
						NotificationType.USER_MANAGEMENT, NotificationType.USER_ASSOCIATED),
				resp.subscribedNotificationTypes());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertEquals("https://school.apple.com", resp.websiteURL());
	}

	@Test
	void test_update_client_configuration_mdm_info_only(WireMockRuntimeInfo wm) throws Exception {
		stubOk("{}");

		TestUtil.createClient(wm).updateClientConfiguration(ClientConfigOptions.ofMdmInfo(APPLE_MDM_INFO));

		verify(postRequestedFor(urlEqualTo("/client/config")).withRequestBody(equalToJson(
				"{\"mdmInfo\": {\"id\": \"522d5c43-44ca-4f7e-ba7a-53570cf60765\", \"metadata\": \"2.13.3\", "
						+ "\"name\": \"Apple Configurator 2\"}}", false, false)));
	}

	@Test
	void test_update_client_configuration_empty_values(WireMockRuntimeInfo wm) throws Exception {
		stubOk("{}");

		TestUtil.createClient(wm).updateClientConfiguration(new ClientConfigOptions(null, "", "", Set.of()));

		verify(postRequestedFor(urlEqualTo("/client/config")).withRequestBody(
				equalToJson("{\"notificationUrl\": \"\", \"notificationAuthToken\": \"\", \"notificationTypes\": []}",
						false, false)));
	}

	@Test
	void test_update_client_configuration_test_notification(WireMockRuntimeInfo wm) throws Exception {
		stubOk("{}");

		TestUtil.createClient(wm).updateClientConfiguration(
				ClientConfigOptions.ofNotifications("https://www.next.com/notification", "SUp3rS3Cr3t",
						EnumSet.of(NotificationType.TEST_NOTIFICATION, NotificationType.UNLIMITED_ASSET)));

		verify(postRequestedFor(urlEqualTo("/client/config")).withRequestBody(equalToJson(
				"{\"notificationUrl\": \"https://www.next.com/notification\", \"notificationAuthToken\": "
						+ "\"SUp3rS3Cr3t\", \"notificationTypes\": [\"TEST_NOTIFICATION\", \"UNLIMITED_ASSET\"]}", true,
				false)));
	}

	@Test
	void test_update_client_configuration_unknown_notification_type(WireMockRuntimeInfo wm) throws Exception {
		var client = TestUtil.createClient(wm);
		var options = ClientConfigOptions.ofNotifications("https://www.next.com/notification", "SUp3rS3Cr3t",
				EnumSet.of(NotificationType.ASSET_COUNT, NotificationType.UNKNOWN));

		assertThrows(IllegalArgumentException.class, () -> client.updateClientConfiguration(options));

		verify(0, postRequestedFor(anyUrl()));
		verify(0, getRequestedFor(urlEqualTo("/service/config")));
	}

	@Test
	void test_update_client_configuration_payload(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{
				    "defaultPlatform": "enterprisestore",
				    "websiteURL": "https://business.apple.com",
				    "subscribedNotificationTypes": ["UNLIMITED_ASSET", "SOMETHING_NEW"],
				    "locationName": null
				}
				""");

		var resp = TestUtil.createClient(wm).updateClientConfiguration(ClientConfigOptions.ofMdmInfo(APPLE_MDM_INFO));

		assertEquals(DefaultPlatform.ENTERPRISESTORE, resp.defaultPlatform());
		assertEquals("https://business.apple.com", resp.websiteURL());
		assertEquals(List.of(NotificationType.UNLIMITED_ASSET, NotificationType.UNKNOWN),
				resp.subscribedNotificationTypes());
		assertNull(resp.mdmInfo());
		assertEquals("", resp.locationName());
		assertEquals("", resp.countryISO2ACode());
	}

	@Test
	void test_update_client_configuration_unknown_platform(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("{\"defaultPlatform\": \"futurestore\"}");

		var resp = TestUtil.createClient(wm).updateClientConfiguration(ClientConfigOptions.ofMdmInfo(APPLE_MDM_INFO));

		assertEquals(DefaultPlatform.UNKNOWN, resp.defaultPlatform());
		assertTrue(resp.subscribedNotificationTypes().isEmpty());
		assertNull(resp.mdmInfo());
		assertEquals("", resp.countryISO2ACode());
		assertEquals("", resp.notificationUrl());
		assertEquals("", resp.websiteURL());
		assertEquals("", resp.notificationAuthToken());
		assertEquals("", resp.tokenExpirationDate());
		assertEquals("", resp.uId());
		assertEquals("", resp.locationName());
	}

	@Test
	void test_update_client_configuration_error(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlPathEqualTo("/client/config")).willReturn(aResponse().withStatus(400).withHeaders(headers)
				.withBody("{\"errorNumber\": 9710, \"errorMessage\": \"Both the notification URL and the "
						+ "authentication token are necessary.\"}")));

		var ex = assertThrows(AppAndBookClientException.class, () -> TestUtil.createClient(wm)
				.updateClientConfiguration(ClientConfigOptions.ofMdmInfo(APPLE_MDM_INFO)));

		assertEquals(9710, ex.getCode());
		assertEquals(400, ex.getStatusCode());
	}

	@Test
	void test_update_client_configuration_resolves_url(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/service/config")).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(
				"{\"urls\": {\"clientConfig\": \"%s/custom/client/config\"}}".formatted(wm.getHttpBaseUrl()))));
		stubFor(post(urlPathEqualTo("/custom/client/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("{}")));

		TestUtil.createClient(wm).updateClientConfiguration(ClientConfigOptions.ofMdmInfo(APPLE_MDM_INFO));

		verify(1, postRequestedFor(urlEqualTo("/custom/client/config")));
		verify(0, postRequestedFor(urlEqualTo("/client/config")));
	}

	@Test
	void test_fetch_client_configuration_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim
		stubGetOk("""
						{
						    "countryISO2ACode": "US",
						    "defaultPlatform": "volumestore",
						    "locationName": "PS01",
						    "mdmInfo": {
						        "id": "522d5c43-44ca-4f7e-ba7a-53570cf60765", 
						        "name": "Apple Configurator 2", 
						        "metadata": "2.13.3"
						    },
						    "notificationAuthToken": "SUp3rS3Cr3t",
						    "notificationUrl": "https://www.next.com/notification",
						    "subscribedNotificationTypes": [
						        "ASSET_COUNT",
						        "ASSET_MANAGEMENT",
						        "USER_MANAGEMENT",
						        "USER_ASSOCIATED"
						    ],
						    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
						    "uId": "2049025000431439",
						    "websiteURL": "https://school.apple.com"
						}
						""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchClientConfiguration();

		verify(getRequestedFor(urlEqualTo("/client/config"))
				.withHeader(HttpConsts.HEADER_AUTHORIZATION, equalTo("Bearer " + TestUtil.getSToken()))
				.withRequestBody(absent()));
		verify(0, postRequestedFor(anyUrl()));
		assertEquals("US", resp.countryISO2ACode());
		assertEquals(DefaultPlatform.VOLUMESTORE, resp.defaultPlatform());
		assertEquals("PS01", resp.locationName());
		assertEquals(APPLE_MDM_INFO, resp.mdmInfo());
		assertEquals("SUp3rS3Cr3t", resp.notificationAuthToken());
		assertEquals("https://www.next.com/notification", resp.notificationUrl());
		assertEquals(List.of(NotificationType.ASSET_COUNT, NotificationType.ASSET_MANAGEMENT,
				NotificationType.USER_MANAGEMENT, NotificationType.USER_ASSOCIATED),
				resp.subscribedNotificationTypes());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertEquals("https://school.apple.com", resp.websiteURL());
	}

	@Test
	void test_fetch_client_configuration_error(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/client/config")).willReturn(aResponse().withStatus(401).withHeaders(headers)
				.withBody("{\"errorNumber\": 9601, \"errorMessage\": \"Login required.\"}")));

		var ex = assertThrows(AppAndBookClientException.class,
				() -> TestUtil.createClient(wm).fetchClientConfiguration());

		assertEquals(9601, ex.getCode());
		assertEquals(401, ex.getStatusCode());
	}

	@Test
	void test_fetch_client_configuration_resolves_url(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/service/config")).willReturn(aResponse().withStatus(200).withHeaders(headers)
				.withBody("{\"urls\": {\"clientConfig\": \"%s/custom/client/config\"}}".formatted(wm.getHttpBaseUrl()))));
		stubFor(get(urlPathEqualTo("/custom/client/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("{}")));

		TestUtil.createClient(wm).fetchClientConfiguration();

		verify(1, getRequestedFor(urlEqualTo("/custom/client/config")));
		verify(0, getRequestedFor(urlEqualTo("/client/config")));
	}
}
