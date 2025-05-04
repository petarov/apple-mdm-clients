package com.github.petarov.mdm.da;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.Security;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceAssignmentClientTests {

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
	void test_fetch_account(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/account")).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
				{
				    "server_name": "MY-DEV-LOCAL (server.petarov.net)",
				    "server_uuid": "F6E1E81A93837E1E87D6F4D99E235117",
				    "facilitator_id": "max.mustermann@petarov.net",
				    "admin_id": "max.mustermann@petarov.appleid.com",
				    "org_name": "petarov GmbH",
				    "org_email": "orga@petarov.net",
				    "org_phone": "0123456789",
				    "org_address": "Unknown Street 123, 12345 City",
				    "org_id": "780420",
				    "org_id_hash": "f6e1e81a93837e1e87d6f4d99e235117f12ccc0b567bd0e7c126d7f28395d3d3",
				    "urls": [
				        {
				            "uri": "/session",
				            "http_method": ["GET"]
				        },
				        {
				            "uri": "/server/devices",
				            "http_method": ["POST"],
				            "limit": {
				                "default": 1000,
				                "maximum": 1000
				            }
				        },
				        {
				            "uri": "/account-driven-enrollment/profile",
				            "http_method": ["DELETE", "POST", "GET"]
				        }
				    ],
				    "org_type": "org",
				    "org_version": "v2"
				}
				""".stripIndent())));

		var accountDetail = TestUtil.createClient(wm).fetchAccount();

		// verify the headers are right
		verify(getRequestedFor(urlEqualTo("/account")).withHeader("x-adm-auth-session",
						equalTo("1745786035268O1O789F19CF078867E47DC9D9BF4682D021O75CA72ECB87046A1B2239D9CFA4D6771O420397O11Op1OB123AA978976E390FF7693C640C92D3F8F6FE7F6O81E6CAAC7816AD3E12D531496695CF5A"))
				.withHeader("x-server-protocol-version", equalTo("3"))
				.withHeader("content-type", equalTo("application/json;charset=UTF8"))
				.withHeader("user-agent", equalTo("apple-mdm-device-assignment-v1")));

		// verify the account data is right
		assertEquals("MY-DEV-LOCAL (server.petarov.net)", accountDetail.serverName());
		assertEquals("F6E1E81A93837E1E87D6F4D99E235117", accountDetail.serverUuid());
		assertEquals("max.mustermann@petarov.appleid.com", accountDetail.adminId());
		assertEquals("petarov GmbH", accountDetail.orgName());
		assertEquals("orga@petarov.net", accountDetail.orgEmail());
		assertEquals("0123456789", accountDetail.orgPhone());
		assertEquals("Unknown Street 123, 12345 City", accountDetail.orgAddress());
		assertEquals("780420", accountDetail.orgId());
		assertEquals("f6e1e81a93837e1e87d6f4d99e235117f12ccc0b567bd0e7c126d7f28395d3d3", accountDetail.orgIdHash());
		assertEquals("org", accountDetail.orgType());
		assertEquals("v2", accountDetail.orgVersion());

		// not all possible urls are verified here
		assertEquals(3, accountDetail.urls().size());
		assertEquals("/session", accountDetail.urls().getFirst().uri());
		assertEquals(1, accountDetail.urls().getFirst().httpMethod().size());
		assertEquals("GET", accountDetail.urls().getFirst().httpMethod().getFirst());
		assertEquals(0, accountDetail.urls().getFirst().limit().defaultLimit());
		assertEquals(0, accountDetail.urls().getFirst().limit().maximum());

		assertEquals("/server/devices", accountDetail.urls().get(1).uri());
		assertEquals("POST", accountDetail.urls().get(1).httpMethod().getFirst());
		assertEquals(1000, accountDetail.urls().get(1).limit().defaultLimit());
		assertEquals(1000, accountDetail.urls().get(1).limit().maximum());

		assertEquals("/account-driven-enrollment/profile", accountDetail.urls().getLast().uri());
		assertEquals(3, accountDetail.urls().getLast().httpMethod().size());
		assertEquals("DELETE", accountDetail.urls().getLast().httpMethod().getFirst());
	}
}
