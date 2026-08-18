package com.devil.core.runtime.research

/**
 * Stage 108 bounded status for constitutional Research evidence evaluation.
 *
 * ASSESSABLE means an approved constitutional research-evidence evaluation
 * mechanism established that one existing ResearchEvidenceSet may approach a
 * later governed research mechanism.
 *
 * UNAVAILABLE means the repository does not currently possess sufficient
 * approved evaluation policy to establish that affirmative state.
 *
 * FAILED represents one genuine operational evaluation failure.
 *
 * ASSESSABLE does not mean:
 *
 * - the supplied evidence is true;
 * - the supplied evidence is verified;
 * - a source is authentic;
 * - a source is trustworthy;
 * - information is factually fresh;
 * - conflicting evidence was resolved;
 * - consensus exists;
 * - synthesis occurred;
 * - a World Model update occurred;
 * - Learning occurred;
 * - Memory was created;
 * - authorization was granted;
 * - an action was executed;
 * - or a verified Outcome exists.
 *
 * RESEARCH_EVIDENCE_ASSESSABLE != TRUE.
 * RESEARCH_EVIDENCE_ASSESSABLE != VERIFIED.
 * RESEARCH_EVIDENCE_ASSESSABLE != CONSENSUS.
 * RESEARCH_EVIDENCE_ASSESSABLE != SYNTHESIS.
 */
enum class ResearchEvidenceEvaluationStatus {
    ASSESSABLE,
    UNAVAILABLE,
    FAILED,
}
