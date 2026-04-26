package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.vexelon.mdm.shared.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileSkipItemTests {

	@Test
	void test_os_types() {
		assertTrue(ProfileSkipItem.ANDROID.isIos());
		assertFalse(ProfileSkipItem.ANDROID.isMacOS());
		assertFalse(ProfileSkipItem.ANDROID.isTvOS());

		assertTrue(ProfileSkipItem.APPLE_ID.isIos());
		assertTrue(ProfileSkipItem.APPLE_ID.isMacOS());
		assertTrue(ProfileSkipItem.APPLE_ID.isTvOS());

		assertFalse(ProfileSkipItem.TV_ROOM.isIos());
		assertFalse(ProfileSkipItem.TV_ROOM.isMacOS());
		assertTrue(ProfileSkipItem.TV_ROOM.isTvOS());

		assertFalse(ProfileSkipItem.TIPS.isIos());
		assertTrue(ProfileSkipItem.TIPS.isVisionOS());

		assertTrue(ProfileSkipItem.ZOOM.isDeprecated());
	}

	@Test
	void test_deprecated_skip_items() throws JsonProcessingException {
		var json = "{ \"skip_setup_items\": [\"MessagingActivationUsingPhoneNumber\", \"OSShowcase\", \"DisplayTone\", \"HomeButtonSensitivity\"] }";
		var profile = JsonUtil.createObjectMapper().readValue(json, Profile.class);

		assertEquals(2, profile.skipSetupItems().size());

		assertTrue(profile.skipSetupItems().contains(ProfileSkipItem.MESSAGING_ACTIVATION_USING_PHONE_NUMBER));
		assertTrue(profile.skipSetupItems().contains(ProfileSkipItem.OS_SHOWCASE));
	}

	@Test
	void test_empty_skip_items() throws JsonProcessingException {
		var profile = JsonUtil.createObjectMapper().readValue("{ \"skip_setup_items\": [] }", Profile.class);
		assertEquals(0, profile.skipSetupItems().size());

		profile = JsonUtil.createObjectMapper().readValue("{}", Profile.class);
		assertEquals(0, profile.skipSetupItems().size());
	}
}
