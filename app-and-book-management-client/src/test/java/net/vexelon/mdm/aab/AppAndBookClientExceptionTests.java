package net.vexelon.mdm.aab;

import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
