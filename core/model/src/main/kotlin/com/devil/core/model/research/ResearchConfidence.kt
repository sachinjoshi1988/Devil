package com.devil.core.model.research

/**
 * Stage 111 bounded confidence associated with one existing constitutional
 * Research corroboration assessment.
 *
 * Research confidence ranges from 0 to 100 inclusive.
 *
 * This value represents only explicitly supplied bounded confidence in the
 * research assessment preserved by Stage 110.
 *
 * It does not establish:
 *
 * - factual truth;
 * - factual correctness;
 * - source authenticity;
 * - source trust;
 * - factual freshness;
 * - source independence;
 * - factual Verification;
 * - conflict resolution;
 * - consensus;
 * - a synthesized research conclusion;
 * - World Model state;
 * - Learning;
 * - Memory;
 * - authorization;
 * - execution;
 * - or verified Outcome.
 *
 * RESEARCH_CONFIDENCE != TRUTH.
 * RESEARCH_CONFIDENCE != VERIFICATION.
 * RESEARCH_CONFIDENCE != CONSENSUS.
 * RESEARCH_CONFIDENCE != SYNTHESIS.
 * RESEARCH_CONFIDENCE != MEMORY_CONFIDENCE.
 * RESEARCH_CONFIDENCE != IDENTITY_CONFIDENCE.
 */
@ConsistentCopyVisibility
data class ResearchConfidence private constructor(
    val value: Int,
) {
    companion object {

        fun from(
            rawValue: Int,
        ): ResearchConfidence {
            require(rawValue in 0..100) {
                "Research confidence must be between 0 and 100 inclusive."
            }

            return ResearchConfidence(
                value = rawValue,
            )
        }
    }
}
