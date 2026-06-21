package net.vexelon.mdm.ab.model.devices;

import com.fasterxml.jackson.annotation.*;
import jakarta.annotation.Nonnull;

/**
 * Attributes for an organization device's AppleCare coverage resource.
 *
 * <p>All date-time fields use ISO 8601 format (e.g. {@code 2025-04-30T22:05:14.192Z}).
 * All nullable string fields default to empty string when absent or {@code null} in the response.
 *
 * @param status                 the current status of device coverage: defaults to
 *                               {@link AppleCareCoverageAttributes.Status#UNKNOWN} when absent or unrecognized
 * @param paymentType            the payment type of device coverage: defaults to
 *                               {@link AppleCareCoverageAttributes.PaymentType#UNKNOWN} when absent or unrecognized
 * @param description            description of device coverage
 * @param agreementNumber        agreement number associated with device coverage; not applicable
 *                               for Limited Warranty and AppleCare+ for Business
 * @param startDateTime          UTC date when coverage period commenced; for AppleCare+ for
 *                               Business, the date when a device enrolls into the plan
 * @param endDateTime            UTC date when coverage period ends for the device; not applicable
 *                               for AppleCare+ for Business
 * @param isRenewable            indicates whether coverage renews after {@code endDateTime}; not
 *                               applicable for Limited Warranty
 * @param isCanceled             indicates whether coverage is canceled for the device; not
 *                               applicable for Limited Warranty and AppleCare+ for Business
 * @param contractCancelDateTime UTC date when coverage was canceled for the device; not applicable
 *                               for Limited Warranty and AppleCare+ for Business
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/applecarecoverage/attributes-data.dictionary">AppleCareCoverage.Attributes</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AppleCareCoverageAttributes(@Nonnull @JsonSetter(nulls = Nulls.DEFAULT) Status status,
                                          @Nonnull @JsonSetter(nulls = Nulls.DEFAULT) PaymentType paymentType,
                                          @JsonSetter(nulls = Nulls.AS_EMPTY) String description,
                                          @JsonSetter(nulls = Nulls.AS_EMPTY) String agreementNumber,
                                          @JsonSetter(nulls = Nulls.AS_EMPTY) String startDateTime,
                                          @JsonSetter(nulls = Nulls.AS_EMPTY) String endDateTime, boolean isRenewable,
                                          boolean isCanceled,
                                          @JsonSetter(nulls = Nulls.AS_EMPTY) String contractCancelDateTime) {

	/**
	 * The current status of device AppleCare coverage.
	 */
	public enum Status {
		@JsonEnumDefaultValue UNKNOWN,
		ACTIVE,
		INACTIVE;

		@JsonCreator
		public static Status fromValue(String value) {
			for (var type : values()) {
				if (type.name().equalsIgnoreCase(value)) {
					return type;
				}
			}
			return UNKNOWN;
		}
	}

	/**
	 * The payment type for device AppleCare coverage.
	 */
	public enum PaymentType {
		@JsonEnumDefaultValue UNKNOWN,
		/**
		 * AppleCare+ for Business.
		 */
		ABE_SUBSCRIPTION,
		/**
		 * Coverage was paid in full at time of purchase.
		 */
		PAID_UP_FRONT,
		/**
		 * Coverage has recurring billing.
		 */
		SUBSCRIPTION,
		/**
		 * No payment type is associated with coverage (e.g. Limited Warranty).
		 */
		NONE;

		@JsonCreator
		public static PaymentType fromValue(String value) {
			for (var type : values()) {
				if (type.name().equalsIgnoreCase(value)) {
					return type;
				}
			}
			return UNKNOWN;
		}
	}
}
