package com.devil.core.runtime.worldmodel

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Neutral platform-independent result of one bounded constitutional World Model
 * update-evidence attempt.
 *
 * ESTABLISHED preserves the exact capability identity whose constitutional
 * Outcome contributed to the proposed World Model update together with one
 * nonblank description of the genuine bounded evidence.
 *
 * The description represents evidence only. It does not mutate the World Model,
 * claim that world state changed, perform Learning, propose Memory, commit
 * Memory, persist Memory, or report broader task or plan completion.
 *
 * DEFERRED contains neither capability identity, description, nor error.
 *
 * FAILED contains one matching operational error and no World Model update
 * evidence.
 */
@ConsistentCopyVisibility
data class WorldModelUpdateEvidenceResult private constructor(
    val traceId: TraceId,
    val status: WorldModelUpdateEvidenceStatus,
    val capabilityId: CapabilityId?,
    val description: String?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: WorldModelUpdateEvidenceStatus,
            capabilityId: CapabilityId? = null,
            description: String? = null,
            error: UniversalErrorRecord? = null,
        ): WorldModelUpdateEvidenceResult {
            val normalizedDescription =
                description?.trim()

            when (status) {
                WorldModelUpdateEvidenceStatus.ESTABLISHED -> {
                    require(
                        capabilityId != null &&
                            !normalizedDescription.isNullOrEmpty() &&
                            error == null,
                    ) {
                        "Established World Model update-evidence results require a capability identity and nonblank description and must not contain an error."
                    }
                }

                WorldModelUpdateEvidenceStatus.DEFERRED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error == null,
                    ) {
                        "Deferred World Model update-evidence results must not contain capability identity, description, or error."
                    }
                }

                WorldModelUpdateEvidenceStatus.FAILED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error != null,
                    ) {
                        "Failed World Model update-evidence results require an error and must not contain World Model update evidence."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "World Model update-evidence result and error must use the same trace identity."
            }

            return WorldModelUpdateEvidenceResult(
                traceId = traceId,
                status = status,
                capabilityId = capabilityId,
                description = normalizedDescription,
                error = error,
            )
        }
    }
}
