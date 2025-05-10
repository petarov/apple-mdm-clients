package net.vexelon.mdm.aab.legacy;

import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LegacyAppAndBookClientExceptionTests {

	@Test
	void test_json_error_parsing() throws Exception {
		var ex = new LegacyAppAndBookClientException(new HttpClientWrapperException("http error", 400,
				"{\"errorNumber\":9602,\"errorMessage\":\"Invalid argument\",\"status\":-1}", Map.of()));
		assertEquals(9602, ex.getCode());
		assertEquals("Invalid argument", ex.getErrorMessage());
	}

	@Test
	void test_parsed_vpp_error() throws Exception {
		var ex = new LegacyAppAndBookClientException(9602, "Invalid argument");
		assertEquals(9602, ex.getCode());
		assertNull(ex.getMessage());
		assertEquals("Invalid argument", ex.getErrorMessage());
	}
}
