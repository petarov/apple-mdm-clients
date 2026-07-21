package net.vexelon.mdm.ab.model.devices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;

/**
 * The data structure that represents an AppleCare coverage resource for an organization device.
 *
 * @param id         the opaque resource ID that uniquely identifies the resource
 * @param type       the resource type; value: {@code appleCareCoverage}
 * @param attributes the resource's attributes
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/applecarecoverage">AppleCareCoverage</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AppleCareCoverage(@JsonSetter(nulls = Nulls.AS_EMPTY) String id,
                                @JsonSetter(nulls = Nulls.AS_EMPTY) String type,
                                @Nonnull AppleCareCoverageAttributes attributes) {}
