package net.vexelon.mdm.da.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;

/**
 * Information about a replacement device, including the original device it replaces and the date the replacement
 * occurred.
 *
 * @param serialNumber               the serial number of the replacement device
 * @param originalDeviceSerialNumber the serial number of the original device that this device replaces
 * @param replacementDate            the date when the device replacement occurred, in ISO 8601 format with day
 *                                   granularity in UTC (for example, {@code 2025-01-15})
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getreplacementdetailsresponse">devicemanagement/getreplacementdetailsresponse</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GetReplacementDetailsResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber,
                                            @JsonSetter(nulls = Nulls.AS_EMPTY) String originalDeviceSerialNumber,
                                            @JsonSetter(nulls = Nulls.AS_EMPTY) String replacementDate) {

	/**
	 * @return parsed {@link #replacementDate()} or {@link LocalDate#MIN} if no date is available
	 */
	@Nonnull
	public LocalDate replacementLocalDate() {
		return replacementDate.isBlank() ? LocalDate.MIN : LocalDate.parse(replacementDate);
	}
}
