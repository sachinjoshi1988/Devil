package com.devil.app.memory

import com.devil.core.runtime.memory.MemoryPersistenceResult
import com.devil.core.runtime.memory.MemoryPersistenceStatus

/**
 * Default Stage 26 coordinator between the core Memory Persistence Authority and
 * one bounded Android persistence store.
 *
 * Core PERSISTABLE means only that a bounded MemoryPersistenceRequest became
 * eligible to reach this Android boundary. It does not mean logical memory was
 * stored.
 *
 * The default Android store remains DEFERRED and performs no durable write.
 */
class DefaultAndroidMemoryPersistenceCoordinator(
    private val store: AndroidMemoryPersistenceStore =
        DefaultAndroidMemoryPersistenceStore(),
) : AndroidMemoryPersistenceCoordinator {

    override fun persist(
        memoryPersistence: MemoryPersistenceResult,
    ): AndroidMemoryPersistenceResult {
        return when (memoryPersistence.status) {
            MemoryPersistenceStatus.PERSISTABLE -> {
                val request =
                    requireNotNull(
                        memoryPersistence.request,
                    )

                val result =
                    store.persist(
                        traceId = memoryPersistence.traceId,
                        request = request,
                    )

                require(
                    result.traceId == memoryPersistence.traceId,
                ) {
                    "Core and Android memory persistence results must use the same trace identity."
                }

                result
            }

            MemoryPersistenceStatus.DEFERRED ->
                AndroidMemoryPersistenceResult.create(
                    traceId = memoryPersistence.traceId,
                    status = AndroidMemoryPersistenceStatus.DEFERRED,
                )

            MemoryPersistenceStatus.FAILED ->
                AndroidMemoryPersistenceResult.create(
                    traceId = memoryPersistence.traceId,
                    status = AndroidMemoryPersistenceStatus.FAILED,
                    error =
                        requireNotNull(
                            memoryPersistence.error,
                        ),
                )
        }
    }
}
