package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppAssociation;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * The response from managing licenses.
 *
 * @param response        {@link VppResponse}
 * @param adamIdStr       the unique identifier for a product in the iTunes Store
 * @param associations    an array of dictionaries representing successful and failed associations. If an association
 *                        succeeds, its dictionary contains the license and either a client-user ID or the serial number
 *                        of the device associated with the license.<p>If an association fails, its dictionary contains
 *                        the error message and number, and either the client-user ID or the serial number of the device
 *                        that couldn't be associated with the license.
 * @param disassociations an array of dictionaries representing successful and failed disassociations. If a
 *                        disassociation succeeds, its dictionary contains the license and either a client-user ID or
 *                        the serial number of the device disassociated from the license.<p>If the disassociation fails,
 *                        its dictionary contains the error message and number, and either the client-user ID or the
 *                        serial number of the device that couldn't be disassociated from the license.
 * @param isIrrevocable   if {@code true}, licenses for the specified product can't be revoked and reassigned
 * @param pricingParam    the quality of a product in the iTunes Store. Possible values are:<ul>
 *                        <li>{@code STDQ}: Standard quality
 *                        <li>{@code PLUS}: High quality
 * @param productTypeId   the type of product. Possible values are:<ul>
 *                        <li>{@code 7} = macOS software
 *                        <li>{@code 8} = iOS or macOS app from the App Store
 *                        <li>{@code 10} = Book
 * @param productTypeName the name of the product type
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/managevpplicensesbyadamidresponse">ManageVppLicensesByAdamIdResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppManageLicensesByAdamIdResponse(@JsonUnwrapped VppResponse response,
                                                @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull String adamIdStr,
                                                @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull List<VppAssociation> associations,
                                                @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull List<VppAssociation> disassociations,
                                                @JsonAlias("irrevocable") boolean isIrrevocable,
                                                @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull String pricingParam,
                                                int productTypeId,
                                                @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull String productTypeName) {}

