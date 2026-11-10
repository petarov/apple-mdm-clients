package net.vexelon.mdm.sls;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SoftwareLookupServiceClientImpl implements SoftwareLookupServiceClient {

	static final Logger logger = LoggerFactory.getLogger(SoftwareLookupServiceClientImpl.class);

	private final HttpClientWrapper client;

	SoftwareLookupServiceClientImpl(@Nonnull HttpClientWrapper client) {
		this.client = client;
	}
}
