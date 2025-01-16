module com.github.petarov.mdm.da {
	requires org.bouncycastle.mail;
	requires org.bouncycastle.pkix;
	requires org.bouncycastle.provider;
	requires jakarta.annotation;

	requires com.github.petarov.mdm.shared;
	requires com.fasterxml.jackson.core;
	requires java.mail;

	exports com.github.petarov.mdm.da;
}
