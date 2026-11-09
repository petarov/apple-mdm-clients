package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * The overall status of an asynchronous event.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/statusresponse">devicemanagement/statusresponse</a>
 */
public enum EventStatus {
	/**
	 * The server is still processing the event.
	 */
	PENDING,
	/**
	 * The server processed all tasks of the event.
	 */
	COMPLETE,
	/**
	 * Some tasks of the event failed; see {@link net.vexelon.mdm.aab.model.response.StatusResponse#failures()}.
	 */
	FAILED,
	@JsonEnumDefaultValue UNKNOWN
}
