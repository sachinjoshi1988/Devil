package com.devil.core.runtime.memory

import com.devil.core.model.memory.MemoryPersistenceRequest

/**
 * Default Stage 21 logical-memory persistence request provider.
 *
 * A request is available only when constitutional logical-memory commitment
 * evaluation has produced a COMMITTABLE MemoryCommitmentResult containing one
 * bounded MemoryCommitmentRequest.
 *
 * A deferred commitment result remains unavailable. Commitment failure
 * preserves its matching error.
 *
 * COMMITTABLE establishes only bounded persistence eligibility. This provider
 * does not create, persist, store, expose, recall, delete, or commit logical
 * memory.
 *
 * It does not assign or alter memory class, sensitivity, confidence, retention
 * policy, source attribution, owner-visible reason, storage destination,
 * deletion policy, encryption policy, replication policy, or any other
 * logical-memory metadata.
 *
 * It does not invoke a database, filesystem, cloud service, Android platform
 * API, network service, or external communication mechanism. It does not mutate
 * world state, change task or plan state, bypass the single Memory Authority,
 * or produce a runtime result.
 */
class DefaultMemoryPersistenceRequestProvider :
    MemoryPersistenceRequestProvider {

    override fun provide(
        commitment: MemoryCommitmentResult,
    ): MemoryPersistenceRequestResult {
        return when (commitment.status) {
            MemoryCommitmentStatus.COMMITTABLE ->
                MemoryPersistenceRequestResult.create(
                    traceId = commitment.traceId,
                    status =
                        MemoryPersistenceRequestStatus.AVAILABLE,
                    request =
                        MemoryPersistenceRequest.create(
                            commitmentRequest =
                                requireNotNull(commitment.request),
                        ),
                )

            MemoryCommitmentStatus.DEFERRED ->
                MemoryPersistenceRequestResult.create(
                    traceId = commitment.traceId,
                    status =
                        MemoryPersistenceRequestStatus.UNAVAILABLE,
                )

            MemoryCommitmentStatus.FAILED ->
                MemoryPersistenceRequestResult.create(
                    traceId = commitment.traceId,
                    status =
                        MemoryPersistenceRequestStatus.FAILED,
                    error = requireNotNull(commitment.error),
                )
        }
    }
}
