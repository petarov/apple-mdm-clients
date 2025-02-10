package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.config.DeviceAssignmentPrivateKey;
import com.github.petarov.mdm.da.config.DeviceAssignmentServerToken;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.InputStream;
import java.security.Security;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceAssignmentServerTokenTests {

	private InputStream serverTokenInput;
	private InputStream privateKeyInput;

	@BeforeAll
	void init() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		serverTokenInput = this.getClass().getResource("/apple-mdm-client-tests-1-server-token.p7m").openStream();
		privateKeyInput = this.getClass().getResource("/apple-mdm-client-tests-1.der").openStream();
	}

	@Test
	void load_server_token_from_smime_envelop() throws Exception {
		var serverToken = DeviceAssignmentServerToken.create(serverTokenInput,
				DeviceAssignmentPrivateKey.createFromDER(privateKeyInput));

		assertEquals(
				"CK_75b2eb55cec9641e869a7d2c88b4bf1f7ffee504716571d191306385b8647ead9a4239ad6a2095ea0197feb46ce61399",
				serverToken.consumerKey());

		assertEquals("CS_810bb5ab05e53becb3bfbe40b3608a0c9039c457", serverToken.consumerSecret());

		assertEquals("AT_O8474885631Oe8ddece11c1a70077b340e281a2c4e2c4924a54cO1737111741803",
				serverToken.accessToken());

		assertEquals("AS_97b944021a19633782a36c2a85038a46d6acaa06", serverToken.accessSecret());

		assertEquals("2026-01-17T11:02:21Z", serverToken.accessTokenExpiry());
		assertEquals(OffsetDateTime.of(2026, 1, 17, 11, 2, 21, 0, ZoneOffset.UTC), serverToken.accessTokenExpiryTime());
	}
}
