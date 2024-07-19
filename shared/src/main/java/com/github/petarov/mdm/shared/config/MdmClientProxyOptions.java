package com.github.petarov.mdm.shared.config;

import jakarta.annotation.Nonnull;

public abstract class MdmClientProxyOptions {

	protected String hostname = "";
	protected int    port     = 8080;
	protected String username = "";
	protected String password = "";
	protected Type   type     = Type.HTTP;

	public enum Type {
		HTTP
	}

	/**
	 * @param hostname proxy server IP address or hostname
	 */
	@Nonnull
	public MdmClientProxyOptions setHostname(@Nonnull String hostname) {
		this.hostname = hostname;
		return this;
	}

	@Nonnull
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param port proxy port. Default is {@code 8080}
	 */
	@Nonnull
	public MdmClientProxyOptions setPort(int port) {
		this.port = port;
		return this;
	}

	public int getPort() {
		return port;
	}

	@Nonnull
	public MdmClientProxyOptions setUsername(@Nonnull String username) {
		this.username = username;
		return this;
	}

	@Nonnull
	public String getUsername() {
		return username;
	}

	@Nonnull
	public MdmClientProxyOptions setPassword(@Nonnull String password) {
		this.password = password;
		return this;
	}

	@Nonnull
	public String getPassword() {
		return password;
	}

	/**
	 * @param type proxy server type. Only {@link Type#HTTP} is supported
	 */
	@Nonnull
	public MdmClientProxyOptions setType(@Nonnull Type type) {
		this.type = type;
		return this;
	}

	@Nonnull
	public Type getType() {
		return type;
	}
}
