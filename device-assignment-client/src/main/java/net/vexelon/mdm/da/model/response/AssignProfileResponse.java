package net.vexelon.mdm.da.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.da.DeviceAssignmentClient;

import java.util.Map;

/**
 * @param devices           a dictionary of devices. See {@link DeviceStatusResponse#devices()}. With
 *                          <i>X-Server-Protocol-Version 9</i> and later, the server may throttle profile assignment
 *                          on a per-device basis, in which case a device's value is
 *                          {@value DeviceAssignmentClient#DEVICE_RESPONSE_STATUS_THROTTLED} instead of
 *                          {@value DeviceAssignmentClient#DEVICE_RESPONSE_STATUS_SUCCESS}.
 * @param profileUuid       the unique identifier for a profile
 * @param retryAfterSeconds with <i>X-Server-Protocol-Version 10</i> and later, present and non-zero when at least one
 *                          device in {@link #devices()} was throttled. Clients should wait at least this many
 *                          seconds before retrying assignment for the throttled devices.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/assignprofileresponse">devicemanagement/assignprofileresponse</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AssignProfileResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull Map<String, String> devices,
                                    @JsonSetter(nulls = Nulls.AS_EMPTY) String profileUuid, int retryAfterSeconds) {}
