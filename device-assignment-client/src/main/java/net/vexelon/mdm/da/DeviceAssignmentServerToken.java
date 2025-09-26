package net.vexelon.mdm.da;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;
import net.vexelon.mdm.shared.http.HttpClientWrapperException;
import net.vexelon.mdm.shared.util.JsonUtil;
import net.vexelon.mdm.shared.util.ParseUtil;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Base64;

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
			// Parse the MIME structure: skip everything from `Content-Type: application/pkcs7-mime` until the first
			// empty line. The CMS content should start from there onwards.
			var contentStr = new String(input.readAllBytes(), StandardCharsets.UTF_8);

			var lines = contentStr.split("\r?\n");
			var cmsBuilder = new StringBuilder();
			var isHeader = false;

			for (var line : lines) {
				// Detect headers
				if (line.startsWith("Content-Type: application/pkcs7-mime") || line.startsWith(
						"Content-Type: application/x-pkcs7-mime")) {
					isHeader = true;
					continue;
				}

				// Skip headers
				if (isHeader) {
					if (line.trim().isEmpty()) {
						isHeader = false;
					}
					continue;
				}

				cmsBuilder.append(line);
			}

			var cmsBytes = Base64.getDecoder().decode(cmsBuilder.toString());
			var envelopedData = new CMSEnvelopedData(cmsBytes);

			var recipients = envelopedData.getRecipientInfos().getRecipients();
			if (recipients.isEmpty()) {
				throw new HttpClientWrapperException(
						"Error reading server token from S/MIME content: public key not found");
			}

			var recipient = recipients.iterator().next();
			var decryptedContent = recipient.getContent(
					new JceKeyTransEnvelopedRecipient(daPrivateKey.privateKey()).setProvider(
							BouncyCastleProvider.PROVIDER_NAME));

			var content = new String(decryptedContent, StandardCharsets.UTF_8);

			// Extract JSON
			int idxBegin = content.indexOf("-----BEGIN MESSAGE-----");
			int idxEnd = content.indexOf("-----END MESSAGE-----");
			var json = content.substring(idxBegin + 23, idxEnd);

			return JsonUtil.createObjectMapper().readValue(json, DeviceAssignmentServerToken.class);
		} catch (JsonProcessingException e) {
			throw new HttpClientWrapperException("Error parsing json from S/MIME text content", e);
		} catch (CMSException | IOException e) {
			throw new HttpClientWrapperException("Error reading server token from S/MIME content", e);
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

	/**
	 * @return {@link DeviceAssignmentServerToken#accessTokenExpiry()} parsed to {@link OffsetDateTime}
	 */
	@Nonnull
	public OffsetDateTime accessTokenExpiryTime() {
		return ParseUtil.parseAppleDateTime(accessTokenExpiry);
	}
}
