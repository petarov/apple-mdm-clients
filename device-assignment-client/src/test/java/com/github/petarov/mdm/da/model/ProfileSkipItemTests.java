package com.github.petarov.mdm.da.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileSkipItemTests {

	@Test
	void verify_os_types() {
		Assertions.assertTrue(ProfileSkipItem.ANDROID.isIos());
		Assertions.assertFalse(ProfileSkipItem.ANDROID.isMacOS());
		Assertions.assertFalse(ProfileSkipItem.ANDROID.isTvOS());

		Assertions.assertTrue(ProfileSkipItem.APPLE_ID.isIos());
		Assertions.assertTrue(ProfileSkipItem.APPLE_ID.isMacOS());
		Assertions.assertTrue(ProfileSkipItem.APPLE_ID.isTvOS());

		Assertions.assertFalse(ProfileSkipItem.TV_ROOM.isIos());
		Assertions.assertFalse(ProfileSkipItem.TV_ROOM.isMacOS());
		Assertions.assertTrue(ProfileSkipItem.TV_ROOM.isTvOS());
	}
}
