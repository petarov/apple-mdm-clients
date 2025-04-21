package com.github.petarov.mdm.shared.http;

public final class HttpConsts {

	public static final int STATUS_OK                  = 200;
	public static final int STATUS_BAD_REQUEST         = 400;
	public static final int STATUS_UNAUTHORIZED        = 401;
	public static final int STATUS_FORBIDDEN           = 403;
	public static final int STATUS_NOT_FOUND           = 404;
	public static final int STATUS_SERVICE_UNAVAILABLE = 503;

	public static final String HEADER_AUTHORIZATION       = "authorization";
	public static final String HEADER_CONTENT_TYPE        = "content-type";
	public static final String HEADER_CONTENT_ENCODING    = "content-encoding";
	public static final String HEADER_USER_AGENT          = "user-agent";
	public static final String HEADER_ACCEPT_ENCODING     = "accept-encoding";
	public static final String HEADER_PROXY_AUTHORIZATION = "proxy-authorization";

	public static final String HEADER_VALUE_APPLICATION_JSON_UTF8 = "application/json;charset=UTF8";
}
