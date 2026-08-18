package com.devil.core.model.task

import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState

/**
 * Immutable Stage 115 request representing one exact Stage 114 continuation
 * candidate that has passed into one fresh constitutional reasoning cycle.
 *
 * The request preserves:
 *
 * - one exact Stage 114 CompoundWorkContinuationRecord;
 * - its exact Stage 77 CompoundWorkRequest and CompoundWorkStep transitively;
 * - and one fresh selected constitutional Decision.
 *
 * The fresh Decision must belong to a different constitutional trace from the
 * originating compound-work Decision.
 *
 * This request does not infer relationship from Decision summary text.
 *
 * Creating this request does not:
 *
 * - grant authorization;
 * - create or mutate a Task;
 * - create or mutate a Plan;
 * - select a capability;
 * - establish capability availability or readiness;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute;
 * - establish Observation;
 * - establish Verification;
 * - establish Outcome;
 * - mutate compound-work state;
 * - automatically continue compound work;
 * - grant Controlled Autonomy;
 * - mutate World Model state;
 * - perform Learning;
 * - operate Memory;
 * - invoke a platform;
 * - or invoke a network.
 *
 * ORIGINAL_DECISION != FRESH_RECONSIDERATION_DECISION.
 * RECONSIDERATION_REQUEST != AUTHORIZATION.
 * RECONSIDERATION_REQUEST != TASK_CREATION.
 * RECONSIDERATION_REQUEST != AUTOMATIC_CONTINUATION.
 * RECONSIDERATION_REQUEST != CONTROLLED_AUTONOMY.
 */
@ConsistentCopyVisibility
data class CompoundWorkReconsiderationRequest private constructor(
    val continuation: CompoundWorkContinuationRecord,
    val freshDecision: DecisionRecord,
) {
    companion object {

        fun create(
            continuation: CompoundWorkContinuationRecord,
            freshDecision: DecisionRecord,
        ): CompoundWorkReconsiderationRequest {
            require(freshDecision.state == DecisionState.SELECTED) {
                "Compound-work reconsideration requires one fresh selected constitutional Decision."
            }

            val originalTraceId =
                continuation.request
                    .decision
                    .understanding
                    .context
                    .traceId

            val freshTraceId =
                freshDecision
                    .understanding
                    .context
                    .traceId

            require(freshTraceId != originalTraceId) {
                "Compound-work reconsideration requires a fresh constitutional trace distinct from the originating compound-work trace."
            }

            return CompoundWorkReconsiderationRequest(
                continuation = continuation,
                freshDecision = freshDecision,
            )
        }
    }
}
