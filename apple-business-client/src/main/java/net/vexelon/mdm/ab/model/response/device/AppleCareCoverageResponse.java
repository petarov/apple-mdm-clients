package net.vexelon.mdm.ab.model.response.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.ab.model.PagingInformation;
import net.vexelon.mdm.ab.model.devices.AppleCareCoverage;

import java.util.List;

/**
 * A response that contains a list of AppleCare coverage resources for an organization device.
 *
 * @param data the resource data
 * @param meta paging information for this response
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/applecarecoverageresponse">AppleCareCoverageResponse</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AppleCareCoverageResponse(@Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) List<AppleCareCoverage> data,
                                        @Nonnull PagingInformation meta) {}
