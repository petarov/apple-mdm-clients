package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AppAndBookClientRetryAfterException extends RuntimeException {

	private static final String HEADER_RETRY_AFTER = "Retry-After";

	private final int           code;
	private final String        statusLine;
	private final ZonedDateTime retryAfter;

	public AppAndBookClientRetryAfterException(@Nonnull HttpClientWrapperException exception) {
		super(exception.getMessage(), exception);

		this.code = exception.getStatusCode();
		this.statusLine = exception.getStatusLine();

		if (exception.getStatusCode() / 100 != 5) {
			throw new IllegalArgumentException(
					"5xx server error response code required: got %d instead".formatted(exception.getStatusCode()),
					exception);
		}

		var retryAfterValue = findRetryAfter(exception.headers()).orElseThrow(
				() -> new IllegalArgumentException("header <Retry-After> is missing"));

		if (Character.isDigit(retryAfterValue.charAt(0))) {
			retryAfter = ZonedDateTime.now().plusSeconds(Long.parseLong(retryAfterValue));
		} else {
			retryAfter = ZonedDateTime.parse(retryAfterValue, DateTimeFormatter.RFC_1123_DATE_TIME);
		}
	}

	/**
	 * Case-insensitive lookup of the first non-empty {@code Retry-After} header value.
	 */
	static Optional<String> findRetryAfter(@Nonnull Map<String, List<String>> headers) {
		return headers.entrySet().stream().filter(e -> HEADER_RETRY_AFTER.equalsIgnoreCase(e.getKey()))
				.flatMap(e -> e.getValue().stream()).filter(v -> !v.isEmpty()).findFirst();
	}

	public int getCode() {
		return code;
	}

	public String getStatusLine() {
		return statusLine;
	}

	@Nonnull
	public ZonedDateTime getRetryAfter() {
		return retryAfter;
	}
}
