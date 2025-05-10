package net.vexelon.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;

/**
 * The response that contains the client configuration.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppclientconfigresponse">VppClientConfigResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class VppClientConfigResponse implements VppHasResponse {

	@JsonUnwrapped
	private VppResponse response;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String apnToken;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String appleId;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String clientContext;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String countryCode;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String defaultPlatform;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String email;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String organizationId;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String organizationIdHash;

	/**
	 * @return {@link VppResponse}
	 */
	@Nonnull
	@Override
	public VppResponse getResponse() {
		return response;
	}

	/**
	 * @return the Apple Push Notification token to use for notifications
	 */
	public String getApnToken() {
		return apnToken;
	}

	public void setApnToken(String apnToken) {
		this.apnToken = apnToken;
	}

	/**
	 * @return the AppleID associated with the provided sToken
	 */
	public String getAppleId() {
		return appleId;
	}

	public void setAppleId(String appleId) {
		this.appleId = appleId;
	}

	/**
	 * @return the value currently associated with the provided sToken
	 */
	public String getClientContext() {
		return clientContext;
	}

	public void setClientContext(String clientContext) {
		this.clientContext = clientContext;
	}

	/**
	 * @return the two-letter ISO 3166-1 code that designates the country where the VPP account is located.
	 * For example, {@code US} stands for United States, {@code CA} for Canada, {@code JP} for Japan, and so on.
	 */
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the value to be passed for the platform parameter in the {@code contentMetadataLookupUrl} request.
	 * Possible values are:<ul>
	 * <li>{@code volumestore}: For apps in the educational store
	 * <li>{@code enterprisestore}: For apps in the enterprise store
	 * </ul>
	 * <p>
	 * See also <a href="https://developer.apple.com/documentation/devicemanagement/app_and_book_management/service_configuration/getting_app_and_book_information">Getting app and book information</a> for more information.
	 */
	public String getDefaultPlatform() {
		return defaultPlatform;
	}

	public void setDefaultPlatform(String defaultPlatform) {
		this.defaultPlatform = defaultPlatform;
	}

	/**
	 * @return the email address associated with the provided sToken
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the unique identifier assigned to an organization by the VPP
	 */
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the hash of {@link #getOrganizationId()}
	 */
	public String getOrganizationIdHash() {
		return organizationIdHash;
	}

	public void setOrganizationIdHash(String organizationIdHash) {
		this.organizationIdHash = organizationIdHash;
	}

	@Override
	public String toString() {
		return "VppClientConfigResponse{" + "response=" + response + ", apnToken='" + apnToken + '\'' + ", appleId='"
				+ appleId + '\'' + ", clientContext='" + clientContext + '\'' + ", countryCode='" + countryCode + '\''
				+ ", defaultPlatform='" + defaultPlatform + '\'' + ", email='" + email + '\'' + ", organizationId='"
				+ organizationId + '\'' + ", organizationIdHash='" + organizationIdHash + '\'' + '}';
	}
}
