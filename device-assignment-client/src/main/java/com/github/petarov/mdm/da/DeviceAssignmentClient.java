package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.config.DeviceAssignmentServerToken;
import com.github.petarov.mdm.shared.http.MdmHttpClient;
import jakarta.annotation.Nonnull;

public class DeviceAssignmentClient {

	private final MdmHttpClient               client;
	private final DeviceAssignmentServerToken serverToken;

	public DeviceAssignmentClient(@Nonnull MdmHttpClient client, @Nonnull DeviceAssignmentServerToken serverToken) {
		this.client = client;
		this.serverToken = serverToken;
	}
}
