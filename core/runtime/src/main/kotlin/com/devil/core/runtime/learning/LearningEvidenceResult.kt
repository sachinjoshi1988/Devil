package com.devil.core.runtime.learning

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Neutral platform-independent result of one bounded constitutional
 * Learning-evidence attempt.
 *
 * ESTABLISHED preserves the exact capability identity whose constitutional
 * World Model update contributed to the potential Learning evaluation together
 * with one nonblank description of the genuine bounded evidence.
 *
 * The description represents evidence only. It does not create Learning,
 * propose Memory, invoke Memory Authority, commit Memory, persist Memory,
 * mutate world state, or report broader task or plan completion.
 *
 * DEFERRED contains neither capability identity, description, nor error.
 *
 * FAILED contains one matching operational error and no Learning evidence.
 */
@ConsistentCopyVisibility
data class LearningEvidenceResult private constructor(
    val traceId: TraceId,
    val status: LearningEvidenceStatus,
    val capabilityId: CapabilityId?,
    val description: String?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: LearningEvidenceStatus,
            capabilityId: CapabilityId? = null,
            description: String? = null,
            error: UniversalErrorRecord? = null,
        ): LearningEvidenceResult {
            val normalizedDescription =
                description?.trim()

            when (status) {
                LearningEvidenceStatus.ESTABLISHED -> {
                    require(
                        capabilityId != null &&
                            !normalizedDescription.isNullOrEmpty() &&
                            error == null,
                    ) {
                        "Established Learning-evidence results require a capability identity and nonblank description and must not contain an error."
                    }
                }

                LearningEvidenceStatus.DEFERRED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error == null,
                    ) {
                        "Deferred Learning-evidence results must not contain capability identity, description, or error."
                    }
                }

                LearningEvidenceStatus.FAILED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error != null,
                    ) {
                        "Failed Learning-evidence results require an error and must not contain Learning evidence."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Learning-evidence result and error must use the same trace identity."
            }

            return LearningEvidenceResult(
                traceId = traceId,
                status = status,
                capabilityId = capabilityId,
                description = normalizedDescription,
                error = error,
            )
        }
    }
}
