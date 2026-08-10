package com.devil.core.model.child

/**
 * Stage 44 bounded status describing guardian-authority evidence supplied by an
 * approved authority source.
 *
 * ESTABLISHED means only that an approved upstream mechanism supplied an
 * established guardian-authority relationship for the bounded context carrying
 * this status.
 *
 * Constructing or possessing this enum does not itself prove guardian authority.
 *
 * Guardian authority
 * != family relationship
 * != identity resolution
 * != authentication
 * != subject trust
 * != Owner Mode
 * != general authorization
 * != Android permission
 * != execution approval.
 */
enum class GuardianAuthorityStatus {
    ESTABLISHED,
    NOT_ESTABLISHED,
    UNKNOWN,
}
