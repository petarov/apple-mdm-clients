package net.vexelon.mdm.aab.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import net.vexelon.mdm.aab.model.NotificationType;
import net.vexelon.mdm.aab.model.ResponseErrorCode;

import java.util.List;
import java.util.Map;

/**
 * The service configuration: service URLs, supported notification types, request limits and possible error codes.
 * The values in {@code limits} and {@code urls} are dynamic and can change without notice; Apple recommends
 * syncing them every 5 minutes.
 *
 * @param errorCodes        the possible error numbers and their messages
 * @param limits            the current request limits, keyed by limit name. The key set is dynamic. Known keys:
 *                          <ul>
 *                          <li>ManageAssetsRequest: {@code maxAssets}, {@code maxClientUserIds},
 *                          {@code maxSerialNumbers}</li>
 *                          <li>RevokeAssetsRequest: {@code maxRevokeClientUserIds},
 *                          {@code maxRevokeSerialNumbers}</li>
 *                          <li>User management: {@code maxUsers}</li>
 *                          <li>Subscription management: {@code maxSubscriptions},
 *                          {@code maxSubscriptionClientUserIds}</li>
 *                          <li>Client Config: {@code maxNotificationLength}, {@code maxMdmNameLength},
 *                          {@code maxMdmMetadataLength}, {@code maxMdmIdLength}</li>
 *                          <li>All requests: {@code maxRequestPerSecond}</li>
 *                          </ul>
 * @param notificationTypes the supported notification types. Never contains
 *                          {@link NotificationType#TEST_NOTIFICATION}
 * @param urls              the current service URLs, keyed by endpoint name, e.g. {@code getAssets},
 *                          {@code clientConfig}, {@code contentMetadataLookup} or {@code invitationEmail}. The key
 *                          set is dynamic. This client resolves its endpoint URLs from these values; see
 *                          {@link net.vexelon.mdm.aab.AppAndBookClient}.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/serviceconfigresponse">devicemanagement/serviceconfigresponse</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ServiceConfigResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) List<ResponseErrorCode> errorCodes,
                                    @JsonSetter(nulls = Nulls.AS_EMPTY) Map<String, Integer> limits,
                                    @JsonSetter(nulls = Nulls.AS_EMPTY) List<NotificationType> notificationTypes,
                                    @JsonSetter(nulls = Nulls.AS_EMPTY) Map<String, String> urls) {}
