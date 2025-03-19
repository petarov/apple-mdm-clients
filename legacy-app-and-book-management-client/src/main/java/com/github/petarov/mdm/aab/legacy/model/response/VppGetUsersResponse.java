package com.github.petarov.mdm.aab.legacy.model.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.petarov.mdm.aab.legacy.model.VppUser;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * The response from the users’ details service.
 *
 * @param response        {@link VppResponse}
 * @param batchCount      the number of users returned in the current batch
 * @param batchToken      use this batchToken value in subsequent requests to get the next batch. A batchToken value is
 *                        generated by the server and can be several kilobytes in size.
 * @param totalBatchCount the total number of round trips needed to get the complete result set
 * @param totalCount      an estimate of the total number of users that will be returned. This value is returned for
 *                        requests that don't include batchToken and for the request that started the batch process
 *                        (the listing request issued with no tokens). The actual number of users returned can be
 *                        different by the time the client has finished retrieving all records.
 * @param users           a list of users managed by the provided sToken
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/getvppusersresponse">GetVppUsersResponse</a>
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/retrieving-a-large-record-set#Batched-Responses">Batched Responses</a>
 */
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record VppGetUsersResponse(@JsonUnwrapped VppResponse response, int batchCount, String batchToken,
                                  String totalBatchCount, int totalCount, @Nonnull List<VppUser> users) {}
