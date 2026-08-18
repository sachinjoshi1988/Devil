package com.devil.core.model.research

/**
 * Immutable Stage 109 bounded assessment of one existing ResearchEvidence item.
 *
 * The exact Stage 107 ResearchEvidence object is preserved.
 *
 * This type does not infer authenticity, trust, or freshness. Those values must
 * already have been explicitly established by an upstream governed mechanism
 * before construction.
 *
 * Creating an assessment does not:
 *
 * - alter the supplied research evidence;
 * - rank sources;
 * - establish factual truth;
 * - establish factual verification;
 * - establish corroboration;
 * - resolve conflicts;
 * - create consensus;
 * - synthesize conclusions;
 * - mutate World Model state;
 * - perform Learning;
 * - create or persist Memory;
 * - grant authorization;
 * - or execute actions.
 *
 * SOURCE_ASSESSED != TRUE.
 * SOURCE_ASSESSED != VERIFIED.
 * SOURCE_ASSESSED != CONSENSUS.
 * SOURCE_ASSESSED != SYNTHESIS.
 */
@ConsistentCopyVisibility
data class ResearchSourceAssessment private constructor(
    val evidence: ResearchEvidence,
    val authenticity: ResearchSourceAuthenticity,
    val trust: ResearchSourceTrust,
    val freshness: ResearchSourceFreshness,
) {
    companion object {

        fun create(
            evidence: ResearchEvidence,
            authenticity: ResearchSourceAuthenticity,
            trust: ResearchSourceTrust,
            freshness: ResearchSourceFreshness,
        ): ResearchSourceAssessment {
            return ResearchSourceAssessment(
                evidence = evidence,
                authenticity = authenticity,
                trust = trust,
                freshness = freshness,
            )
        }
    }
}
