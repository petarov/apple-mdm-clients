package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A ranged limit.
 *
 * @param defaultLimit default value of limit
 * @param maximum      maximum value of limit
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/limit">devicemanagement/limit</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Limit(@JsonProperty("default") int defaultLimit, int maximum) {

	public Limit() {
		this(0, 0);
	}
}
