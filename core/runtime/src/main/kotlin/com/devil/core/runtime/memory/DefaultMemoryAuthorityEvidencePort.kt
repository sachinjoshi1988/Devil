package com.devil.core.runtime.memory

/**
 * Default fail-closed core Memory Authority evidence port.
 *
 * No production Memory Authority evidence mechanism is configured inside core
 * runtime.
 *
 * Therefore:
 *
 * - PROPOSABLE Memory Proposal remains DEFERRED rather than being fabricated as
 *   Memory Authority evidence;
 * - DEFERRED Memory Proposal remains DEFERRED;
 * - FAILED Memory Proposal preserves its matching operational error.
 *
 * A platform or other authorized embodiment may implement
 * MemoryAuthorityEvidencePort outside core and inject it through the normal
 * Unified Devil Runtime composition boundary.
 *
 * This default approves no logical memory, commits no logical memory, persists
 * no logical memory, assigns no memory metadata, mutates no world state, and
 * invents no evidence.
 */
class DefaultMemoryAuthorityEvidencePort :
    MemoryAuthorityEvidencePort {

    override fun establish(
        memoryProposal: MemoryProposalResult,
    ): MemoryAuthorityEvidenceResult {
        return when (memoryProposal.status) {
            MemoryProposalStatus.PROPOSABLE -> {
                val request =
                    requireNotNull(memoryProposal.request)

                val capabilityId =
                    request.learning
                        .worldModelUpdate
                        .outcome
                        .verification
                        .observation
                        .execution
                        .capability
                        .capabilityId

                require(
                    capabilityId.value.isNotBlank(),
                ) {
                    "Proposable constitutional Memory Proposal must preserve one capability identity before Memory Authority evidence may be attempted."
                }

                MemoryAuthorityEvidenceResult.create(
                    traceId = memoryProposal.traceId,
                    status = MemoryAuthorityEvidenceStatus.DEFERRED,
                )
            }

            MemoryProposalStatus.DEFERRED ->
                MemoryAuthorityEvidenceResult.create(
                    traceId = memoryProposal.traceId,
                    status = MemoryAuthorityEvidenceStatus.DEFERRED,
                )

            MemoryProposalStatus.FAILED ->
                MemoryAuthorityEvidenceResult.create(
                    traceId = memoryProposal.traceId,
                    status = MemoryAuthorityEvidenceStatus.FAILED,
                    error = requireNotNull(memoryProposal.error),
                )
        }
    }
}
