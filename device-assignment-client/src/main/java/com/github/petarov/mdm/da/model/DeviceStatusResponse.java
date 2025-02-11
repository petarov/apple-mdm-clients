package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * @param devices a dictionary of devices. Each key in this dictionary is the serial number of a device in the
 *                original request. Each value is one of the following values:<ul>
 *                <li>{@code SUCCESS}: Device was successfully disowned or the profile was mapped to the device.
 *                <li>{@code NOT_ACCESSIBLE}: A device with the specified ID was not accessible by this MDM server.
 *                <li>{@code FAILED}: Either of:
 *                - Disowning the device failed for an unexpected reason. If three retries fail, the user should contact
 *                Apple support.
 *                - Assigning the profile failed for an unexpected reason. If three retries fail, the user should
 *                contact Apple support.
 *                </ul>
 *                If no devices were provided in the original request, this dictionary may be absent.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/devicestatusresponse">DeviceStatusResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DeviceStatusResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull Map<String, String> devices) {}
