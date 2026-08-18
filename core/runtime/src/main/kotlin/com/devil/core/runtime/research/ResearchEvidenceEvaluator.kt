package com.devil.core.runtime.research

import com.devil.core.model.research.ResearchEvidenceSet

/**
 * Stage 108 platform-independent constitutional Research evidence evaluator.
 *
 * An evaluator may inspect only the already-established Stage 107
 * ResearchEvidenceSet supplied to this boundary.
 *
 * It must not fabricate evidence, erase conflicting evidence, invent source
 * trust, infer factual truth, manufacture freshness, resolve conflicts without
 * governed policy, synthesize conclusions, mutate World Model state, perform
 * Learning or Memory operations, authorize execution, or execute actions.
 */
interface ResearchEvidenceEvaluator {

    fun evaluate(
        evidenceSet: ResearchEvidenceSet,
    ): ResearchEvidenceEvaluationResult
}
