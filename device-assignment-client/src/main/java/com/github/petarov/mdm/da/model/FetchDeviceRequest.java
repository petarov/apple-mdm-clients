package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @param cursor a hex string that represents the starting position for a request. Use this to retrieve the list of
 *               devices that have been added or removed since a previous request. The string can be up to 1000
 *               characters. On the initial request, this should be omitted.
 * @param limit  the maximum number of entries to return, from {@code 100} to {@code 1000}
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/fetchdevicerequest">FetchDeviceRequest</a>
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record FetchDeviceRequest(String cursor, int limit) {

	public FetchDeviceRequest() {
		this("", 100); // default limit
	}
}
