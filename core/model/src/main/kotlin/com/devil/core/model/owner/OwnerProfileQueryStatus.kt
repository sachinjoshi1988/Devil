package com.devil.core.model.owner

/**
 * Stage 43 bounded owner-profile query status.
 *
 * AVAILABLE means the requested descriptive information exists in the supplied
 * OwnerProfileSnapshot.
 *
 * UNAVAILABLE means the snapshot contains no value for that query.
 *
 * AVAILABLE
 * != authenticated
 * != trusted
 * != authorized
 * != guardian approved
 * != persisted
 * != verified Outcome.
 */
enum class OwnerProfileQueryStatus {
    AVAILABLE,
    UNAVAILABLE,
}
