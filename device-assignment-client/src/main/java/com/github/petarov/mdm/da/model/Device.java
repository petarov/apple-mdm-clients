package com.github.petarov.mdm.da.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @param assetTag           the device's asset tag
 * @param color              the color of the device
 * @param description        a description of the device
 * @param deviceAssignedBy   the email of the person who assigned the device
 * @param deviceAssignedDate a time stamp in ISO 8601 format that indicates when the device was assigned to the MDM server
 * @param deviceFamily       the device's Apple product family: {@code iPad}, {@code iPhone}, {@code iPod}, {@code Mac},
 *                           or {@code AppleTV}. This key is valid in <i>X-Server-Protocol-Version 2</i> and later.
 * @param model              the model name
 * @param opDate             a time stamp in ISO 8601 format that indicates when the device was added, updated, or
 *                           deleted. If the value of {@link #opType()} is added, this is the same as
 *                           {@link #deviceAssignedDate()}. This field is only applicable with the
 *                           <i>Sync the List of Devices</i> command. TODO: link client method
 * @param opType             indicates whether the device was added (assigned to the MDM server), modified, or deleted.
 *                           Contains one of the following strings: added, modified, or deleted. This field is only
 *                           applicable with the <i>Sync the List of Devices</i> command. TODO: link client method
 * @param os                 the device's operating system: {@code iOS}, {@code OSX}, or {@code tvOS}. This key is valid
 *                           in <i>X-Server-Protocol-Version 2</i> and later.
 * @param profileAssignTime  a time stamp in ISO 8601 format that indicates when a profile was assigned to the device
 * @param profilePushTime    a time stamp in ISO 8601 format that indicates when a profile was pushed to the device
 * @param profileStatus      the status of profile installation — either {@code empty}, {@code assigned}, {@code pushed}
 *                           or {@code removed}.
 * @param profileUuid        the unique ID of the assigned profile
 * @param serialNumber       the device's serial number
 * @param responseStatus     a string indicating whether a particular device's data could be retrieved, either
 *                           {@code SUCCESS} or {@code NOT_FOUND}. Available after calling <i>>fetchDeviceDetails.</i> TODO: link client method
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Device(@JsonSetter(nulls = Nulls.AS_EMPTY) String assetTag,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String color,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String description,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceAssignedBy,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceAssignedDate,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String deviceFamily,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String model,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String opDate,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String opType, @JsonSetter(nulls = Nulls.AS_EMPTY) String os,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String profileAssignTime,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String profilePushTime,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String profileStatus,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String profileUuid,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String serialNumber,
                     @JsonSetter(nulls = Nulls.AS_EMPTY) String responseStatus) {}
