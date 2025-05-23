package net.vexelon.mdm.aab.legacy;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import net.vexelon.mdm.shared.http.HttpConsts;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class TestUtil {

	static String getSToken() {
		return "eyJ0b2tlbiI6IlZHaHBjeUJwY3lCaElITmhiWEJzWlNCMFpYaDBJSGRvYVdOb0lIZGhjeUIxYzJWa0lIUnZJR055WldGMFpTQjBhR1VnYzJsdGRXeGhkRzl5SUhSdmEyVnVDZz09IiwiZXhwRGF0ZSI6IjIwMjAtMTItMzFUMTM6NTc6MTkrMDI6MDAiLCJvcmdOYW1lIjoiYmxhYmxhIEdtYkgifQ==";
	}

	static LegacyAppAndBookClient createClient(WireMockRuntimeInfo wm) {
		var sTokenInput = TestUtil.class.getResourceAsStream("/apple-mdm-client-tests-stoken-1.stoken");

		var builder = LegacyAppAndBookClient.newBuilder();
		builder.setAppleHost(wm.getHttpBaseUrl());
		builder.setUserAgent("apple-mdm-legacy-aab-v1");
		builder.setServerToken(LegacyAppAndBookToken.create(sTokenInput));

		return builder.build();
	}

	static HttpHeaders createDefaultHeaders() {
		return com.github.tomakehurst.wiremock.http.HttpHeaders.noHeaders()
				.plus(HttpHeader.httpHeader(HttpConsts.HEADER_CONTENT_TYPE,
						HttpConsts.HEADER_VALUE_APPLICATION_JSON_UTF8));
	}

	static void createServiceConfig(String baseUrl, HttpHeaders headers) {
		stubFor(get(urlEqualTo("/VPPServiceConfigSrv")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "getLicensesSrvUrl": "%s/mdm/getVPPLicensesSrv",
						    "contentMetadataLookupUrl": "https://uclient-api.itunes.apple.com/WebObjects/MZStorePlatform.woa/wa/lookup",
						    "clientConfigSrvUrl": "%s/mdm/VPPClientConfigSrv",
						    "registerUserSrvUrl": "%s/mdm/registerVPPUserSrv",
						    "getAssignmentsSrvUrl": "%s/mdm/getAssignments",
						    "disassociateLicenseSrvUrl": "%s/mdm/disassociateVPPLicenseSrv",
						    "vppWebsiteUrl": "%s/",
						    "retireUserSrvUrl": "%s/mdm/retireVPPUserSrv",
						    "invitationEmailUrl": "https://buy.itunes.apple.com/WebObjects/MZFinance.woa/wa/associateVPPUserWithITSAccount?cc=us&inviteCode=%25inviteCode%25&mt=8",
						    "editUserSrvUrl": "%s/mdm/editVPPUserSrv",
						    "status": 0,
						    "maxBatchDisassociateLicenseCount": 10,
						    "associateLicenseSrvUrl": "%s/mdm/associateVPPLicenseSrv",
						    "maxBatchAssociateLicenseCount": 10,
						    "getUserSrvUrl": "%s/mdm/getVPPUserSrv",
						    "getVPPAssetsSrvUrl": "%s/mdm/getVPPAssetsSrv",
						    "manageVPPLicensesByAdamIdSrvUrl": "%s/mdm/manageVPPLicensesByAdamIdSrv",
						    "errorCodes": [
						        {
						            "errorNumber": 9600,
						            "errorMessage": "Missing required argument"
						        },
						        {
						            "errorNumber": 9601,
						            "errorMessage": "Login required."
						        },
						        {
						            "errorNumber": 9602,
						            "errorMessage": "Invalid argument."
						        },
						        {
						            "errorNumber": 9603,
						            "errorMessage": "Internal error."
						        },
						        {
						            "errorNumber": 9604,
						            "errorMessage": "Result not found"
						        },
						        {
						            "errorNumber": 9605,
						            "errorMessage": "Account storefront incorrect"
						        },
						        {
						            "errorNumber": 9606,
						            "errorMessage": "Error constructing token"
						        },
						        {
						            "errorNumber": 9607,
						            "errorMessage": "License is irrevocable"
						        },
						        {
						            "errorNumber": 9608,
						            "errorMessage": "Empty response from SharedData service"
						        },
						        {
						            "errorNumber": 9609,
						            "errorMessage": "Unable to find the registered user."
						        },
						        {
						            "errorNumber": 9610,
						            "errorMessage": "License not found"
						        },
						        {
						            "errorNumber": 9611,
						            "errorMessage": "Admin user not found"
						        },
						        {
						            "errorNumber": 9612,
						            "errorMessage": "Failed to create claim job"
						        },
						        {
						            "errorNumber": 9613,
						            "errorMessage": "Failed to create unclaim job"
						        },
						        {
						            "errorNumber": 9614,
						            "errorMessage": "Invalid date format"
						        },
						        {
						            "errorNumber": 9615,
						            "errorMessage": "OrgCountry not found"
						        },
						        {
						            "errorNumber": 9616,
						            "errorMessage": "License already assigned"
						        },
						        {
						            "errorNumber": 9617,
						            "errorMessage": "The URL has been moved. Please call VPPServiceConfigSrv to find the new URL."
						        },
						        {
						            "errorNumber": 9618,
						            "errorMessage": "The user has already been retired."
						        },
						        {
						            "errorNumber": 9619,
						            "errorMessage": "License not associated"
						        },
						        {
						            "errorNumber": 9620,
						            "errorMessage": "The user has already been deleted."
						        },
						        {
						            "errorNumber": 9621,
						            "errorMessage": "The token has expired. You need to generate a new token online using your organization's account at either school.apple.com or business.apple.com."
						        },
						        {
						            "errorNumber": 9622,
						            "errorMessage": "Invalid authentication token"
						        },
						        {
						            "errorNumber": 9623,
						            "errorMessage": "Invalid APN token"
						        },
						        {
						            "errorNumber": 9624,
						            "errorMessage": "License was refunded and is no longer valid."
						        },
						        {
						            "errorNumber": 9625,
						            "errorMessage": "The server has revoked the sToken."
						        },
						        {
						            "errorNumber": 9626,
						            "errorMessage": "License already assigned to other user"
						        },
						        {
						            "errorNumber": 9627,
						            "errorMessage": "License disassociation fail due to frequent reassociation"
						        },
						        {
						            "errorNumber": 9628,
						            "errorMessage": "License not eligible for device assignment."
						        },
						        {
						            "errorNumber": 9629,
						            "errorMessage": "The sToken is inapplicable to batchToken"
						        },
						        {
						            "errorNumber": 9630,
						            "errorMessage": "Too many recent identical calls were made to assign a license that failed due to license being already assigned to the user or device"
						        },
						        {
						            "errorNumber": 9631,
						            "errorMessage": "Too many recent identical calls were made to assign a license that failed due to no license being being available."
						        },
						        {
						            "errorNumber": 9632,
						            "errorMessage": "Too many recent calls to manage licenses with identical requests"
						        },
						        {
						            "errorNumber": 9633,
						            "errorMessage": "No batch data recovered for token."
						        },
						        {
						            "errorNumber": 9634,
						            "errorMessage": "This service is no longer available."
						        },
						        {
						            "errorNumber": 9635,
						            "errorMessage": "Apple Account can't be associated with registered user."
						        },
						        {
						            "errorNumber": 9636,
						            "errorMessage": "No registered user found."
						        },
						        {
						            "errorNumber": 9637,
						            "errorMessage": "Facilitator operation not allowed."
						        },
						        {
						            "errorNumber": 9641,
						            "errorMessage": "Apple Account already associated to registered user."
						        },
						        {
						            "errorNumber": 9642,
						            "errorMessage": "Apple Account passed cannot be used at this time because it's a VPP manager and the iTunes Store account not yet created and such creation requires user to agree to Terms."
						        },
						        {
						            "errorNumber": 9643,
						            "errorMessage": "The license is currently locked for a pending transfer. Please try again later."
						        },
						        {
						            "errorNumber": 9644,
						            "errorMessage": "Volume Purchase Program is currently in maintenance mode. Please try again later."
						        },
						        {
						            "errorNumber": 9646,
						            "errorMessage": "There are too many recent requests. Try again momentarily."
						        },
						        {
						            "errorNumber": 9649,
						            "errorMessage": "A page index is required when passing a request ID."
						        },
						        {
						            "errorNumber": 9650,
						            "errorMessage": "The provided page index must be greater than 0, and less than the total number of pages."
						        },
						        {
						            "errorNumber": 9729,
						            "errorMessage": "The server has since moved, please refresh DNS entry."
						        }
						    ],
						    "getUsersSrvUrl": "%s/mdm/getVPPUsersSrv"
						}
						""".stripIndent().replace("%s", baseUrl))));
	}
}
