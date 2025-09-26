package net.vexelon.mdm.da.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.vexelon.mdm.da.model.ProfileSkipItem;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProfileSkipItemSetDeserializer extends JsonDeserializer<Set<ProfileSkipItem>> {

	@Override
	public Set<ProfileSkipItem> deserialize(JsonParser parser, DeserializationContext deserializationContext)
			throws IOException, JacksonException {

		if (parser.getCodec().readTree(parser) instanceof ArrayNode arrayNode) {
			return StreamSupport.stream(arrayNode.spliterator(), false).map(JsonNode::asText).map(this::safeValueOf)
					.filter(Objects::nonNull).collect(Collectors.toSet());
		}

		return Set.of();
	}

	private ProfileSkipItem safeValueOf(String enumValue) {
		try {
			return ProfileSkipItem.valueOf(enumValue);
		} catch (IllegalArgumentException e) {
			// nulls will be filtered out above
			return null;
		}
	}
}
