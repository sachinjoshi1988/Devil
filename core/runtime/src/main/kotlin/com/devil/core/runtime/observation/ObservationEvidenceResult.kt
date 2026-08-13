package com.devil.core.runtime.observation

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Neutral platform-independent result of one bounded observation-evidence
 * attempt.
 *
 * OBSERVED preserves the exact capability identity for which genuine evidence
 * was produced together with a nonblank bounded description of that evidence.
 *
 * The description is evidence representation only. It grants no authority and
 * establishes no verification or Outcome.
 *
 * DEFERRED contains neither capability identity, description, nor error.
 *
 * FAILED contains one matching operational error and no observation evidence.
 */
@ConsistentCopyVisibility
data class ObservationEvidenceResult private constructor(
    val traceId: TraceId,
    val status: ObservationEvidenceStatus,
    val capabilityId: CapabilityId?,
    val description: String?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ObservationEvidenceStatus,
            capabilityId: CapabilityId? = null,
            description: String? = null,
            error: UniversalErrorRecord? = null,
        ): ObservationEvidenceResult {
            val normalizedDescription =
                description?.trim()

            when (status) {
                ObservationEvidenceStatus.OBSERVED -> {
                    require(
                        capabilityId != null &&
                            !normalizedDescription.isNullOrEmpty() &&
                            error == null,
                    ) {
                        "Observed evidence results require a capability identity and nonblank description and must not contain an error."
                    }
                }

                ObservationEvidenceStatus.DEFERRED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error == null,
                    ) {
                        "Deferred observation-evidence results must not contain capability identity, description, or error."
                    }
                }

                ObservationEvidenceStatus.FAILED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error != null,
                    ) {
                        "Failed observation-evidence results require an error and must not contain observation evidence."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Observation-evidence result and error must use the same trace identity."
            }

            return ObservationEvidenceResult(
                traceId = traceId,
                status = status,
                capabilityId = capabilityId,
                description = normalizedDescription,
                error = error,
            )
        }
    }
}
