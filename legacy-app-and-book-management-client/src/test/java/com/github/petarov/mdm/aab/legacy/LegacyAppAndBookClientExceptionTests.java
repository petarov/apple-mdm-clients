package com.github.petarov.mdm.aab.legacy;

import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LegacyAppAndBookClientExceptionTests {

	@Test
	void test_json_error_parsing() throws Exception {
		var ex = new LegacyAppAndBookClientException(new HttpClientWrapperException("http error", 400,
				"{\"errorNumber\":9602,\"errorMessage\":\"Invalid argument\",\"status\":-1}", Map.of()));
		assertEquals(9602, ex.getCode());
		assertEquals("Invalid argument", ex.getErrorMessage());
	}
}
