package net.vexelon.mdm.da;

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
		stubFor(get(urlEqualTo("/session")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody(TestUtil.authSessionToken())));
	}

	@Test
	void test_fetch_devices_sync_devices(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/server/devices")).withRequestBody(equalToJson("""
				{"limit": 100}
				""".stripIndent())).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
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
				            "profile_status": "",
				            "device_assigned_by": "max.mustermann@petarov.appleid.com"
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
		assertEquals("max.mustermann@petarov.appleid.com", devices.getLast().deviceAssignedBy());
		assertEquals("", devices.getLast().profilePushTime());
		assertEquals("", devices.getLast().profileStatus());
		assertEquals(OffsetDateTime.MIN, devices.getLast().profilePushDateTime());
		assertEquals("", devices.getLast().profileAssignTime());
		assertEquals(OffsetDateTime.MIN, devices.getLast().opDateTime());
		assertEquals("", devices.getLast().opDate());
		assertEquals(OffsetDateTime.MIN, devices.getLast().profileAssignDateTime());
		assertEquals("", devices.getLast().deviceAssignedDate());
		assertEquals(OffsetDateTime.MIN, devices.getLast().deviceAssignedDateTime());

		// --- sync test #1

		stubFor(post(urlEqualTo("/devices/sync")).withRequestBody(equalToJson("""
				{"cursor":"MDovOjE7NDU6Njc0MjIyODU1MTc0NTgyNzQyMjI3MTp0cnVlOjE0NDU7Mjc1LiIjUDU", "limit": 500}
				""".stripIndent())).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
				{"devices":[],"fetched_until":"2025-05-03T08:28:03Z","more_to_follow":false,"cursor":"MTAwOjA5MTc0NjI2MDg4MzIyNToxNyQ3NjYwODgzMjI1OnRydWU6MTc0NjI2MDg4MzIyNQ"}
				""".stripIndent())));

		var syncResponse = TestUtil.createClient(wm)
				.syncDevices("MDovOjE7NDU6Njc0MjIyODU1MTc0NTgyNzQyMjI3MTp0cnVlOjE0NDU7Mjc1LiIjUDU", 500);

		assertEquals("MTAwOjA5MTc0NjI2MDg4MzIyNToxNyQ3NjYwODgzMjI1OnRydWU6MTc0NjI2MDg4MzIyNQ", syncResponse.cursor());
		assertFalse(syncResponse.moreToFollow());
		assertEquals(OffsetDateTime.parse("2025-05-03T08:28:03Z"), syncResponse.fetchedUntil());
		assertEquals(0, syncResponse.devices().size());

		// --- sync test #2

		stubFor(post(urlEqualTo("/devices/sync")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "devices": [
						        {
						            "serial_number": "C112342756",
						            "description": "IPHONE 14 MIDNIGHT 128GB-ZDD",
						            "model": "iPhone 14",
						            "os": "iOS",
						            "device_family": "iPhone",
						            "color": "MIDNIGHT",
						            "profile_uuid": "95C2189CA0EFB3272AC8B3C66201F33",
						            "profile_assign_time": "2025-04-29T18:06:53Z",
						            "profile_status": "removed",
						            "device_assigned_by": "max.mustermann@petarov.appleid.com",
						            "device_assigned_date": "2025-05-03T20:59:31Z",
						            "op_type": "modified",
						            "op_date": "2025-05-03T20:59:31Z"
						        },
						        {
						            "serial_number": "C112342756",
						            "description": "IPHONE 14 MIDNIGHT 128GB-ZDD",
						            "model": "iPhone 14",
						            "os": "iOS",
						            "device_family": "iPhone",
						            "color": "MIDNIGHT",
						            "profile_uuid": "95C2189CA0EFB3272AC8B3C66201F33",
						            "profile_assign_time": "2025-04-29T18:06:53Z",
						            "profile_status": "removed",
						            "device_assigned_by": "max.mustermann@petarov.appleid.com",
						            "device_assigned_date": "2025-05-03T21:01:55Z",
						            "op_type": "modified",
						            "op_date": "2025-05-03T21:01:55Z"
						        }
						    ],
						    "fetched_until": "2025-05-03T21:01:55Z",
						    "more_to_follow": false,
						    "cursor": "MTAwOjA6MTc0NjMwNjExNTA1OToxNzQ2MzA2NDgyNTM2OnRydWU6MTc0NjMwNjExNTA1OQ"
						}
						""".stripIndent())));

		var syncResponse2 = TestUtil.createClient(wm)
				.syncDevices("MTAwOjA6MTc0NjMwNjExNTA1OToxNzQ2MzA2NDgyNTM2OnRydWU6MTc0NjMwNjExNTA1OQ");

		assertEquals("MTAwOjA6MTc0NjMwNjExNTA1OToxNzQ2MzA2NDgyNTM2OnRydWU6MTc0NjMwNjExNTA1OQ", syncResponse2.cursor());
		assertFalse(syncResponse2.moreToFollow());
		assertEquals(OffsetDateTime.parse("2025-05-03T21:01:55Z"), syncResponse2.fetchedUntil());
		assertEquals(2, syncResponse2.devices().size());
		assertEquals("C112342756", syncResponse2.devices().getFirst().serialNumber());
		assertEquals("modified", syncResponse2.devices().getFirst().opType());
		assertEquals("2025-05-03T20:59:31Z", syncResponse2.devices().getFirst().opDate());
		assertEquals(OffsetDateTime.parse("2025-05-03T20:59:31Z"), syncResponse2.devices().getFirst().opDateTime());

		assertEquals("C112342756", syncResponse2.devices().getLast().serialNumber());
		assertEquals("modified", syncResponse2.devices().getLast().opType());
		assertEquals("2025-05-03T21:01:55Z", syncResponse2.devices().getLast().opDate());
		assertEquals(OffsetDateTime.parse("2025-05-03T21:01:55Z"), syncResponse2.devices().getLast().opDateTime());
	}

	@Test
	void test_fetch_device_details(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/devices")).withRequestBody(equalToJson("""
						{"devices":["B9FPP3Q6GMK7", "BNPT0GHHM7252"]}
						""".stripIndent(), true, false))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
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

	@Test
	void test_enable_activation_lock(WireMockRuntimeInfo wm) throws Exception {
		stubFor(post(urlEqualTo("/device/activationlock")).withRequestBody(equalToJson("""
						{"device": "B9FPP3Q6GMK7"}
						""".stripIndent(), true, false))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"serial_number":"B9FPP3Q6GMK7","response_status":"SUCCESS"}
						""".stripIndent())));

		var response1 = TestUtil.createClient(wm).enableActivationLock("B9FPP3Q6GMK7");

		assertEquals("B9FPP3Q6GMK7", response1.serialNumber());
		assertEquals("SUCCESS", response1.responseStatus());


		stubFor(post(urlEqualTo("/device/activationlock")).withRequestBody(equalToJson("""
						{"device": "BNPT0GHHM7252", "escrow_key": "eqMk9Jc4cIq0WeOmnBHYyET4aeBZ+JNfOmIPgMiEgMg=", "lost_message": "call me at 0555 555"}
						""".stripIndent(), true, false))
				.willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"serial_number":"BNPT0GHHM7252","response_status":"NOT_ACCESSIBLE"}
						""".stripIndent())));

		var response2 = TestUtil.createClient(wm)
				.enableActivationLock("BNPT0GHHM7252", "eqMk9Jc4cIq0WeOmnBHYyET4aeBZ+JNfOmIPgMiEgMg=",
						"call me at 0555 555");

		assertEquals("BNPT0GHHM7252", response2.serialNumber());
		assertEquals("NOT_ACCESSIBLE", response2.responseStatus());
	}
}