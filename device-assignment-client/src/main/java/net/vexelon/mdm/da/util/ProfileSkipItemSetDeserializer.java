package net.vexelon.mdm.da.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import net.vexelon.mdm.da.model.ProfileSkipItem;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProfileSkipItemSetDeserializer extends JsonDeserializer<Set<ProfileSkipItem>> {

	@Override
	public Set<ProfileSkipItem> deserialize(JsonParser parser, DeserializationContext deserializationContext)
			throws IOException, JacksonException {

		if (parser.getCodec().readTree(parser) instanceof ArrayNode arrayNode) {
			return StreamSupport.stream(arrayNode.spliterator(), false).map(JsonNode::asText)
					.map(ProfileSkipItem::ofKey).flatMap(Optional::stream).collect(Collectors.toSet());
		}

		return Set.of();
	}

	@Override
	public Set<ProfileSkipItem> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
		return Set.of();
	}
}
