package com.github.petarov.mdm.da;

import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviceAssignmentExceptionTests {

	@Test
	void test_errors() throws Exception {
		try {
			throw new DeviceAssignmentException(
					new HttpClientWrapperException("http error", 500, "EXHAUSTED_CURSOR", Map.of()));
		} catch (DeviceAssignmentException e) {
			assertEquals("http error", e.getMessage());
			assertEquals(500, e.getCode());
			assertEquals("EXHAUSTED_CURSOR", e.getStatusLine());
			assertEquals("The cursor had returned all devices in previous calls.", e.getErrorFromStatus());
			assertEquals(
					"com.github.petarov.mdm.da.DeviceAssignmentException: http error: The cursor had returned all devices in previous calls.",
					e.toString());
		}
	}

	@Test
	void test_oauth_errors() throws Exception {
		try {
			throw new DeviceAssignmentException(
					new HttpClientWrapperException("http error", 500, "OAUTH_BAD_REQUEST", Map.of()));
		} catch (DeviceAssignmentException e) {
			assertEquals("OAUTH_BAD_REQUEST", e.getStatusLine());
			assertEquals(
					"Indicates one of the following: Unsupported oauth parameters, Unsupported signature method, Missing required authorization parameter or Duplicated OAuth protocol parameter.",
					e.getErrorFromStatus());
		}

		try {
			throw new DeviceAssignmentException(
					new HttpClientWrapperException("http error", 500, "oauth_problem_adviceBad Request", Map.of()));
		} catch (DeviceAssignmentException e) {
			assertEquals("oauth_problem_adviceBad Request", e.getStatusLine());
			assertEquals(
					"Indicates one of the following: Unsupported oauth parameters, Unsupported signature method, Missing required authorization parameter or Duplicated OAuth protocol parameter.",
					e.getErrorFromStatus());
		}
	}
}
