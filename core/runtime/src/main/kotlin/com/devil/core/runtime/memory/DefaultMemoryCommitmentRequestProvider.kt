package com.devil.core.runtime.memory

import com.devil.core.model.memory.MemoryCommitmentRequest

/**
 * Default Stage 20 logical-memory commitment request provider.
 *
 * A request is available only when the single Memory Authority has produced a
 * COMMITTABLE MemoryAuthorityResult containing one bounded
 * MemoryAuthorityRequest.
 *
 * A deferred Memory Authority result remains unavailable. Memory Authority
 * failure preserves its matching error.
 *
 * COMMITTABLE is only commitment eligibility. This implementation does not
 * create, persist, store, expose, recall, or commit logical memory.
 *
 * It does not assign memory class, sensitivity, confidence, retention policy,
 * source attribution, owner-visible reason, storage destination, or deletion
 * policy.
 *
 * It does not invoke a database, filesystem, cloud service, Android platform
 * API, or external communication mechanism. It does not mutate world state,
 * change task or plan state, bypass the single Memory Authority, or produce a
 * runtime result.
 */
class DefaultMemoryCommitmentRequestProvider :
    MemoryCommitmentRequestProvider {

    override fun provide(
        memory: MemoryAuthorityResult,
    ): MemoryCommitmentRequestResult {
        return when (memory.status) {
            MemoryAuthorityStatus.COMMITTABLE ->
                MemoryCommitmentRequestResult.create(
                    traceId = memory.traceId,
                    status =
                        MemoryCommitmentRequestStatus.AVAILABLE,
                    request =
                        MemoryCommitmentRequest.create(
                            authorityRequest =
                                requireNotNull(memory.request),
                        ),
                )

            MemoryAuthorityStatus.DEFERRED ->
                MemoryCommitmentRequestResult.create(
                    traceId = memory.traceId,
                    status =
                        MemoryCommitmentRequestStatus.UNAVAILABLE,
                )

            MemoryAuthorityStatus.FAILED ->
                MemoryCommitmentRequestResult.create(
                    traceId = memory.traceId,
                    status =
                        MemoryCommitmentRequestStatus.FAILED,
                    error = requireNotNull(memory.error),
                )
        }
    }
}
