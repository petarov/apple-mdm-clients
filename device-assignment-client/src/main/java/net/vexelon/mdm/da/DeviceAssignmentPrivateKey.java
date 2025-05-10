package net.vexelon.mdm.da;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import jakarta.annotation.Nonnull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
			throw new HttpClientWrapperException("Error read PKCS8 binary input stream", e);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new HttpClientWrapperException("Error reading private key", e);
		}
	}

	/**
	 * @see #createFromDER(InputStream)
	 */
	@Nonnull
	public static DeviceAssignmentPrivateKey createFromDER(@Nonnull Path path) throws IOException {
		try (var input = Files.newInputStream(path)) {
			return createFromDER(input);
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
			throw new HttpClientWrapperException("Error read PEM input stream", e);
		}
	}

	/**
	 * @see #createFromPEM(InputStream)
	 */
	@Nonnull
	public static DeviceAssignmentPrivateKey createFromPEM(@Nonnull Path path) throws IOException {
		try (var input = Files.newInputStream(path)) {
			return createFromPEM(input);
		}
	}
}
