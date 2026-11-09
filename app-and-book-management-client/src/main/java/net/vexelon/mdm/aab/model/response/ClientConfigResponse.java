package net.vexelon.mdm.aab.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.annotation.Nullable;
import net.vexelon.mdm.aab.model.DefaultPlatform;
import net.vexelon.mdm.aab.model.MdmInfo;
import net.vexelon.mdm.aab.model.NotificationType;

import java.util.List;

/**
 * The client configuration stored on the server. Apple recommends checking it regularly to verify the expected
 * values.
 *
 * @param countryISO2ACode            the ISO 3166-1 alpha-2 country code of the organization
 * @param defaultPlatform             the value to use for the {@code platform} parameter of the
 *                                    {@code contentMetadataLookup} request
 * @param notificationUrl             the URL to which notifications are sent, or an empty string if none is set
 * @param subscribedNotificationTypes the notification types the MDM is subscribed to. Never contains
 *                                    {@link NotificationType#TEST_NOTIFICATION}
 * @param websiteURL                  the URL of the organization's portal, {@code https://school.apple.com} or
 *                                    {@code https://business.apple.com}
 * @param mdmInfo                     the MDM server information, or {@code null} if none is set
 * @param notificationAuthToken       the bearer token the server sends in the {@code Authorization} header of
 *                                    notifications
 * @param tokenExpirationDate         the ISO-8601 expiration date of the token, in UTC
 * @param uId                         the unique identifier of the organization. Use it to detect and skip
 *                                    duplicate organizations
 * @param locationName                the name of the location
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/clientconfigresponse">devicemanagement/clientconfigresponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientConfigResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String countryISO2ACode,
                                   @JsonSetter(nulls = Nulls.AS_EMPTY) DefaultPlatform defaultPlatform,
                                   @JsonSetter(nulls = Nulls.AS_EMPTY) String notificationUrl, @JsonSetter(
		nulls = Nulls.AS_EMPTY) List<NotificationType> subscribedNotificationTypes,
                                   @JsonSetter(nulls = Nulls.AS_EMPTY) String websiteURL, @Nullable MdmInfo mdmInfo,
                                   @JsonSetter(nulls = Nulls.AS_EMPTY) String notificationAuthToken,
                                   @JsonSetter(nulls = Nulls.AS_EMPTY) String tokenExpirationDate,
                                   @JsonSetter(nulls = Nulls.AS_EMPTY) String uId,
                                   @JsonSetter(nulls = Nulls.AS_EMPTY) String locationName) {}
