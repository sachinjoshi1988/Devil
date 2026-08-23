package com.devil.core.runtime.personality

import com.devil.core.model.personality.DevilCorePersonalityRecord

/**
 * Stage 245 bounded Devil Core Personality result.
 *
 * ESTABLISHED contains exactly one DevilCorePersonalityRecord.
 *
 * DEFERRED contains no Stage 245 core-personality record.
 *
 * This result does not:
 *
 * - replace the Stage 244 Personality Foundation V2 record;
 * - implement Stage 246 Relationship Continuity;
 * - implement Stage 247 Adaptive Communication Style;
 * - implement Stage 248 Humor & Social Interaction;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience;
 * - create another Devil, Brain, Constitution, or authority;
 * - make decisions;
 * - grant authorization;
 * - execute actions;
 * - establish verified truth;
 * - mutate World Model state;
 * - perform Learning;
 * - write or persist Memory.
 *
 * CORE_PERSONALITY != AUTHORITY.
 * CORE_PERSONALITY != DECISION.
 * CORE_PERSONALITY != AUTHORIZATION.
 * CORE_PERSONALITY != EXECUTION.
 * CORE_PERSONALITY != VERIFICATION.
 * CORE_PERSONALITY != MEMORY.
 */
@ConsistentCopyVisibility
data class DevilCorePersonalityResult private constructor(
    val status: DevilCorePersonalityStatus,
    val corePersonality: DevilCorePersonalityRecord?,
) {
    companion object {

        fun create(
            status: DevilCorePersonalityStatus,
            corePersonality: DevilCorePersonalityRecord? = null,
        ): DevilCorePersonalityResult {
            return when (status) {
                DevilCorePersonalityStatus.ESTABLISHED -> {
                    requireNotNull(corePersonality) {
                        "Established Stage 245 Devil Core Personality requires a core-personality record."
                    }

                    DevilCorePersonalityResult(
                        status = status,
                        corePersonality = corePersonality,
                    )
                }

                DevilCorePersonalityStatus.DEFERRED -> {
                    require(corePersonality == null) {
                        "Deferred Stage 245 Devil Core Personality must not contain a core-personality record."
                    }

                    DevilCorePersonalityResult(
                        status = status,
                        corePersonality = null,
                    )
                }
            }
        }
    }
}
