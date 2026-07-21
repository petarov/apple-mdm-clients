package net.vexelon.mdm.shared.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Nonnull;

public final class JsonUtil {

	/**
	 * @return new Jackson {@link ObjectMapper} instance with default settings
	 */
	@Nonnull
	public static ObjectMapper createObjectMapper() {
		var mapper = JsonMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build();
		mapper.registerModule(new JavaTimeModule());
		// format date-time as ISO-8601
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		// do not convert date-time to local timezone
		mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
		// unrecognized enum values fall back to the @JsonEnumDefaultValue-annotated constant instead of throwing
		mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
		return mapper;
	}
}
