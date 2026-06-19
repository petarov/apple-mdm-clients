package net.vexelon.mdm.ab.model.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseTests {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	// language=json
	private static final String SINGLE_ERROR_JSON = """
			{
			  "errors": [
			    {
			      "id": "d41f0ce2-8e12-41b7-bffd-91fd628f6628",
			      "status": "404",
			      "code": "NOT_FOUND",
			      "title": "The specified resource doesn't exist",
			      "detail": "There is no resource of type 'orgDevices' with id 'XABC123X0ABC123X0'"
			    }
			  ]
			}
			""";

	@Test
	void deserializes_single_error() throws Exception {
		var response = MAPPER.readValue(SINGLE_ERROR_JSON, ErrorResponse.class);

		assertEquals(1, response.errors().size());
		var error = response.errors().getFirst();
		assertEquals("d41f0ce2-8e12-41b7-bffd-91fd628f6628", error.id());
		assertEquals("404", error.status());
		assertEquals("NOT_FOUND", error.code());
		assertEquals("The specified resource doesn't exist", error.title());
		assertEquals("There is no resource of type 'orgDevices' with id 'XABC123X0ABC123X0'", error.detail());
	}

	@Test
	void deserializes_multiple_errors() throws Exception {
		var json = """
				{
				  "errors": [
				    {
				      "id": "d41f0ce2-8e12-41b7-bffd-91fd628f6628",
				      "status": "404",
				      "code": "NOT_FOUND",
				      "title": "The specified resource doesn't exist",
				      "detail": "There is no resource of type 'orgDevices' with id 'XABC123X0ABC123X0'"
				    },
				    {
				      "id": "a92c1df3-3b47-52e8-cffe-82fe739g7739",
				      "status": "403",
				      "code": "FORBIDDEN",
				      "title": "Insufficient permissions",
				      "detail": "The token does not have the required scope for this operation"
				    }
				  ]
				}
				""";

		var response = MAPPER.readValue(json, ErrorResponse.class);

		assertEquals(2, response.errors().size());
		assertEquals("NOT_FOUND", response.errors().get(0).code());
		assertEquals("FORBIDDEN", response.errors().get(1).code());
	}

	@Test
	void null_errors_array_defaults_to_empty_list() throws Exception {
		var response = MAPPER.readValue("{\"errors\": null}", ErrorResponse.class);

		assertEquals(List.of(), response.errors());
	}

	@Test
	void absent_errors_array_defaults_to_empty_list() throws Exception {
		var response = MAPPER.readValue("{}", ErrorResponse.class);

		assertEquals(List.of(), response.errors());
	}

	@Test
	void null_error_fields_default_to_empty_string() throws Exception {
		var response = MAPPER.readValue("""
				{"errors": [{"id": null, "status": null, "code": null, "title": null, "detail": null}]}
				""", ErrorResponse.class);

		var error = response.errors().getFirst();
		assertEquals("", error.id());
		assertEquals("", error.status());
		assertEquals("", error.code());
		assertEquals("", error.title());
		assertEquals("", error.detail());
	}
}
