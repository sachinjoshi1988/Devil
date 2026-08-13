package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.learning.LearningRequest

/**
 * Evaluates one bounded constitutional Learning request together with genuine
 * bounded Learning evidence.
 *
 * ESTABLISHED Learning evidence is necessary before an evaluator may determine
 * that one bounded Learning proposal is constitutionally learnable.
 *
 * This evaluator contract grants no authority of its own. It must not create
 * Learning evidence, create Learning, propose Memory, invoke Memory Authority,
 * commit Memory, persist Memory, mutate world state, change task or plan state,
 * communicate externally, or produce a runtime result.
 *
 * WORLD_MODEL_UPDATE != LEARNING_EVIDENCE.
 * LEARNING_EVIDENCE != LEARNING.
 * LEARNING != MEMORY_PROPOSAL.
 */
interface LearningEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: LearningRequest,
        evidence: LearningEvidenceResult,
    ): LearningEvaluationResult
}
