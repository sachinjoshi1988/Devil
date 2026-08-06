package com.devil.core.runtime.memory

import com.devil.core.model.memory.MemoryProposalRequest
import com.devil.core.runtime.learning.LearningResult
import com.devil.core.runtime.learning.LearningStatus

/**
 * Default Stage 18 constitutional memory-proposal-request provider.
 *
 * A request is available only when constitutional learning evaluation produced
 * a LEARNABLE LearningResult containing one bounded LearningRequest.
 *
 * Deferred learning remains unavailable. Learning failure preserves its
 * matching error.
 *
 * This implementation does not create a memory proposal, approve or commit
 * logical memory, mutate world state, change task or plan state, communicate
 * externally, bypass the single Memory Authority, or produce a runtime result.
 */
class DefaultMemoryProposalRequestProvider :
    MemoryProposalRequestProvider {

    override fun provide(
        learning: LearningResult,
    ): MemoryProposalRequestResult {
        return when (learning.status) {
            LearningStatus.LEARNABLE ->
                MemoryProposalRequestResult.create(
                    traceId = learning.traceId,
                    status = MemoryProposalRequestStatus.AVAILABLE,
                    request = MemoryProposalRequest.create(
                        learning = requireNotNull(learning.request),
                    ),
                )

            LearningStatus.DEFERRED ->
                MemoryProposalRequestResult.create(
                    traceId = learning.traceId,
                    status = MemoryProposalRequestStatus.UNAVAILABLE,
                )

            LearningStatus.FAILED ->
                MemoryProposalRequestResult.create(
                    traceId = learning.traceId,
                    status = MemoryProposalRequestStatus.FAILED,
                    error = requireNotNull(learning.error),
                )
        }
    }
}
