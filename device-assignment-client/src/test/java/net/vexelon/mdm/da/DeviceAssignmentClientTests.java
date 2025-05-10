package net.vexelon.mdm.da;

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

	@Test
	void test_fetch_beta_enrollment_tokens(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/os-beta-enrollment/tokens")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "betaEnrollmentTokens": [
						        {
						            "token": "ZS16DKkGwAGFsF5on9evLPYkFmq99fs2EFBHd3F2JUXpJVUvAYBDwyFRQn1nVLp8",
						            "title": "visionOS Developer Beta",
						            "os": "visionOS"
						        },
						        {
						            "token": "eLcTr7mLGCKMG9iLdyVJ6DuPPcVSeks8dbh19EYmmcaSq5gWgu2XKLSTPKUF9r9y",
						            "title": "iOS 17 AppleSeed Beta",
						            "os": "iOS"
						        },
						        {
						            "token": "LtbJZUKkXWQuX9R5TtMMNXwreDhScfdZ5FmEnzLtqAKXYqRTRgnYahoG3DnWZaUh",
						            "title": "iOS 16 AppleSeed Beta",
						            "os": "iOS"
						        },
						        {
						            "token": "3zok2onMHfoMjGuoaSpDbq5DdQtq4cRYfS2eApLovxmEPYfrVTAA3uwdNqRnnjiZ",
						            "title": "watchOS 9 AppleSeed Beta",
						            "os": "watchOS"
						        },
						        {
						            "token": "ETf1Kskkx5z6R5pB7vkKLbxf63MHXvksPd2PmzHHhBb6PycCU1FeGy2PcDxVEQZa",
						            "title": "tvOS 16 AppleSeed Beta",
						            "os": "tvOS"
						        },
						        {
						            "token": "GLZUYgFJBNG6pwrppRGBZEUzqv1n6tKpPkhtTT7UyUECi9JtRsZubbRJj67RbnjJ",
						            "title": "macOS Sonoma AppleSeed Beta",
						            "os": "OSX"
						        },
						        {
						            "token": "sokXoiafhinCQkM2SfSJxVfstnSfL1HX2ZK9vFnHSdzpNnBXu8WYX1yHkvJNhZso",
						            "title": "macOS Ventura AppleSeed Beta",
						            "os": "OSX"
						        },
						        {
						            "token": "n2TmDBAy11PWhueoJFT8HoFyuiw1U8a34u6mAbCyVSJpD4St9VRDxs95kmGxjUG3",
						            "title": "iOS 18 AppleSeed Beta",
						            "os": "iOS"
						        },
						        {
						            "token": "TDtaNhvtQQyiF2g3ADywnQhFUYXoodMrVjxYDqmy187vg3vEZpNdfbbNxhCVBRpm",
						            "title": "tvOS 18 AppleSeed Beta",
						            "os": "tvOS"
						        },
						        {
						            "token": "ybVYn7FqNBFeu8NVFvePE1BtfwMf1j6N8dzBazv131FxR4ARk7UeQ7vfQRjTy1w4",
						            "title": "visionOS 2 AppleSeed Beta",
						            "os": "visionOS"
						        },
						        {
						            "token": "pMvBppeG6gahZVdLY6BP7r5PCidwcDUBG33azJMCJsgKxvhtCaknPt6aASoE2cg1",
						            "title": "watchOS 11 AppleSeed Beta",
						            "os": "watchOS"
						        },
						        {
						            "token": "LzRt9NaUMbw7xyyJ2Zav6q5tzCuWcC9T1bDRSfajS87GQ5dW2mMevwTygHUZ8GzM",
						            "title": "tvOS 17 AppleSeed Beta",
						            "os": "tvOS"
						        },
						        {
						            "token": "njMBtZAZFk1uv9LVjqajxBqA5ZYvaqf8hjW5iFVjUVHGSx4zUyUuiAYaqWc6gM9n",
						            "title": "macOS Sequoia AppleSeed Beta",
						            "os": "OSX"
						        },
						        {
						            "token": "q1toS113vePpcfWLKnWVhdwYD1zd5tHK55WWcJQ1MLdHTi17XdRdpFLrHzpduUNT",
						            "title": "watchOS 10 AppleSeed Beta",
						            "os": "watchOS"
						        },
						        {
						            "token": "WkaTiJV3QAvUkeo46QJynL631Pxja6sbGTiPjcjHUncAZdL1fozjSPbmMhy1u2Ws",
						            "title": "macOS Sequoia 15.2 AppleSeed Beta",
						            "os": "OSX"
						        }
						    ]
						}
						""".stripIndent())));

		var seedBuild = TestUtil.createClient(wm).fetchBetaEnrollmentTokens();
		assertEquals(15, seedBuild.betaEnrollmentTokens().size());
		assertEquals("ZS16DKkGwAGFsF5on9evLPYkFmq99fs2EFBHd3F2JUXpJVUvAYBDwyFRQn1nVLp8",
				seedBuild.betaEnrollmentTokens().getFirst().token());
		assertEquals("visionOS Developer Beta", seedBuild.betaEnrollmentTokens().getFirst().title());
		assertEquals("visionOS", seedBuild.betaEnrollmentTokens().getFirst().os());
		assertEquals("WkaTiJV3QAvUkeo46QJynL631Pxja6sbGTiPjcjHUncAZdL1fozjSPbmMhy1u2Ws",
				seedBuild.betaEnrollmentTokens().getLast().token());
		assertEquals("macOS Sequoia 15.2 AppleSeed Beta", seedBuild.betaEnrollmentTokens().getLast().title());
		assertEquals("OSX", seedBuild.betaEnrollmentTokens().getLast().os());
	}
}
