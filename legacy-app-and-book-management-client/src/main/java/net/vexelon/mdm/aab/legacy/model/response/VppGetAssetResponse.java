package net.vexelon.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.vexelon.mdm.aab.legacy.model.VppAsset;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

/**
 * The response with the asset.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getvppassetresponse">GetVppAssetResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class VppGetAssetResponse implements VppHasResponse {

	@JsonUnwrapped
	private VppResponse response;

	private int totalCount;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private List<VppAsset> assets = new ArrayList<>();

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
	 * @return the total number of assets that will be returned
	 */
	public int totalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the list of assets managed by the provided sToken
	 */
	@Nonnull
	public List<VppAsset> assets() {
		return assets;
	}

	public void setAssets(@Nonnull List<VppAsset> assets) {
		this.assets = assets;
	}

	@Override
	public String toString() {
		return "VppGetAssetResponse{" + "response=" + response + ", totalCount=" + totalCount + ", assets=" + assets
				+ '}';
	}
}