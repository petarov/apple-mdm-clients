package com.github.petarov.mdm.da.config;

import com.github.petarov.mdm.shared.util.JsonUtils;
import jakarta.annotation.Nonnull;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEUtil;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

public record DeviceAssignmentServerToken(String consumerKey, String consumerSecret, String accessToken,
                                          String accessSecret, String accessTokenExpiry) {

	@Nonnull
	public static DeviceAssignmentServerToken create(@Nonnull InputStream input,
			@Nonnull DeviceAssignmentPrivateKey daPrivateKey) throws Exception {
		var envelop = new SMIMEEnveloped(SMIMEUtil.toMimeBodyPart(input));

		for (var recipientInfo : envelop.getRecipientInfos().getRecipients()) {
			var mimePart = SMIMEUtil.toMimeBodyPart(recipientInfo.getContent(
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
		}

		throw new RuntimeException("Error reading server token S/MIME content");
	}

	@Nonnull
	public OffsetDateTime accessTokenExpiryTime() {
		return OffsetDateTime.parse(accessTokenExpiry);
	}
}
