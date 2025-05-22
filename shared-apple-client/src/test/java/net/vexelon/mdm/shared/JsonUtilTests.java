package net.vexelon.mdm.shared;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import net.vexelon.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonUtilTests {

	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public record UserData(String firstName, int unitsTotalCount) {}

	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public record LoginData(String username, @Nonnull OffsetDateTime loginTime) {}

	@Test
	public void test_snake_case() {
		try {
			var userData = new UserData("Max Mustermann", 42);
			var mapper = JsonUtil.createObjectMapper();
			var json = mapper.writer().writeValueAsString(userData);
			assertEquals("{\"first_name\":\"Max Mustermann\",\"units_total_count\":42}", json);

			var jsonData = "{\"first_name\":\"Bob Mustermann\",\"units_total_count\":56}";
			userData = mapper.reader().readValue(jsonData, UserData.class);
			assertEquals(new UserData("Bob Mustermann", 56), userData);
		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	public void test_date_time() {
		try {
			var time = OffsetDateTime.of(2025, 2, 14, 14, 0, 0, 0, ZoneOffset.of("+1"));
			var data = new LoginData("maxmustermann", time);
			var mapper = JsonUtil.createObjectMapper();
			var json = mapper.writer().writeValueAsString(data);
			assertEquals("{\"username\":\"maxmustermann\",\"login_time\":\"2025-02-14T14:00:00+01:00\"}", json);

			var jsonData = "{\"username\":\"bobmustermann\",\"login_time\":\"2025-02-14T13:00:00+00:00\"}";
			data = mapper.reader().readValue(jsonData, LoginData.class);
			assertEquals(data, new LoginData("bobmustermann", time.withOffsetSameInstant(ZoneOffset.UTC)));
		} catch (Exception e) {
			fail(e);
		}
	}
}
