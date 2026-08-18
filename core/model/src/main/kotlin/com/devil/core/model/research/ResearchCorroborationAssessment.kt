package com.devil.core.model.research

/**
 * Immutable Stage 110 bounded Research corroboration and conflict assessment.
 *
 * The exact Stage 109 ResearchSourceAssessmentSet is preserved.
 *
 * corroboration and conflict values must already have been explicitly
 * established by an upstream governed mechanism. This contract does not infer
 * either value from source count, source trust, authenticity, freshness, prose,
 * majority agreement, or any other heuristic.
 *
 * Creating this assessment does not:
 *
 * - alter Stage 107 ResearchEvidence;
 * - alter Stage 109 source assessments;
 * - rank sources;
 * - calculate source weight;
 * - infer source independence;
 * - establish factual truth;
 * - establish factual Verification;
 * - resolve conflicting material;
 * - select a winning claim;
 * - create consensus;
 * - calculate confidence;
 * - synthesize a research conclusion;
 * - mutate World Model state;
 * - perform Learning;
 * - create, commit, persist, or recall Memory;
 * - grant authorization;
 * - execute an action;
 * - or establish verified success.
 *
 * CORROBORATION_ASSESSMENT != FACT_VERIFICATION.
 * CONFLICT_ASSESSMENT != CONFLICT_RESOLUTION.
 * CORROBORATION_ASSESSMENT != CONSENSUS.
 * CORROBORATION_ASSESSMENT != SYNTHESIS.
 */
@ConsistentCopyVisibility
data class ResearchCorroborationAssessment private constructor(
    val sourceAssessmentSet: ResearchSourceAssessmentSet,
    val corroboration: ResearchCorroborationStatus,
    val conflict: ResearchConflictStatus,
) {
    companion object {

        fun create(
            sourceAssessmentSet: ResearchSourceAssessmentSet,
            corroboration: ResearchCorroborationStatus,
            conflict: ResearchConflictStatus,
        ): ResearchCorroborationAssessment {
            return ResearchCorroborationAssessment(
                sourceAssessmentSet = sourceAssessmentSet,
                corroboration = corroboration,
                conflict = conflict,
            )
        }
    }
}
