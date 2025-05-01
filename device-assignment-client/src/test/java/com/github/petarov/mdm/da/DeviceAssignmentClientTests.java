package com.github.petarov.mdm.da;

import com.github.tomakehurst.wiremock.http.HttpHeader;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceAssignmentClientTests {

	private HttpHeaders headers;

	private DeviceAssignmentClient newClient(WireMockRuntimeInfo wm) {
		var serverTokenInput = this.getClass().getResourceAsStream("/apple-mdm-client-tests-1-server-token.p7m");
		var privateKeyInput = this.getClass().getResourceAsStream("/apple-mdm-client-tests-1.der");

		var builder = DeviceAssignmentClient.newBuilder();
		builder.setAppleHost(wm.getHttpBaseUrl());
		builder.setUserAgent("apple-mdm-device-assignment-v1");
		builder.setServerToken(DeviceAssignmentServerToken.create(serverTokenInput,
				DeviceAssignmentPrivateKey.createFromDER(privateKeyInput)));

		return builder.build();
	}

	@BeforeAll
	void init() {
		Security.addProvider(new BouncyCastleProvider());
		headers = HttpHeaders.noHeaders().plus(HttpHeader.httpHeader("Content-Type", "application/json;charset=UTF8"));
	}

	@BeforeEach
	void test_session() {
		stubFor(get(urlEqualTo("/session")).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
				{"auth_session_token":"1745786035268O1O789F19CF078867E47DC9D9BF4682D021O75CA72ECB87046A1B2239D9CFA4D6771O420397O11Op1OB123AA978976E390FF7693C640C92D3F8F6FE7F6O81E6CAAC7816AD3E12D531496695CF5A"}
				""".stripIndent())));
	}

	@Test
	void test_fetch_device_details(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/devices")).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
				{
				    "devices": {
				        "B9FPP3Q6GMK7": {
				            "serial_number": "B9FPP3Q6GMK7",
				            "description": "IPAD MINI 4 WI-FI 16GB SPACE GRAY-FRD",
				            "model": "iPad mini 4",
				            "os": "iOS",
				            "device_family": "iPad",
				            "color": "SPACE GRAY",
				            "profile_uuid": "95C2189CB0EFB3192BC7B3C555091D22",
				            "profile_assign_time": "2025-04-29T18:06:53Z",
				            "profile_status": "assigned",
				            "device_assigned_by": "max-muster-work@petarov.net",
				            "device_assigned_date": "2022-03-03T08:16:27Z",
				            "response_status": "SUCCESS"
				        },
				        "BNPT0GHHM7252": {
				            "response_status": "NOT_ACCESSIBLE"
				        }
				    }
				}
				""".stripIndent())));

		var response = newClient(wm).fetchDeviceDetails(Set.of("B9FPP3Q6GMK7", "BNPT0GHHM7252"));

		assertEquals(2, response.devices().size());

		var device = response.devices().get("B9FPP3Q6GMK7");
		assertEquals("B9FPP3Q6GMK7", device.serialNumber());
		assertEquals("IPAD MINI 4 WI-FI 16GB SPACE GRAY-FRD", device.description());
		assertEquals("iPad mini 4", device.model());
		assertEquals("iOS", device.os());
		assertEquals("iPad", device.deviceFamily());
		assertEquals("SPACE GRAY", device.color());
		assertEquals("95C2189CB0EFB3192BC7B3C555091D22", device.profileUuid());
		assertEquals("2025-04-29T18:06:53Z", device.profileAssignTime());
		assertEquals("assigned", device.profileStatus());
		assertEquals("max-muster-work@petarov.net", device.deviceAssignedBy());
		assertEquals("2022-03-03T08:16:27Z", device.deviceAssignedDate());
		assertEquals("SUCCESS", device.responseStatus());

		assertEquals("NOT_ACCESSIBLE", response.devices().get("BNPT0GHHM7252").responseStatus());
	}

	@Test
	void test_fetch_devices(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/server/devices")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "devices": [
						        {
						            "serial_number": "F6BRR3Z6GLK0",
						            "description": "IPAD MINI 4 WI-FI 16GB SPACE GRAY-FRD",
						            "model": "iPad mini 4",
						            "os": "iOS",
						            "device_family": "iPad",
						            "color": "SPACE GRAY",
						            "profile_uuid": "722081EC2F9D9F6CAC4106A7CE1AD6A7",
						            "profile_assign_time": "2025-02-18T15:43:38Z",
						            "profile_push_time": "2025-02-18T20:13:06Z",
						            "profile_status": "pushed",
						            "device_assigned_by": "max-muster-work@petarov.net",
						            "device_assigned_date": "2022-03-03T08:16:27Z"
						        },
						        {
						            "serial_number": "M1525642873",
						            "description": "IPHONE 14 MIDNIGHT 128GB-ZDD",
						            "model": "iPhone 14",
						            "os": "iOS",
						            "device_family": "iPhone",
						            "color": "MIDNIGHT",
						            "profile_uuid": "714081EC2F9D9F6CAC4152A79E1006B1",
						            "profile_assign_time": "2025-02-25T21:14:39Z",
						            "profile_push_time": "2025-03-03T17:18:03Z",
						            "profile_status": "pushed",
						            "device_assigned_by": "max.mustermann@midpointsde.appleid.com",
						            "device_assigned_date": "2025-02-14T12:28:26Z"
						        }
						    ],
						    "fetched_until": "2025-04-28T08:03:42Z",
						    "more_to_follow": false,
						    "cursor": "MDovOjE7NDU5Mjc0MjIyODU1MTc0NTgyNzQyMjI2NTp0cnVlOjE0NDU7Mjc1LiIjUDU"
						}
						""".stripIndent())));

		var response = newClient(wm).fetchDevices();

		assertEquals("2025-04-28T08:03:42Z", response.fetchedUntil().toString());
		assertFalse(response.moreToFollow());
		assertEquals("MDovOjE7NDU5Mjc0MjIyODU1MTc0NTgyNzQyMjI2NTp0cnVlOjE0NDU7Mjc1LiIjUDU", response.cursor());

		var devices = response.devices();
		assertEquals(2, devices.size());

		assertEquals("F6BRR3Z6GLK0", devices.getFirst().serialNumber());
		assertEquals("IPAD MINI 4 WI-FI 16GB SPACE GRAY-FRD", devices.getFirst().description());
		assertEquals("iPad mini 4", devices.getFirst().model());
		assertEquals("iOS", devices.getFirst().os());
		assertEquals("iPad", devices.getFirst().deviceFamily());
		assertEquals("SPACE GRAY", devices.getFirst().color());
		assertEquals("722081EC2F9D9F6CAC4106A7CE1AD6A7", devices.getFirst().profileUuid());
		assertEquals("2025-02-18T15:43:38Z", devices.getFirst().profileAssignTime());
		assertEquals("2025-02-18T20:13:06Z", devices.getFirst().profilePushTime());
		assertEquals("pushed", devices.getFirst().profileStatus());
		assertEquals("max-muster-work@petarov.net", devices.getFirst().deviceAssignedBy());
		assertEquals("2022-03-03T08:16:27Z", devices.getFirst().deviceAssignedDate());

		assertEquals("M1525642873", devices.getLast().serialNumber());
		assertEquals("IPHONE 14 MIDNIGHT 128GB-ZDD", devices.getLast().description());
		assertEquals("max.mustermann@midpointsde.appleid.com", devices.getLast().deviceAssignedBy());
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

		var accountDetail = newClient(wm).fetchAccount();

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
}
