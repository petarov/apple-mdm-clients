package com.github.petarov.mdm.aab.legacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.github.petarov.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Base64;

public record LegacyAppAndBookToken(String sToken, @Nonnull AppAndBookTokenDetails details) {

	/**
	 * Create an sToken wrapper byte data input.
	 */
	@Nonnull
	public static LegacyAppAndBookToken create(@Nonnull InputStream input) {
		try {
			String sToken = new String(input.readAllBytes(), StandardCharsets.UTF_8).stripTrailing();
			return new LegacyAppAndBookToken(sToken, JsonUtil.createObjectMapper()
					.readValue(Base64.getDecoder().decode(sToken), AppAndBookTokenDetails.class));
		} catch (IOException e) {
			throw new RuntimeException("Error read sToken input stream", e);
		}
	}

	/**
	 * @see #create(InputStream)
	 */
	@Nonnull
	public static LegacyAppAndBookToken create(@Nonnull Path path) throws IOException {
		try (var input = Files.newInputStream(path)) {
			return create(input);
		}
	}

	public record AppAndBookTokenDetails(@JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull String token,
	                                     @JsonSetter(nulls = Nulls.AS_EMPTY) @JsonProperty("expDate") @Nonnull OffsetDateTime expiryDateTime,
	                                     @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull String orgName) {}
}
