package com.github.petarov.mdm.aab.legacy.model.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppLocation;
import jakarta.annotation.Nullable;

/**
 * Base class the contains common properties for various service responses.
 *
 * @param clientContext    The value currently associated with the provided sToken. This field is only included in the
 *                         response when a value has been set via the <a href="https://developer.apple.com/documentation/devicemanagement/client_configuration">Client Configuration</a> endpoint.
 *                         See <a href="https://developer.apple.com/documentation/devicemanagement/app_and_book_management/managing_apps_and_books_through_web_services/protecting_your_vpp_account">Protecting Your VPP Account</a> for more information.
 * @param errorMessage     the human-readable explanation of the error
 * @param errorNumber      the numeric code of the error
 * @param expirationMillis the UNIX epoch timestamp, in milliseconds, when the account's sToken or password expires
 *                         (whichever is earlier)
 * @param location         the location associated with the provided sToken. This field is only returned when a location token is used with an Apple School Manager account.
 * @param status           The status code for the response. Possible values are:<ul>
 *                         <li>{@code 0}: Success
 *                         <li>{@code –1}: Failure
 *                         </ul>
 * @param uId              the unique library identifier. When querying records using multiple tokens that may share
 *                         libraries, use the {@code uId} field to filter duplicates. In this way, you can avoid
 *                         double-counting records when duplicate tokens are uploaded by different content managers.
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record VppResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String clientContext,
                          @JsonSetter(nulls = Nulls.AS_EMPTY) String errorMessage, int errorNumber,
                          long expirationMillis, @Nullable VppLocation location, int status,
                          @JsonSetter(nulls = Nulls.AS_EMPTY) String uId) {}
