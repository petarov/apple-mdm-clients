package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.util.Set;

/**
 * @param profileUuid unique identifier for a profile
 * @param devices     the serial numbers of the devices that will be disowned
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/profileservicerequest">ProfileServiceRequest</a>
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/clearprofilerequest">ClearProfileRequest</a>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProfileDevicesRequest(String profileUuid, @Nonnull Set<String> devices) {}

