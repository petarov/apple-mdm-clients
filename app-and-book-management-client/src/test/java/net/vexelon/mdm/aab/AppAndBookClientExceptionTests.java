package net.vexelon.mdm.aab;

import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppAndBookClientExceptionTests {

	@Test
	void test_json_error_parsing() throws Exception {
		var ex = new AppAndBookClientException(new HttpClientWrapperException("http error", 400,
				"{\"errorNumber\":9726,\"errorMessage\":\"Invalid argument\"}", Map.of()));
		assertEquals(9726, ex.getCode());
		assertEquals(400, ex.getStatusCode());
		assertEquals("Invalid argument", ex.getErrorMessage());
		assertEquals("http error (9726) Invalid argument", ex.getMessage());
	}

	@Test
	void test_non_json_error_body() throws Exception {
		var ex = new AppAndBookClientException(
				new HttpClientWrapperException("http error", 502, "Bad Gateway", Map.of()));
		assertEquals(502, ex.getCode());
		assertEquals(502, ex.getStatusCode());
		assertEquals("Bad Gateway", ex.getErrorMessage());
		assertEquals("http error", ex.getMessage());
	}

	@Test
	void test_json_error_parsing_error_number_after_long_error_info() throws Exception {
		// NOT an Apple example payload
		var ids = IntStream.range(0, 20).mapToObj(i -> "\"client-" + i + "\"").collect(Collectors.joining(","));
		var body = "{\"errorInfo\":{\"clientUserIds\":[" + ids + "]},\"errorNumber\":9609,"
				+ "\"errorMessage\":\"Unable to find the registered user.\"}";
		assertTrue(body.indexOf("errorNumber") > 100);

		var ex = new AppAndBookClientException(new HttpClientWrapperException("http error", 400, body, Map.of()));

		assertEquals(9609, ex.getCode());
		assertEquals(400, ex.getStatusCode());
		assertEquals("Unable to find the registered user.", ex.getErrorMessage());
	}
}
