package com.github.petarov.mdm.da;

import com.github.petarov.mdm.da.model.AccountDetail;
import com.github.petarov.mdm.da.model.DevicesResponse;
import jakarta.annotation.Nonnull;

public interface DeviceAssignmentClient {

	@Nonnull
	AccountDetail fetchAccount();

	@Nonnull
	DevicesResponse fetchDevices(String cursor, int limit);
}
