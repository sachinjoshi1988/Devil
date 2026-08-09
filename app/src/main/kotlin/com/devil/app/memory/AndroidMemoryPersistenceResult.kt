package com.devil.app.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the stable Android result of controlled logical-memory persistence.
 *
 * PERSISTED is an evidence-bearing Android storage result. It must never be
 * produced merely because the core runtime marked a MemoryPersistenceRequest
 * PERSISTABLE.
 *
 * DEFERRED contains no error because no persistence attempt was justified.
 *
 * FAILED contains one matching error.
 */
@ConsistentCopyVisibility
data class AndroidMemoryPersistenceResult private constructor(
    val traceId: TraceId,
    val status: AndroidMemoryPersistenceStatus,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: AndroidMemoryPersistenceStatus,
            error: UniversalErrorRecord? = null,
        ): AndroidMemoryPersistenceResult {
            when (status) {
                AndroidMemoryPersistenceStatus.PERSISTED,
                AndroidMemoryPersistenceStatus.DEFERRED,
                -> {
                    require(error == null) {
                        "Persisted and deferred Android memory persistence results must not contain an error."
                    }
                }

                AndroidMemoryPersistenceStatus.FAILED -> {
                    require(error != null) {
                        "Failed Android memory persistence results require an error."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Android memory persistence result and error must use the same trace identity."
            }

            return AndroidMemoryPersistenceResult(
                traceId = traceId,
                status = status,
                error = error,
            )
        }
    }
}
