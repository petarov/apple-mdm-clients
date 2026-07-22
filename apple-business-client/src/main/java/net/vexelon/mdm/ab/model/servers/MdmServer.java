package net.vexelon.mdm.ab.model.servers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;

/**
 * The data structure that represents a device management service resource in an organization.
 *
 * @param id         the opaque resource ID that uniquely identifies the resource
 * @param type       the resource type; value: {@code mdmServers}
 * @param attributes the resource's attributes
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/mdmserver">applebusinessapi/mdmserver</a>
 * @since Apple Business API 2.2+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MdmServer(@JsonSetter(nulls = Nulls.AS_EMPTY) String id, @JsonSetter(nulls = Nulls.AS_EMPTY) String type,
                        @Nonnull MdmServerAttributes attributes) {}
