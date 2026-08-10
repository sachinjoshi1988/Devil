package com.devil.core.model.owner

/**
 * Result of one Stage 43 bounded owner-profile structural update evaluation.
 *
 * ACCEPTED contains exactly one derived snapshot and no rejection reason.
 *
 * REJECTED contains the unchanged original snapshot and one bounded reason.
 *
 * The result does not persist either snapshot.
 */
@ConsistentCopyVisibility
data class OwnerProfileUpdateResult private constructor(
    val status: OwnerProfileUpdateStatus,
    val snapshot: OwnerProfileSnapshot,
    val reason: String?,
) {
    companion object {

        fun accepted(
            snapshot: OwnerProfileSnapshot,
        ): OwnerProfileUpdateResult {
            return OwnerProfileUpdateResult(
                status = OwnerProfileUpdateStatus.ACCEPTED,
                snapshot = snapshot,
                reason = null,
            )
        }

        fun rejected(
            snapshot: OwnerProfileSnapshot,
            reason: String,
        ): OwnerProfileUpdateResult {
            val normalizedReason =
                reason.trim()

            require(normalizedReason.isNotEmpty()) {
                "Rejected owner-profile update requires a reason."
            }

            return OwnerProfileUpdateResult(
                status = OwnerProfileUpdateStatus.REJECTED,
                snapshot = snapshot,
                reason = normalizedReason,
            )
        }
    }
}
