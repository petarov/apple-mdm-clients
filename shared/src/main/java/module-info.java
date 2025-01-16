module com.github.petarov.mdm.shared {
	requires java.net.http;
	requires jakarta.annotation;
	requires com.fasterxml.jackson.kotlin;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires com.fasterxml.jackson.databind;

	exports com.github.petarov.mdm.shared.config;
	exports com.github.petarov.mdm.shared.util;
}
