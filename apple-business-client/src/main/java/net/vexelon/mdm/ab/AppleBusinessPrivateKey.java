package net.vexelon.mdm.ab;

import jakarta.annotation.Nonnull;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.stream.Collectors;

public record AppleBusinessPrivateKey(@Nonnull PrivateKey privateKey) {

	/**
	 * @see #createFromPEM(InputStream)
	 */
	@Nonnull
	public static AppleBusinessPrivateKey createFromPEM(@Nonnull Path path) throws IOException {
		try (var input = Files.newInputStream(path)) {
			return createFromPEM(input);
		}
	}

	/**
	 * Creates a private key wrapper from PEM-formatted text input.
	 */
	@Nonnull
	public static AppleBusinessPrivateKey createFromPEM(@Nonnull InputStream input) {
		try {
			return new AppleBusinessPrivateKey(
					new JcaPEMKeyConverter().getPrivateKey(pkcs8InfoFromPEM(input.readAllBytes())));
		} catch (IOException e) {
			throw new IllegalArgumentException("Error reading PEM input stream", e);
		}
	}

	/**
	 * Strips the PEM envelope from {@code pemData}, base64-decodes the body, and parses the result
	 * directly as a {@link PrivateKeyInfo} (PKCS#8). Used when {@code BEGIN EC PRIVATE KEY} headers
	 * wrap PKCS#8 content instead of a true SEC1 key.
	 */
	private static PrivateKeyInfo pkcs8InfoFromPEM(byte[] pemData) throws IOException {
		var base64Body = new String(pemData, StandardCharsets.UTF_8).lines().filter(line -> !line.startsWith("-----"))
				.collect(Collectors.joining());
		var der = Base64.getDecoder().decode(base64Body);
		return PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(der));
	}
}
