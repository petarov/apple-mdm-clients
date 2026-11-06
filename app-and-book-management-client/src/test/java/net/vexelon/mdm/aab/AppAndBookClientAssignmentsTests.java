package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.aab.AppAndBookClient.FetchAssignmentsOptions;
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
public class AppAndBookClientAssignmentsTests {

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
	void test_fetch_assignments_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim (example request: ?adamId=408709785)
		stubOk("/assignments", """
						{
						    "assignments": [
						        {
						            "adamId": "408709785",
						            "clientUserId": "client-1",
						            "pricingParam": "STDQ"
						        },
						        {
						            "adamId": "408709785",
						            "serialNumber": "serial-1",
						            "pricingParam": "STDQ"
						        }
						    ],
						    "size": 2,
						    "currentPageIndex": 0,
						    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
						    "totalPages": 1,
						    "uId": "2049025000431439",
						    "versionId": "009061cb-87d1-4ea8-ae4c-7849dc49224e"
						}
						""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchAssignments("408709785");

		verify(getRequestedFor(urlEqualTo("/assignments?adamId=408709785")).withHeader(
				HttpConsts.HEADER_AUTHORIZATION, equalTo("Bearer " + TestUtil.getSToken())));
		assertEquals(2, resp.assignments().size());
		var first = resp.assignments().getFirst();
		assertEquals("408709785", first.adamId());
		assertEquals("client-1", first.clientUserId());
		assertEquals("", first.serialNumber());
		assertEquals(PricingParam.STDQ, first.pricingParam());
		assertEquals("", first.idHash());
		assertEquals(UserStatus.UNKNOWN, first.userStatus());
		var second = resp.assignments().get(1);
		assertEquals("serial-1", second.serialNumber());
		assertEquals("", second.clientUserId());
		assertEquals(2, resp.size());
		assertEquals(0, resp.currentPageIndex());
		assertEquals(0, resp.nextPageIndex());
		assertEquals(1, resp.totalPages());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertEquals("009061cb-87d1-4ea8-ae4c-7849dc49224e", resp.versionId());
		assertNull(resp.mdmInfo());
	}

	@Test
	void test_fetch_assignments_user_state_query(WireMockRuntimeInfo wm) throws Exception {
		stubOk("/assignments", "{}");

		TestUtil.createClient(wm).fetchAssignments("408709785",
				new FetchAssignmentsOptions("", "", null, null, true, ""));

		verify(getRequestedFor(urlEqualTo("/assignments?adamId=408709785&includeUserState=true")));
	}

	@Test
	void test_fetch_assignments_all_filters(WireMockRuntimeInfo wm) throws Exception {
		stubOk("/assignments", "{}");

		TestUtil.createClient(wm).fetchAssignments("408709785",
				new FetchAssignmentsOptions("client-1", "serial-1", PricingParam.PLUS, false, true, "version-1"), 3);

		verify(getRequestedFor(urlPathEqualTo("/assignments")).withQueryParam("pageIndex", equalTo("3"))
				.withQueryParam("adamId", equalTo("408709785")).withQueryParam("clientUserId", equalTo("client-1"))
				.withoutQueryParam("serialNumber").withQueryParam("pricingParam", equalTo("PLUS"))
				.withQueryParam("excludeInactiveUsers", equalTo("false"))
				.withQueryParam("includeUserState", equalTo("true"))
				.withQueryParam("sinceVersionId", equalTo("version-1")));
	}

	@Test
	void test_fetch_assignments_serial_number(WireMockRuntimeInfo wm) throws Exception {
		stubOk("/assignments", "{}");

		TestUtil.createClient(wm).fetchAssignments("", FetchAssignmentsOptions.ofSerialNumber("serial-1"));

		verify(getRequestedFor(urlEqualTo("/assignments?serialNumber=serial-1")));
	}

	@Test
	void test_fetch_assignments_encodes_values(WireMockRuntimeInfo wm) throws Exception {
		stubOk("/assignments", "{}");

		TestUtil.createClient(wm).fetchAssignments("", FetchAssignmentsOptions.ofClientUserId("a b&c"));

		verify(getRequestedFor(urlPathEqualTo("/assignments")).withQueryParam("clientUserId", equalTo("a b&c")));
	}

	@Test
	void test_fetch_assignments_user_state_payload(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload: built from the documented schemas
		stubOk("/assignments", """
				{
				    "assignments": [
				        {
				            "adamId": "408709785",
				            "clientUserId": "client-1",
				            "idHash": "abc123hash",
				            "pricingParam": "STDQ",
				            "userStatus": "Associated"
				        },
				        {
				            "adamId": "408709785",
				            "clientUserId": "client-2",
				            "pricingParam": "GOLD",
				            "userStatus": "Deleted"
				        },
				        {
				            "adamId": "408709785",
				            "clientUserId": "client-3",
				            "pricingParam": "PLUS",
				            "userStatus": "Suspended"
				        }
				    ],
				    "currentPageIndex": 0,
				    "nextPageIndex": 1,
				    "size": 3,
				    "totalPages": 2,
				    "uId": "2049025000431439",
				    "versionId": "009061cb-87d1-4ea8-ae4c-7849dc49224e",
				    "mdmInfo": {
				        "id": "522d5c43-0000-0000-0000-000000000000",
				        "metadata": "2.13.3",
				        "name": "Apple Configurator 2"
				    }
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchAssignments("408709785", FetchAssignmentsOptions.ofEmpty());

		assertEquals(3, resp.assignments().size());
		assertEquals(UserStatus.ASSOCIATED, resp.assignments().get(0).userStatus());
		assertEquals("abc123hash", resp.assignments().get(0).idHash());
		assertEquals(UserStatus.DELETED, resp.assignments().get(1).userStatus());
		assertEquals(PricingParam.UNKNOWN, resp.assignments().get(1).pricingParam());
		assertEquals(UserStatus.UNKNOWN, resp.assignments().get(2).userStatus());
		assertEquals(PricingParam.PLUS, resp.assignments().get(2).pricingParam());
		assertEquals(1, resp.nextPageIndex());
		assertEquals(2, resp.totalPages());
		assertNotNull(resp.mdmInfo());
		assertEquals("Apple Configurator 2", resp.mdmInfo().name());
	}

	@Test
	void test_fetch_assignments_unknown_pricing_param(WireMockRuntimeInfo wm) throws Exception {
		var client = TestUtil.createClient(wm);

		var ex = assertThrows(IllegalArgumentException.class, () -> client.fetchAssignments("",
				new FetchAssignmentsOptions("", "", PricingParam.UNKNOWN, null, null, "")));
		assertEquals("pricingParam must not be UNKNOWN", ex.getMessage());
		verify(0, getRequestedFor(anyUrl()));
	}

	@Test
	void test_fetch_assignments_error(WireMockRuntimeInfo wm) throws Exception {
		var message = "The provided page index must be greater than 0, and less than the total number of pages.";
		stubFor(get(urlPathEqualTo("/assignments")).willReturn(aResponse().withStatus(400).withHeaders(headers)
				.withBody("{\"errorNumber\": 9650, \"errorMessage\": \"%s\"}".formatted(message))));

		var ex = assertThrows(AppAndBookClientException.class, () -> TestUtil.createClient(wm).fetchAssignments(""));

		assertEquals(9650, ex.getCode());
		assertEquals(400, ex.getStatusCode());
		assertEquals(message, ex.getErrorMessage());
	}
}
