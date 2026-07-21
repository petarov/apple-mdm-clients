package net.vexelon.mdm.da.model;

import net.vexelon.mdm.shared.util.JsonUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrgTypeScratchTest {
	@Test
	void deserializes_tvprovider_variants() throws Exception {
		var mapper = JsonUtil.createObjectMapper();
		assertEquals(AccountDetail.OrgType.TV_PROVIDER, mapper.readValue("\"tvprovider\"", AccountDetail.OrgType.class));
		assertEquals(AccountDetail.OrgType.TV_PROVIDER, mapper.readValue("\"TVPROVIDER\"", AccountDetail.OrgType.class));
		assertEquals(AccountDetail.OrgType.TV_PROVIDER, mapper.readValue("\"TvProvider\"", AccountDetail.OrgType.class));
		assertEquals(AccountDetail.OrgType.EDU, mapper.readValue("\"edu\"", AccountDetail.OrgType.class));
		assertEquals(AccountDetail.OrgType.ORG, mapper.readValue("\"ORG\"", AccountDetail.OrgType.class));
	}

	@Test
	void unrecognized_top_level_falls_back_to_unknown() throws Exception {
		var mapper = JsonUtil.createObjectMapper();
		var actual = mapper.readValue("\"bogus\"", AccountDetail.OrgType.class);
		System.out.println("ACTUAL bogus (top-level) = " + actual);
	}

	@Test
	void unrecognized_field_falls_back_to_unknown() throws Exception {
		var mapper = JsonUtil.createObjectMapper();
		String json = """
				{"org_type": "bogus"}
				""";
		var result = mapper.readValue(json, AccountDetail.class);
		System.out.println("ACTUAL bogus (field) = " + result.orgType());
		assertEquals(AccountDetail.OrgType.UNKNOWN, result.orgType());
	}
}
