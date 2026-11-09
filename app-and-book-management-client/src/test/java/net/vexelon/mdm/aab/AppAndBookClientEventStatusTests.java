package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.aab.model.EventStatus;
import net.vexelon.mdm.aab.model.EventType;
import net.vexelon.mdm.aab.model.PricingParam;
import net.vexelon.mdm.aab.model.ResponseErrorAsset;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookClientEventStatusTests {

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
		stubFor(get(urlPathEqualTo("/status")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	@Test
	void test_fetch_event_status_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim
		stubOk("""
				{
				    "eventStatus": "COMPLETE",
				    "eventType": "ASSOCIATE",
				    "numCompleted": 4000,
				    "numRequested": 4000,
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439"
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchEventStatus("1905643d-1afb-4c2d-ad74-1b268e92c880");

		verify(getRequestedFor(urlEqualTo("/status?eventId=1905643d-1afb-4c2d-ad74-1b268e92c880")).withHeader(
				HttpConsts.HEADER_AUTHORIZATION, equalTo("Bearer " + TestUtil.getSToken())).withRequestBody(absent()));
		assertEquals(EventStatus.COMPLETE, resp.eventStatus());
		assertEquals(EventType.ASSOCIATE, resp.eventType());
		assertEquals(4000, resp.numCompleted());
		assertEquals(4000, resp.numRequested());
		assertTrue(resp.failures().isEmpty());
		assertNull(resp.mdmInfo());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
	}

	@Test
	void test_fetch_event_status_pending(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim (from the managing-assets article)
		stubOk("""
				{
				    "eventStatus": "PENDING",
				    "eventType": "ASSOCIATE",
				    "numCompleted": 8,
				    "numRequested": 16,
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439"
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchEventStatus("29ddf6fe-8b2e-4c3f-91d9-aea3c63e4235");

		assertEquals(EventStatus.PENDING, resp.eventStatus());
		assertEquals(EventType.ASSOCIATE, resp.eventType());
		assertEquals(8, resp.numCompleted());
		assertEquals(16, resp.numRequested());
		assertTrue(resp.failures().isEmpty());
	}

	@Test
	void test_fetch_event_status_failed(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim (from the managing-assets article)
		stubOk("""
				{
				    "eventStatus": "FAILED",
				    "eventType": "ASSOCIATE",
				    "failures": [
				        {
				            "errorInfo": {
				                "clientUserIds": [
				                    "client-4"
				                ]
				            },
				            "errorNumber": 9609,
				            "errorMessage": "Registered user not found"
				        }
				    ],
				    "numCompleted": 14,
				    "numRequested": 16,
				    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
				    "uId": "2049025000431439"
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchEventStatus("a8e0edbf-bfc2-405f-92bd-08d6d72e7a1d");

		assertEquals(EventStatus.FAILED, resp.eventStatus());
		assertEquals(14, resp.numCompleted());
		assertEquals(16, resp.numRequested());
		assertEquals(1, resp.failures().size());
		var failure = resp.failures().getFirst();
		assertEquals(9609, failure.errorNumber());
		assertEquals("Registered user not found", failure.errorMessage());
		assertEquals(List.of("client-4"), failure.errorInfo().clientUserIds());
		assertTrue(failure.errorInfo().assets().isEmpty());
		assertTrue(failure.errorInfo().serialNumbers().isEmpty());
	}

	@Test
	void test_fetch_event_status_failure_details(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{
				    "eventStatus": "FAILED",
				    "eventType": "ASSOCIATE",
				    "failures": [
				        {
				            "errorInfo": {
				                "assets": [{"adamId": "408709785", "pricingParam": "STDQ"}, {"adamId": "377298193"}],
				                "serialNumbers": ["serial-1"]
				            },
				            "errorNumber": 9709,
				            "errorMessage": "Not enough licenses."
				        },
				        {
				            "errorNumber": 9603,
				            "errorMessage": "Internal error."
				        }
				    ]
				}
				""");

		var resp = TestUtil.createClient(wm).fetchEventStatus("e1");

		assertEquals(2, resp.failures().size());
		var info = resp.failures().getFirst().errorInfo();
		assertEquals(List.of(new ResponseErrorAsset("408709785", PricingParam.STDQ),
				new ResponseErrorAsset("377298193", PricingParam.UNKNOWN)), info.assets());
		assertEquals(List.of("serial-1"), info.serialNumbers());
		assertTrue(info.clientUserIds().isEmpty());
		assertNull(resp.failures().get(1).errorInfo());
	}

	@Test
	void test_fetch_event_status_unknown_values(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		var client = TestUtil.createClient(wm);

		stubOk("{\"eventStatus\": \"CANCELLED\", \"eventType\": \"SUBSCRIBE\"}");
		var a = client.fetchEventStatus("e1");
		assertEquals(EventStatus.UNKNOWN, a.eventStatus());
		assertEquals(EventType.UNKNOWN, a.eventType());

		stubOk("{\"eventStatus\": null, \"eventType\": null, \"failures\": null}");
		var b = client.fetchEventStatus("e1");
		assertEquals(EventStatus.UNKNOWN, b.eventStatus());
		assertEquals(EventType.UNKNOWN, b.eventType());
		assertTrue(b.failures().isEmpty());

		stubOk("{}");
		var c = client.fetchEventStatus("e1");
		assertEquals(EventStatus.UNKNOWN, c.eventStatus());
		assertEquals(EventType.UNKNOWN, c.eventType());
		assertTrue(c.failures().isEmpty());
		assertEquals(0, c.numCompleted());
		assertEquals(0, c.numRequested());
	}

	@Test
	void test_fetch_event_status_lowercase_values(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("{\"eventStatus\": \"complete\", \"eventType\": \"revoke\"}");

		var resp = TestUtil.createClient(wm).fetchEventStatus("e1");

		assertEquals(EventStatus.COMPLETE, resp.eventStatus());
		assertEquals(EventType.REVOKE, resp.eventType());
	}

	@Test
	void test_fetch_event_status_encodes_event_id(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("{}");

		TestUtil.createClient(wm).fetchEventStatus("a b&c");

		verify(1, getRequestedFor(urlPathEqualTo("/status")).withQueryParam("eventId", equalTo("a b&c")));
		var requests = findAll(getRequestedFor(urlPathEqualTo("/status")));
		assertEquals(1, requests.size());
		assertEquals(1, requests.getFirst().getQueryParams().size());
	}

	@Test
	void test_fetch_event_status_rejects_blank_event_id(WireMockRuntimeInfo wm) throws Exception {
		var client = TestUtil.createClient(wm);

		assertThrows(IllegalArgumentException.class, () -> client.fetchEventStatus(""));
		assertThrows(IllegalArgumentException.class, () -> client.fetchEventStatus("  "));

		verify(0, anyRequestedFor(anyUrl()));
	}

	@Test
	void test_fetch_event_status_error(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/status")).willReturn(aResponse().withStatus(400).withHeaders(headers)
				.withBody("{\"errorNumber\": 9708, \"errorMessage\": \"EventId could not be found.\"}")));

		var ex = assertThrows(AppAndBookClientException.class, () -> TestUtil.createClient(wm).fetchEventStatus("e1"));

		assertEquals(9708, ex.getCode());
		assertEquals(400, ex.getStatusCode());
	}

	@Test
	void test_fetch_event_status_resolves_url(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/service/config")).willReturn(aResponse().withStatus(200).withHeaders(headers)
				.withBody("{\"urls\": {\"eventStatus\": \"%s/custom/status\"}}".formatted(wm.getHttpBaseUrl()))));
		stubFor(get(urlPathEqualTo("/custom/status")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("{}")));

		TestUtil.createClient(wm).fetchEventStatus("e1");

		verify(1, getRequestedFor(urlEqualTo("/custom/status?eventId=e1")));
		verify(0, getRequestedFor(urlPathEqualTo("/status")));
	}
}
