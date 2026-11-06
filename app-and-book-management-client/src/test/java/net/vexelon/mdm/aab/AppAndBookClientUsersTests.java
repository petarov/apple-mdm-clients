package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.aab.AppAndBookClient.FetchUsersOptions;
import net.vexelon.mdm.aab.model.PricingParam;
import net.vexelon.mdm.aab.model.UserStatus;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookClientUsersTests {

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = TestUtil.createDefaultHeaders();
	}

	private void stubOk(String path, String body) {
		stubFor(get(urlPathEqualTo(path)).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	@Test
	void test_fetch_users_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim (example request: ?activeOnly=true)
		stubOk("/users", """
						{
						    "currentPageIndex": 0,
						    "size": 3,
						    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
						    "totalPages": 1,
						    "uId": "2049025000431439",
						    "users": [
						        {
						            "clientUserId": "client-101",
						            "email": "client-101@apple.com",
						            "inviteCode": "46bc93ea3acd41e0a4919c02db0d7d3a",
						            "status": "Registered"
						        },
						        {
						            "clientUserId": "client-102",
						            "email": "client-102@apple.com",
						            "inviteCode": "d2ab1319ff6448f89bb1b0e010cf68e0",
						            "status": "Registered"
						        },
						        {
						            "clientUserId": "client-103",
						            "email": "client-1031@apple.com",
						            "status": "Retired"
						        }
						    ],
						    "versionId": "021f10a0-7035-11eb-9f67-bd1df52e1e13"
						}
						""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchUsers(new FetchUsersOptions("", "", true, null));

		verify(getRequestedFor(urlEqualTo("/users?activeOnly=true")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
				equalTo("Bearer " + TestUtil.getSToken())));
		assertEquals(3, resp.users().size());
		var first = resp.users().getFirst();
		assertEquals("client-101", first.clientUserId());
		assertEquals("client-101@apple.com", first.email());
		assertEquals("46bc93ea3acd41e0a4919c02db0d7d3a", first.inviteCode());
		assertEquals(UserStatus.REGISTERED, first.status());
		assertEquals("", first.idHash());
		var third = resp.users().get(2);
		assertEquals(UserStatus.RETIRED, third.status());
		assertEquals("", third.inviteCode());
		assertEquals("client-1031@apple.com", third.email());
		assertEquals(3, resp.size());
		assertEquals(0, resp.currentPageIndex());
		assertEquals(0, resp.nextPageIndex());
		assertEquals(1, resp.totalPages());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertEquals("021f10a0-7035-11eb-9f67-bd1df52e1e13", resp.versionId());
		assertNull(resp.mdmInfo());
	}

	@Test
	void test_fetch_users_real_world_payload(WireMockRuntimeInfo wm) throws Exception {
		// Shape of a real response with randomized values: mixed field presence, repeated clientUserIds, shared idHash
		stubOk("/users", """
				{"currentPageIndex":0,"size":12,"tokenExpirationDate":"2027-03-14T10:15:00+0000","totalPages":1,"uId":"2105000009876543","users":[
				{"clientUserId":"5d4f0f68-8fcb-45d6-85eb-b7858d7620ea","email":"jdoe@example.com","idHash":"bP/MYr3aNVL0dXS6CRodE3SYJrg=","status":"Associated"},
				{"clientUserId":"df464d5e-88d1-4f36-b7b6-fadd84128cd1","inviteCode":"4948c3bdbf09a2ff5069d3737dfa5774","status":"Registered"},
				{"clientUserId":"Z25vbWVAZXhhbXBsZS5vcmc=","email":"gnome@example.org","inviteCode":"ffb8005072a5aa76b5153c2f81d38dee","status":"Registered"},
				{"clientUserId":"fdc0de90e25ce4ef0607a9f5e1d4468821662912","email":"faun@example.org","idHash":"yfYNq7s696IQC7GnDonBJ1+aX44=","status":"Retired"},
				{"clientUserId":"Q049SmFuZSBSb2UvTz1BY21lL0M9dXM=","idHash":"/oKoeofnjFfOHs4iJ/v9xtoMu4s=","status":"Retired"},
				{"clientUserId":"Q049SmFuZSBSb2UvTz1BY21lL0M9dXM=","idHash":"Xq4t9ZpLm2Rw8VbN1cJ0eHs7KdA=","status":"Retired"},
				{"clientUserId":"114b5447-fb86-49d0-99fa-3e19bcb6ddb0","email":"kobold@example.org","idHash":"bP/MYr3aNVL0dXS6CRodE3SYJrg=","status":"Associated"},
				{"clientUserId":"c025c7041948b4dd2ec80f3141efbe61a6ea03bd","inviteCode":"00ecbd5e5f8f0ae32d27d0a9beb05420","status":"Registered"},
				{"clientUserId":"09c768ea-7cf8-4db5-b42e-9083d984a4e1","email":"qa.user2@example.net","idHash":"bP/MYr3aNVL0dXS6CRodE3SYJrg=","status":"Associated"},
				{"clientUserId":"38238a88-b4b6-40a9-af44-f78edd2b2c71","email":"sprite@example.org","inviteCode":"73bc9fd34acecc505c47eb45f4d48010","status":"Registered"},
				{"clientUserId":"S-1-5-21-1111111111-2222222222-3333333333-1001","email":"qa.user1@example.net","idHash":"bP/MYr3aNVL0dXS6CRodE3SYJrg=","status":"Associated"},
				{"clientUserId":"c025c7041948b4dd2ec80f3141efbe61a6ea03bd","idHash":"yfYNq7s696IQC7GnDonBJ1+aX44=","status":"Retired"}
				],"versionId":"86f963bd-7340-4a88-bbba-4859943fb5e7"}
				""");

		var resp = TestUtil.createClient(wm).fetchUsers();

		assertEquals(12, resp.users().size());
		assertEquals(12, resp.size());
		assertEquals(0, resp.nextPageIndex());
		assertEquals("2027-03-14T10:15:00+0000", resp.tokenExpirationDate());
		assertEquals("2105000009876543", resp.uId());
		assertEquals("86f963bd-7340-4a88-bbba-4859943fb5e7", resp.versionId());

		var noEmail = resp.users().get(1);
		assertEquals("", noEmail.email());
		assertEquals("", noEmail.idHash());
		assertEquals("4948c3bdbf09a2ff5069d3737dfa5774", noEmail.inviteCode());
		assertEquals(UserStatus.REGISTERED, noEmail.status());

		var idHashOnly = resp.users().get(4);
		assertEquals("", idHashOnly.email());
		assertEquals("", idHashOnly.inviteCode());
		assertEquals("/oKoeofnjFfOHs4iJ/v9xtoMu4s=", idHashOnly.idHash());

		assertEquals(resp.users().get(4).clientUserId(), resp.users().get(5).clientUserId());
		assertEquals(resp.users().get(7).clientUserId(), resp.users().get(11).clientUserId());
		assertEquals(UserStatus.REGISTERED, resp.users().get(7).status());
		assertEquals(UserStatus.RETIRED, resp.users().get(11).status());
		assertEquals("S-1-5-21-1111111111-2222222222-3333333333-1001", resp.users().get(10).clientUserId());

		assertEquals(4, resp.users().stream().filter(u -> u.status() == UserStatus.ASSOCIATED).count());
		assertEquals(4, resp.users().stream().filter(u -> u.status() == UserStatus.REGISTERED).count());
		assertEquals(4, resp.users().stream().filter(u -> u.status() == UserStatus.RETIRED).count());
		assertEquals(4,
				resp.users().stream().filter(u -> "bP/MYr3aNVL0dXS6CRodE3SYJrg=".equals(u.idHash())).count());
	}

	@Test
	void test_fetch_users_no_filters(WireMockRuntimeInfo wm) throws Exception {
		stubOk("/users", "{}");

		TestUtil.createClient(wm).fetchUsers();

		verify(getRequestedFor(urlEqualTo("/users")));
	}

	@Test
	void test_fetch_users_all_filters(WireMockRuntimeInfo wm) throws Exception {
		stubOk("/users", "{}");

		TestUtil.createClient(wm).fetchUsers(new FetchUsersOptions("client-1", "version-1", false, true), 1);

		verify(getRequestedFor(urlPathEqualTo("/users")).withQueryParam("pageIndex", equalTo("1"))
				.withQueryParam("clientUserId", equalTo("client-1"))
				.withQueryParam("sinceVersionId", equalTo("version-1"))
				.withQueryParam("activeOnly", equalTo("false")).withQueryParam("retiredOnly", equalTo("true")));
	}

	@Test
	void test_fetch_users_status_payload(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload: built from the documented schemas
		stubOk("/users", """
				{
				    "users": [
				        {"clientUserId": "client-1", "email": "c1@example.com", "idHash": "abc123hash", "status": "Associated"},
				        {"clientUserId": "client-2", "email": "c2@example.com", "status": "Deleted"},
				        {"clientUserId": "client-3", "email": "c3@example.com", "status": "Suspended"}
				    ],
				    "currentPageIndex": 1,
				    "nextPageIndex": 2,
				    "size": 3,
				    "totalPages": 3
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchUsers(1);

		assertEquals(UserStatus.ASSOCIATED, resp.users().get(0).status());
		assertEquals("abc123hash", resp.users().get(0).idHash());
		assertEquals(UserStatus.DELETED, resp.users().get(1).status());
		assertEquals(UserStatus.UNKNOWN, resp.users().get(2).status());
		assertEquals(2, resp.nextPageIndex());
		assertEquals("", resp.uId());
		verify(getRequestedFor(urlEqualTo("/users?pageIndex=1")));
	}
}
