package com.github.petarov.mdm.shared.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Nonnull;

public final class JsonUtils {

	/**
	 * @return new Jackson {@link ObjectMapper} instance with default settings
	 */
	@Nonnull
	public static ObjectMapper createObjectMapper() {
		var mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		// Do not convert to local timezone
		mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
		return mapper;
	}
}
