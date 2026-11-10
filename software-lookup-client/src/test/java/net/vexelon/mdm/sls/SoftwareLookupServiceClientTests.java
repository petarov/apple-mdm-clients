package net.vexelon.mdm.sls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import net.vexelon.mdm.shared.http.HttpConsts;
import net.vexelon.mdm.sls.model.PlatformReleases;
import net.vexelon.mdm.sls.model.SoftwareRelease;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SoftwareLookupServiceClientTests {

	private static final String USER_AGENT = "apple-mdm-sls-test";

	private HttpHeaders headers;

	@BeforeAll
	void init() {
		headers = HttpHeaders.noHeaders()
				.plus(HttpHeader.httpHeader(HttpConsts.HEADER_CONTENT_TYPE, HttpConsts.HEADER_VALUE_APPLICATION_JSON));
	}

	private static SoftwareLookupServiceClient createClient(WireMockRuntimeInfo wm) {
		return SoftwareLookupServiceClient.newBuilder().setAppleServiceUrl(wm.getHttpBaseUrl()).setUserAgent(USER_AGENT)
				.build();
	}

	private void stubOk(String body) {
		stubFor(get(urlEqualTo("/pmv")).willReturn(aResponse().withStatus(200).withHeaders(headers).withBody(body)));
	}

	private static String fixture() throws IOException {
		try (var input = SoftwareLookupServiceClientTests.class.getResourceAsStream("/pmv-response.json")) {
			return new String(Objects.requireNonNull(input).readAllBytes(), StandardCharsets.UTF_8);
		}
	}

	private static void assertRelease(SoftwareRelease release, String productVersion, String build,
			String productVersionExtra, String prerequisiteBuild, String postingDate, String expirationDate,
			List<String> supportedDevices) {
		assertEquals(productVersion, release.productVersion());
		assertEquals(build, release.build());
		assertEquals(productVersionExtra, release.productVersionExtra());
		assertEquals(prerequisiteBuild, release.prerequisiteBuild());
		assertEquals(postingDate, release.postingDate());
		assertEquals(expirationDate, release.expirationDate());
		assertEquals(supportedDevices, release.supportedDevices());
	}

	private static void assertEmpty(PlatformReleases releases) {
		assertNotNull(releases);
		assertEquals(List.of(), releases.ios());
		assertEquals(List.of(), releases.macos());
		assertEquals(List.of(), releases.visionos());
	}

	private SoftwareLookupServiceClientException assertParseFailure(WireMockRuntimeInfo wm, String body) {
		stubOk(body);
		var ex = assertThrows(SoftwareLookupServiceClientException.class,
				() -> createClient(wm).fetchSoftwareReleases());
		assertEquals(0, ex.getStatusCode());
		assertInstanceOf(HttpClientWrapperException.class, ex.getCause());
		assertInstanceOf(JsonProcessingException.class, ex.getCause().getCause());
		return ex;
	}

	@Test
	void test_fetch_sends_unauthenticated_get(WireMockRuntimeInfo wm) throws Exception {
		stubOk("{}");

		createClient(wm).fetchSoftwareReleases();

		verify(1, getRequestedFor(urlEqualTo("/pmv")).withHeader(HttpConsts.HEADER_USER_AGENT, equalTo(USER_AGENT))
				.withoutHeader(HttpConsts.HEADER_AUTHORIZATION).withRequestBody(absent()));
	}

	@Test
	void test_fetch_full_fixture(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload: trimmed from a live gdmf.apple.com response
		stubOk(fixture());

		var resp = createClient(wm).fetchSoftwareReleases();

		var pub = resp.publicAssetSets();
		assertEquals(2, pub.ios().size());
		assertEquals(1, pub.visionos().size());
		assertEquals(2, pub.macos().size());
		assertRelease(pub.ios().get(0), "27.2.1", "24B92034", "", "", "2026-11-09", "",
				List.of("iPhone19,4", "iPad16,6"));
		assertRelease(pub.ios().get(1), "26.7.2", "23H321", "", "", "2026-10-20", "2027-02-08",
				List.of("iPhone12,1", "Watch7,1"));
		assertRelease(pub.visionos().get(0), "27.1", "24N501", "", "", "2026-11-03", "", List.of("RealityDevice14,1"));
		assertRelease(pub.macos().get(0), "27.2", "26C84", "", "", "2026-11-09", "",
				List.of("J132AP", "Mac-1E7E29AD0135F9BC"));
		assertRelease(pub.macos().get(1), "26.7.1", "25H112", "", "", "2026-10-14", "2027-02-08",
				List.of("VMA2MACOSAP", "VMM-x86_64"));

		var assets = resp.assetSets();
		assertEquals(3, assets.ios().size());
		assertEquals(1, assets.visionos().size());
		assertEquals(1, assets.macos().size());
		assertRelease(assets.ios().get(0), "26.7.2", "23H321", "", "", "2026-10-20", "2027-02-08",
				List.of("iPhone12,1"));
		assertRelease(assets.ios().get(1), "26.7.2", "23H321", "", "", "2026-10-20", "2027-03-15",
				List.of("AppleTV18,1", "AudioAccessory1,1", "HomeAccessory17,1"));
		assertRelease(assets.ios().get(2), "27.2.1", "24B92034", "", "", "2026-11-09", "", List.of("iPhone19,4"));
		assertRelease(assets.visionos().get(0), "26.4", "23O230", "", "", "2026-05-12", "2027-01-10",
				List.of("RealityDevice14,1"));
		assertRelease(assets.macos().get(0), "26.7.1", "25H112", "", "", "2026-10-14", "2027-02-08", List.of("J132AP"));

		var bsi = resp.publicBackgroundSecurityImprovements();
		assertEquals(2, bsi.ios().size());
		assertEquals(1, bsi.macos().size());
		assertEquals(List.of(), bsi.visionos());
		assertRelease(bsi.ios().get(0), "26.3.1", "23D771330a", "(a)", "23D8133", "2026-03-17", "2027-02-08",
				List.of("iPad11,1", "iPhone12,1"));
		assertRelease(bsi.ios().get(1), "26.3.1", "23D771330b", "(b)", "23D771330a", "2026-04-02", "",
				List.of("iPhone12,1"));
		assertRelease(bsi.macos().get(0), "26.3.1", "25D771280a", "(a)", "25D2128", "2026-03-17", "2027-02-08",
				List.of("J132AP"));
	}

	@Test
	void test_fetch_gzip_response(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		var bytes = new ByteArrayOutputStream();
		try (var gzip = new GZIPOutputStream(bytes)) {
			gzip.write(fixture().getBytes(StandardCharsets.UTF_8));
		}
		stubFor(get(urlEqualTo("/pmv")).willReturn(
				aResponse().withStatus(200).withHeaders(headers).withHeader(HttpConsts.HEADER_CONTENT_ENCODING, "gzip")
						.withBody(bytes.toByteArray())));

		var resp = createClient(wm).fetchSoftwareReleases();

		assertEquals(3, resp.assetSets().ios().size());
		assertEquals("(a)", resp.publicBackgroundSecurityImprovements().macos().get(0).productVersionExtra());
	}

	@Test
	void test_fetch_platform_absent_or_null(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{"PublicAssetSets": {"iOS": null, "macOS": [{"ProductVersion": "27.2", "Build": "26C84"}]}}
				""");

		var resp = createClient(wm).fetchSoftwareReleases();

		assertEquals(List.of(), resp.publicAssetSets().ios());
		assertEquals(List.of(), resp.publicAssetSets().visionos());
		assertEquals(1, resp.publicAssetSets().macos().size());
	}

	@Test
	void test_fetch_sections_absent(WireMockRuntimeInfo wm) throws Exception {
		stubOk("{}");

		var resp = createClient(wm).fetchSoftwareReleases();

		assertEmpty(resp.publicAssetSets());
		assertEmpty(resp.assetSets());
		assertEmpty(resp.publicBackgroundSecurityImprovements());
	}

	@Test
	void test_fetch_sections_null(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{"PublicAssetSets": null, "AssetSets": null, "PublicBackgroundSecurityImprovements": null}
				""");

		var resp = createClient(wm).fetchSoftwareReleases();

		assertEmpty(resp.publicAssetSets());
		assertEmpty(resp.assetSets());
		assertEmpty(resp.publicBackgroundSecurityImprovements());
	}

	@Test
	void test_fetch_release_optional_fields(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{"AssetSets": {"iOS": [
				  {"ProductVersion": "1", "Build": "B1"},
				  {"ProductVersion": "2", "Build": "B2", "ExpirationDate": null, "SupportedDevices": null},
				  {"ProductVersion": "3", "Build": "B3", "SupportedDevices": []},
				  {"ProductVersion": null, "Build": null, "ProductVersionExtra": null, "PrerequisiteBuild": null,
				   "PostingDate": null}
				]}}
				""");

		var ios = createClient(wm).fetchSoftwareReleases().assetSets().ios();

		assertEquals(4, ios.size());
		assertRelease(ios.get(0), "1", "B1", "", "", "", "", List.of());
		assertRelease(ios.get(1), "2", "B2", "", "", "", "", List.of());
		assertRelease(ios.get(2), "3", "B3", "", "", "", "", List.of());
		assertRelease(ios.get(3), "", "", "", "", "", "", List.of());
	}

	@Test
	void test_fetch_null_list_elements_skipped(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{"AssetSets": {"iOS": [null], "macOS": [null, {"Build": "26C84", "SupportedDevices": [null, "J132AP"]}]}}
				""");

		var assets = createClient(wm).fetchSoftwareReleases().assetSets();

		assertEquals(List.of(), assets.ios());
		assertEquals(1, assets.macos().size());
		assertEquals(List.of("J132AP"), assets.macos().get(0).supportedDevices());
	}

	@Test
	void test_fetch_ignores_unknown_keys(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{"SomethingNew": {"iOS": []},
				 "AssetSets": {"tvOS": [{"ProductVersion": "27.0"}],
				   "iOS": [{"ProductVersion": "27.2.1", "Build": "24B92034", "NewField": 42}]}}
				""");

		var assets = createClient(wm).fetchSoftwareReleases().assetSets();

		assertEquals(1, assets.ios().size());
		assertEquals("27.2.1", assets.ios().get(0).productVersion());
		assertEquals("24B92034", assets.ios().get(0).build());
		assertEquals(List.of(), assets.macos());
	}

	@Test
	void test_fetch_scalar_coerced_to_string(WireMockRuntimeInfo wm) throws Exception {
		// NOT an Apple example payload
		stubOk("""
				{"AssetSets": {"iOS": [{"ProductVersion": 26.3}]}}
				""");

		var assets = createClient(wm).fetchSoftwareReleases().assetSets();

		assertEquals("26.3", assets.ios().get(0).productVersion());
	}

	@Test
	void test_fetch_platform_wrong_type_fails(WireMockRuntimeInfo wm) {
		// NOT an Apple example payload
		assertParseFailure(wm, """
				{"AssetSets": {"iOS": {"ProductVersion": "27.2.1"}}}
				""");
	}

	@Test
	void test_fetch_supported_devices_wrong_type_fails(WireMockRuntimeInfo wm) {
		// NOT an Apple example payload
		assertParseFailure(wm, """
				{"AssetSets": {"iOS": [{"ProductVersion": "27.2.1", "SupportedDevices": "iPhone19,4"}]}}
				""");
	}

	@Test
	void test_fetch_malformed_json_fails(WireMockRuntimeInfo wm) {
		assertParseFailure(wm, "{\"AssetSets\": {\"iOS\": [");
	}

	@Test
	void test_fetch_empty_body_fails(WireMockRuntimeInfo wm) {
		assertParseFailure(wm, "");
	}

	@Test
	void test_fetch_null_body_fails(WireMockRuntimeInfo wm) {
		stubOk("null");

		var ex = assertThrows(SoftwareLookupServiceClientException.class,
				() -> createClient(wm).fetchSoftwareReleases());

		assertEquals(0, ex.getStatusCode());
		assertEquals("Empty response body", ex.getMessage());
		assertInstanceOf(HttpClientWrapperException.class, ex.getCause());
		assertNull(ex.getCause().getCause());
	}

	@Test
	void test_fetch_http_500_fails(WireMockRuntimeInfo wm) {
		stubFor(get(urlEqualTo("/pmv")).willReturn(aResponse().withStatus(500).withBody("Internal Server Error")));

		var ex = assertThrows(SoftwareLookupServiceClientException.class,
				() -> createClient(wm).fetchSoftwareReleases());

		assertEquals(500, ex.getStatusCode());
	}

	@Test
	void test_fetch_http_404_fails(WireMockRuntimeInfo wm) {
		stubFor(get(urlEqualTo("/pmv")).willReturn(aResponse().withStatus(404)));

		var ex = assertThrows(SoftwareLookupServiceClientException.class,
				() -> createClient(wm).fetchSoftwareReleases());

		assertEquals(404, ex.getStatusCode());
	}
}
