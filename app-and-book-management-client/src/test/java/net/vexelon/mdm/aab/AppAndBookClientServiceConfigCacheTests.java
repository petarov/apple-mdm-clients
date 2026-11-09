package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Duration;
import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookClientServiceConfigCacheTests {

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = TestUtil.createDefaultHeaders();
	}

	private TestClock clock;

	@BeforeEach
	void initClock() {
		clock = new TestClock(Instant.parse("2026-01-01T00:00:00Z"));
		stubFor(get(urlPathEqualTo("/assets")).willReturn(okPage()));
		stubFor(get(urlPathEqualTo("/users")).willReturn(okPage()));
		stubFor(get(urlPathEqualTo("/assignments")).willReturn(okPage()));
		stubFor(get(urlPathEqualTo("/custom/assets")).willReturn(okPage()));
		stubFor(get(urlPathEqualTo("/custom/assignments")).willReturn(okPage()));
		stubFor(get(urlPathEqualTo("/custom/users")).willReturn(okPage()));
		stubFor(get(urlPathEqualTo("/a/assets")).willReturn(okPage()));
		stubFor(get(urlPathEqualTo("/b/assets")).willReturn(okPage()));
	}

	private ResponseDefinitionBuilder okPage() {
		// NOT an Apple example payload
		return aResponse().withStatus(200).withHeaders(headers)
				.withBody("{\"currentPageIndex\":0,\"size\":0,\"totalPages\":1}");
	}

	private void stubServiceConfig(String body) {
		stubFor(get(urlEqualTo("/service/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	private static RequestPatternBuilder sc() {
		return getRequestedFor(urlEqualTo("/service/config"));
	}

	@Test
	void test_first_call_fetches_service_config(WireMockRuntimeInfo wm) throws Exception {
		TestUtil.stubServiceConfig(wm);

		TestUtil.createClient(wm, clock).fetchAssets();

		verify(1, sc().withoutHeader(HttpConsts.HEADER_AUTHORIZATION));
		verify(1, getRequestedFor(urlEqualTo("/assets")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
				equalTo("Bearer " + TestUtil.getSToken())));
	}

	@Test
	void test_content_type_is_application_json(WireMockRuntimeInfo wm) throws Exception {
		TestUtil.stubServiceConfig(wm);

		TestUtil.createClient(wm, clock).fetchAssets();

		verify(sc().withHeader(HttpConsts.HEADER_CONTENT_TYPE, equalTo("application/json")));
		verify(getRequestedFor(urlEqualTo("/assets")).withHeader(HttpConsts.HEADER_CONTENT_TYPE,
				equalTo("application/json")));
	}

	@Test
	void test_cache_hit_within_ttl(WireMockRuntimeInfo wm) throws Exception {
		TestUtil.stubServiceConfig(wm);
		var client = TestUtil.createClient(wm, clock);

		client.fetchAssets();
		clock.advance(Duration.ofMinutes(4).plusSeconds(59));
		client.fetchUsers();

		verify(1, sc());
		verify(1, getRequestedFor(urlEqualTo("/assets")));
		verify(1, getRequestedFor(urlEqualTo("/users")));
	}

	@Test
	void test_refetch_after_ttl(WireMockRuntimeInfo wm) throws Exception {
		TestUtil.stubServiceConfig(wm);
		var client = TestUtil.createClient(wm, clock);

		client.fetchAssets();
		clock.advance(AppAndBookClientImpl.SERVICE_CONFIG_TTL);
		client.fetchAssets();

		verify(2, sc());
	}

	@Test
	void test_resolves_urls_from_service_config(WireMockRuntimeInfo wm) throws Exception {
		var base = wm.getHttpBaseUrl();
		stubServiceConfig("""
				{"urls": {"getAssets": "%1$s/custom/assets", "getAssignments": "%1$s/custom/assignments", \
				"getUsers": "%1$s/custom/users"}}
				""".formatted(base));
		var client = TestUtil.createClient(wm, clock);

		client.fetchAssets(1);
		client.fetchAssignments("1");
		client.fetchUsers();

		verify(1, getRequestedFor(urlEqualTo("/custom/assets?pageIndex=1")));
		verify(1, getRequestedFor(urlEqualTo("/custom/assignments?adamId=1")));
		verify(1, getRequestedFor(urlEqualTo("/custom/users")));
		verify(0, getRequestedFor(urlPathEqualTo("/assets")));
	}

	@Test
	void test_fallback_when_key_missing_or_blank(WireMockRuntimeInfo wm) throws Exception {
		stubServiceConfig("{\"urls\": {\"getUsers\": \"\"}}");
		var client = TestUtil.createClient(wm, clock);

		client.fetchAssets();
		client.fetchUsers();

		verify(1, getRequestedFor(urlEqualTo("/assets")));
		verify(1, getRequestedFor(urlEqualTo("/users")));
		verify(1, sc());
	}

	@Test
	void test_service_config_failure_propagates(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/service/config")).willReturn(aResponse().withStatus(500).withHeaders(headers)
				.withBody("{\"errorNumber\": 9603, \"errorMessage\": \"Internal error.\"}")));
		var client = TestUtil.createClient(wm, clock);

		var ex = assertThrows(AppAndBookClientException.class, client::fetchAssets);

		assertEquals(9603, ex.getCode());
		assertEquals(500, ex.getStatusCode());
		verify(0, getRequestedFor(urlPathEqualTo("/assets")));

		TestUtil.stubServiceConfig(wm);
		client.fetchAssets();

		verify(2, sc());
		verify(1, getRequestedFor(urlPathEqualTo("/assets")));
	}

	@Test
	void test_fetch_service_configuration_refreshes_cache(WireMockRuntimeInfo wm) throws Exception {
		var base = wm.getHttpBaseUrl();
		stubServiceConfig("{\"urls\": {\"getAssets\": \"%s/a/assets\"}}".formatted(base));
		var client = TestUtil.createClient(wm, clock);

		client.fetchAssets();
		verify(1, sc());
		verify(1, getRequestedFor(urlEqualTo("/a/assets")));

		clock.advance(Duration.ofMinutes(4));
		stubServiceConfig("{\"urls\": {\"getAssets\": \"%s/b/assets\"}}".formatted(base));
		client.fetchServiceConfiguration();
		verify(2, sc());

		clock.advance(Duration.ofMinutes(4));
		client.fetchAssets();

		verify(2, sc());
		verify(1, getRequestedFor(urlEqualTo("/b/assets")));
	}
}
