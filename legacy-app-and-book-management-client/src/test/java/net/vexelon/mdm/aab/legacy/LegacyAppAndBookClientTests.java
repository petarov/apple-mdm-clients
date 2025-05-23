package net.vexelon.mdm.aab.legacy;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LegacyAppAndBookClientTests {

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
	void test_service_configuration(WireMockRuntimeInfo wm) throws Exception {
		var serverConfig = TestUtil.createClient(wm).fetchServiceConfiguration();

		// verify the headers are right
		verify(getRequestedFor(urlEqualTo("/VPPServiceConfigSrv")).withHeader("content-type",
						equalTo(HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8))
				.withHeader(HttpConsts.HEADER_USER_AGENT, equalTo("apple-mdm-legacy-aab-v1")));

		// verify the account data is right
		assertEquals("%s/mdm/getVPPLicensesSrv".formatted(wm.getHttpBaseUrl()), serverConfig.getLicensesSrvUrl());
		assertEquals("%s/mdm/getVPPUserSrv".formatted(wm.getHttpBaseUrl()), serverConfig.getUserSrvUrl());
		assertEquals("%s/mdm/getVPPUsersSrv".formatted(wm.getHttpBaseUrl()), serverConfig.getUsersSrvUrl());
		assertEquals(0, serverConfig.status());
		assertEquals(10, serverConfig.maxBatchDisassociateLicenseCount());
		assertEquals(10, serverConfig.maxBatchAssociateLicenseCount());
		assertEquals("%s/mdm/manageVPPLicensesByAdamIdSrv".formatted(wm.getHttpBaseUrl()),
				serverConfig.manageVPPLicensesByAdamIdSrvUrl());
		assertEquals(46, serverConfig.errorCodes().size());
		assertEquals(9600, serverConfig.errorCodes().getFirst().errorNumber());
		assertEquals("Missing required argument", serverConfig.errorCodes().getFirst().errorMessage());
		assertEquals(9729, serverConfig.errorCodes().getLast().errorNumber());
		assertEquals("The server has since moved, please refresh DNS entry.",
				serverConfig.errorCodes().getLast().errorMessage());
	}

	@Test
	void test_update_configuration(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/mdm/VPPClientConfigSrv")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "countryCode": "DE",
						    "clientContext": "{\\"id\\":\\"somesever8910\\"}\\n",
						    "organizationId": 2105000001686525,
						    "location": {
						        "locationName": "Petarov GmbH - Test",
						        "locationId": 1106700002122099
						    },
						    "email": "max.mustermann@petarov.appleid.com",
						    "notificationToken": "",
						    "uId": "9397866",
						    "status": 0,
						    "defaultPlatform": "enterprisestore",
						    "expirationMillis": 1776630125812,
						    "organizationIdHash": "c481927db98abe52e4e64e4a2f8affde2494d71445c7e16afbb4df946c08729a",
						    "appleId": "max.mustermann@petarov.appleid.com",
						    "apnToken": "ET4aeBZ+JNfOmIPgMiEgMg="
						}
						""".stripIndent())));

		var clientConfig = TestUtil.createClient(wm)
				.updateClientConfiguration("{\"id\":\"somesever8910\"}", "ET4aeBZ+JNfOmIPgMiEgMg=");

		// verify the request body has the correct sToken
		verify(postRequestedFor(urlEqualTo("/mdm/VPPClientConfigSrv")).withRequestBody(equalToJson("""
				{"clientContext": "{\\"id\\":\\"somesever8910\\"}", "notificationToken": "ET4aeBZ+JNfOmIPgMiEgMg=", "sToken": "%s"}
				""".stripIndent().formatted(TestUtil.getSToken()), true, false)));

		// verify the account data is right
		assertEquals("DE", clientConfig.getCountryCode());
		assertEquals("{\"id\":\"somesever8910\"}\n", clientConfig.getClientContext());
		assertEquals("2105000001686525", clientConfig.getOrganizationId());
		assertEquals("Petarov GmbH - Test", clientConfig.getResponse().location().locationName());
		assertEquals(1106700002122099L, clientConfig.getResponse().location().locationId());
		assertEquals("max.mustermann@petarov.appleid.com", clientConfig.getEmail());
		assertEquals("9397866", clientConfig.getResponse().uId());
		assertEquals(0, clientConfig.getResponse().status());
		assertEquals("enterprisestore", clientConfig.getDefaultPlatform());
		assertEquals(1776630125812L, clientConfig.getResponse().expirationMillis());
		assertEquals("c481927db98abe52e4e64e4a2f8affde2494d71445c7e16afbb4df946c08729a",
				clientConfig.getOrganizationIdHash());
		assertEquals("max.mustermann@petarov.appleid.com", clientConfig.getAppleId());
		assertEquals("ET4aeBZ+JNfOmIPgMiEgMg=", clientConfig.getApnToken());
	}
}