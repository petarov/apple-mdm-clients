package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;

/**
 * Information about the MDM server, as set through the client configuration.
 *
 * @param id       the unique identifier of the MDM server
 * @param metadata additional information about the MDM server, such as its version
 * @param name     the name of the MDM server
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/mdminfo">devicemanagement/mdminfo</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MdmInfo(@Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) String id,
                      @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) String metadata,
                      @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) String name) {}
