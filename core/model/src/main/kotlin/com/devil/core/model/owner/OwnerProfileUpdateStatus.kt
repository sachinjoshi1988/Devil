package com.devil.core.model.owner

/**
 * Stage 43 bounded structural owner-profile update result.
 *
 * ACCEPTED means only that one new transient OwnerProfileSnapshot was
 * structurally derived from the supplied request.
 *
 * REJECTED means the requested structural update could not be justified.
 *
 * ACCEPTED
 * != authenticated
 * != trusted
 * != authorized
 * != persisted
 * != committed memory
 * != execution success.
 */
enum class OwnerProfileUpdateStatus {
    ACCEPTED,
    REJECTED,
}
