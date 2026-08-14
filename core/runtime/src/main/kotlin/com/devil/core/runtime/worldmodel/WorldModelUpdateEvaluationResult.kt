package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.worldmodel.WorldModelRepresentation
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Represents the bounded result of constitutional World Model update
 * evaluation.
 *
 * An applicable result preserves the evaluated WorldModelUpdateRequest and may
 * preserve one evidence-backed WorldModelRepresentation produced from genuine
 * established World Model update evidence.
 *
 * During the bounded Stage 73 migration, representation remains nullable so
 * existing constitutional callers can be migrated before the stronger
 * APPLICABLE invariant is enforced.
 *
 * An unavailable result contains neither request, representation, nor error.
 * A failed result contains one matching error.
 *
 * Preserving a request or representation does not itself mutate world state,
 * claim that world state changed, change task or plan state, create memory or
 * learning, communicate externally, or produce a runtime result.
 */
@ConsistentCopyVisibility
data class WorldModelUpdateEvaluationResult private constructor(
    val traceId: TraceId,
    val status: WorldModelUpdateEvaluationStatus,
    val request: WorldModelUpdateRequest?,
    val representation: WorldModelRepresentation?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: WorldModelUpdateEvaluationStatus,
            request: WorldModelUpdateRequest? = null,
            representation: WorldModelRepresentation? = null,
            error: UniversalErrorRecord? = null,
        ): WorldModelUpdateEvaluationResult {
            when (status) {
                WorldModelUpdateEvaluationStatus.APPLICABLE -> {
                    require(
                        request != null &&
                            error == null,
                    ) {
                        "Applicable World Model update evaluation results require a request and must not contain an error."
                    }
                }

                WorldModelUpdateEvaluationStatus.UNAVAILABLE -> {
                    require(
                        request == null &&
                            representation == null &&
                            error == null,
                    ) {
                        "Unavailable World Model update evaluation results must not contain a request, representation, or error."
                    }
                }

                WorldModelUpdateEvaluationStatus.FAILED -> {
                    require(
                        request == null &&
                            representation == null &&
                            error != null,
                    ) {
                        "Failed World Model update evaluation results require an error and must not contain a request or representation."
                    }
                }
            }

            require(
                request == null ||
                    request.outcome
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
                "World Model update evaluation result and request must use the same trace identity."
            }

            require(
                representation == null ||
                    representation.traceId == traceId,
            ) {
                "World Model update evaluation result and representation must use the same trace identity."
            }

            require(
                request == null ||
                    representation == null ||
                    representation.capabilityId ==
                    request.outcome
                        .verification
                        .observation
                        .execution
                        .capability
                        .capabilityId,
            ) {
                "World Model update evaluation request and representation must use the same capability identity."
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "World Model update evaluation result and error must use the same trace identity."
            }

            return WorldModelUpdateEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                representation = representation,
                error = error,
            )
        }
    }
}
