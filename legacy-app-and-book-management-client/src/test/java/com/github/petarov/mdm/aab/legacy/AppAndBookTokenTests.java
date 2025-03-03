package com.github.petarov.mdm.aab.legacy;

import com.github.petarov.mdm.aab.legacy.config.AppAndBookToken;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.InputStream;
import java.security.Security;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppAndBookTokenTests {

	private InputStream sTokenInput;

	@BeforeAll
	void init() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		sTokenInput = this.getClass().getResourceAsStream("/apple-mdm-client-tests-stoken-1.stoken");
	}

	@Test
	void load_stoken_from_file() throws Exception {
		var serverToken = AppAndBookToken.create(sTokenInput);

		Assertions.assertEquals(
				"eyJ0b2tlbiI6IlZHaHBjeUJwY3lCaElITmhiWEJzWlNCMFpYaDBJSGRvYVdOb0lIZGhjeUIxYzJWa0lIUnZJR055WldGMFpTQjBhR1VnYzJsdGRXeGhkRzl5SUhSdmEyVnVDZz09IiwiZXhwRGF0ZSI6IjIwMjAtMTItMzFUMTM6NTc6MTkrMDI6MDAiLCJvcmdOYW1lIjoiYmxhYmxhIEdtYkgifQ==",
				serverToken.sToken());
	}
}
