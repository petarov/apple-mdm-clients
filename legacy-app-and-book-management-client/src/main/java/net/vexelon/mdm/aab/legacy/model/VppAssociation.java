package net.vexelon.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

/**
 * An association between a license and a user or device.
 *
 * @param clientUserIdStr the client-supplied identifier used when registering a user
 * @param errorMessage    the human-readable explanation of the error
 * @param errorNumber     the numeric code of the error
 * @param licenseIdStr    the license identifier assigned to a user or device
 * @param serialNumber    the device serial number
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppassociation">VppAssociation</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record VppAssociation(@JsonSetter(nulls = Nulls.AS_EMPTY) String clientUserIdStr,
                             @JsonSetter(nulls = Nulls.AS_EMPTY) String errorMessage, int errorNumber,
                             @JsonSetter(nulls = Nulls.AS_EMPTY) String licenseIdStr,
                             @JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber) {

	@Nonnull
	public static VppAssociation ofEmpty() {
		return new VppAssociation("", "", 0, "", "");
	}
}
