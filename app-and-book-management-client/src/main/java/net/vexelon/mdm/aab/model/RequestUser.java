package net.vexelon.mdm.aab.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * A user to create or update.
 *
 * @param clientUserId   the unique identifier the MDM assigns to the user
 * @param email          the email address of the user, or {@code null} to omit it from the request
 * @param managedAppleId the Managed Apple Account of the user, or {@code null} to omit it from the request
 * @see <a href="https://developer.apple.com/documentation/devicemanagement/requestuser">devicemanagement/requestuser</a>
 */
public record RequestUser(@Nonnull String clientUserId, @Nullable String email, @Nullable String managedAppleId) {}
