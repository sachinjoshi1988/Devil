package com.devil.core.model.memory

/**
 * Represents bounded confidence associated with one logical-memory
 * representation.
 *
 * Confidence ranges from 0 to 100 inclusive.
 *
 * Memory confidence does not establish:
 *
 * - truth;
 * - factual correctness;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - identity confidence;
 * - authentication;
 * - authorization;
 * - Memory Authority approval;
 * - commitment;
 * - or persistence.
 *
 * MEMORY_CONFIDENCE != TRUTH.
 * MEMORY_CONFIDENCE != VERIFICATION.
 * MEMORY_CONFIDENCE != IDENTITY_CONFIDENCE.
 */
@ConsistentCopyVisibility
data class MemoryConfidence private constructor(
    val value: Int,
) {
    companion object {

        fun from(rawValue: Int): MemoryConfidence {
            require(rawValue in 0..100) {
                "Memory confidence must be between 0 and 100 inclusive."
            }

            return MemoryConfidence(
                value = rawValue,
            )
        }
    }
}
