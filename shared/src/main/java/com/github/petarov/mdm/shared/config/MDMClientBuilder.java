package com.github.petarov.mdm.shared.config;

import jakarta.annotation.Nonnull;

import java.time.Duration;

public abstract class MDMClientBuilder<CFG extends MDMClientBuilder<CFG, CLIENT>, CLIENT> {

	protected String                serviceUrl;
	protected String                userAgent;
	protected boolean               skipSslVerify = false;
	protected Duration              connectTimeout;
	protected Duration              readTimeout;
	protected MDMClientProxyOptions proxyOptions;

	@SuppressWarnings("unchecked")
	public CFG setServiceUrl(@Nonnull String serviceUrl) {
		this.serviceUrl = serviceUrl;
		return (CFG) this;
	}

	@SuppressWarnings("unchecked")
	public CFG setUserAgent(@Nonnull String userAgent) {
		this.userAgent = userAgent;
		return (CFG) this;
	}

	/**
	 * @param skipSslVerify if {@code true}, it skips SSL certificate verification. Default is {@code false}
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
	public CFG setProxyOptions(@Nonnull MDMClientProxyOptions proxyOptions) {
		this.proxyOptions = proxyOptions;
		return (CFG) this;
	}

	@Nonnull
	public abstract CLIENT build();
}