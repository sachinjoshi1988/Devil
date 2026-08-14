package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.worldmodel.WorldModelRepresentation
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Represents the stable operational result of constitutional World Model
 * update evaluation.
 *
 * An applicable result preserves one WorldModelUpdateRequest for which genuine
 * constitutional update evidence was established and may preserve the bounded
 * evidence-backed WorldModelRepresentation produced during evaluation.
 *
 * During the bounded Stage 73 migration, representation remains nullable so
 * existing synthetic callers can be migrated before the stronger APPLICABLE
 * invariant is enforced.
 *
 * Preserving a request or representation does not mutate world state, claim
 * that state changed, change task or plan state, create memory or learning,
 * communicate externally, or bypass unified runtime handling.
 *
 * A deferred result contains neither request, representation, nor error.
 * A failed result contains one matching error.
 *
 * WORLD_MODEL_REPRESENTATION != WORLD_STATE_MUTATION.
 * WORLD_MODEL_REPRESENTATION != MEMORY.
 */
@ConsistentCopyVisibility
data class WorldModelUpdateResult private constructor(
    val traceId: TraceId,
    val status: WorldModelUpdateStatus,
    val request: WorldModelUpdateRequest?,
    val representation: WorldModelRepresentation?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: WorldModelUpdateStatus,
            request: WorldModelUpdateRequest? = null,
            representation: WorldModelRepresentation? = null,
            error: UniversalErrorRecord? = null,
        ): WorldModelUpdateResult {
            when (status) {
                WorldModelUpdateStatus.APPLICABLE -> {
                    require(
                        request != null &&
                            error == null,
                    ) {
                        "Applicable World Model update results require a request and must not contain an error."
                    }
                }

                WorldModelUpdateStatus.DEFERRED -> {
                    require(
                        request == null &&
                            representation == null &&
                            error == null,
                    ) {
                        "Deferred World Model update results must not contain a request, representation, or error."
                    }
                }

                WorldModelUpdateStatus.FAILED -> {
                    require(
                        request == null &&
                            representation == null &&
                            error != null,
                    ) {
                        "Failed World Model update results require an error and must not contain a request or representation."
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
                "World Model update result and request must use the same trace identity."
            }

            require(
                representation == null ||
                    representation.traceId == traceId,
            ) {
                "World Model update result and representation must use the same trace identity."
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
                "World Model update request and representation must use the same capability identity."
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "World Model update result and error must use the same trace identity."
            }

            return WorldModelUpdateResult(
                traceId = traceId,
                status = status,
                request = request,
                representation = representation,
                error = error,
            )
        }
    }
}
