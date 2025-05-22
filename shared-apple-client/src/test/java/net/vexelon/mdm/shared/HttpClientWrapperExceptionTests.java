package net.vexelon.mdm.shared;

import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpClientWrapperExceptionTests {

	@Test
	void test_headers() throws Exception {
		try {
			throw new HttpClientWrapperException("http error", 503, "",
					Map.of("apple-originating-system", List.of("UnknownOriginatingSystem"), "apple-timing-app",
							List.of("34 ms", "12 ms"), "apple-tk", List.of("false")));
		} catch (HttpClientWrapperException e) {
			assertEquals("http error", e.getMessage());
			assertEquals(503, e.getStatusCode());

			assertTrue(e.headers().containsKey("apple-originating-system"));
			assertIterableEquals(List.of("UnknownOriginatingSystem"), e.headers().get("apple-originating-system"));

			assertTrue(e.headers().containsKey("apple-timing-app"));
			assertIterableEquals(List.of("34 ms", "12 ms"), e.headers().get("apple-timing-app"));

			assertTrue(e.headers().containsKey("apple-tk"));
			assertIterableEquals(List.of("false"), e.headers().get("apple-tk"));
		}
	}

	@Test
	void test_non_http() throws Exception {
		try {
			throw new HttpClientWrapperException("http request error", new IOException("channel closed"));
		} catch (HttpClientWrapperException e) {
			assertEquals("http request error", e.getMessage());
			assertEquals(0, e.getStatusCode());
			assertTrue(e.getStatusLine().isEmpty());
			assertTrue(e.headers().isEmpty());
			assertInstanceOf(IOException.class, e.getCause());
		}
	}
}
