package net.vexelon.mdm.aab.legacy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

/**
 * An assignment’s properties and their values.
 *
 * @param adamIdStr       the unique identifier for a product in the iTunes Store
 * @param clientUserIdStr the client user ID of the user that the device is currently assigned to
 * @param pricingParam    the quality of a product in the iTunes Store. Possible values are:<ul>
 *                        <li>{@code STDQ}: Standard quality
 *                        <li>{@code PLUS}: High quality
 *                        </ul>
 * @param serialNumber    the device's serial number that the license is currently assigned to
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppassignment">VppAssignment</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record VppAssignment(@JsonSetter(nulls = Nulls.AS_EMPTY) String adamIdStr,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String clientUserIdStr,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String pricingParam,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber) {

	@Nonnull
	public static VppAssignment ofEmpty() {
		return new VppAssignment("", "", "", "");
	}
}
