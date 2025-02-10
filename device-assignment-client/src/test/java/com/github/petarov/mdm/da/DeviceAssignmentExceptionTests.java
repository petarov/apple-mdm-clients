package com.github.petarov.mdm.da;

import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeviceAssignmentExceptionTests {

	@Test
	void test_errors() throws Exception {
		try {
			throw new DeviceAssignmentException(new HttpClientWrapperException("http error", 500, "EXHAUSTED_CURSOR"));
		} catch (DeviceAssignmentException e) {
			Assertions.assertEquals("http error", e.getMessage());
			Assertions.assertEquals(500, e.getCode());
			Assertions.assertEquals("EXHAUSTED_CURSOR", e.getStatusLine());
			Assertions.assertEquals("The cursor had returned all devices in previous calls.", e.getErrorFromStatus());
			Assertions.assertEquals(
					"com.github.petarov.mdm.da.DeviceAssignmentException: http error: The cursor had returned all devices in previous calls.",
					e.toString());
		}
	}

	@Test
	void test_oauth_errors() throws Exception {
		try {
			throw new DeviceAssignmentException(new HttpClientWrapperException("http error", 500, "OAUTH_BAD_REQUEST"));
		} catch (DeviceAssignmentException e) {
			Assertions.assertEquals("OAUTH_BAD_REQUEST", e.getStatusLine());
			Assertions.assertEquals(
					"Indicates one of the following: Unsupported oauth parameters, Unsupported signature method, Missing required authorization parameter or Duplicated OAuth protocol parameter.",
					e.getErrorFromStatus());
		}

		try {
			throw new DeviceAssignmentException(
					new HttpClientWrapperException("http error", 500, "oauth_problem_adviceBad Request"));
		} catch (DeviceAssignmentException e) {
			Assertions.assertEquals("oauth_problem_adviceBad Request", e.getStatusLine());
			Assertions.assertEquals(
					"Indicates one of the following: Unsupported oauth parameters, Unsupported signature method, Missing required authorization parameter or Duplicated OAuth protocol parameter.",
					e.getErrorFromStatus());
		}
	}
}
