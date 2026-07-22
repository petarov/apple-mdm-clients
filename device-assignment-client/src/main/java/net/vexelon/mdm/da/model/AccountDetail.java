package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * The response that contains the details for an account.
 *
 * @param adminId    the Apple ID of the person who generated the currently in-use tokens
 * @param orgAddress the organization address
 * @param orgEmail   the organization email
 * @param orgId      the customer ID. This key is available only in protocol version 3 and later.
 * @param orgIdHash  the SHA hash of an org identifier. This helps Mobile Device Management (MDM) server match the hash
 *                   with the organizationIdHash key in the Client Configuration API.
 *                   This key is available only in <i>X-Server-Protocol-Version 3</i> and later.
 * @param orgName    the organization name
 * @param orgPhone   the organization phone
 * @param orgType    the type of organization: defaults to {@link OrgType#UNKNOWN} when absent or unrecognized.
 *                   This key is available only in <i>X-Server-Protocol-Version 3</i> and later.
 * @param orgVersion possible values: {@code v1} for Apple Deployment Programs (like Device Enrollment Program or
 *                   Volume Purchase Program) organizations, {@code v2} for Apple School Manager (ASM) organizations.
 *                   This key is available only in <i>X-Server-Protocol-Version 3</i> and later.
 * @param serverName the name of the MDM server
 * @param serverUuid the system-generated server identifier
 * @param urls       the list of URLs available in the MDM service.
 *                   This key is valid in <i>X-Server-Protocol-Version 3</i> and later.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/accountdetail">devicemanagement/accountdetail</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountDetail(@JsonSetter(nulls = Nulls.AS_EMPTY) String adminId,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String orgAddress,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String orgEmail,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String orgId,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String orgIdHash,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String orgName, String orgPhone,
                            @Nonnull @JsonSetter(nulls = Nulls.AS_EMPTY) OrgType orgType,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String orgVersion,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String serverName,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) String serverUuid,
                            @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull List<Url> urls) {

	/**
	 * @return an empty {@link AccountDetail} with empty field values
	 */
	@Nonnull
	public static AccountDetail ofEmpty() {
		return new AccountDetail("", "", "", "", "", "", "", OrgType.UNKNOWN, "", "", "", List.of());
	}

	/**
	 * The type of organization.
	 */
	public enum OrgType {
		@JsonEnumDefaultValue UNKNOWN,
		EDU,
		ORG,
		TVPROVIDER
	}
}
