package com.devil.core.model.research

/**
 * Immutable Stage 107 collection of bounded research evidence.
 *
 * The set preserves already-established ResearchEvidence objects without
 * rewriting, combining, summarizing, ranking, reconciling, or interpreting
 * their descriptions.
 *
 * Multiple evidence items may disagree.
 *
 * Preserving disagreement is intentional. Conflict resolution belongs to a
 * later governed Research Intelligence stage.
 *
 * A ResearchEvidenceSet does not establish:
 *
 * - source independence;
 * - source trust;
 * - factual truth;
 * - factual freshness;
 * - corroboration;
 * - consensus;
 * - confidence;
 * - a synthesized research conclusion;
 * - constitutional Verification;
 * - World Model state;
 * - Learning;
 * - Memory;
 * - authorization;
 * - execution;
 * - or verified success.
 *
 * EVIDENCE_SET != CONSENSUS.
 * EVIDENCE_SET != SYNTHESIS.
 * EVIDENCE_SET != VERIFIED_FACT.
 */
@ConsistentCopyVisibility
data class ResearchEvidenceSet private constructor(
    val evidence: List<ResearchEvidence>,
) {
    companion object {

        fun create(
            evidence: List<ResearchEvidence>,
        ): ResearchEvidenceSet {
            require(evidence.isNotEmpty()) {
                "Research evidence set requires at least one evidence item."
            }

            return ResearchEvidenceSet(
                evidence = evidence.toList(),
            )
        }
    }
}
