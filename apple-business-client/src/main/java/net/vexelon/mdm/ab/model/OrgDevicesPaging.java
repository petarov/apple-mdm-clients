package net.vexelon.mdm.ab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

/**
 * Paging metadata from {@code meta.paging} in an Apple Business API list response.
 *
 * @param nextCursor opaque cursor to pass to the next page request; empty when no more pages
 * @param limit      the effective page size used for this response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrgDevicesPaging(@JsonSetter(nulls = Nulls.AS_EMPTY) String nextCursor, int limit) {}
