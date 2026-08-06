package com.devil.core.runtime.learning

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.learning.LearningRequest

/**
 * Represents the stable operational result of constitutional learning
 * evaluation.
 *
 * A learnable result preserves one LearningRequest for which genuine
 * constitutional learning evidence was established. Preserving that request
 * does not create learning, create or commit memory, mutate world state, change
 * task or plan state, communicate externally, or bypass unified runtime
 * handling.
 *
 * A deferred result contains neither request nor error. A failed result
 * contains one matching error.
 */
@ConsistentCopyVisibility
data class LearningResult private constructor(
    val traceId: TraceId,
    val status: LearningStatus,
    val request: LearningRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: LearningStatus,
            request: LearningRequest? = null,
            error: UniversalErrorRecord? = null,
        ): LearningResult {
            when (status) {
                LearningStatus.LEARNABLE -> {
                    require(request != null && error == null) {
                        "Learnable learning results require a request and must not contain an error."
                    }
                }

                LearningStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred learning results must not contain a request or error."
                    }
                }

                LearningStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed learning results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.worldModelUpdate
                        .outcome
                        .verification
                        .observation
                        .execution
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Learning result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Learning result and error must use the same trace identity."
            }

            return LearningResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
