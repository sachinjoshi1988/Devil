package com.devil.core.model.embodiment

/**
 * Immutable Stage 81 representation of one bounded Devil embodiment.
 *
 * The record represents architectural embodiment identity only:
 *
 * - one embodiment identity;
 * - one platform identity;
 * - and one nonblank human-readable description.
 *
 * Every EmbodimentRecord remains an adapter/host representation around the
 * same constitutionally governed Devil intelligence.
 *
 * This record deliberately contains no:
 *
 * - Brain;
 * - Constitution;
 * - Executive;
 * - Planner;
 * - Memory Authority;
 * - Security Authority;
 * - session;
 * - authorization result;
 * - capability registration;
 * - capability availability or health;
 * - Task or Plan;
 * - execution request;
 * - execution attempt;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - Memory commitment;
 * - persistence authority;
 * - platform permission;
 * - or automatic runtime-entry authority.
 *
 * EMBODIMENT != INTELLIGENCE.
 * EMBODIMENT != CAPABILITY.
 * EMBODIMENT != AUTHORIZATION.
 * EMBODIMENT != EXECUTION.
 */
@ConsistentCopyVisibility
data class EmbodimentRecord private constructor(
    val embodimentId: EmbodimentId,
    val platformId: EmbodimentPlatformId,
    val description: String,
) {
    companion object {

        fun create(
            embodimentId: EmbodimentId,
            platformId: EmbodimentPlatformId,
            description: String,
        ): EmbodimentRecord {
            val normalizedDescription =
                description.trim()

            require(normalizedDescription.isNotEmpty()) {
                "Embodiment description must not be blank."
            }

            return EmbodimentRecord(
                embodimentId = embodimentId,
                platformId = platformId,
                description = normalizedDescription,
            )
        }
    }
}
