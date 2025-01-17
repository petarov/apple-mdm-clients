package com.github.petarov.mdm.da.config;

import jakarta.annotation.Nonnull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public record DeviceAssignmentPrivateKey(@Nonnull PrivateKey privateKey) {

	/**
	 * Creates a private key wrapper from PKCS8 byte data input.
	 */
	@Nonnull
	public static DeviceAssignmentPrivateKey createFromDER(@Nonnull InputStream input) {
		try {
			var spec = new PKCS8EncodedKeySpec(input.readAllBytes());
			return new DeviceAssignmentPrivateKey(KeyFactory.getInstance("RSA").generatePrivate(spec));
		} catch (IOException e) {
			throw new RuntimeException("Error read PKCS8 binary input stream", e);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException("Error reading private key", e);
		}
	}

	/**
	 * Creates a private key wrapper from PEM-formatted text input.
	 */
	@Nonnull
	public static DeviceAssignmentPrivateKey createFromPEM(@Nonnull InputStream input) {
		try {
			var strippedPem = new String(input.readAllBytes(), StandardCharsets.UTF_8).replace(
					"-----BEGIN RSA PRIVATE KEY-----\n", "").replace("-----END RSA PRIVATE KEY-----", "");
			try (var encodedInput = new ByteArrayInputStream(Base64.getMimeDecoder().decode(strippedPem))) {
				return createFromDER(encodedInput);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error read PEM input stream", e);
		}
	}
}
