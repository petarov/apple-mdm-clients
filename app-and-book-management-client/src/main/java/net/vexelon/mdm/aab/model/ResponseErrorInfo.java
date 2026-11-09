package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.List;

/**
 * The request-specific information about a failure.
 *
 * @param assets        the requested assets that resulted in the error
 * @param clientUserIds the requested users that resulted in the error
 * @param serialNumbers the requested devices that resulted in the error
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/responseerrorinfo">devicemanagement/responseerrorinfo</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponseErrorInfo(@JsonSetter(nulls = Nulls.AS_EMPTY) List<ResponseErrorAsset> assets,
                                @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> clientUserIds,
                                @JsonSetter(nulls = Nulls.AS_EMPTY) List<String> serialNumbers) {}
