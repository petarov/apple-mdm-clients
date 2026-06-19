package net.vexelon.mdm.ab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.response.AccessTokenResponse;
import net.vexelon.mdm.ab.util.OAuth2;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import net.vexelon.mdm.shared.http.HttpConsts;
import net.vexelon.mdm.shared.util.JsonUtil;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Owns the OAuth 2.0 token lifecycle for the Apple Business Manager API: JWT client-assertion
 * minting, token exchange, and bearer-token caching.
 *
 * <p>Token parameters are POSTed as URL query parameters as documented by Apple (the
 * {@code Content-Type: application/x-www-form-urlencoded} header is still required).
 */
class AppleBusinessClientAuth {

	private static final String GRANT_TYPE            = "client_credentials";
	private static final String CLIENT_ASSERTION_TYPE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
	private static final String SCOPE                 = "business.api";

	private final HttpClientWrapper       client;
	private final String                  clientId;
	private final String                  keyId;
	private final String                  teamId;
	private final AppleBusinessPrivateKey privateKey;
	private final String                  authUrl;
	private final Duration                tokenValidity;

	private String accessToken;

	AppleBusinessClientAuth(@Nonnull HttpClientWrapper client, @Nonnull String clientId, @Nonnull String keyId,
			@Nonnull String teamId, @Nonnull AppleBusinessPrivateKey privateKey, @Nonnull String authUrl,
			@Nonnull Duration tokenValidity) {
		this.client = client;
		this.clientId = clientId;
		this.keyId = keyId;
		this.teamId = teamId;
		this.privateKey = privateKey;
		this.authUrl = authUrl;
		this.tokenValidity = tokenValidity;
	}

	/**
	 * Creates new client assertion, exchanges it for a new bearer token, caches it, and returns it.
	 */
	@Nonnull
	String refreshAccessToken() {
		var oauth = new OAuth2(clientId, keyId, teamId, privateKey.privateKey(), JsonUtil.createObjectMapper(),
				tokenValidity);
		var assertion = oauth.createClientAssertion();

		var query = "grant_type=" + GRANT_TYPE + "&client_id=" + encode(clientId) + "&client_assertion_type=" + encode(
				CLIENT_ASSERTION_TYPE) + "&client_assertion=" + encode(assertion) + "&scope=" + SCOPE;

		var request = client.createRequestBuilder(authUrl + "?" + query).POST(HttpRequest.BodyPublishers.noBody())
				.setHeader(HttpConsts.HEADER_CONTENT_TYPE, HttpConsts.HEADER_VALUE_APPLICATION_FORM_URL_ENCODED)
				.build();

		var response = client.send(request, AccessTokenResponse.class);
		accessToken = response.accessToken();
		return accessToken;
	}

	/**
	 * Returns the cached access token, obtaining a new one if none is cached yet.
	 */
	@Nonnull
	String getAccessToken() {
		if (accessToken == null) {
			refreshAccessToken();
		}
		return accessToken;
	}

	private static String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}
}
