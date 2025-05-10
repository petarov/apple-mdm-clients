package net.vexelon.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.vexelon.mdm.aab.legacy.model.VppUser;
import jakarta.annotation.Nonnull;

/**
 * The response from retiring a user.
 * <p>
 * If the user passes the {@code userId} value for an already-retired user, this request returns an error that indicates
 * the user has already been retired.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/retirevppuserresponse">RetireVppUserResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class VppRetireUserResponse implements VppHasResponse {

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
	 * @return the retired user
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
		return "VppRetireUserResponse{" + "response=" + response + ", user=" + user + '}';
	}
}