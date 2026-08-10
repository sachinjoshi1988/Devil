package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId

/**
 * Immutable Stage 43 descriptive profile for one Devil owner identity.
 *
 * The profile may preserve bounded owner-facing presentation information that
 * has already been supplied through an approved source.
 *
 * It does not determine whether the current subject is that owner.
 *
 * Owner profile
 * != identity resolution
 * != authentication
 * != ownership proof
 * != trust
 * != relationship authority
 * != authorization
 * != Owner Mode
 * != memory commitment
 * != permission to act.
 *
 * Profile information must never be used as authentication evidence merely
 * because it matches information known about the owner.
 */
@ConsistentCopyVisibility
data class OwnerProfile private constructor(
    val ownerIdentityId: IdentityId,
    val displayName: String?,
    val preferredFormOfAddress: String?,
) {
    companion object {

        fun create(
            ownerIdentityId: IdentityId,
            displayName: String? = null,
            preferredFormOfAddress: String? = null,
        ): OwnerProfile {
            return OwnerProfile(
                ownerIdentityId = ownerIdentityId,
                displayName =
                    normalizeOptionalText(
                        value = displayName,
                    ),
                preferredFormOfAddress =
                    normalizeOptionalText(
                        value = preferredFormOfAddress,
                    ),
            )
        }

        private fun normalizeOptionalText(
            value: String?,
        ): String? {
            return value
                ?.trim()
                ?.takeIf {
                    it.isNotEmpty()
                }
        }
    }
}
