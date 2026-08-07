package com.devil.core.runtime.memory

import com.devil.core.model.memory.MemoryAuthorityRequest

/**
 * Default Stage 19 Memory Authority request provider.
 *
 * A request is available only when constitutional memory-proposal evaluation
 * produced a PROPOSABLE MemoryProposalResult containing one bounded
 * MemoryProposalRequest.
 *
 * Deferred proposal evaluation remains unavailable. Proposal failure preserves
 * its matching error.
 *
 * This implementation does not approve, create, persist, or commit logical
 * memory. It does not assign memory class, sensitivity, retention policy,
 * confidence, source, owner-visible reason, or storage destination.
 *
 * It does not mutate world state, change task or plan state, communicate
 * externally, bypass constitutional security review, or produce a runtime
 * result.
 */
class DefaultMemoryAuthorityRequestProvider :
    MemoryAuthorityRequestProvider {

    override fun provide(
        proposal: MemoryProposalResult,
    ): MemoryAuthorityRequestResult {
        return when (proposal.status) {
            MemoryProposalStatus.PROPOSABLE ->
                MemoryAuthorityRequestResult.create(
                    traceId = proposal.traceId,
                    status = MemoryAuthorityRequestStatus.AVAILABLE,
                    request = MemoryAuthorityRequest.create(
                        proposal = requireNotNull(proposal.request),
                    ),
                )

            MemoryProposalStatus.DEFERRED ->
                MemoryAuthorityRequestResult.create(
                    traceId = proposal.traceId,
                    status = MemoryAuthorityRequestStatus.UNAVAILABLE,
                )

            MemoryProposalStatus.FAILED ->
                MemoryAuthorityRequestResult.create(
                    traceId = proposal.traceId,
                    status = MemoryAuthorityRequestStatus.FAILED,
                    error = requireNotNull(proposal.error),
                )
        }
    }
}
