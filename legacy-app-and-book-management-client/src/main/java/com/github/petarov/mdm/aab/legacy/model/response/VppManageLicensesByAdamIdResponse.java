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
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/managevpplicensesbyadamidresponse">
 * ManageVppLicensesByAdamIdResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class VppManageLicensesByAdamIdResponse implements VppHasResponse {

	@JsonUnwrapped
	private VppResponse response;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String adamIdStr;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private List<VppAssociation> associations;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private List<VppAssociation> disassociations;

	@JsonAlias("irrevocable")
	private boolean isIrrevocable;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String pricingParam;

	private int productTypeId;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String productTypeName;

	/**
	 * @return {@link VppResponse}
	 */
	@Nonnull
	@Override
	public VppResponse getResponse() {
		return response;
	}

	public void setResponse(VppResponse response) {
		this.response = response;
	}

	/**
	 * @return the unique identifier for a product in the iTunes Store
	 */
	@Nonnull
	public String getAdamIdStr() {
		return adamIdStr;
	}

	public void setAdamIdStr(@Nonnull String adamIdStr) {
		this.adamIdStr = adamIdStr;
	}

	/**
	 * @return a list of dictionaries representing successful and failed associations. If an association
	 * succeeds, its dictionary contains the license and either a client-user ID or the serial number
	 * of the device associated with the license.
	 * <p>If an association fails, its dictionary contains the error message and number, and either the client-user ID
	 * or the serial number of the device that couldn't be associated with the license.
	 */
	@Nonnull
	public List<VppAssociation> getAssociations() {
		return associations;
	}

	public void setAssociations(@Nonnull List<VppAssociation> associations) {
		this.associations = associations;
	}

	/**
	 * @return a list of dictionaries representing successful and failed disassociations. If a
	 * disassociation succeeds, its dictionary contains the license and either a client-user ID or
	 * the serial number of the device disassociated from the license.
	 * <p>If the disassociation fails, its dictionary contains the error message and number, and either the
	 * client-user ID or the serial number of the device that couldn't be disassociated from the license.
	 */
	@Nonnull
	public List<VppAssociation> getDisassociations() {
		return disassociations;
	}

	public void setDisassociations(@Nonnull List<VppAssociation> disassociations) {
		this.disassociations = disassociations;
	}

	/**
	 * @return if {@code true}, licenses for the specified product can't be revoked and reassigned
	 */
	public boolean isIrrevocable() {
		return isIrrevocable;
	}

	public void setIrrevocable(boolean irrevocable) {
		isIrrevocable = irrevocable;
	}

	/**
	 * @return the quality of a product in the iTunes Store. Possible values are:
	 * <ul>
	 * <li>{@code STDQ}: Standard quality
	 * <li>{@code PLUS}: High quality
	 */
	@Nonnull
	public String getPricingParam() {
		return pricingParam;
	}

	public void setPricingParam(@Nonnull String pricingParam) {
		this.pricingParam = pricingParam;
	}

	/**
	 * @return the type of product. Possible values are:
	 * <ul>
	 * <li>{@code 7} = macOS software
	 * <li>{@code 8} = iOS or macOS app from the App Store
	 * <li>{@code 10} = Book
	 */
	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	/**
	 * @return the name of the product type
	 */
	@Nonnull
	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(@Nonnull String productTypeName) {
		this.productTypeName = productTypeName;
	}

	@Override
	public String toString() {
		return "VppManageLicensesByAdamIdResponse{" + "response=" + response + ", adamIdStr='" + adamIdStr + '\''
				+ ", associations=" + associations + ", disassociations=" + disassociations + ", isIrrevocable="
				+ isIrrevocable + ", pricingParam='" + pricingParam + '\'' + ", productTypeId=" + productTypeId
				+ ", productTypeName='" + productTypeName + '\'' + '}';
	}
}
