package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.da.DeviceAssignmentClient;

/**
 * @see DeviceAssignmentClient#enableActivationLock(String, String, String)
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ActivationLockRequest(String device, String escrowKey, String lostMessage) {}
