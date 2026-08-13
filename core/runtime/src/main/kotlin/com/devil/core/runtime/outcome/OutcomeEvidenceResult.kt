package com.devil.core.runtime.outcome

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Neutral platform-independent result of one bounded constitutional
 * outcome-evidence attempt.
 *
 * ESTABLISHED preserves the exact capability identity for which genuine bounded
 * outcome evidence was produced together with one nonblank description of that
 * evidence.
 *
 * The description represents evidence only. It grants no authority, establishes
 * no task or plan completion, updates no World Model state, performs no Learning,
 * and commits no Memory.
 *
 * DEFERRED contains neither capability identity, description, nor error.
 *
 * FAILED contains one matching operational error and no outcome evidence.
 */
@ConsistentCopyVisibility
data class OutcomeEvidenceResult private constructor(
    val traceId: TraceId,
    val status: OutcomeEvidenceStatus,
    val capabilityId: CapabilityId?,
    val description: String?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: OutcomeEvidenceStatus,
            capabilityId: CapabilityId? = null,
            description: String? = null,
            error: UniversalErrorRecord? = null,
        ): OutcomeEvidenceResult {
            val normalizedDescription =
                description?.trim()

            when (status) {
                OutcomeEvidenceStatus.ESTABLISHED -> {
                    require(
                        capabilityId != null &&
                            !normalizedDescription.isNullOrEmpty() &&
                            error == null,
                    ) {
                        "Established outcome-evidence results require a capability identity and nonblank description and must not contain an error."
                    }
                }

                OutcomeEvidenceStatus.DEFERRED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error == null,
                    ) {
                        "Deferred outcome-evidence results must not contain capability identity, description, or error."
                    }
                }

                OutcomeEvidenceStatus.FAILED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error != null,
                    ) {
                        "Failed outcome-evidence results require an error and must not contain outcome evidence."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Outcome-evidence result and error must use the same trace identity."
            }

            return OutcomeEvidenceResult(
                traceId = traceId,
                status = status,
                capabilityId = capabilityId,
                description = normalizedDescription,
                error = error,
            )
        }
    }
}
