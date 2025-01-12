package com.github.petarov.mdm.shared.http;

public interface MdmHttpClient {

	default boolean isResponseOk(int code) {
		return code / 100 == 2;
	}

	default boolean isResponseAuthError(int code) {
		return code == 401 || code == 403;
	}
}
