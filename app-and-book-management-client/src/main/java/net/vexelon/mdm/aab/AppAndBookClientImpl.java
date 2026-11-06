package net.vexelon.mdm.aab;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AppAndBookClientImpl implements AppAndBookClient {

	static final Logger logger = LoggerFactory.getLogger(AppAndBookClientImpl.class);

	private final HttpClientWrapper client;
	private final AppAndBookToken   serverToken;

	AppAndBookClientImpl(@Nonnull HttpClientWrapper client, @Nonnull AppAndBookToken serverToken) {
		this.client = client;
		this.serverToken = serverToken;
	}
}
