package net.vexelon.mdm.da.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * @param devices     a dictionary of devices. See {@link DeviceStatusResponse#devices()}.
 * @param profileUuid the unique identifier for a profile
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/defineprofileresponse">DefineProfileResponse</a>
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/assignprofileresponse">AssignProfileResponse</a>
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/clearprofileresponse">ClearProfileResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProfileDevicesResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull Map<String, String> devices,
                                     @JsonSetter(nulls = Nulls.AS_EMPTY) String profileUuid) {}
