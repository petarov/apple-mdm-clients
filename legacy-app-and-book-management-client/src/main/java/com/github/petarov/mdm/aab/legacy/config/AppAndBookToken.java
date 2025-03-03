package com.github.petarov.mdm.aab.legacy.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppAndBookToken(@Nonnull String sToken) {

	/**
	 * Create an sToken wrapper byte data input.
	 */
	@Nonnull
	public static AppAndBookToken create(@Nonnull InputStream input) {
		try {
			return new AppAndBookToken(new String(input.readAllBytes(), StandardCharsets.UTF_8).stripTrailing());
		} catch (IOException e) {
			throw new RuntimeException("Error read sToken input stream", e);
		}
	}

	/**
	 * @see #create(InputStream)
	 */
	@Nonnull
	public static AppAndBookToken create(@Nonnull Path path) throws IOException {
		try (var input = Files.newInputStream(path)) {
			return create(input);
		}
	}
}
