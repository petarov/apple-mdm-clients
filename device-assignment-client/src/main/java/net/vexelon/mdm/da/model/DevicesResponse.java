package net.vexelon.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @param cursor       indicates when this request was processed by the enrollment server. The MDM server can use
 *                     this value in future requests if it wants to retrieve only records added or removed since this
 *                     request.
 * @param devices      an array of dictionaries that provide information about devices. The devices are sorted in
 *                     chronological order by the time stamp of the operation {@link Device#opDate()} performed on
 *                     the {@link Device}.
 * @param fetchedUntil a date stamp that indicates the progress of the device fetch request, in ISO 8601 format
 * @param moreToFollow indicates that the request's limit and cursor values resulted in only a partial list of devices.
 *                     The MDM server should immediately make another request (starting from the newly returned cursor)
 *                     to obtain additional records.
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/fetchdeviceresponse">FetchDeviceResponse</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DevicesResponse(@JsonSetter(nulls = Nulls.AS_EMPTY) String cursor,
                              @JsonSetter(nulls = Nulls.AS_EMPTY) @Nonnull List<Device> devices,
                              @Nullable OffsetDateTime fetchedUntil, boolean moreToFollow) {}
