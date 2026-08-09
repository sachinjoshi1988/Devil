package com.devil.app.memory

import com.devil.core.runtime.memory.MemoryPersistenceResult

/**
 * Bridges the core constitutional memory-persistence result into the Android
 * persistence boundary.
 *
 * The coordinator does not create memory, invent persistence eligibility,
 * assign memory metadata, grant authorization, or bypass the single Memory
 * Authority.
 */
interface AndroidMemoryPersistenceCoordinator {

    fun persist(
        memoryPersistence: MemoryPersistenceResult,
    ): AndroidMemoryPersistenceResult
}
