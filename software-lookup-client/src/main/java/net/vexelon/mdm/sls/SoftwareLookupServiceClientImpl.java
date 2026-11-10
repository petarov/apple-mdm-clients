package net.vexelon.mdm.sls;

import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.http.HttpClientWrapper;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import net.vexelon.mdm.sls.model.response.SoftwareLookupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SoftwareLookupServiceClientImpl implements SoftwareLookupServiceClient {

	static final Logger logger = LoggerFactory.getLogger(SoftwareLookupServiceClientImpl.class);

	private final HttpClientWrapper client;

	SoftwareLookupServiceClientImpl(@Nonnull HttpClientWrapper client) {
		this.client = client;
	}

	@Nonnull
	@Override
	public SoftwareLookupResponse fetchSoftwareReleases() {
		SoftwareLookupResponse response;
		try {
			response = client.send(client.createRequestBuilder(client.complementURI("/pmv")).GET().build(),
					SoftwareLookupResponse.class);
		} catch (HttpClientWrapperException e) {
			throw new SoftwareLookupServiceClientException(e);
		}
		if (response == null) {
			throw new SoftwareLookupServiceClientException("Empty software lookup response", null);
		}
		return response;
	}
}
