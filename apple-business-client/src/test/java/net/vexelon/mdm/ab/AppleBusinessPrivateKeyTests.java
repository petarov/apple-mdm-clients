package net.vexelon.mdm.ab;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.Security;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppleBusinessPrivateKeyTests {

	// Apple key format: generate PKCS#8 DER content wrapped in a BEGIN EC PRIVATE KEY
	// -----
	// openssl ecparam -name prime256v1 -genkey -noout -out private_key.pem
	// sed 's/BEGIN PRIVATE KEY/BEGIN EC PRIVATE KEY/; s/END PRIVATE KEY/END EC PRIVATE KEY/' private_key.pem > test_private_apple_style_key.pem
	// -----

	@BeforeAll
	void init() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Test
	void load_ec_private_key_from_sec1_pem() throws Exception {
		var key = AppleBusinessPrivateKey.createFromPEM(
				Objects.requireNonNull(getClass().getResourceAsStream("/test_private_apple_style_key.pem")));
		assertEquals("ECDSA", key.privateKey().getAlgorithm());
		assertEquals("PKCS#8", key.privateKey().getFormat());
	}

	@Test
	void load_ec_private_key_from_pkcs8_pem() throws Exception {
		var key = AppleBusinessPrivateKey.createFromPEM(
				Objects.requireNonNull(getClass().getResourceAsStream("/test_private_pkcs8_key.pem")));
		assertEquals("ECDSA", key.privateKey().getAlgorithm());
		assertEquals("PKCS#8", key.privateKey().getFormat());
	}

	@Test
	void failed_load_private_key_from_openssh_pem() throws Exception {
		var throwable = Assertions.assertThrows(IllegalArgumentException.class,
				() -> AppleBusinessPrivateKey.createFromPEM(
						Objects.requireNonNull(getClass().getResourceAsStream("/test_private_ssh_key.pem"))));
		assertEquals("Error reading PEM input stream", throwable.getMessage());
	}
}
