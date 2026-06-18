package net.vexelon.mdm.ab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nonnull;

/**
 * The {@code meta} block of an Apple Business API list response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrgDevicesMeta(@Nonnull OrgDevicesPaging paging) {}
