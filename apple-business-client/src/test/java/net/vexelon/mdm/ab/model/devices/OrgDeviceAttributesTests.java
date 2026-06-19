package net.vexelon.mdm.ab.model.devices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrgDeviceAttributesTests {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	// language=json
	private static final String FULL_JSON = """
			{
			  "productFamily": "iPhone",
			  "orderDateTime": "2018-12-13T21:47:11Z",
			  "meid": [],
			  "eid": "",
			  "serialNumber": "H7KN2QR4XVTM",
			  "orderNumber": "b3f1a902-7d4e-4c8b-9f0a-1e2d3c4b5a6f",
			  "color": "SPACE GRAY",
			  "purchaseSourceType": "MANUALLY_ADDED",
			  "deviceCapacity": "256GB",
			  "updatedDateTime": "2025-11-03T14:37:52.419Z",
			  "wifiMacAddress": "3A7F2C9D1B4E",
			  "releasedFromOrgDateTime": null,
			  "ethernetMacAddress": [],
			  "bluetoothMacAddress": "8E0D5F3A6C2B",
			  "imei": ["742903615284017"],
			  "deviceModel": "iPhone 8",
			  "partNumber": "MQ7C2ZD/A",
			  "addedToOrgDateTime": "2018-12-13T13:47:11.886Z",
			  "productType": "iPhone10,4",
			  "purchaseSourceId": "",
			  "status": "UNASSIGNED"
			}
			""";

	private static String json(String field, String value) {
		return "{\"" + field + "\": " + value + "}";
	}

	@Test
	void deserializes_full_attributes_json() throws Exception {
		var attrs = MAPPER.readValue(FULL_JSON, OrgDeviceAttributes.class);

		assertEquals("H7KN2QR4XVTM", attrs.serialNumber());
		assertEquals("b3f1a902-7d4e-4c8b-9f0a-1e2d3c4b5a6f", attrs.orderNumber());
		assertEquals("2025-11-03T14:37:52.419Z", attrs.updatedDateTime());
		assertEquals("3A7F2C9D1B4E", attrs.wifiMacAddress());
		assertEquals("8E0D5F3A6C2B", attrs.bluetoothMacAddress());
		assertEquals(List.of("742903615284017"), attrs.imei());
		assertEquals("iPhone", attrs.productFamily());
		assertEquals("iPhone 8", attrs.deviceModel());
		assertEquals("iPhone10,4", attrs.productType());
		assertEquals("256GB", attrs.deviceCapacity());
		assertEquals("SPACE GRAY", attrs.color());
		assertEquals("MQ7C2ZD/A", attrs.partNumber());
		assertEquals("2018-12-13T21:47:11Z", attrs.orderDateTime());
		assertEquals("2018-12-13T13:47:11.886Z", attrs.addedToOrgDateTime());
		assertEquals(List.of(), attrs.meid());
		assertEquals(List.of(), attrs.ethernetMacAddress());
		assertEquals("", attrs.eid());
		assertEquals("", attrs.purchaseSourceId());
		assertEquals(OrgDeviceAttributes.Status.UNASSIGNED, attrs.status());
		assertEquals(OrgDeviceAttributes.PurchaseSourceType.MANUALLY_ADDED, attrs.purchaseSourceType());
	}

	@Test
	void null_released_date_defaults_to_empty_string() throws Exception {
		var attrs = MAPPER.readValue(FULL_JSON, OrgDeviceAttributes.class);

		assertEquals("", attrs.releasedFromOrgDateTime());
	}

	// --- Status enum ---

	@Test
	void status_assigned_deserializes() throws Exception {
		var attrs = MAPPER.readValue(json("status", "\"ASSIGNED\""), OrgDeviceAttributes.class);
		assertEquals(OrgDeviceAttributes.Status.ASSIGNED, attrs.status());
	}

	@Test
	void status_unassigned_deserializes() throws Exception {
		var attrs = MAPPER.readValue(json("status", "\"UNASSIGNED\""), OrgDeviceAttributes.class);
		assertEquals(OrgDeviceAttributes.Status.UNASSIGNED, attrs.status());
	}

	@Test
	void status_unknown_for_unrecognized_value() throws Exception {
		var attrs = MAPPER.readValue(json("status", "\"FUTURE_VALUE\""), OrgDeviceAttributes.class);
		assertEquals(OrgDeviceAttributes.Status.UNKNOWN, attrs.status());
	}

	// --- PurchaseSourceType enum ---

	@Test
	void purchase_source_type_apple_deserializes() throws Exception {
		var attrs = MAPPER.readValue(json("purchaseSourceType", "\"APPLE\""), OrgDeviceAttributes.class);
		assertEquals(OrgDeviceAttributes.PurchaseSourceType.APPLE, attrs.purchaseSourceType());
	}

	@Test
	void purchase_source_type_reseller_deserializes() throws Exception {
		var attrs = MAPPER.readValue(json("purchaseSourceType", "\"RESELLER\""), OrgDeviceAttributes.class);
		assertEquals(OrgDeviceAttributes.PurchaseSourceType.RESELLER, attrs.purchaseSourceType());
	}

	@Test
	void purchase_source_type_manually_added_deserializes() throws Exception {
		var attrs = MAPPER.readValue(json("purchaseSourceType", "\"MANUALLY_ADDED\""), OrgDeviceAttributes.class);
		assertEquals(OrgDeviceAttributes.PurchaseSourceType.MANUALLY_ADDED, attrs.purchaseSourceType());
	}

	@Test
	void purchase_source_type_unknown_for_unrecognized_value() throws Exception {
		var attrs = MAPPER.readValue(json("purchaseSourceType", "\"FUTURE_VALUE\""), OrgDeviceAttributes.class);
		assertEquals(OrgDeviceAttributes.PurchaseSourceType.UNKNOWN, attrs.purchaseSourceType());
	}
}
