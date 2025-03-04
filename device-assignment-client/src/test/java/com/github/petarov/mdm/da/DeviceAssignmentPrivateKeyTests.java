package com.github.petarov.mdm.da;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.security.Security;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceAssignmentPrivateKeyTests {

	@BeforeAll
	void init() {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Test
	void load_private_key_from_DER() throws Exception {
		var key = DeviceAssignmentPrivateKey.createFromDER(
				Objects.requireNonNull(this.getClass().getResourceAsStream("/apple-mdm-client-tests-1.der")));
		assertEquals("RSA", key.privateKey().getAlgorithm());
		assertEquals("PKCS#8", key.privateKey().getFormat());
	}

	@Test
	void load_private_key_from_PEM() throws Exception {
		var key = DeviceAssignmentPrivateKey.createFromPEM(
				Objects.requireNonNull(this.getClass().getResourceAsStream("/apple-mdm-client-tests-1.pem")));
		assertEquals("RSA", key.privateKey().getAlgorithm());
		assertEquals("PKCS#8", key.privateKey().getFormat());
	}
}
