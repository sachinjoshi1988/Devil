package com.devil.core.runtime.personality

import com.devil.core.model.personality.PersonalityFoundationRecord

/**
 * Stage 244 bounded Personality Foundation V2 result.
 *
 * ESTABLISHED contains exactly one PersonalityFoundationRecord.
 *
 * DEFERRED contains no personality foundation.
 *
 * This result does not:
 *
 * - implement Stage 245 Devil Core Personality;
 * - establish relationship continuity;
 * - adapt communication style;
 * - generate humor or social interaction;
 * - establish persona presentation;
 * - implement Owner Experience;
 * - create another Devil, Brain, Constitution, or authority;
 * - make decisions;
 * - authorize or execute actions;
 * - establish verified truth;
 * - mutate World Model state;
 * - perform Learning;
 * - write or persist Memory.
 *
 * PERSONALITY_FOUNDATION != AUTHORITY.
 * PERSONALITY_FOUNDATION != DECISION.
 * PERSONALITY_FOUNDATION != AUTHORIZATION.
 * PERSONALITY_FOUNDATION != EXECUTION.
 * PERSONALITY_FOUNDATION != VERIFICATION.
 * PERSONALITY_FOUNDATION != MEMORY.
 */
@ConsistentCopyVisibility
data class PersonalityFoundationResult private constructor(
    val status: PersonalityFoundationStatus,
    val foundation: PersonalityFoundationRecord?,
) {
    companion object {

        fun create(
            status: PersonalityFoundationStatus,
            foundation: PersonalityFoundationRecord? = null,
        ): PersonalityFoundationResult {
            return when (status) {
                PersonalityFoundationStatus.ESTABLISHED -> {
                    requireNotNull(foundation) {
                        "Established Stage 244 Personality Foundation requires a personality foundation record."
                    }

                    PersonalityFoundationResult(
                        status = status,
                        foundation = foundation,
                    )
                }

                PersonalityFoundationStatus.DEFERRED -> {
                    require(foundation == null) {
                        "Deferred Stage 244 Personality Foundation must not contain a personality foundation record."
                    }

                    PersonalityFoundationResult(
                        status = status,
                        foundation = null,
                    )
                }
            }
        }
    }
}
