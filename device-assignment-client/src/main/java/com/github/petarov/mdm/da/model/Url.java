package com.github.petarov.mdm.da.model;

import java.util.List;

/**
 * @param httpMethod possible values: GET, POST, PUT, DELETE
 * @param limit      the limit for the URL
 * @param uri        URI for the API
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/url">Url</a>
 */
public record Url(String httpMethod, List<Limit> limit, String uri) {}
