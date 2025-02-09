package com.github.petarov.mdm.shared.config;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.security.SecureRandom;
import java.time.Duration;

public abstract class MdmClientBuilder<CFG extends MdmClientBuilder<CFG, CLIENT>, CLIENT> {

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
	protected CFG setServiceUrl(@Nonnull String serviceUrl) {
		this.serviceUrl = serviceUrl;
		return (CFG) this;
	}

	/**
	 * Sets the user-agent to use when sending requests to Apple's servers. Setting a user-agent is recommended.
	 */
	@SuppressWarnings("unchecked")
	public CFG setUserAgent(@Nonnull String userAgent) {
		this.userAgent = userAgent;
		return (CFG) this;
	}

	/**
	 * @param skipSslVerify if {@code true}, it skips SSL certificate verification. Default is {@code false}.
	 */
	@SuppressWarnings("unchecked")
	public CFG setSkipSslVerify(boolean skipSslVerify) {
		this.skipSslVerify = skipSslVerify;
		return (CFG) this;
	}

	/**
	 * @param connectTimeout TCP socket connect timeout. Not set by default
	 */
	@SuppressWarnings("unchecked")
	public CFG setConnectTimeout(@Nonnull Duration connectTimeout) {
		this.connectTimeout = connectTimeout;
		return (CFG) this;
	}

	/**
	 * @param readTimeout TCP socket read timeout. Not set by default
	 */
	@SuppressWarnings("unchecked")
	public CFG setReadTimeout(@Nonnull Duration readTimeout) {
		this.readTimeout = readTimeout;
		return (CFG) this;
	}

	/**
	 * @param proxyOptions optional proxy settings
	 */
	@SuppressWarnings("unchecked")
	public CFG setProxyOptions(@Nonnull MdmClientProxyOptions proxyOptions) {
		this.proxyOptions = proxyOptions;
		return (CFG) this;
	}

	/**
	 * @param random set a custom random generator, uses {@link SecureRandom} by default
	 */
	@SuppressWarnings("unchecked")
	public CFG setRandom(@Nonnull SecureRandom random) {
		this.secureRandom = random;
		return (CFG) this;
	}

	@Nonnull
	public abstract CLIENT build();
}