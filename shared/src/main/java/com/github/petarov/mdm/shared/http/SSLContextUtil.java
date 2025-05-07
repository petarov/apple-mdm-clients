package com.github.petarov.mdm.shared.http;

import jakarta.annotation.Nonnull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

final class SSLContextUtil {

	public static final String DEFAULT_PROTOCOL = "TLS";

	/**
	 * @return new {@link SSLContext} TLS protocol instance that does not verify remote server certificates
	 */
	@Nonnull
	public static SSLContext newTrustAllSSLContext(@Nonnull SecureRandom secureRandom)
			throws NoSuchAlgorithmException, KeyManagementException {
		var ctx = SSLContext.getInstance(DEFAULT_PROTOCOL);
		ctx.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, secureRandom);
		return ctx;
	}

	static class TrustAllX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// ignore
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// ignore
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}
}
