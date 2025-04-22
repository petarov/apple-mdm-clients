package com.github.petarov.mdm.aab.legacy;

import com.github.petarov.mdm.shared.http.HttpClientWrapperException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyAppAndBookClientRetryAfterExceptionTests {

	@Test
	void test_retry_after_seconds() throws Exception {
		var ex = new LegacyAppAndBookClientRetryAfterException(
				new HttpClientWrapperException("http error", 503, "Service Unavailable",
						Map.of("Retry-After", List.of("300"))));
		assertEquals(503, ex.getCode());
		assertEquals("Service Unavailable", ex.getStatusLine());
		var diff = Duration.between(ex.getRetryAfter(),
				ZonedDateTime.now().plusSeconds(300)); // e.g. PT0.001827S, PT0.000389S
		assertTrue(diff.isPositive());
		assertTrue(diff.compareTo(Duration.ofMillis(10)) < 0); // PT0.01S > PT0.001827S
	}

	@Test
	void test_retry_after_rfc1123() throws Exception {
		var ex = new LegacyAppAndBookClientRetryAfterException(
				new HttpClientWrapperException("http error", 503, "Service Unavailable",
						Map.of("Retry-After", List.of("Tue, 22 Apr 2025 23:47:01 GMT"))));
		assertEquals(503, ex.getCode());
		assertEquals("Service Unavailable", ex.getStatusLine());
		assertEquals(ZonedDateTime.parse("Tue, 22 Apr 2025 23:47:01 GMT", DateTimeFormatter.RFC_1123_DATE_TIME),
				ex.getRetryAfter());
	}

	@Test
	void test_retry_after_wrong_http_code() throws Exception {
		var ex = assertThrows(RuntimeException.class, () -> new LegacyAppAndBookClientRetryAfterException(
				new HttpClientWrapperException("http error", 429, "Too Many Requests",
						Map.of("Retry-After", List.of()))));
		assertEquals("503 Service Unavailable response code required: got 429 instead", ex.getMessage());
	}
}
