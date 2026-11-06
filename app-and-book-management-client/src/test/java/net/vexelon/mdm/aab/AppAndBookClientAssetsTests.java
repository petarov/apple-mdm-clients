package net.vexelon.mdm.aab;

import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.aab.AppAndBookClient.FetchAssetsOptions;
import net.vexelon.mdm.aab.model.Platform;
import net.vexelon.mdm.aab.model.PricingParam;
import net.vexelon.mdm.aab.model.ProductType;
import net.vexelon.mdm.shared.http.HttpConsts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookClientAssetsTests {

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = TestUtil.createDefaultHeaders();
	}

	private static String errorBody(int number, String message) {
		return "{\"errorNumber\": %d, \"errorMessage\": \"%s\"}".formatted(number, message);
	}

	@Test
	void test_fetch_assets(WireMockRuntimeInfo wm) throws Exception {
		// Built from the documented GetAssetsResponse schema
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "assets": [
						        {
						            "adamId": "408709785",
						            "assignedCount": 3,
						            "availableCount": 7,
						            "deviceAssignable": true,
						            "pricingParam": "STDQ",
						            "productType": "App",
						            "retiredCount": 1,
						            "revocable": true,
						            "totalCount": 11,
						            "supportedPlatforms": ["iOS"]
						        },
						        {
						            "adamId": "1111111111",
						            "assignedCount": 0,
						            "availableCount": 5,
						            "deviceAssignable": false,
						            "pricingParam": "PLUS",
						            "productType": "Book",
						            "retiredCount": 0,
						            "revocable": false,
						            "totalCount": 5,
						            "supportedPlatforms": ["iOS", "macOS"]
						        },
						        {
						            "adamId": "2222222222",
						            "assignedCount": 1,
						            "availableCount": 1,
						            "deviceAssignable": true,
						            "pricingParam": "STDQ",
						            "productType": "App",
						            "retiredCount": 0,
						            "revocable": true,
						            "totalCount": 2,
						            "supportedPlatforms": ["tvOS", "visionOS", "watchOS"]
						        }
						    ],
						    "currentPageIndex": 0,
						    "size": 3,
						    "totalPages": 1,
						    "tokenExpirationDate": "2026-11-20T10:00:00Z",
						    "uId": "2049025000431439",
						    "versionId": "70e8c740-514c-11eb-bb63-a90b882fcd52"
						}
						""".stripIndent())));

		var resp = TestUtil.createClient(wm).fetchAssets();

		verify(getRequestedFor(urlEqualTo("/assets")).withHeader(HttpConsts.HEADER_AUTHORIZATION,
						equalTo("Bearer " + TestUtil.getSToken()))
				.withHeader(HttpConsts.HEADER_USER_AGENT, equalTo("apple-mdm-aab-v1")));

		assertEquals(3, resp.assets().size());
		assertTrue(resp.unlimitedAssets().isEmpty());
		var asset = resp.assets().getFirst();
		assertEquals("408709785", asset.adamId());
		assertEquals(3, asset.assignedCount());
		assertEquals(7, asset.availableCount());
		assertTrue(asset.isDeviceAssignable());
		assertEquals(PricingParam.STDQ, asset.pricingParam());
		assertEquals(ProductType.APP, asset.productType());
		assertEquals(1, asset.retiredCount());
		assertTrue(asset.isRevocable());
		assertEquals(11, asset.totalCount());
		assertEquals(List.of(Platform.IOS), asset.supportedPlatforms());
		assertEquals(ProductType.BOOK, resp.assets().get(1).productType());
		assertEquals(PricingParam.PLUS, resp.assets().get(1).pricingParam());
		assertEquals(List.of(Platform.IOS, Platform.MACOS), resp.assets().get(1).supportedPlatforms());
		assertEquals(List.of(Platform.TVOS, Platform.VISIONOS, Platform.WATCHOS),
				resp.assets().get(2).supportedPlatforms());
		assertEquals(0, resp.currentPageIndex());
		assertEquals(0, resp.nextPageIndex());
		assertEquals(3, resp.size());
		assertEquals(1, resp.totalPages());
		assertEquals("2026-11-20T10:00:00Z", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertEquals("70e8c740-514c-11eb-bb63-a90b882fcd52", resp.versionId());
		assertNull(resp.mdmInfo());
	}

	@Test
	void test_fetch_assets_apple_example(WireMockRuntimeInfo wm) throws Exception {
		// Apple's example response, verbatim (example request: ?pageIndex=0&pricingParam=STDQ&productType=App)
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "assets": [
						        {
						            "adamId": "408709785",
						            "assignedCount": 5000,
						            "availableCount": 10000,
						            "deviceAssignable": true,
						            "pricingParam": "STDQ",
						            "productType": "App",
						            "retiredCount": 0,
						            "revocable": true,
						            "supportedPlatforms": ["iOS"],
						            "totalCount": 15000
						        },
						        {
						            "adamId": "377298193",
						            "assignedCount": 5000,
						            "availableCount": 10000,
						            "deviceAssignable": true,
						            "pricingParam": "STDQ",
						            "productType": "App",
						            "retiredCount": 0,
						            "revocable": true,
						            "supportedPlatforms": ["iOS"],
						            "totalCount": 15000
						        },
						        {
						            "adamId": "361309726",
						            "assignedCount": 5000,
						            "availableCount": 10000,
						            "deviceAssignable": true,
						            "pricingParam": "STDQ",
						            "productType": "App",
						            "retiredCount": 0,
						            "revocable": true,
						            "supportedPlatforms": ["iOS"],
						            "totalCount": 15000
						        },
						        {
						            "adamId": "361304891",
						            "assignedCount": 5000,
						            "availableCount": 10000,
						            "deviceAssignable": true,
						            "pricingParam": "STDQ",
						            "productType": "App",
						            "retiredCount": 0,
						            "revocable": true,
						            "supportedPlatforms": ["iOS"],
						            "totalCount": 15000
						        },
						        {
						            "adamId": "361285480",
						            "assignedCount": 5000,
						            "availableCount": 10000,
						            "deviceAssignable": true,
						            "pricingParam": "STDQ",
						            "productType": "App",
						            "retiredCount": 0,
						            "revocable": true,
						            "supportedPlatforms": ["iOS"],
						            "totalCount": 15000
						        }
						    ],
						    "currentPageIndex": 0,
						    "size": 5,
						    "tokenExpirationDate": "2030-11-08T22:33:22+0000",
						    "totalPages": 1,
						    "uId": "2049025000431439",
						    "versionId": "70e8c740-514c-11eb-bb63-a90b882fcd52"
						}
						""".stripIndent())));

		var resp = TestUtil.createClient(wm).fetchAssets();

		assertEquals(5, resp.assets().size());
		var asset = resp.assets().getFirst();
		assertEquals("408709785", asset.adamId());
		assertEquals(5000, asset.assignedCount());
		assertEquals(10000, asset.availableCount());
		assertTrue(asset.isDeviceAssignable());
		assertEquals(PricingParam.STDQ, asset.pricingParam());
		assertEquals(ProductType.APP, asset.productType());
		assertEquals(0, asset.retiredCount());
		assertTrue(asset.isRevocable());
		assertEquals(15000, asset.totalCount());
		assertEquals(List.of(Platform.IOS), asset.supportedPlatforms());
		assertEquals(0, resp.currentPageIndex());
		assertEquals(5, resp.size());
		assertEquals(1, resp.totalPages());
		assertEquals("2030-11-08T22:33:22+0000", resp.tokenExpirationDate());
		assertEquals("2049025000431439", resp.uId());
		assertEquals("70e8c740-514c-11eb-bb63-a90b882fcd52", resp.versionId());
		assertTrue(resp.unlimitedAssets().isEmpty());
		assertEquals(0, resp.nextPageIndex());
		assertNull(resp.mdmInfo());
	}

	@Test
	void test_fetch_assets_real_world_payload(WireMockRuntimeInfo wm) throws Exception {
		// Shape of a real response with randomized adamId/uId/versionId: Integer.MAX_VALUE license counts, 9 and 10
		// digit adamIds, mixed platform lists
		stubFor(get(urlPathEqualTo("/assets")).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody("""
				{"assets":[
				{"adamId":"4502283496","assignedCount":1,"availableCount":2147483646,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS","tvOS"],"totalCount":2147483647},
				{"adamId":"8719966855","assignedCount":2,"availableCount":2147483645,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"7017091956","assignedCount":2,"availableCount":2147483645,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"8573731881","assignedCount":1,"availableCount":2147483646,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"5149933681","assignedCount":0,"availableCount":2147483647,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"5945106184","assignedCount":0,"availableCount":2147483647,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"2292299297","assignedCount":0,"availableCount":2,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2},
				{"adamId":"6760115735","assignedCount":0,"availableCount":2147483647,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"7746627752","assignedCount":0,"availableCount":2147483647,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"9133268114","assignedCount":4,"availableCount":2147483643,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"543636691","assignedCount":0,"availableCount":2147483647,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS","macOS","watchOS"],"totalCount":2147483647},
				{"adamId":"476409757","assignedCount":4,"availableCount":2147483643,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS","watchOS"],"totalCount":2147483647},
				{"adamId":"685642320","assignedCount":3,"availableCount":2147483644,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS","tvOS"],"totalCount":2147483647},
				{"adamId":"561644697","assignedCount":1,"availableCount":2147483646,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"284180808","assignedCount":0,"availableCount":2147483647,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS","tvOS"],"totalCount":2147483647},
				{"adamId":"316979708","assignedCount":0,"availableCount":2147483647,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS","visionOS"],"totalCount":2147483647},
				{"adamId":"3617035366","assignedCount":3,"availableCount":2147483644,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"472405037","assignedCount":0,"availableCount":2147483647,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"9805903626","assignedCount":4,"availableCount":2147483643,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"774553903","assignedCount":1,"availableCount":2147483646,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":2147483647},
				{"adamId":"860530677","assignedCount":0,"availableCount":154,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS"],"totalCount":154},
				{"adamId":"916563899","assignedCount":4,"availableCount":2147483643,"deviceAssignable":true,"pricingParam":"STDQ","productType":"App","retiredCount":0,"revocable":true,"supportedPlatforms":["iOS","visionOS"],"totalCount":2147483647}
				],"currentPageIndex":0,"size":22,"tokenExpirationDate":"2027-06-22T08:27:27+0000","totalPages":1,"uId":"2105000009993362","versionId":"00f40084-a9ea-4bfd-9505-ce6d7f5a8799"}
				""")));

		var resp = TestUtil.createClient(wm).fetchAssets();

		assertEquals(22, resp.assets().size());
		assertEquals(22, resp.size());
		assertEquals(0, resp.nextPageIndex());
		assertEquals("2027-06-22T08:27:27+0000", resp.tokenExpirationDate());
		assertEquals("2105000009993362", resp.uId());
		assertEquals("00f40084-a9ea-4bfd-9505-ce6d7f5a8799", resp.versionId());
		assertTrue(resp.unlimitedAssets().isEmpty());

		var first = resp.assets().getFirst();
		assertEquals("4502283496", first.adamId());
		assertEquals(1, first.assignedCount());
		assertEquals(Integer.MAX_VALUE - 1, first.availableCount());
		assertEquals(Integer.MAX_VALUE, first.totalCount());
		assertEquals(0, first.retiredCount());
		assertTrue(first.isDeviceAssignable());
		assertTrue(first.isRevocable());
		assertEquals(PricingParam.STDQ, first.pricingParam());
		assertEquals(ProductType.APP, first.productType());
		assertEquals(List.of(Platform.IOS, Platform.TVOS), first.supportedPlatforms());

		assertEquals("543636691", resp.assets().get(10).adamId());
		assertEquals(List.of(Platform.IOS, Platform.MACOS, Platform.WATCHOS),
				resp.assets().get(10).supportedPlatforms());
		assertEquals(List.of(Platform.IOS, Platform.VISIONOS), resp.assets().get(21).supportedPlatforms());
		assertEquals(154, resp.assets().get(20).totalCount());
		assertEquals(154, resp.assets().get(20).availableCount());
		assertTrue(resp.assets().stream().noneMatch(a -> a.supportedPlatforms().contains(Platform.UNKNOWN)));
	}

	@Test
	void test_fetch_assets_apple_example_query(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("{}")));

		TestUtil.createClient(wm).fetchAssets(
				new FetchAssetsOptions("", ProductType.APP,
						PricingParam.STDQ, null, null, null, null, null, null, null), 0);

		verify(getRequestedFor(urlPathEqualTo("/assets")).withQueryParam("productType", equalTo("App"))
				.withQueryParam("pricingParam", equalTo("STDQ")).withoutQueryParam("pageIndex")
				.withoutQueryParam("revocable").withoutQueryParam("unlimited").withoutQueryParam("adamId"));
	}

	@Test
	void test_fetch_assets_all_filters(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("{}")));

		TestUtil.createClient(wm).fetchAssets(
				new FetchAssetsOptions("408709785", ProductType.BOOK,
						PricingParam.PLUS, false, true, true, 0, 10, 1, 20), 2);

		verify(getRequestedFor(urlPathEqualTo("/assets")).withQueryParam("pageIndex", equalTo("2"))
				.withQueryParam("productType", equalTo("Book")).withQueryParam("pricingParam", equalTo("PLUS"))
				.withQueryParam("revocable", equalTo("false")).withQueryParam("deviceAssignable", equalTo("true"))
				.withQueryParam("maxAvailableCount", equalTo("10")).withQueryParam("minAvailableCount", equalTo("0"))
				.withQueryParam("maxAssignedCount", equalTo("20")).withQueryParam("minAssignedCount", equalTo("1"))
				.withQueryParam("adamId", equalTo("408709785")).withQueryParam("unlimited", equalTo("true")));
	}

	@Test
	void test_fetch_assets_product_type_wire_values(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("{}")));

		var client = TestUtil.createClient(wm);
		client.fetchAssets(FetchAssetsOptions.ofProductType(ProductType.APP));
		client.fetchAssets(FetchAssetsOptions.ofProductType(ProductType.BOOK));

		verify(1, getRequestedFor(urlEqualTo("/assets?productType=App")));
		verify(1, getRequestedFor(urlEqualTo("/assets?productType=Book")));
	}

	@Test
	void test_fetch_assets_without_product_type(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("{}")));

		TestUtil.createClient(wm).fetchAssets(FetchAssetsOptions.ofAdamId("408709785"));

		verify(getRequestedFor(urlEqualTo("/assets?adamId=408709785")));
	}

	@Test
	void test_fetch_assets_unknown_product_type(WireMockRuntimeInfo wm) throws Exception {
		var client = TestUtil.createClient(wm);

		var ex = assertThrows(IllegalArgumentException.class,
				() -> client.fetchAssets(FetchAssetsOptions.ofProductType(ProductType.UNKNOWN)));
		assertEquals("productType must not be UNKNOWN", ex.getMessage());
		verify(0, getRequestedFor(anyUrl()));
	}

	@Test
	void test_fetch_assets_unknown_pricing_param(WireMockRuntimeInfo wm) throws Exception {
		var client = TestUtil.createClient(wm);

		var ex = assertThrows(IllegalArgumentException.class, () -> client.fetchAssets(
				new FetchAssetsOptions("", null, PricingParam.UNKNOWN, null, null, null, null, null, null, null)));
		assertEquals("pricingParam must not be UNKNOWN", ex.getMessage());
		verify(0, getRequestedFor(anyUrl()));
	}

	@Test
	void test_fetch_assets_unlimited_and_paging(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload: built from the documented schemas. Deliberately omits "assets" and
		// "tokenExpirationDate" (only sent when the token expires within 15 days) and uses an unknown platform.
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{
						    "unlimitedAssets": [
						        {
						            "adamId": "333333333",
						            "assignedCount": 4,
						            "deviceAssignable": true,
						            "pricingParam": "STDQ",
						            "productType": "App",
						            "revocable": false,
						            "supportedPlatforms": ["iOS", "someFuturePlatform"]
						        }
						    ],
						    "currentPageIndex": 0,
						    "nextPageIndex": 1,
						    "size": 1,
						    "totalPages": 2,
						    "uId": "2049025000431439",
						    "versionId": "70e8c740-514c-11eb-bb63-a90b882fcd52",
						    "mdmInfo": {
						        "id": "522d5c43-0000-0000-0000-000000000000",
						        "metadata": "2.13.3",
						        "name": "Apple Configurator 2"
						    }
						}
						""".stripIndent())));

		var resp = TestUtil.createClient(wm).fetchAssets(FetchAssetsOptions.ofEmpty());

		assertTrue(resp.assets().isEmpty());
		assertEquals("", resp.tokenExpirationDate());
		assertEquals(1, resp.nextPageIndex());
		assertEquals(2, resp.totalPages());
		assertEquals(1, resp.unlimitedAssets().size());
		var asset = resp.unlimitedAssets().getFirst();
		assertEquals("333333333", asset.adamId());
		assertEquals(4, asset.assignedCount());
		assertTrue(asset.isDeviceAssignable());
		assertFalse(asset.isRevocable());
		assertEquals(PricingParam.STDQ, asset.pricingParam());
		assertEquals(ProductType.APP, asset.productType());
		assertEquals(List.of(Platform.IOS, Platform.UNKNOWN), asset.supportedPlatforms());
		assertNotNull(resp.mdmInfo());
		assertEquals("522d5c43-0000-0000-0000-000000000000", resp.mdmInfo().id());
		assertEquals("2.13.3", resp.mdmInfo().metadata());
		assertEquals("Apple Configurator 2", resp.mdmInfo().name());
	}

	@Test
	void test_fetch_assets_unknown_enum_values(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withBody("""
						{"assets": [{"adamId": "1", "pricingParam": "GOLD", "productType": "Podcast"}]}
						""")));

		var asset = TestUtil.createClient(wm).fetchAssets().assets().getFirst();

		assertEquals(PricingParam.UNKNOWN, asset.pricingParam());
		assertEquals(ProductType.UNKNOWN, asset.productType());
		assertTrue(asset.supportedPlatforms().isEmpty());
	}

	@Test
	void test_fetch_assets_error(WireMockRuntimeInfo wm) throws Exception {
		var message = "The token has expired. You need to generate a new token online using your organization's account at either school.apple.com or business.apple.com.";
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(401).withHeaders(headers).withBody(errorBody(9621, message))));

		var ex = assertThrows(AppAndBookClientException.class, () -> TestUtil.createClient(wm).fetchAssets());

		assertEquals(9621, ex.getCode());
		assertEquals(401, ex.getStatusCode());
		assertEquals(message, ex.getErrorMessage());
	}

	@Test
	void test_fetch_assets_retry_after(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(503).withHeaders(headers.plus(
								com.github.tomakehurst.wiremock.http.HttpHeader.httpHeader("Retry-After", "120")))
						.withBody(errorBody(9729, "The service is temporarily unavailable. Try again later."))));

		var ex = assertThrows(AppAndBookClientRetryAfterException.class,
				() -> TestUtil.createClient(wm).fetchAssets());

		assertEquals(503, ex.getCode());
		assertTrue(ex.getRetryAfter().isAfter(java.time.ZonedDateTime.now()));
	}

	@Test
	void test_fetch_assets_5xx_without_retry_after(WireMockRuntimeInfo wm) throws Exception {
		stubFor(get(urlPathEqualTo("/assets")).willReturn(
				aResponse().withStatus(500).withHeaders(headers).withBody(errorBody(9603, "Internal error."))));

		var ex = assertThrows(AppAndBookClientException.class, () -> TestUtil.createClient(wm).fetchAssets());

		assertEquals(9603, ex.getCode());
		assertEquals(500, ex.getStatusCode());
	}
}
