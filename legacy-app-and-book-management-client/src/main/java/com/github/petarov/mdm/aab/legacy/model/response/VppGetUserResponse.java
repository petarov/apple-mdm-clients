package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppUser;
import jakarta.annotation.Nonnull;

/**
 * The response from the user details service.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getvppuserresponse">GetVppUserResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class VppGetUserResponse implements VppHasResponse {

	@JsonUnwrapped
	private VppResponse response;

	private VppUser user;

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
	 * @return the requested user
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
		return "VppGetUserResponse{" + "response=" + response + ", user=" + user + '}';
	}
}
