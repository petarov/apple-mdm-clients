package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppUser;
import jakarta.annotation.Nonnull;

/**
 * The response from editing a user.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/editvppuserresponse">EditVppUserResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class VppEditUserResponse implements VppHasResponse {

	@JsonUnwrapped
	private VppResponse response;

	private VppUser user;

	public VppEditUserResponse() {
	}

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
	 * @return the updated user
	 */
	@Nonnull
	public VppUser getUser() {
		return user;
	}

	public void setUser(@Nonnull VppUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "VppEditUserResponse{" + "response=" + response + ", user=" + user + '}';
	}
}
