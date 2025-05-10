package net.vexelon.mdm.shared.config;

import jakarta.annotation.Nonnull;

public class MdmClientProxyOptions {

	protected String hostname = "";
	protected int    port     = 8080;
	protected String username = "";
	protected String password = "";
	protected Type   type     = Type.HTTP;

	public enum Type {
		HTTP
	}

	@Nonnull
	public static MdmClientProxyOptions ofHttp(String hostname, int port, String username, String password) {
		var options = new MdmClientProxyOptions();
		options.hostname = hostname;
		options.port = port;
		options.username = username;
		options.password = password;
		options.type = Type.HTTP;
		return options;
	}

	@Nonnull
	public static MdmClientProxyOptions ofHttp(String hostname, int port) {
		return ofHttp(hostname, port, "", "");
	}

	private MdmClientProxyOptions() {
	}

	/**
	 * @return proxy server IP address or hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @return proxy port. Default is {@code 8080}
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return proxy username or blank
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return proxy password or blank
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return proxy server type. Only {@link Type#HTTP} is supported
	 */
	@Nonnull
	public Type getType() {
		return type;
	}
}
