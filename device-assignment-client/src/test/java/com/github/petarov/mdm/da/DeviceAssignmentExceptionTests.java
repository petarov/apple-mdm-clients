package com.github.petarov.mdm.da;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeviceAssignmentExceptionTests {

	@Test
	void test_errors() throws Exception {
		try {
			throw new DeviceAssignmentException("http error", 500, "EXHAUSTED_CURSOR");
		} catch (DeviceAssignmentException e) {
			Assertions.assertEquals("The cursor had returned all devices in previous calls.", e.getErrorMessage());
		}
	}

	@Test
	void test_oauth_errors() throws Exception {
		try {
			throw new DeviceAssignmentException("http error", 500, "OAUTH_BAD_REQUEST");
		} catch (DeviceAssignmentException e) {
			Assertions.assertEquals(
					"Indicates one of the following: Unsupported oauth parameters, Unsupported signature method, Missing required authorization parameter or Duplicated OAuth protocol parameter.",
					e.getErrorMessage());
		}

		try {
			throw new DeviceAssignmentException("http error", 500, "oauth_problem_adviceBad Request");
		} catch (DeviceAssignmentException e) {
			Assertions.assertEquals(
					"Indicates one of the following: Unsupported oauth parameters, Unsupported signature method, Missing required authorization parameter or Duplicated OAuth protocol parameter.",
					e.getErrorMessage());
		}
	}
}
