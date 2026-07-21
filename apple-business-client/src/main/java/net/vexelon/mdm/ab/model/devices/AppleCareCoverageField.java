package net.vexelon.mdm.ab.model.devices;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * The set of fields that may be requested via the {@code fields[appleCareCoverage]} query parameter
 * when calling the Apple Business Manager AppleCare coverage endpoints.
 *
 * @see AppleCareCoverageAttributes
 */
public enum AppleCareCoverageField {
	STATUS("status"),
	PAYMENT_TYPE("paymentType"),
	DESCRIPTION("description"),
	AGREEMENT_NUMBER("agreementNumber"),
	START_DATE_TIME("startDateTime"),
	END_DATE_TIME("endDateTime"),
	IS_RENEWABLE("isRenewable"),
	IS_CANCELED("isCanceled"),
	CONTRACT_CANCEL_DATE_TIME("contractCancelDateTime");

	private final String fieldName;

	AppleCareCoverageField(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return camelCase field name as sent in the {@code fields[appleCareCoverage]} query parameter
	 */
	@Nonnull
	public String fieldName() {
		return fieldName;
	}

	/**
	 * Returns an {@link EnumSet} of zero or more specified {@code fields}.
	 *
	 * @return mutable {@link EnumSet} in declaration order; empty when no arguments are given
	 */
	@Nonnull
	public static EnumSet<AppleCareCoverageField> of(@Nonnull AppleCareCoverageField... fields) {
		var result = EnumSet.noneOf(AppleCareCoverageField.class);
		result.addAll(Arrays.asList(fields));
		return result;
	}
}
