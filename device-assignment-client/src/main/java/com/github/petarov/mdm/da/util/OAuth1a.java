package com.github.petarov.mdm.da.util;

import com.github.petarov.mdm.da.DeviceAssignmentServerToken;
import jakarta.annotation.Nonnull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * OAuth 1.0a signature generation.
 *
 * @see <a href="https://oauth.net/core/1.0a">OAuth Core 1.0 Revision A</a>
 */
public final class OAuth1a {

	private final DeviceAssignmentServerToken serverToken;

	public OAuth1a(@Nonnull DeviceAssignmentServerToken serverToken) {
		this.serverToken = serverToken;
	}

	/**
	 * @param random a {@code random} seed to generate the {@code oauth_nonce}
	 * @return a map of OAuth keys, i.e. {@code oauth_consumer_key}, {@code oauth_nonce},
	 * {@code oauth_signature_method}, {@code oauth_timestamp}, {@code oauth_token}, {@code oauth_version}
	 */
	public Map<String, String> getAuthParams(@Nonnull Random random) {
		byte[] randomBytes = new byte[16];
		random.nextBytes(randomBytes);
		String nonce = HexFormat.of().formatHex(randomBytes);
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);

		return Map.of("oauth_consumer_key", serverToken.consumerKey(), "oauth_nonce", nonce, "oauth_signature_method",
				"HMAC-SHA1", "oauth_timestamp", timestamp, "oauth_token", serverToken.accessToken(), "oauth_version",
				"1.0");
	}

	/**
	 * @param method  HTTP method
	 * @param baseUrl host address url
	 * @param params  a key-value set of OAuth parameters
	 * @return Base64-encoded signature given the input parameters
	 * @see <a href="https://oauth.net/core/1.0a/#signing_process">OAuth 1.0a - Signing Requests</a>
	 */
	public String generateSignature(String method, String baseUrl, @Nonnull Map<String, String> params) {
		var queryParams = new StringBuilder();
		for (var entry : new TreeMap<>(params).entrySet()) {
			queryParams.append(percentEncode(entry.getKey())).append("=").append(percentEncode(entry.getValue()))
					.append("&");
		}
		queryParams.setLength(queryParams.length() - 1);

		String baseString =
				method.toUpperCase() + "&" + percentEncode(baseUrl) + "&" + percentEncode(queryParams.toString());
		String signingKey =
				percentEncode(serverToken.consumerSecret()) + "&" + percentEncode(serverToken.accessSecret());

		return percentEncode(Base64.getEncoder().encodeToString(
				signWithHMacSHA1(signingKey.getBytes(StandardCharsets.UTF_8),
						baseString.getBytes(StandardCharsets.UTF_8))));
	}

	public static byte[] signWithHMacSHA1(byte[] key, byte[] data) {
		try {
			var mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(key, "HmacSHA1"));
			return mac.doFinal(data);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new IllegalStateException("Error calculate HMAC-SHA1", e);
		}
	}

	private static String percentEncode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20").replace("*", "%2A")
				.replace("%7E", "~");
	}
}
