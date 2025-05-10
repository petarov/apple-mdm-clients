package net.vexelon.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.vexelon.mdm.aab.legacy.model.VppAssignment;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

/**
 * The response that contains a list of assignments.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppassignmentsresponse">VppAssignmentsResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class VppGetAssignmentsResponse implements VppHasResponse {

	@JsonUnwrapped
	private VppResponse response;

	int totalPages;
	int currentPageIndex;
	int nextPageIndex;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	String requestId;
	int totalAssignments;
	int assignmentsInCurrentPage;

	@JsonSetter(nulls = Nulls.AS_EMPTY)
	List<VppAssignment> assignments = new ArrayList<>();

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
	 * @return the total number of pages of assignments. There will be 300 assignments per page.
	 */
	public int totalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the index of the page being returned
	 */
	public int currentPageIndex() {
		return currentPageIndex;
	}

	public void setCurrentPageIndex(int currentPageIndex) {
		this.currentPageIndex = currentPageIndex;
	}

	/**
	 * This field will only be returned if there are additional assignments pages to read.
	 *
	 * @return the index of the next assignments page
	 */
	public int nextPageIndex() {
		return nextPageIndex;
	}

	public void setNextPageIndex(int nextPageIndex) {
		this.nextPageIndex = nextPageIndex;
	}

	/**
	 * This field will only be returned if there are greater than 300 assignments.
	 *
	 * @return the ID to be used for subsequent assignments page lookups
	 */
	@Nonnull
	public String requestId() {
		return requestId;
	}

	public void setRequestId(@Nonnull String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the total number of assignments for the provided criteria
	 */
	public int totalAssignments() {
		return totalAssignments;
	}

	public void setTotalAssignments(int totalAssignments) {
		this.totalAssignments = totalAssignments;
	}

	/**
	 * @return the total number of assignments in the current page
	 */
	public int assignmentsInCurrentPage() {
		return assignmentsInCurrentPage;
	}

	public void setAssignmentsInCurrentPage(int assignmentsInCurrentPage) {
		this.assignmentsInCurrentPage = assignmentsInCurrentPage;
	}

	/**
	 * @return the current assignments
	 */
	@Nonnull
	public List<VppAssignment> assignments() {
		return assignments;
	}

	public void setAssignments(@Nonnull List<VppAssignment> assignments) {
		this.assignments = assignments;
	}

	@Override
	public String toString() {
		return "VppGetAssignmentsResponse{" + "response=" + response + ", totalPages=" + totalPages
				+ ", currentPageIndex=" + currentPageIndex + ", nextPageIndex=" + nextPageIndex + ", requestId='"
				+ requestId + '\'' + ", totalAssignments=" + totalAssignments + ", assignmentsInCurrentPage="
				+ assignmentsInCurrentPage + ", assignments=" + assignments + '}';
	}
}

