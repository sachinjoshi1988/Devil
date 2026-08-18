package com.devil.core.model.research

/**
 * Immutable Stage 111 bounded Research confidence assessment.
 *
 * The exact Stage 110 ResearchCorroborationAssessment remains attached so its
 * Stage 107 evidence, Stage 109 source assessments, and Stage 110 corroboration
 * and conflict provenance remain intact.
 *
 * confidence must already have been explicitly established by an upstream
 * governed mechanism before construction.
 *
 * This contract does not calculate confidence from:
 *
 * - source count;
 * - majority agreement;
 * - source authenticity;
 * - source trust;
 * - source freshness;
 * - corroboration status;
 * - conflict status;
 * - prose similarity;
 * - or any other heuristic.
 *
 * Creating this assessment does not:
 *
 * - alter Research evidence;
 * - alter source assessments;
 * - rank or weight sources;
 * - infer source independence;
 * - establish factual truth;
 * - perform factual Verification;
 * - resolve conflicting material;
 * - create consensus;
 * - synthesize a research conclusion;
 * - mutate World Model state;
 * - perform Learning;
 * - create, commit, persist, or recall Memory;
 * - grant authorization;
 * - execute actions;
 * - or establish verified success.
 *
 * CONFIDENCE_ASSESSMENT != VERIFIED_FACT.
 * CONFIDENCE_ASSESSMENT != CONSENSUS.
 * CONFIDENCE_ASSESSMENT != SYNTHESIS.
 */
@ConsistentCopyVisibility
data class ResearchConfidenceAssessment private constructor(
    val corroborationAssessment: ResearchCorroborationAssessment,
    val confidence: ResearchConfidence,
) {
    companion object {

        fun create(
            corroborationAssessment: ResearchCorroborationAssessment,
            confidence: ResearchConfidence,
        ): ResearchConfidenceAssessment {
            return ResearchConfidenceAssessment(
                corroborationAssessment = corroborationAssessment,
                confidence = confidence,
            )
        }
    }
}
