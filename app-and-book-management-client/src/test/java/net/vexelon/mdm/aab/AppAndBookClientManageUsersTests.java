package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.aab.model.RequestUser;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookClientManageUsersTests {

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = TestUtil.createDefaultHeaders();
	}

	@BeforeEach
	void stubServiceConfig(WireMockRuntimeInfo wm) {
		TestUtil.stubServiceConfig(wm);
	}

	private void stubOk(String path, String body) {
		stubFor(post(urlPathEqualTo(path)).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	private static Set<String> set(String... values) {
		return new LinkedHashSet<>(List.of(values));
	}

	@Test
	void test_create_users_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim
		stubOk("/users/create", """
				{
				    "eventId": "af70eb9b-0ab6-405d-87d8-3b9ed0c4f370",
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439"
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).createUsers(new LinkedHashSet<>(
				List.of(new RequestUser("client-100", "client-100@next.com", "maid-100@next.com"),
						new RequestUser("client-101", "client-101@next.com", "maid-101@next.com"),
						new RequestUser("client-102", "client-102@next.com", null))));

		// Apple's example request, verbatim
		verify(1, postRequestedFor(urlEqualTo("/users/create")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
						equalTo("Bearer " + TestUtil.getSToken()))
				.withHeader(HttpConsts.HEADER_CONTENT_TYPE, equalTo("application/json")).withRequestBody(equalToJson("""
						{
						    "users": [
						        {
						            "clientUserId": "client-100",
						            "email": "client-100@next.com",
						            "managedAppleId": "maid-100@next.com"
						        },
						        {
						            "clientUserId": "client-101",
						            "email": "client-101@next.com",
						            "managedAppleId": "maid-101@next.com"
						        },
						        {
						            "clientUserId": "client-102",
						            "email": "client-102@next.com"
						        }
						    ]
						}
						""".stripIndent(), true, false)));
		assertEquals("af70eb9b-0ab6-405d-87d8-3b9ed0c4f370", resp.eventId());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertNull(resp.mdmInfo());
	}

	@Test
	void test_create_users_omits_null_fields(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("/users/create", "{\"eventId\": \"e1\"}");

		TestUtil.createClient(wm).createUsers(Set.of(new RequestUser("client-1", null, null)));

		verify(1, postRequestedFor(urlEqualTo("/users/create")).withRequestBody(
				equalToJson("{\"users\": [{\"clientUserId\": \"client-1\"}]}", false, false)));
	}

	@Test
	void test_update_users_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response (missing comma after tokenExpirationDate fixed)
		stubOk("/users/update", """
				{
				    "eventId": "dcd54e0c-5898-4837-8cea-b220f9570835",
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439"
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).updateUsers(new LinkedHashSet<>(
				List.of(new RequestUser("client-100", "client-100@apple.com", null),
						new RequestUser("client-101", "client-101@apple.com", null),
						new RequestUser("client-102", "client-102@apple.com", "maid-102@apple.com"))));

		// Apple's example request, verbatim
		verify(1, postRequestedFor(urlEqualTo("/users/update")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
						equalTo("Bearer " + TestUtil.getSToken()))
				.withHeader(HttpConsts.HEADER_CONTENT_TYPE, equalTo("application/json")).withRequestBody(equalToJson("""
						{
						    "users": [
						        {
						            "clientUserId": "client-100",
						            "email": "client-100@apple.com"
						        },
						        {
						            "clientUserId": "client-101",
						            "email": "client-101@apple.com"
						        },
						        {
						            "clientUserId": "client-102",
						            "email": "client-102@apple.com",
						            "managedAppleId": "maid-102@apple.com"
						        }
						    ]
						}
						""".stripIndent(), true, false)));
		verify(0, postRequestedFor(urlEqualTo("/users/create")));
		assertEquals("dcd54e0c-5898-4837-8cea-b220f9570835", resp.eventId());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
	}

	@Test
	void test_retire_users_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim
		stubOk("/users/retire", """
				{
				    "eventId": "dafdad60-4ef6-49b0-8150-64323f56d88d",
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439"
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).retireUsers(Set.of("client-100", "client-101"));

		// Apple's example request, verbatim
		verify(1, postRequestedFor(urlEqualTo("/users/retire")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
						equalTo("Bearer " + TestUtil.getSToken()))
				.withHeader(HttpConsts.HEADER_CONTENT_TYPE, equalTo("application/json")).withRequestBody(equalToJson("""
						{
						    "users": [
						        {
						            "clientUserId": "client-100"
						        },
						        {
						            "clientUserId": "client-101"
						        }
						    ]
						}
						""".stripIndent(), true, false)));
		assertEquals("dafdad60-4ef6-49b0-8150-64323f56d88d", resp.eventId());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
	}

	@Test
	void test_manage_users_send_empty_sets(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("/users/create", "{}");
		stubOk("/users/update", "{}");
		stubOk("/users/retire", "{}");
		var client = TestUtil.createClient(wm);

		client.createUsers(Set.of());
		client.updateUsers(Set.of());
		client.retireUsers(Set.of());

		verify(1, postRequestedFor(urlEqualTo("/users/create")).withRequestBody(equalToJson("{\"users\": []}")));
		verify(1, postRequestedFor(urlEqualTo("/users/update")).withRequestBody(equalToJson("{\"users\": []}")));
		verify(1, postRequestedFor(urlEqualTo("/users/retire")).withRequestBody(equalToJson("{\"users\": []}")));
	}

	@Test
	void test_create_users_error(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubFor(post(urlPathEqualTo("/users/create")).willReturn(aResponse().withStatus(400).withHeaders(headers)
				.withBody("{\"errorNumber\": 9715, \"errorMessage\": \"This request contains duplicate users. "
						+ "Change the request to send unique users.\"}")));

		var ex = assertThrows(AppAndBookClientException.class,
				() -> TestUtil.createClient(wm).createUsers(Set.of(new RequestUser("client-1", null, null))));

		assertEquals(9715, ex.getCode());
		assertEquals(400, ex.getStatusCode());
	}

	@Test
	void test_manage_users_fallback_when_key_missing(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/service/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("{\"urls\": {}}")));
		stubOk("/users/create", "{}");
		stubOk("/users/update", "{}");
		stubOk("/users/retire", "{}");
		var client = TestUtil.createClient(wm);

		client.createUsers(Set.of(new RequestUser("client-1", null, null)));
		client.updateUsers(Set.of(new RequestUser("client-1", null, null)));
		client.retireUsers(set("client-1"));

		verify(1, postRequestedFor(urlEqualTo("/users/create")));
		verify(1, postRequestedFor(urlEqualTo("/users/update")));
		verify(1, postRequestedFor(urlEqualTo("/users/retire")));
	}

	@Test
	void test_manage_users_resolve_urls(WireMockRuntimeInfo wm) throws Exception {
		var base = wm.getHttpBaseUrl();
		stubFor(get(urlEqualTo("/service/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"urls": {"createUsers": "%1$s/custom/create", \
						"updateUsers": "%1$s/custom/update", "retireUsers": "%1$s/custom/retire"}}
						""".formatted(base))));
		stubOk("/custom/create", "{}");
		stubOk("/custom/update", "{}");
		stubOk("/custom/retire", "{}");
		var client = TestUtil.createClient(wm);

		client.createUsers(Set.of(new RequestUser("client-1", null, null)));
		client.updateUsers(Set.of(new RequestUser("client-1", null, null)));
		client.retireUsers(set("client-1"));

		verify(1, postRequestedFor(urlEqualTo("/custom/create")));
		verify(1, postRequestedFor(urlEqualTo("/custom/update")));
		verify(1, postRequestedFor(urlEqualTo("/custom/retire")));
		verify(0, postRequestedFor(urlEqualTo("/users/create")));
		verify(0, postRequestedFor(urlEqualTo("/users/update")));
		verify(0, postRequestedFor(urlEqualTo("/users/retire")));
	}
}
