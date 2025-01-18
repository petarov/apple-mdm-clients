package com.github.petarov.mdm.da.model;

import java.util.List;

/**
 * @param httpMethod possible values: {@code GET}, {@code POST}, {@code PUT}, {@code DELETE}
 * @param limit      the limit for the URL
 * @param uri        URI for the API
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/url">Url</a>
 */
public record Url(String httpMethod, List<Limit> limit, String uri) {

	public static final String URL_HTTP_METHOD_GET    = "GET";
	public static final String URL_HTTP_METHOD_POST   = "POST";
	public static final String URL_HTTP_METHOD_PUT    = "PUT";
	public static final String URL_HTTP_METHOD_DELETE = "DELETE";
}
