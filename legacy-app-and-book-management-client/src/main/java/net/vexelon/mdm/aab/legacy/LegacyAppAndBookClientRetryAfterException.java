package net.vexelon.mdm.aab.legacy;

import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import jakarta.annotation.Nonnull;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LegacyAppAndBookClientRetryAfterException extends RuntimeException {

	private final int           code;
	private final String        statusLine;
	private final ZonedDateTime retryAfter;

	public LegacyAppAndBookClientRetryAfterException(@Nonnull HttpClientWrapperException exception) {
		super(exception.getMessage(), exception);

		this.code = exception.getStatusCode();
		this.statusLine = exception.getStatusLine();

		if (exception.getStatusCode() != 503) {
			throw new IllegalArgumentException(
					"503 Service Unavailable response code required: got %d instead".formatted(
							exception.getStatusCode()), exception);
		}

		var retryAfterValue = Objects.requireNonNull(exception.headers().get("Retry-After"),
				"header <Retry-After> is <null>").getFirst();

		if (Character.isDigit(retryAfterValue.charAt(0))) {
			retryAfter = ZonedDateTime.now().plusSeconds(Long.parseLong(retryAfterValue));
		} else {
			retryAfter = ZonedDateTime.parse(retryAfterValue, DateTimeFormatter.RFC_1123_DATE_TIME);
		}
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
