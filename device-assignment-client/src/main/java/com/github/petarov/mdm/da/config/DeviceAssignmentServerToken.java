package com.github.petarov.mdm.da.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.petarov.mdm.shared.util.JsonUtils;
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
import java.time.OffsetDateTime;

public record DeviceAssignmentServerToken(String consumerKey, String consumerSecret, String accessToken,
                                          String accessSecret, String accessTokenExpiry) {

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

			return JsonUtils.createObjectMapper().readValue(json, DeviceAssignmentServerToken.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing json from S/MIME text content", e);
		} catch (SMIMEException | CMSException | MessagingException | IOException e) {
			throw new RuntimeException("Error reading server token from S/MIME content", e);
		}
	}

	@Nonnull
	public OffsetDateTime accessTokenExpiryTime() {
		return OffsetDateTime.parse(accessTokenExpiry);
	}
}
