package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * @param httpMethod possible values: {@code GET}, {@code POST}, {@code PUT}, {@code DELETE}
 * @param limit      the limit for the URL
 * @param uri        URI for the API
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/url">Url</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Url(@JsonSetter(nulls = Nulls.AS_EMPTY) List<String> httpMethod,
                  @JsonSetter(nulls = Nulls.AS_EMPTY) Limit limit, @JsonSetter(nulls = Nulls.AS_EMPTY) String uri) {

	public static final String URL_HTTP_METHOD_GET    = "GET";
	public static final String URL_HTTP_METHOD_POST   = "POST";
	public static final String URL_HTTP_METHOD_PUT    = "PUT";
	public static final String URL_HTTP_METHOD_DELETE = "DELETE";
}
