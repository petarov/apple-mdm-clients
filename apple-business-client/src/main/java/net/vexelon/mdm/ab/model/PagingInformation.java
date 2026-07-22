package net.vexelon.mdm.ab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nonnull;

/**
 * Paging information for data responses.
 *
 * @param paging the paging information details
 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/paginginformation">applebusinessapi/paginginformation</a>
 * @since Apple Business API 2.1+
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PagingInformation(@Nonnull Paging paging) {

	/**
	 * Paging details, such as the total number of resources and the per-page limit.
	 *
	 * @param limit      the maximum number of resources returned per page
	 * @param nextCursor the cursor to use for the next request, in case of pagination.
	 * @param total      the total number of resources available
	 * @see <a href="https://developer.apple.com/documentation/applebusinessapi/paginginformation/paging-data.dictionary">applebusinessapi/paginginformation/paging-data.dictionary</a>
	 * @since Apple Business API 2.1+
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Paging(int limit, @JsonSetter(nulls = Nulls.AS_EMPTY) String nextCursor, int total) {}
}
