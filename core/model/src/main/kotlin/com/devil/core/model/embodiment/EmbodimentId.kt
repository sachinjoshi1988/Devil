package com.devil.core.model.embodiment

/**
 * Identifies one bounded embodiment of the single Devil intelligence.
 *
 * An embodiment identity distinguishes one architectural host or adapter
 * boundary from another. It does not create another Devil identity, Brain,
 * Constitution, Executive, Planner, Memory Authority, Security Architecture,
 * or Unified Devil Runtime.
 *
 * Possessing an EmbodimentId does not establish:
 *
 * - subject identity;
 * - trust;
 * - authentication;
 * - authorization;
 * - session validity;
 * - Owner Mode;
 * - capability registration;
 * - capability availability;
 * - capability health;
 * - platform permission;
 * - execution approval;
 * - observation;
 * - verification;
 * - Outcome;
 * - Memory eligibility;
 * - or persistence authority.
 *
 * EMBODIMENT_IDENTITY != DEVIL_IDENTITY.
 */
@ConsistentCopyVisibility
data class EmbodimentId private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): EmbodimentId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Embodiment identity must not be blank."
            }

            return EmbodimentId(
                value = normalizedValue,
            )
        }
    }
}
