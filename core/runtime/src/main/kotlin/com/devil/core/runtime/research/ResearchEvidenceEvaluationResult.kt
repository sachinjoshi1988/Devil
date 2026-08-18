package com.devil.core.runtime.research

import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.research.ResearchEvidenceSet

/**
 * Immutable Stage 108 result of one bounded constitutional Research evidence
 * evaluation.
 *
 * The exact Stage 107 ResearchEvidenceSet remains attached to the result.
 *
 * This is important because one research-evidence set may deliberately preserve
 * multiple independent constitutional trace identities. Stage 108 therefore
 * does not invent one synthetic trace identity for the complete set.
 *
 * ASSESSABLE requires the supplied Stage 107 evidence set and no error.
 *
 * UNAVAILABLE preserves the supplied Stage 107 evidence set and no error.
 *
 * FAILED preserves the supplied Stage 107 evidence set together with one
 * genuine error whose trace identity belongs to at least one evidence item in
 * that set.
 *
 * Preserving or evaluating the set does not establish factual truth, factual
 * verification, source authenticity, source trust, freshness, consensus,
 * synthesis, World Model state, Learning, Memory, authorization, execution, or
 * verified Outcome.
 */
@ConsistentCopyVisibility
data class ResearchEvidenceEvaluationResult private constructor(
    val status: ResearchEvidenceEvaluationStatus,
    val evidenceSet: ResearchEvidenceSet,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            status: ResearchEvidenceEvaluationStatus,
            evidenceSet: ResearchEvidenceSet,
            error: UniversalErrorRecord? = null,
        ): ResearchEvidenceEvaluationResult {
            when (status) {
                ResearchEvidenceEvaluationStatus.ASSESSABLE,
                ResearchEvidenceEvaluationStatus.UNAVAILABLE,
                -> {
                    require(error == null) {
                        "Assessable or unavailable Research evidence evaluation must not contain an error."
                    }
                }

                ResearchEvidenceEvaluationStatus.FAILED -> {
                    require(error != null) {
                        "Failed Research evidence evaluation requires one error."
                    }

                    require(
                        evidenceSet.evidence.any { evidence ->
                            evidence.traceId == error.traceId
                        },
                    ) {
                        "Research evidence evaluation failure must use a trace identity preserved by the supplied evidence set."
                    }
                }
            }

            return ResearchEvidenceEvaluationResult(
                status = status,
                evidenceSet = evidenceSet,
                error = error,
            )
        }
    }
}
