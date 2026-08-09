package com.devil.app.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.memory.MemoryPersistenceRequest

/**
 * Android storage boundary for one constitutionally eligible logical-memory
 * persistence request.
 *
 * Implementations must not treat eligibility as authorization to invent memory
 * metadata or bypass the single Memory Authority.
 *
 * A production implementation may report PERSISTED only after an explicitly
 * authorized durable operation succeeds and genuine persistence evidence exists.
 */
interface AndroidMemoryPersistenceStore {

    fun persist(
        traceId: TraceId,
        request: MemoryPersistenceRequest,
    ): AndroidMemoryPersistenceResult
}
