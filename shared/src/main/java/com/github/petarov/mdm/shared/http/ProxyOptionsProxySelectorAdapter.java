package com.github.petarov.mdm.shared.http;

import com.github.petarov.mdm.shared.config.MdmClientProxyOptions;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.net.*;
import java.util.List;

final class ProxyOptionsProxySelectorAdapter extends ProxySelector {

	private final MdmClientProxyOptions options;
	private       List<Proxy>           proxies;

	public ProxyOptionsProxySelectorAdapter(@Nonnull MdmClientProxyOptions options) {
		this.options = options;
	}

	@Override
	public List<Proxy> select(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI can't be null");
		}

		if (proxies == null) {
			if (options.getType() == MdmClientProxyOptions.Type.HTTP) {
				proxies = List.of(
						new Proxy(Proxy.Type.HTTP, new InetSocketAddress(options.getHostname(), options.getPort())));
			} else {
				throw new IllegalArgumentException(
						"Error in proxy type: " + options.getType() + " is not a supported proxy type");
			}
		}

		return proxies;
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ex) {
		if (uri == null || sa == null || ex == null) {
			throw new IllegalArgumentException("Arguments can't be null");
		}

		throw new RuntimeException("Error connecting to " + uri + " using proxy server", ex);
	}
}