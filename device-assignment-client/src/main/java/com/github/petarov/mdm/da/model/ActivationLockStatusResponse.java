package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @param responseStatus possible values:<ul>
 *                       <li>{@code SUCCESS}: The device was successfully locked.
 *                       <li>{@code NOT_ACCESSIBLE}: The device with this serial number is not accessible by this user.
 *                       <li>{@code ORG_NOT_SUPPORTED}: The device with this serial number is not supported because it is not present in the new program.
 *                       <li>{@code DEVICE_NOT_SUPPORTED}: The device type is not supported.
 *                       <li>{@code DEVICE_ALREADY_LOCKED}: The device is already locked by someone.
 *                       <li>{@code FAILED}: Activation lock of the device failed for unexpected reason. If retry fails, the client should contact Apple support.
 *                       </ul>
 * @param serialNumber   serial number of the device
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/activationlockstatusresponse">ActivationLockStatusResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ActivationLockStatusResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String responseStatus,
                                           @JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber) {}
