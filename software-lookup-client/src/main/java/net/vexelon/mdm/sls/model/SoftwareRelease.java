package net.vexelon.mdm.sls.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.List;

/**
 * A software release that Apple publishes for a platform.
 *
 * @param productVersion      the version of the release, such as {@code 26.3.1}
 * @param build               the build version of the release
 * @param productVersionExtra the extra version suffix, such as {@code (a)}. Only set for Background Security
 *                            Improvements, otherwise {@code ""}
 * @param prerequisiteBuild   the build that must be installed before this release. Only set for Background Security
 *                            Improvements, otherwise {@code ""}
 * @param postingDate         the date Apple posted the release, as {@code yyyy-MM-dd}
 * @param expirationDate      the date the release expires, as {@code yyyy-MM-dd}, or {@code ""} if it has not
 *                            expired yet
 * @param supportedDevices    the product types or board ids of the devices that support the release
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SoftwareRelease(@JsonProperty("ProductVersion") @JsonSetter(nulls = Nulls.AS_EMPTY) String productVersion,
                              @JsonProperty("Build") @JsonSetter(nulls = Nulls.AS_EMPTY) String build,
                              @JsonProperty("ProductVersionExtra") @JsonSetter(
		                              nulls = Nulls.AS_EMPTY) String productVersionExtra,
                              @JsonProperty("PrerequisiteBuild") @JsonSetter(
		                              nulls = Nulls.AS_EMPTY) String prerequisiteBuild,
                              @JsonProperty("PostingDate") @JsonSetter(nulls = Nulls.AS_EMPTY) String postingDate,
                              @JsonProperty("ExpirationDate") @JsonSetter(nulls = Nulls.AS_EMPTY) String expirationDate,
                              @JsonProperty("SupportedDevices") @JsonSetter(nulls = Nulls.AS_EMPTY,
		                              contentNulls = Nulls.SKIP) List<String> supportedDevices) {}
