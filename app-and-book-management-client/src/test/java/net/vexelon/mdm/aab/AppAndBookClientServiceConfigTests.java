package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.aab.model.NotificationType;
import net.vexelon.mdm.aab.model.ResponseErrorCode;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookClientServiceConfigTests {

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = TestUtil.createDefaultHeaders();
	}

	private void stubOk(String body) {
		stubFor(get(urlEqualTo("/service/config")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	private static String errorBody(int number, String message) {
		return "{\"errorNumber\": %d, \"errorMessage\": \"%s\"}".formatted(number, message);
	}

	@Test
	void test_fetch_service_configuration_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim (the example request is a plain GET)
		stubOk("""
				{
				    "errorCodes": [
				        {
				            "errorMessage": "Login required.",
				            "errorNumber": 9601
				        },
				        {
				            "errorMessage": "Invalid argument.",
				            "errorNumber": 9602
				        },
				        {
				            "errorMessage": "Internal error.",
				            "errorNumber": 9603
				        },
				        {
				            "errorMessage": "Unable to find the registered user.",
				            "errorNumber": 9609
				        },
				        {
				            "errorMessage": "The token has expired. You need to generate a new token online using your organization's account at either school.apple.com or business.apple.com.",
				            "errorNumber": 9621
				        },
				        {
				            "errorMessage": "The server has revoked the sToken.",
				            "errorNumber": 9625
				        },
				        {
				            "errorMessage": "Service removed.",
				            "errorNumber": 9634
				        },
				        {
				            "errorMessage": "There are too many recent requests. Try again momentarily.",
				            "errorNumber": 9646
				        },
				        {
				            "errorMessage": "The provided page index must be greater than 0, and less than the total number of pages.",
				            "errorNumber": 9650
				        },
				        {
				            "errorMessage": "Max assets limit on request has been passed, please stay within given limit.",
				            "errorNumber": 9700
				        },
				        {
				            "errorMessage": "Max clientUserIds limit on request has been passed, please stay within given limit.",
				            "errorNumber": 9701
				        },
				        {
				            "errorMessage": "Max serialNumbers limit on request has been passed, please stay within given limit.",
				            "errorNumber": 9702
				        },
				        {
				            "errorMessage": "Duplicate assets found in request, please send unique assets.",
				            "errorNumber": 9703
				        },
				        {
				            "errorMessage": "Duplicate clientUserIds found in request, please send unique clients.",
				            "errorNumber": 9704
				        },
				        {
				            "errorMessage": "Duplicate serialNumbers found in request, please send unique serials.",
				            "errorNumber": 9705
				        },
				        {
				            "errorMessage": "Asset in request is not revocable, please send revocable assets.",
				            "errorNumber": 9706
				        },
				        {
				            "errorMessage": "Asset in request is not device assignable, please send device assignable assets.",
				            "errorNumber": 9707
				        },
				        {
				            "errorMessage": "EventId could not be found.",
				            "errorNumber": 9708
				        },
				        {
				            "errorMessage": "There are not enough licenses for this app or book available to assign to the selected group.",
				            "errorNumber": 9709
				        },
				        {
				            "errorMessage": "Both the notification URL and the authentication token are necessary.",
				            "errorNumber": 9710
				        },
				        {
				            "errorMessage": "Either the notification URL or the authentication token exceeds its maximum length. Change the request to stay within the specified limit.",
				            "errorNumber": 9711
				        },
				        {
				            "errorMessage": "The provided notification URL must be a valid URL and use HTTPS as the protocol.",
				            "errorNumber": 9712
				        },
				        {
				            "errorMessage": "The MDM metadata exceeds the maximum length. Change the request to stay within the specified limit.",
				            "errorNumber": 9713
				        },
				        {
				            "errorMessage": "The number of users exceeds the maximum. Change the request to stay within the specified limit.",
				            "errorNumber": 9714
				        },
				        {
				            "errorMessage": "This request contains duplicate users. Change the request to send unique users.",
				            "errorNumber": 9715
				        },
				        {
				            "errorMessage": "A registered user already exists with the specified clientUserId.",
				            "errorNumber": 9716
				        },
				        {
				            "errorMessage": "This request contains an invalid email address. Change the request to send valid email addresses.",
				            "errorNumber": 9717
				        },
				        {
				            "errorMessage": "This request doesn't contain an asset, which is a required argument. Change the request to provide an asset.",
				            "errorNumber": 9718
				        },
				        {
				            "errorMessage": "Either clientUserIds or serialNumbers are required arguments. Change the request to provide assignable users and devices.",
				            "errorNumber": 9719
				        },
				        {
				            "errorMessage": "Users are a required argument. Change the request to provide a user.",
				            "errorNumber": 9720
				        },
				        {
				            "errorMessage": "This request contains an invalid version ID. Change the request to send the version ID from the read APIs.",
				            "errorNumber": 9721
				        },
				        {
				            "errorMessage": "This request contains an invalid authorization header. Change the request to send an authorization header that matches the format 'Bearer {sToken}'.",
				            "errorNumber": 9722
				        },
				        {
				            "errorMessage": "You must provide an MDM name, ID, and metadata when setting the MDM information.",
				            "errorNumber": 9723
				        },
				        {
				            "errorMessage": "The MDM ID exceeds the maximum length. Change the request to stay within the specified limit.",
				            "errorNumber": 9724
				        },
				        {
				            "errorMessage": "The MDM name exceeds the maximum length. Change the request to stay within the specified limit.",
				            "errorNumber": 9725
				        },
				        {
				            "errorMessage": "This request contains an unsupported HTTP method for the requested endpoint.",
				            "errorNumber": 9726
				        },
				        {
				            "errorMessage": "This request contains an unsupported Content-Type for the requested endpoint.",
				            "errorNumber": 9727
				        },
				        {
				            "errorMessage": "The provided notification URL is unreachable.",
				            "errorNumber": 9728
				        }
				    ],
				    "limits": {
				        "maxAssets": 25,
				        "maxUsers": 100,
				        "maxNotificationLength": 512,
				        "maxRevokeClientUserIds": 100,
				        "maxClientUserIds": 1000,
				        "maxSerialNumbers": 1000,
				        "maxRevokeSerialNumbers": 100,
				        "maxRequestPerSecond": 15,
				        "maxSubscriptions": 25,
				        "maxSubscriptionClientUserIds": 1000,
				        "maxMdmNameLength": 100,
				        "maxMdmMetadataLength": 255,
				        "maxMdmIdLength": 100
				    },
				    "notificationTypes": [
				        "ASSET_MANAGEMENT",
				        "USER_MANAGEMENT",
				        "USER_ASSOCIATED",
				        "ASSET_COUNT"
				    ],
				    "urls": {
				        "invitationEmail": "https://buy.itunes.apple.com/WebObjects/MZFinance.woa/wa/associateVPPUserWithITSAccount?inviteCode=%25inviteCode%25&mt=8",
				        "clientConfig": "https://vpp.itunes.apple.com/mdm/v2/client/config",
				        "createUsers": "https://vpp.itunes.apple.com/mdm/v2/users/create",
				        "getAssignments": "https://vpp.itunes.apple.com/mdm/v2/assignments",
				        "revokeAssets": "https://vpp.itunes.apple.com/mdm/v2/assets/revoke",
				        "contentMetadataLookup": "https://uclient-api.itunes.apple.com/WebObjects/MZStorePlatform.woa/wa/lookup",
				        "getUsers": "https://vpp.itunes.apple.com/mdm/v2/users",
				        "eventStatus": "https://vpp.itunes.apple.com/mdm/v2/status",
				        "associateAssets": "https://vpp.itunes.apple.com/mdm/v2/assets/associate",
				        "disassociateAssets": "https://vpp.itunes.apple.com/mdm/v2/assets/disassociate",
				        "updateUsers": "https://vpp.itunes.apple.com/mdm/v2/users/update",
				        "getAssets": "https://vpp.itunes.apple.com/mdm/v2/assets",
				        "retireUsers": "https://vpp.itunes.apple.com/mdm/v2/users/retire"
				    }
				}
				""".stripIndent());

		var resp = TestUtil.createClient(wm).fetchServiceConfiguration();

		verify(getRequestedFor(urlEqualTo("/service/config")).withoutHeader(HttpConsts.HEADER_AUTHORIZATION));
		assertEquals(38, resp.errorCodes().size());
		assertEquals(new ResponseErrorCode("Login required.", 9601), resp.errorCodes().getFirst());
		assertEquals(new ResponseErrorCode("The provided notification URL is unreachable.", 9728),
				resp.errorCodes().getLast());
		assertEquals(13, resp.limits().size());
		assertEquals(25, resp.limits().get("maxAssets"));
		assertEquals(512, resp.limits().get("maxNotificationLength"));
		assertEquals(15, resp.limits().get("maxRequestPerSecond"));
		assertEquals(255, resp.limits().get("maxMdmMetadataLength"));
		assertEquals(List.of(NotificationType.ASSET_MANAGEMENT, NotificationType.USER_MANAGEMENT,
				NotificationType.USER_ASSOCIATED, NotificationType.ASSET_COUNT), resp.notificationTypes());
		assertEquals(13, resp.urls().size());
		assertEquals("https://vpp.itunes.apple.com/mdm/v2/assets", resp.urls().get("getAssets"));
		assertEquals("https://vpp.itunes.apple.com/mdm/v2/client/config", resp.urls().get("clientConfig"));
		assertEquals("https://uclient-api.itunes.apple.com/WebObjects/MZStorePlatform.woa/wa/lookup",
				resp.urls().get("contentMetadataLookup"));
		assertTrue(resp.urls().get("invitationEmail").endsWith("inviteCode=%25inviteCode%25&mt=8"));
	}

	@Test
	void test_fetch_service_configuration_unknown_values(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{
				    "errorCodes": [{"errorMessage": null, "errorNumber": 1}],
				    "limits": {"maxAssets": 25, "maxSomethingNew": 7},
				    "notificationTypes": ["ASSET_COUNT", "SOMETHING_NEW"],
				    "urls": {"getAssets": "https://example.com/assets", "somethingNew": "https://example.com/new"}
				}
				""");

		var resp = TestUtil.createClient(wm).fetchServiceConfiguration();

		assertEquals(List.of(NotificationType.ASSET_COUNT, NotificationType.UNKNOWN), resp.notificationTypes());
		assertEquals(7, resp.limits().get("maxSomethingNew"));
		assertEquals("https://example.com/new", resp.urls().get("somethingNew"));
		assertEquals(new ResponseErrorCode("", 1), resp.errorCodes().getFirst());
	}

	@Test
	void test_fetch_service_configuration_empty_payload(WireMockRuntimeInfo wm) throws Exception {
		stubOk("{}");

		var resp = TestUtil.createClient(wm).fetchServiceConfiguration();

		assertTrue(resp.errorCodes().isEmpty());
		assertTrue(resp.limits().isEmpty());
		assertTrue(resp.notificationTypes().isEmpty());
		assertTrue(resp.urls().isEmpty());
	}

	@Test
	void test_fetch_service_configuration_error(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/service/config")).willReturn(
				aResponse().withStatus(500).withHeaders(headers).withBody(errorBody(9603, "Internal error."))));

		var ex = assertThrows(AppAndBookClientException.class,
				() -> TestUtil.createClient(wm).fetchServiceConfiguration());

		assertEquals(9603, ex.getCode());
		assertEquals(500, ex.getStatusCode());
	}

	@Test
	void test_fetch_service_configuration_retry_after(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlEqualTo("/service/config")).willReturn(
				aResponse().withStatus(503).withHeaders(headers.plus(HttpHeader.httpHeader("Retry-After", "120")))
						.withBody(errorBody(9729, "The service is temporarily unavailable. Try again later."))));

		var ex = assertThrows(AppAndBookClientRetryAfterException.class,
				() -> TestUtil.createClient(wm).fetchServiceConfiguration());

		assertEquals(503, ex.getCode());
	}
}
