package com.devil.app.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryPersistenceRequest

/**
 * Default Stage 26 Android logical-memory persistence store boundary.
 *
 * No approved production memory classification, sensitivity assessment,
 * confidence policy, retention policy, source-attribution policy,
 * owner-visible reason, deletion policy, storage destination, encryption
 * policy, security-review mechanism, or authorized durable logical-memory
 * store is available yet.
 *
 * Therefore this implementation performs no filesystem, SharedPreferences,
 * DataStore, Room, SQLite, cloud, or network write and truthfully returns
 * DEFERRED.
 */
class DefaultAndroidMemoryPersistenceStore :
    AndroidMemoryPersistenceStore {

    override fun persist(
        traceId: TraceId,
        request: MemoryPersistenceRequest,
    ): AndroidMemoryPersistenceResult {
        require(
            request.commitmentRequest
                .authorityRequest
                .proposal
                .learning
                .worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .task
                .decision
                .understanding
                .context
                .traceId == traceId,
        ) {
            "Android memory persistence store trace and request must use the same trace identity."
        }

        return AndroidMemoryPersistenceResult.create(
            traceId = traceId,
            status = AndroidMemoryPersistenceStatus.DEFERRED,
        )
    }
}
