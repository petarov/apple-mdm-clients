package net.vexelon.mdm.aab.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * The type of an asynchronous event, reflecting the request that created it.
 * <p>
 * Unrecognized wire values map to {@link #UNKNOWN}.
 *
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/statusresponse">devicemanagement/statusresponse</a>
 */
public enum EventType {
	/**
	 * Created by an associate assets or associate subscriptions request.
	 */
	ASSOCIATE,
	/**
	 * Created by a disassociate assets or disassociate subscriptions request.
	 */
	DISASSOCIATE,
	/**
	 * Created by a revoke assets request. The {@code ASSET_MANAGEMENT} notifications it generates report
	 * {@code DISASSOCIATE} instead.
	 */
	REVOKE,
	/**
	 * Created by a create users request.
	 */
	CREATE,
	/**
	 * Created by an update users request.
	 */
	UPDATE,
	/**
	 * Created by a retire users request.
	 */
	RETIRE,
	@JsonEnumDefaultValue UNKNOWN
}
