package com.devil.core.runtime.memory

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Neutral constitutional evidence result between Learning and Memory Proposal.
 *
 * ESTABLISHED preserves the exact capability identity whose bounded Learning
 * path received genuine Memory Proposal evidence together with a nonblank
 * evidence description.
 *
 * Preserving Memory Proposal evidence does not create a Memory Proposal,
 * invoke or bypass Memory Authority, commit Memory, persist Memory, mutate
 * world state, or report completion.
 *
 * DEFERRED contains neither capability identity, description, nor error.
 *
 * FAILED contains one matching operational error and no Memory Proposal
 * evidence.
 */
@ConsistentCopyVisibility
data class MemoryProposalEvidenceResult private constructor(
    val traceId: TraceId,
    val status: MemoryProposalEvidenceStatus,
    val capabilityId: CapabilityId?,
    val description: String?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryProposalEvidenceStatus,
            capabilityId: CapabilityId? = null,
            description: String? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryProposalEvidenceResult {
            val normalizedDescription =
                description?.trim()

            when (status) {
                MemoryProposalEvidenceStatus.ESTABLISHED -> {
                    require(
                        capabilityId != null &&
                            !normalizedDescription.isNullOrEmpty() &&
                            error == null,
                    ) {
                        "Established Memory Proposal evidence results require a capability identity and nonblank description and must not contain an error."
                    }
                }

                MemoryProposalEvidenceStatus.DEFERRED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error == null,
                    ) {
                        "Deferred Memory Proposal evidence results must not contain capability identity, description, or error."
                    }
                }

                MemoryProposalEvidenceStatus.FAILED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error != null,
                    ) {
                        "Failed Memory Proposal evidence results require an error and must not contain Memory Proposal evidence."
                    }
                }
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Memory Proposal evidence result and error must use the same trace identity."
            }

            return MemoryProposalEvidenceResult(
                traceId = traceId,
                status = status,
                capabilityId = capabilityId,
                description = normalizedDescription,
                error = error,
            )
        }
    }
}
