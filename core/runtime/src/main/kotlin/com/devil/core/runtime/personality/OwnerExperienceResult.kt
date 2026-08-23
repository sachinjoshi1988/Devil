package com.devil.core.runtime.personality

import com.devil.core.model.personality.OwnerExperienceRecord

/**
 * Stage 250 bounded Owner Experience result.
 *
 * ESTABLISHED contains exactly one OwnerExperienceRecord.
 *
 * DEFERRED contains no owner-experience record.
 *
 * This result does not authenticate, authorize, enter Owner Mode,
 * establish High-Security Confirmation, generate responses, render UI,
 * synthesize voice, execute actions, establish verified truth, or
 * create/persist Memory.
 *
 * OWNER_EXPERIENCE != AUTHORITY.
 * OWNER_EXPERIENCE != AUTHENTICATION.
 * OWNER_EXPERIENCE != OWNER_MODE.
 * OWNER_EXPERIENCE != AUTHORIZATION.
 * OWNER_EXPERIENCE != DECISION.
 * OWNER_EXPERIENCE != EXECUTION.
 * OWNER_EXPERIENCE != VERIFICATION.
 * OWNER_EXPERIENCE != MEMORY.
 */
@ConsistentCopyVisibility
data class OwnerExperienceResult private constructor(
    val status: OwnerExperienceStatus,
    val experience: OwnerExperienceRecord?,
) {
    companion object {

        fun create(
            status: OwnerExperienceStatus,
            experience: OwnerExperienceRecord? = null,
        ): OwnerExperienceResult {
            return when (status) {
                OwnerExperienceStatus.ESTABLISHED -> {
                    requireNotNull(experience) {
                        "Established Stage 250 Owner Experience requires an owner-experience record."
                    }

                    OwnerExperienceResult(
                        status = status,
                        experience = experience,
                    )
                }

                OwnerExperienceStatus.DEFERRED -> {
                    require(experience == null) {
                        "Deferred Stage 250 Owner Experience must not contain an owner-experience record."
                    }

                    OwnerExperienceResult(
                        status = status,
                        experience = null,
                    )
                }
            }
        }
    }
}
