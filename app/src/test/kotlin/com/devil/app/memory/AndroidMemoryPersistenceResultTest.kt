package com.devil.app.memory

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidMemoryPersistenceResultTest {

    @Test
    fun `create preserves persisted result without error`() {
        val traceId =
            TraceId.from(
                "trace-android-memory-result-001",
            )

        val result =
            AndroidMemoryPersistenceResult.create(
                traceId = traceId,
                status =
                    AndroidMemoryPersistenceStatus.PERSISTED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            AndroidMemoryPersistenceStatus.PERSISTED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without error`() {
        val traceId =
            TraceId.from(
                "trace-android-memory-result-002",
            )

        val result =
            AndroidMemoryPersistenceResult.create(
                traceId = traceId,
                status =
                    AndroidMemoryPersistenceStatus.DEFERRED,
            )

        assertEquals(
            AndroidMemoryPersistenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId =
            TraceId.from(
                "trace-android-memory-result-003",
            )
        val error = createError(traceId)

        val result =
            AndroidMemoryPersistenceResult.create(
                traceId = traceId,
                status =
                    AndroidMemoryPersistenceStatus.FAILED,
                error = error,
            )

        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidMemoryPersistenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-memory-result-004",
                    ),
                status =
                    AndroidMemoryPersistenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects persisted result with error`() {
        val traceId =
            TraceId.from(
                "trace-android-memory-result-005",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidMemoryPersistenceResult.create(
                traceId = traceId,
                status =
                    AndroidMemoryPersistenceStatus.PERSISTED,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed error from different trace`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidMemoryPersistenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-memory-result-006",
                    ),
                status =
                    AndroidMemoryPersistenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-android-memory-result-other",
                        ),
                    ),
            )
        }
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "ANDROID_MEMORY_PERSISTENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_301_000L,
                ),
            summary =
                "Android logical-memory persistence failed.",
        )
    }
}
