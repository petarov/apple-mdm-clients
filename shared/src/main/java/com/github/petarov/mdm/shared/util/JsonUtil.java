package com.github.petarov.mdm.shared.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Nonnull;

public final class JsonUtil {

	/**
	 * @return new Jackson {@link ObjectMapper} instance with default settings
	 */
	@Nonnull
	public static ObjectMapper createObjectMapper() {
		var mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		// Format date-time as ISO-8601
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// Do not convert date-time to local timezone
		mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
		return mapper;
	}
}
