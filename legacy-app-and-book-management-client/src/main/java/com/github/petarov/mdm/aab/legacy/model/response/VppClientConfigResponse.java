package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * The response that contains the client configuration.
 *
 * @param apnToken           the Apple Push Notification token to use for notifications
 * @param appleId            the AppleID associated with the provided sToken
 * @param countryCode        the two-letter ISO 3166-1 code that designates the country where the VPP account is located. For example, {@code US} stands for United States, {@code CA} for Canada, {@code JP} for Japan, and so on.
 * @param defaultPlatform    the value to be passed for the platform parameter in the contentMetadataLookupUrl request. Possible values are:<ul>
 *                           <li>{@code volumestore}: For apps in the educational store
 *                           <li>{@code enterprisestore}: For apps in the enterprise store
 *                           <p>See also <a href="https://developer.apple.com/documentation/devicemanagement/app_and_book_management/service_configuration/getting_app_and_book_information">Getting app and book information</a> for more information.
 * @param email              the email address associated with the provided sToken
 * @param organizationId     the unique identifier assigned to an organization by the VPP
 * @param organizationIdHash the hash of {@link #organizationId()}
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppclientconfigresponse">VppClientConfigResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppClientConfigResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String apnToken,
                                      @JsonSetter(nulls = Nulls.AS_EMPTY) String appleId,
                                      @JsonSetter(nulls = Nulls.AS_EMPTY) String countryCode,
                                      @JsonSetter(nulls = Nulls.AS_EMPTY) String defaultPlatform,
                                      @JsonSetter(nulls = Nulls.AS_EMPTY) String email,
                                      @JsonSetter(nulls = Nulls.AS_EMPTY) String organizationId,
                                      @JsonSetter(nulls = Nulls.AS_EMPTY) String organizationIdHash) {}
