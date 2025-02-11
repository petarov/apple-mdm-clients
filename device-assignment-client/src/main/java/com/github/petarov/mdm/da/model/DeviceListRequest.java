package com.github.petarov.mdm.da.model;


import jakarta.annotation.Nonnull;

import java.util.Set;

/**
 * @param devices the serial numbers of the devices that will be fetched
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/devicelistrequest">DeviceListRequest</a>
 */
public record DeviceListRequest(@Nonnull Set<String> devices) {}
