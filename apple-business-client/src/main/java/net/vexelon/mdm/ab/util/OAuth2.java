package net.vexelon.mdm.ab.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nonnull;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * OAuth 2.0 client-assertion JWT generation for the Apple Business / School Manager API (ES256).
 *
 * <p>The JWT is signed using ECDSA P-256 ({@code SHA256withECDSAinP1363Format}), which produces
 * the fixed-length raw R‖S signature required by JOSE/Apple. Plain {@code SHA256withECDSA} would
 * emit a DER-encoded signature and must <em>not</em> be used.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/implementing-oauth-for-the-apple-school-manager-and-apple-business-api">
 * Implementing OAuth for the Apple School Manager and Apple Business API</a>
 */
public final class OAuth2 {

	private static final String AUDIENCE = "https://account.apple.com/auth/oauth2/v2/token";

	private final String       clientId;
	private final String       keyId;
	private final String       teamId;
	private final PrivateKey   privateKey;
	private final ObjectMapper objectMapper;
	private final Duration     tokenValidity;

	public OAuth2(@Nonnull String clientId, @Nonnull String keyId, @Nonnull String teamId,
			@Nonnull PrivateKey privateKey, @Nonnull ObjectMapper objectMapper, @Nonnull Duration tokenValidity) {
		this.clientId = clientId;
		this.keyId = keyId;
		this.teamId = teamId;
		this.privateKey = privateKey;
		this.objectMapper = objectMapper;
		this.tokenValidity = tokenValidity;
	}

	/**
	 * Mints and signs a client-assertion JWT (ES256) ready for use in an OAuth 2 token request.
	 *
	 * @return compact serialization {@code header.payload.signature}
	 */
	@Nonnull
	public String createClientAssertion() {
		long nowInSeconds = System.currentTimeMillis() / 1000L;

		String header = base64Str(new TreeMap<>(Map.of("alg", "ES256", "kid", keyId)));

		String payload = base64Str(new TreeMap<>(
				Map.of("aud", AUDIENCE, "exp", nowInSeconds + tokenValidity.toSeconds(), "iat", nowInSeconds, "iss", teamId, "jti",
						UUID.randomUUID().toString(), "sub", clientId)));

		String signingInput = header + "." + payload;
		String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(sign(signingInput));

		return signingInput + "." + signature;
	}

	private String base64Str(@Nonnull Map<?, ?> map) {
		try {
			return Base64.getUrlEncoder().withoutPadding().encodeToString(objectMapper.writeValueAsBytes(map));
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Error serializing JWT part to JSON", e);
		}
	}

	private byte[] sign(@Nonnull String signingInput) {
		try {
			// Re-encode through the JDK KeyFactory so that keys loaded via BouncyCastle (which are
			// BC-provider-bound) become SunEC keys compatible with SHA256withECDSAinP1363Format.
			var jdkKey = KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(privateKey.getEncoded()));
			var sig = Signature.getInstance("SHA256withECDSAinP1363Format");
			sig.initSign(jdkKey);
			sig.update(signingInput.getBytes(StandardCharsets.UTF_8));
			return sig.sign();
		} catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | SignatureException e) {
			throw new IllegalStateException("Error signing JWT client assertion", e);
		}
	}
}
