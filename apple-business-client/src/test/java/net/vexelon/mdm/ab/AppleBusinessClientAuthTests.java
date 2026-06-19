package net.vexelon.mdm.ab;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.shared.config.MdmClientBuilder;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.SecureRandom;
import java.security.Security;
import java.time.Duration;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppleBusinessClientAuthTests {

	private static final String CLIENT_ID = "BUSINESSAPI.9703f56c-10ce-4876-8f59-e78e5e23a152";
	private static final String TEAM_ID   = "BUSINESSAPI.9703f56c-10ce-4876-8f59-e78e5e23a152";
	private static final String KEY_ID    = "d136aa66-0c3b-4bd4-9892-c20e8db024ab";

	private static final String TOKEN_RESPONSE = """
			{
			    "access_token": "test-bearer-token-12345",
			    "token_type": "Bearer",
			    "expires_in": 3600,
			    "scope": "business.api"
			}
			""".stripIndent();

	@BeforeAll
	void init() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Test
	void refresh_access_token_returns_token(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlPathEqualTo("/auth/oauth2/token")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json").withBody(TOKEN_RESPONSE)));

		var auth = createAuth(wm);
		var token = auth.refreshAccessToken();

		assertEquals("test-bearer-token-12345", token);
	}

	@Test
	void get_access_token_is_cached(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlPathEqualTo("/auth/oauth2/token")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json").withBody(TOKEN_RESPONSE)));

		var auth = createAuth(wm);

		// first call triggers a refresh
		var first = auth.getAccessToken();
		// second call must use cache - no second HTTP request
		var second = auth.getAccessToken();

		assertEquals("test-bearer-token-12345", first);
		assertEquals(first, second);
		verify(1, postRequestedFor(urlPathEqualTo("/auth/oauth2/token")));
	}

	@Test
	void token_request_contains_required_query_params(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlPathEqualTo("/auth/oauth2/token")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json").withBody(TOKEN_RESPONSE)));

		createAuth(wm).refreshAccessToken();

		verify(postRequestedFor(urlPathEqualTo("/auth/oauth2/token")).withQueryParam("grant_type",
						equalTo("client_credentials")).withQueryParam("client_id", equalTo(CLIENT_ID))
				.withQueryParam("client_assertion_type",
						equalTo("urn:ietf:params:oauth:client-assertion-type:jwt-bearer"))
				.withQueryParam("scope", equalTo("business.api"))
				.withQueryParam("client_assertion", matching(".+\\..+\\..+")));
	}

	@Test
	void token_request_has_form_urlencoded_content_type(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlPathEqualTo("/auth/oauth2/token")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json").withBody(TOKEN_RESPONSE)));

		createAuth(wm).refreshAccessToken();

		verify(postRequestedFor(urlPathEqualTo("/auth/oauth2/token")).withHeader("content-type",
				containing("application/x-www-form-urlencoded")));
	}

	@Test
	void explicit_refresh_replaces_cached_token(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlPathEqualTo("/auth/oauth2/token")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json").withBody(TOKEN_RESPONSE)));

		var auth = createAuth(wm);
		auth.getAccessToken();

		// Simulate a new token response for the explicit refresh
		stubFor(post(urlPathEqualTo("/auth/oauth2/token")).willReturn(
				aResponse().withStatus(200).withHeader("content-type", "application/json").withBody("""
						{
						    "access_token": "refreshed-bearer-token-99999",
						    "token_type": "Bearer",
						    "expires_in": 3600,
						    "scope": "business.api"
						}
						""".stripIndent())));

		var refreshed = auth.refreshAccessToken();
		assertEquals("refreshed-bearer-token-99999", refreshed);
		assertEquals("refreshed-bearer-token-99999", auth.getAccessToken());
	}

	private AppleBusinessClientAuth createAuth(WireMockRuntimeInfo wm) throws Exception {
		var privateKey = AppleBusinessPrivateKey.createFromPEM(
				Objects.requireNonNull(getClass().getResourceAsStream("/test_private_apple_style_key.pem")));

		var options = new MdmClientBuilder.MdmBuilderOptions(wm.getHttpBaseUrl(), "apple-business-client-test", false,
				Duration.ofSeconds(5), Duration.ofSeconds(10), null, new SecureRandom());
		var client = new HttpClientWrapper(options, AppleBusinessClientImpl.logger);

		return new AppleBusinessClientAuth(client, CLIENT_ID, KEY_ID, TEAM_ID, privateKey,
				wm.getHttpBaseUrl() + "/auth/oauth2/token", Duration.ofHours(1));
	}
}
