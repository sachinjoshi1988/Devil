package com.devil.core.runtime.memory

import com.devil.core.runtime.learning.LearningResult
import com.devil.core.runtime.learning.LearningStatus

/**
 * Default fail-closed core Memory Proposal evidence port.
 *
 * No production Memory Proposal evidence mechanism is configured inside core
 * runtime.
 *
 * Therefore:
 *
 * - LEARNABLE Learning remains DEFERRED rather than being fabricated as Memory
 *   Proposal evidence;
 * - DEFERRED Learning remains DEFERRED;
 * - FAILED Learning preserves its matching operational error.
 *
 * A platform or other authorized embodiment may implement
 * MemoryProposalEvidencePort outside core and be injected through the normal
 * Unified Devil Runtime composition boundary.
 *
 * This default creates no Memory Proposal, invokes no Memory Authority,
 * commits no Memory, persists no Memory, mutates no world state, and invents
 * no evidence.
 */
class DefaultMemoryProposalEvidencePort :
    MemoryProposalEvidencePort {

    override fun establish(
        learning: LearningResult,
    ): MemoryProposalEvidenceResult {
        return when (learning.status) {
            LearningStatus.LEARNABLE -> {
                val request =
                    requireNotNull(learning.request)

                val capabilityId =
                    request.worldModelUpdate
                        .outcome
                        .verification
                        .observation
                        .execution
                        .capability
                        .capabilityId

                require(
                    capabilityId.value.isNotBlank(),
                ) {
                    "Learnable constitutional Learning must preserve one capability identity before Memory Proposal evidence may be attempted."
                }

                MemoryProposalEvidenceResult.create(
                    traceId = learning.traceId,
                    status =
                        MemoryProposalEvidenceStatus.DEFERRED,
                )
            }

            LearningStatus.DEFERRED ->
                MemoryProposalEvidenceResult.create(
                    traceId = learning.traceId,
                    status =
                        MemoryProposalEvidenceStatus.DEFERRED,
                )

            LearningStatus.FAILED ->
                MemoryProposalEvidenceResult.create(
                    traceId = learning.traceId,
                    status =
                        MemoryProposalEvidenceStatus.FAILED,
                    error = requireNotNull(learning.error),
                )
        }
    }
}
