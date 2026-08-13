package com.devil.core.runtime.memory

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Stable neutral result of bounded constitutional Memory Authority evidence
 * establishment.
 *
 * ESTABLISHED preserves one capability identity and one nonblank bounded
 * evidence description.
 *
 * DEFERRED contains neither evidence nor error.
 *
 * FAILED contains one matching operational error and no Memory Authority
 * evidence.
 *
 * This result does not approve logical memory, commit logical memory, persist
 * logical memory, assign memory metadata, mutate world state, or bypass the
 * single Memory Authority.
 */
@ConsistentCopyVisibility
data class MemoryAuthorityEvidenceResult private constructor(
    val traceId: TraceId,
    val status: MemoryAuthorityEvidenceStatus,
    val capabilityId: CapabilityId?,
    val description: String?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryAuthorityEvidenceStatus,
            capabilityId: CapabilityId? = null,
            description: String? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryAuthorityEvidenceResult {
            val normalizedDescription =
                description?.trim()

            when (status) {
                MemoryAuthorityEvidenceStatus.ESTABLISHED -> {
                    require(
                        capabilityId != null &&
                            !normalizedDescription.isNullOrEmpty() &&
                            error == null,
                    ) {
                        "Established Memory Authority evidence results require a capability identity and nonblank description and must not contain an error."
                    }
                }

                MemoryAuthorityEvidenceStatus.DEFERRED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error == null,
                    ) {
                        "Deferred Memory Authority evidence results must not contain capability identity, description, or error."
                    }
                }

                MemoryAuthorityEvidenceStatus.FAILED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error != null,
                    ) {
                        "Failed Memory Authority evidence results require an error and must not contain Memory Authority evidence."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Memory Authority evidence result and error must use the same trace identity."
            }

            return MemoryAuthorityEvidenceResult(
                traceId = traceId,
                status = status,
                capabilityId = capabilityId,
                description = normalizedDescription,
                error = error,
            )
        }
    }
}
