package com.devil.app.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Default Stage 30 Android execution performer.
 *
 * Stage 30 establishes the first safe Android execution-adapter boundary, but
 * no production capability-to-platform-action implementation has yet been
 * approved.
 *
 * Therefore this performer truthfully returns DEFERRED rather than selecting
 * an Android API, launching an Intent, mutating device state, or fabricating an
 * execution attempt.
 *
 * Later bounded execution adapters may replace this performer only for
 * explicitly registered and constitutionally governed capabilities.
 */
class DefaultAndroidExecutionPerformer : AndroidExecutionPerformer {

    override fun perform(
        traceId: TraceId,
        request: ExecutionRequest,
    ): AndroidExecutionAttemptResult {
        require(
            request.plan.task.decision.understanding.context.traceId ==
                traceId,
        ) {
            "Android execution performer trace and request must use the same trace identity."
        }

        return AndroidExecutionAttemptResult.create(
            traceId = traceId,
            status = AndroidExecutionAttemptStatus.DEFERRED,
        )
    }
}
