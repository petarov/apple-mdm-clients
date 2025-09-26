package net.vexelon.mdm.shared.util;

import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public final class ParseUtil {

	private static final DateTimeFormatter APPLE_DT_FORMATTER = new DateTimeFormatterBuilder()
			// date/time
			.append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
			// offset (hh:mm - "+00:00" when it's zero)
			.optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
			// offset (hhmm - "+0000" when it's zero)
			.optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
			// offset (hh - "Z" when it's zero)
			.optionalStart().appendOffset("+HH", "Z").optionalEnd().toFormatter();

	/**
	 * Parses date-time as returned by Apple's web services. Normally dates should be ISO-8601 compliant, however, there
	 * are cases where the {@code OffsetDateTime} parser fails and a fallback mechanism is required.
	 */
	@Nonnull
	public static OffsetDateTime parseAppleDateTime(String dateTimeValue) {
		return OffsetDateTime.parse(dateTimeValue, APPLE_DT_FORMATTER);
	}
}
