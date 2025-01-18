package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/profile">Profile</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
// TODO
public record Profile(@Nonnull List<String> anchorCerts) {}
