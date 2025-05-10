package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.util.Map;

/**
 * @param devices a dictionary of devices. Each key in this dictionary is the serial number of a device in the
 *                original request. Each value is a {@link Device} with the {@link Device#responseStatus()} filled in.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/devicelistresponse">DeviceListResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DeviceListResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull Map<String, Device> devices) {}
