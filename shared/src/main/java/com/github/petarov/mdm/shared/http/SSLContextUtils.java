package com.github.petarov.mdm.shared.http;

import jakarta.annotation.Nonnull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;

class SSLContextUtils {

	public static final String DEFAULT_PROTOCOL = "TLS";

	/**
	 * @return new {@link SSLContext} TLS protocol instance that does not verify remote server certificates
	 */
	public static SSLContext newTrustAllSSLContext(@Nonnull SecureRandom secureRandom) throws Exception {
		var ctx = SSLContext.getInstance(DEFAULT_PROTOCOL);
		ctx.init(null, new TrustManager[] { new TrustAllManager() }, secureRandom);
		return ctx;
	}
}
