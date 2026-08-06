package com.devil.core.runtime.outcome

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.outcome.OutcomeRequest

/**
 * Represents the stable operational result of constitutional outcome
 * evaluation.
 *
 * An established result preserves one OutcomeRequest for which genuine
 * constitutional outcome evidence was established. Preserving that request
 * does not update world state, change task or plan state, create memory or
 * learning, communicate externally, or bypass unified runtime handling.
 *
 * A deferred result contains neither request nor error. A failed result contains
 * one matching error.
 */
@ConsistentCopyVisibility
data class OutcomeResult private constructor(
    val traceId: TraceId,
    val status: OutcomeStatus,
    val request: OutcomeRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: OutcomeStatus,
            request: OutcomeRequest? = null,
            error: UniversalErrorRecord? = null,
        ): OutcomeResult {
            when (status) {
                OutcomeStatus.ESTABLISHED -> {
                    require(request != null && error == null) {
                        "Established outcome results require a request and must not contain an error."
                    }
                }

                OutcomeStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred outcome results must not contain a request or error."
                    }
                }

                OutcomeStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed outcome results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.verification
                        .observation
                        .execution
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Outcome result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Outcome result and error must use the same trace identity."
            }

            return OutcomeResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
