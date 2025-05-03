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
import java.time.OffsetDateTime;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceAssignmentDeviceManagementTests {

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
	void test_fetch_devices_sync_devices(WireMockRuntimeInfo wm) throws Exception {
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
						    "cursor": "MDovOjE7NDU6Njc0MjIyODU1MTc0NTgyNzQyMjI3MTp0cnVlOjE0NDU7Mjc1LiIjUDU"
						}
						""".stripIndent())));

		var fetchResponse = TestUtil.createClient(wm).fetchDevices();

		assertEquals("2025-04-28T08:03:42Z", fetchResponse.fetchedUntil().toString());
		assertFalse(fetchResponse.moreToFollow());
		assertEquals("MDovOjE7NDU6Njc0MjIyODU1MTc0NTgyNzQyMjI3MTp0cnVlOjE0NDU7Mjc1LiIjUDU", fetchResponse.cursor());

		var devices = fetchResponse.devices();
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


		stubFor(post(urlEqualTo("/devices/sync")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"devices":[],"fetched_until":"2025-05-03T08:28:03Z","more_to_follow":false,"cursor":"MTAwOjA5MTc0NjI2MDg4MzIyNToxNyQ3NjYwODgzMjI1OnRydWU6MTc0NjI2MDg4MzIyNQ"}
						""".stripIndent())));

		var syncResponse = TestUtil.createClient(wm)
				.syncDevices("MDovOjE7NDU6Njc0MjIyODU1MTc0NTgyNzQyMjI3MTp0cnVlOjE0NDU7Mjc1LiIjUDU");

		assertEquals("MTAwOjA5MTc0NjI2MDg4MzIyNToxNyQ3NjYwODgzMjI1OnRydWU6MTc0NjI2MDg4MzIyNQ", syncResponse.cursor());
		assertFalse(syncResponse.moreToFollow());
		assertEquals(OffsetDateTime.parse("2025-05-03T08:28:03Z"), syncResponse.fetchedUntil());
		assertEquals(0, syncResponse.devices().size());
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

		var response = TestUtil.createClient(wm).fetchDeviceDetails(Set.of("B9FPP3Q6GMK7", "BNPT0GHHM7252"));

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
}
