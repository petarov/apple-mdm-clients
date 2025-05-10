package com.github.petarov.mdm.shared.config;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.security.SecureRandom;
import java.time.Duration;

public abstract class MdmClientBuilder<T extends MdmClientBuilder<T, U>, U> {

	protected static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(10);
	protected static final Duration DEFAULT_READ_TIMEOUT    = Duration.ofSeconds(20);
	protected static final String DEFAULT_USER_AGENT      = "apple-mdm-clients-v1";

	protected String                serviceUrl;
	protected String                userAgent;
	protected boolean               skipSslVerify = false;
	protected Duration              connectTimeout;
	protected Duration              readTimeout;
	protected MdmClientProxyOptions proxyOptions;
	protected SecureRandom          secureRandom;

	public record MdmBuilderOptions(String serviceUrl, String userAgent, boolean isSkipSslVerify,
	                                Duration connectTimeout, Duration readTimeout,
	                                @Nullable MdmClientProxyOptions proxyOptions, SecureRandom random) {}

	@SuppressWarnings("unchecked")
	protected T setServiceUrl(@Nonnull String serviceUrl) {
		this.serviceUrl = serviceUrl;
		return (T) this;
	}

	/**
	 * Sets the user-agent to use when sending requests to Apple's servers. Setting a user-agent is recommended.
	 */
	@SuppressWarnings("unchecked")
	public T setUserAgent(@Nonnull String userAgent) {
		this.userAgent = userAgent;
		return (T) this;
	}

	/**
	 * @param skipSslVerify if {@code true}, it skips SSL certificate verification. Default is {@code false}.
	 */
	@SuppressWarnings("unchecked")
	public T setSkipSslVerify(boolean skipSslVerify) {
		this.skipSslVerify = skipSslVerify;
		return (T) this;
	}

	/**
	 * @param connectTimeout TCP socket connect timeout. Not set by default
	 */
	@SuppressWarnings("unchecked")
	public T setConnectTimeout(@Nonnull Duration connectTimeout) {
		this.connectTimeout = connectTimeout;
		return (T) this;
	}

	/**
	 * @param readTimeout TCP socket read timeout. Not set by default
	 */
	@SuppressWarnings("unchecked")
	public T setReadTimeout(@Nonnull Duration readTimeout) {
		this.readTimeout = readTimeout;
		return (T) this;
	}

	/**
	 * @param proxyOptions optional proxy settings
	 */
	@SuppressWarnings("unchecked")
	public T setProxyOptions(@Nonnull MdmClientProxyOptions proxyOptions) {
		this.proxyOptions = proxyOptions;
		return (T) this;
	}

	/**
	 * @param random set a custom random generator, uses {@link SecureRandom} by default
	 */
	@SuppressWarnings("unchecked")
	public T setRandom(@Nonnull SecureRandom random) {
		this.secureRandom = random;
		return (T) this;
	}

	@Nonnull
	public abstract U build();
}