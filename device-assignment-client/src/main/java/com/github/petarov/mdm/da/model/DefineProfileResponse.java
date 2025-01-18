package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * @param devices     a dictionary of devices. See {@link DeviceStatusListResponse#devices()}.
 * @param profileUuid the unique identifier for a profile
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/defineprofileresponse">DefineProfileResponse</a>
 */

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DefineProfileResponse(@Nonnull Map<String, String> devices, String profileUuid) {}
