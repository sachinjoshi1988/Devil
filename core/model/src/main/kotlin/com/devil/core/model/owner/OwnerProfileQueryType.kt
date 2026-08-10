package com.devil.core.model.owner

/**
 * Stage 43 bounded owner-profile query operations.
 *
 * Queries expose descriptive owner-profile state only.
 *
 * Query type
 * != identity resolution
 * != authentication
 * != trust
 * != guardian authority
 * != authorization
 * != Owner Mode
 * != memory authority
 * != execution.
 */
enum class OwnerProfileQueryType {
    PROFILE,
    PREFERRED_FORM_OF_ADDRESS,
    RELATIONSHIP_FOR_SUBJECT,
}
