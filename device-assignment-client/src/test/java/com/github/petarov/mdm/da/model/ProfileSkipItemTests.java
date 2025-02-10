package com.github.petarov.mdm.da.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileSkipItemTests {

	@Test
	void verify_os_types() {
		assertTrue(ProfileSkipItem.ANDROID.isIos());
		assertFalse(ProfileSkipItem.ANDROID.isMacOS());
		assertFalse(ProfileSkipItem.ANDROID.isTvOS());

		assertTrue(ProfileSkipItem.APPLE_ID.isIos());
		assertTrue(ProfileSkipItem.APPLE_ID.isMacOS());
		assertTrue(ProfileSkipItem.APPLE_ID.isTvOS());

		assertFalse(ProfileSkipItem.TV_ROOM.isIos());
		assertFalse(ProfileSkipItem.TV_ROOM.isMacOS());
		assertTrue(ProfileSkipItem.TV_ROOM.isTvOS());
	}
}
