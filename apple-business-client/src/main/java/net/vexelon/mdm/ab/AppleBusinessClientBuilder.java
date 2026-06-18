package net.vexelon.mdm.ab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.config.MdmClientBuilder;
import net.vexelon.mdm.shared.http.HttpClientWrapper;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Objects;

public class AppleBusinessClientBuilder extends MdmClientBuilder<AppleBusinessClientBuilder, AppleBusinessClient> {

	private static final String DEFAULT_APPLE_OAUTH_URL       = "https://account.apple.com/auth/oauth2/token";
	private static final String DEFAULT_APPLE_PRODUCTION_HOST = "https://api-business.apple.com/v1";

	public static final Duration MIN_TOKEN_VALIDITY     = Duration.ofHours(1);
	/**
	 * Maximum allowed JWT client-assertion validity per Apple's documentation (180 days).
	 */
	public static final Duration MAX_TOKEN_VALIDITY     = Duration.ofDays(180);
	public static final Duration DEFAULT_TOKEN_VALIDITY = Duration.ofHours(2);


	private String                  clientId;
	private String                  keyId;
	private String                  teamId;
	private AppleBusinessPrivateKey privateKey;
	private String                  appleAuthUrl;
	private Duration                tokenValidity = DEFAULT_TOKEN_VALIDITY;

	AppleBusinessClientBuilder() {
	}

	/**
	 * Sets an Apple host other than the default {@link AppleBusinessClientBuilder#DEFAULT_APPLE_PRODUCTION_HOST}.
	 */
	@Nonnull
	public AppleBusinessClientBuilder setAppleServiceUrl(String serviceUrl) {
		return this.setServiceUrl(serviceUrl);
	}

	/**
	 * Sets the OAuth authorization server URL. Defaults to {@link AppleBusinessClientBuilder#DEFAULT_APPLE_OAUTH_URL}.
	 */
	@Nonnull
	public AppleBusinessClientBuilder setAppleAuthUrl(String authUrl) {
		this.appleAuthUrl = authUrl;
		return this;
	}

	/**
	 * Sets the {@code clientId} returned when uploading a public key in Apple Business Manager.
	 * Used as the {@code sub} and {@code client_id} in the OAuth client-assertion flow.
	 */
	@Nonnull
	public AppleBusinessClientBuilder setClientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	/**
	 * Sets the key ID returned when uploading a public key in Apple Business Manager.
	 * Used as the {@code kid} header in the JWT client assertion.
	 */
	@Nonnull
	public AppleBusinessClientBuilder setKeyId(String keyId) {
		this.keyId = keyId;
		return this;
	}

	/**
	 * Sets the team ID used as the JWT {@code iss} claim. When not set, defaults to {@code clientId}.
	 */
	@Nonnull
	public AppleBusinessClientBuilder setTeamId(String teamId) {
		this.teamId = teamId;
		return this;
	}

	/**
	 * Sets the EC private key used to sign the JWT client assertion (ES256).
	 */
	@Nonnull
	public AppleBusinessClientBuilder setPrivateKey(@Nonnull AppleBusinessPrivateKey privateKey) {
		this.privateKey = privateKey;
		return this;
	}

	/**
	 * Sets the JWT client-assertion validity window.
	 * <p>
	 * Must be between {@link #MIN_TOKEN_VALIDITY} (1 hour) and {@link #MAX_TOKEN_VALIDITY} (180 days) inclusive.
	 * Defaults to {@link #DEFAULT_TOKEN_VALIDITY} (2 hours).
	 */
	@Nonnull
	public AppleBusinessClientBuilder setTokenValidity(@Nonnull Duration duration) {
		Objects.requireNonNull(duration, "tokenValidity must not be null");
		if (duration.compareTo(MIN_TOKEN_VALIDITY) < 0 || duration.compareTo(MAX_TOKEN_VALIDITY) > 0) {
			throw new IllegalArgumentException(
					"tokenValidity must be between " + MIN_TOKEN_VALIDITY + " and " + MAX_TOKEN_VALIDITY);
		}
		this.tokenValidity = duration;
		return this;
	}

	@Nonnull
	@Override
	public AppleBusinessClient build() {
		Objects.requireNonNull(clientId, "clientId is required");
		Objects.requireNonNull(keyId, "keyId is required");
		Objects.requireNonNull(privateKey, "privateKey is required");

		setServiceUrl(Objects.requireNonNullElse(serviceUrl, DEFAULT_APPLE_PRODUCTION_HOST));
		setUserAgent(Objects.requireNonNullElse(userAgent, DEFAULT_USER_AGENT));
		setConnectTimeout(Objects.requireNonNullElse(connectTimeout, DEFAULT_CONNECT_TIMEOUT));
		setReadTimeout(Objects.requireNonNullElse(readTimeout, DEFAULT_READ_TIMEOUT));
		setRandom(Objects.requireNonNullElseGet(secureRandom, SecureRandom::new));

		appleAuthUrl = Objects.requireNonNullElse(appleAuthUrl, DEFAULT_APPLE_OAUTH_URL);
		teamId = Objects.requireNonNullElse(teamId, clientId);

		var client = new HttpClientWrapper(
				new MdmBuilderOptions(serviceUrl, userAgent, skipSslVerify, connectTimeout, readTimeout, proxyOptions,
						secureRandom), AppleBusinessClientImpl.logger);
		var auth = new AppleBusinessClientAuth(client, clientId, keyId, teamId, privateKey, appleAuthUrl,
				tokenValidity);
		return new AppleBusinessClientImpl(client, auth);
	}
}
