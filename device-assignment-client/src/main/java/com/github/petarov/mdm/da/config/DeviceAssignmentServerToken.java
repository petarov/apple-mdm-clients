package com.github.petarov.mdm.da.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.shared.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMEUtil;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DeviceAssignmentServerToken(String consumerKey, String consumerSecret, String accessToken,
                                          String accessSecret, String accessTokenExpiry) {

	/**
	 * Creates a server token wrapper from encrypted and signed MIME data.
	 */
	@Nonnull
	public static DeviceAssignmentServerToken create(@Nonnull InputStream input,
			@Nonnull DeviceAssignmentPrivateKey daPrivateKey) {

		try {
			var recipients = new SMIMEEnveloped(SMIMEUtil.toMimeBodyPart(input)).getRecipientInfos().getRecipients();
			if (recipients.isEmpty()) {
				throw new RuntimeException("Error reading server token from S/MIME content: public key not found");
			}

			var mimePart = SMIMEUtil.toMimeBodyPart(recipients.iterator().next().getContent(
					new JceKeyTransEnvelopedRecipient(daPrivateKey.privateKey()).setProvider(
							BouncyCastleProvider.PROVIDER_NAME)));

			var content = switch (mimePart.getContent()) {
				case InputStream is -> new String(is.readAllBytes(), StandardCharsets.UTF_8);
				default -> (String) mimePart.getContent();
			};

			int idxBegin = content.indexOf("-----BEGIN MESSAGE-----");
			int idxEnd = content.indexOf("-----END MESSAGE-----");
			var json = content.substring(idxBegin + 23, idxEnd);

			return JsonUtil.createObjectMapper().readValue(json, DeviceAssignmentServerToken.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing json from S/MIME text content", e);
		} catch (SMIMEException | CMSException | MessagingException | IOException e) {
			throw new RuntimeException("Error reading server token from S/MIME content", e);
		}
	}

	/**
	 * @see #create(InputStream, DeviceAssignmentPrivateKey)
	 */
	@Nonnull
	public static DeviceAssignmentServerToken create(@Nonnull Path path,
			@Nonnull DeviceAssignmentPrivateKey daPrivateKey) throws IOException {
		try (var input = Files.newInputStream(path)) {
			return create(input, daPrivateKey);
		}
	}

	@Nonnull
	public OffsetDateTime accessTokenExpiryTime() {
		return OffsetDateTime.parse(accessTokenExpiry);
	}
}
