package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppAssignment;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * The response that contains a list of assignments.
 *
 * @param response                 {@link VppResponse}
 * @param totalPages               the total number of pages of assignments. There will be 300 assignments per page.
 * @param currentPageIndex         the index of the page being returned
 * @param nextPageIndex            The index of the next assignments page. This field will only be returned if there are additional assignments pages to read.
 * @param requestId                the ID to be used for subsequent assignments page lookups. This field will only be returned if there are greater than 300 assignments.
 * @param totalAssignments         the total number of assignments for the provided criteria
 * @param assignmentsInCurrentPage the total number of assignments in the current page
 * @param assignments              the current assignments
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/vppassignmentsresponse">VppAssignmentsResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppGetAssignmentsResponse(@JsonUnwrapped VppResponse response, int totalPages, int currentPageIndex,
                                        int nextPageIndex, String requestId, int totalAssignments,
                                        int assignmentsInCurrentPage,
                                        @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull List<VppAssignment> assignments) {}

