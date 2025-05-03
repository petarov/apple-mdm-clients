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
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		stubFor(get(urlEqualTo("/session")).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
				{"auth_session_token":"1745786035268O1O789F19CF078867E47DC9D9BF4682D021O75CA72ECB87046A1B2239D9CFA4D6771O420397O11Op1OB123AA978976E390FF7693C640C92D3F8F6FE7F6O81E6CAAC7816AD3E12D531496695CF5A"}
				""".stripIndent())));
	}

	@Test
	void test_unassign_profile(WireMockRuntimeInfo wm) throws Exception {
		stubFor(delete(urlEqualTo("/profile/devices")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"devices":{"C112342756":"SUCCESS","B222342AF8":"NOT_ACCESSIBLE"}}
						""".stripIndent())));

		var response = TestUtil.createClient(wm)
				.unassignProfile("95C2189CA0EFB3272AC8B3C66201F33", Set.of("C112342756", "B222342AF8"));

		assertEquals(2, response.devices().size());
		assertEquals("SUCCESS", response.devices().get("C112342756"));
		assertEquals("NOT_ACCESSIBLE", response.devices().get("B222342AF8"));
	}
}
