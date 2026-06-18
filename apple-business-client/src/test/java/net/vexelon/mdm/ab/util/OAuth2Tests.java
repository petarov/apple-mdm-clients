package net.vexelon.mdm.ab.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPairGenerator;
import java.time.Duration;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OAuth2Tests {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Test
	void jwt_has_three_parts() throws Exception {
		var kp = generateKeyPair();
		var oauth = new OAuth2("client-1", "key-1", "team-1", kp.getPrivate(), MAPPER, Duration.ofHours(2));

		var jwt = oauth.createClientAssertion();
		var parts = jwt.split("\\.");

		assertEquals(3, parts.length, "JWT must have header.payload.signature");
	}

	@Test
	void header_contains_alg_and_kid() throws Exception {
		var kp = generateKeyPair();
		var oauth = new OAuth2("client-1", "key-99", "team-1", kp.getPrivate(), MAPPER, Duration.ofHours(2));

		var parts = oauth.createClientAssertion().split("\\.");
		var header = decodeJson(parts[0]);

		assertEquals("ES256", header.get("alg"));
		assertEquals("key-99", header.get("kid"));
	}

	@Test
	void payload_contains_required_claims() throws Exception {
		var kp = generateKeyPair();
		var oauth = new OAuth2("client-abc", "key-1", "team-xyz", kp.getPrivate(), MAPPER, Duration.ofHours(2));

		var parts = oauth.createClientAssertion().split("\\.");
		var payload = decodeJson(parts[1]);

		assertEquals("https://account.apple.com/auth/oauth2/v2/token", payload.get("aud"));
		assertEquals("client-abc", payload.get("sub"));
		assertEquals("team-xyz", payload.get("iss"));
		assertNotNull(payload.get("jti"), "jti must be present");
		assertNotNull(payload.get("iat"), "iat must be present");
		assertNotNull(payload.get("exp"), "exp must be present");

		long iat = ((Number) payload.get("iat")).longValue();
		long exp = ((Number) payload.get("exp")).longValue();
		assertTrue(exp > iat, "exp must be after iat");
	}

	@Test
	void teamId_defaults_are_not_enforced_by_util() throws Exception {
		// The util uses whatever teamId it receives; defaulting teamId=clientId is the builder's job.
		var kp = generateKeyPair();
		var oauth = new OAuth2("client-1", "key-1", "client-1", kp.getPrivate(), MAPPER, Duration.ofHours(2));
		var parts = oauth.createClientAssertion().split("\\.");
		var payload = decodeJson(parts[1]);

		assertEquals("client-1", payload.get("iss"));
		assertEquals("client-1", payload.get("sub"));
	}

	@Test
	void signature_verifies_with_public_key() throws Exception {
		var kp = generateKeyPair();
		var oauth = new OAuth2("client-1", "key-1", "team-1", kp.getPrivate(), MAPPER, Duration.ofHours(2));

		var jwt = oauth.createClientAssertion();
		var parts = jwt.split("\\.");

		var signingInput = (parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8);
		var signatureBytes = Base64.getUrlDecoder().decode(parts[2]);

		var verifier = Signature.getInstance("SHA256withECDSAinP1363Format");
		verifier.initVerify(kp.getPublic());
		verifier.update(signingInput);

		assertTrue(verifier.verify(signatureBytes), "ES256 signature must verify against the public key");
	}

	// -------------------------------------------------------------------------

	private static java.security.KeyPair generateKeyPair() throws Exception {
		var gen = KeyPairGenerator.getInstance("EC");
		gen.initialize(new ECGenParameterSpec("secp256r1"));
		return gen.generateKeyPair();
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> decodeJson(String b64url) throws Exception {
		var json = Base64.getUrlDecoder().decode(b64url);
		return MAPPER.readValue(json, Map.class);
	}
}
