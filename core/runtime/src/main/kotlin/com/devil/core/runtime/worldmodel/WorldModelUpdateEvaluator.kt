package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Evaluates one bounded constitutional World Model update request together with
 * genuine World Model update evidence.
 *
 * A request alone is insufficient to make a World Model update applicable.
 *
 * ESTABLISHED World Model update evidence is necessary before an evaluator may
 * produce APPLICABLE.
 *
 * This evaluator does not mutate World Model state, claim that world state
 * changed, perform Learning, propose Memory, commit Memory, persist Memory, or
 * create another authority or runtime.
 *
 * OUTCOME != WORLD_MODEL_UPDATE_EVIDENCE.
 * WORLD_MODEL_UPDATE_EVIDENCE != WORLD_MODEL_UPDATE.
 * WORLD_MODEL_UPDATE != WORLD_STATE_CHANGED.
 */
fun interface WorldModelUpdateEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: WorldModelUpdateRequest,
        evidence: WorldModelUpdateEvidenceResult,
    ): WorldModelUpdateEvaluationResult
}
