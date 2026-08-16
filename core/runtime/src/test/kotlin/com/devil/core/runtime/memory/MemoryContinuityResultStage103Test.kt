package com.devil.core.runtime.memory

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryClass
import com.devil.core.model.memory.MemoryConfidence
import com.devil.core.model.memory.MemoryContinuityRecord
import com.devil.core.model.memory.MemoryId
import com.devil.core.model.memory.MemoryRetention
import com.devil.core.model.memory.MemorySensitivity
import com.devil.core.model.memory.MemorySource
import com.devil.core.model.memory.OwnerVisibleMemoryReason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class MemoryContinuityResultStage103Test {

    @Test
    fun `established result preserves exact continuity record`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-result-established",
            )

        val record =
            createContinuityRecord()

        val result =
            MemoryContinuityResult.create(
                traceId = traceId,
                status =
                    MemoryContinuityStatus.ESTABLISHED,
                record = record,
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            MemoryContinuityStatus.ESTABLISHED,
            result.status,
        )

        assertSame(
            record,
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `deferred result contains no continuity state`() {
        val result =
            MemoryContinuityResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-103-result-deferred",
                    ),
                status =
                    MemoryContinuityStatus.DEFERRED,
            )

        assertEquals(
            MemoryContinuityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `established result requires continuity record`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryContinuityResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-103-result-missing-record",
                    ),
                status =
                    MemoryContinuityStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `failed result requires matching upstream error`() {
        val traceId =
            TraceId.from(
                "trace-stage-103-result-failed",
            )

        val error =
            createError(
                traceId = traceId,
                code =
                    "MEMORY_CONTINUITY_STAGE_103_FAILED",
            )

        val result =
            MemoryContinuityResult.create(
                traceId = traceId,
                status =
                    MemoryContinuityStatus.FAILED,
                error = error,
            )

        assertEquals(
            MemoryContinuityStatus.FAILED,
            result.status,
        )

        assertEquals(
            error,
            result.error,
        )

        assertNull(
            result.record,
        )
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryContinuityResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-103-result-primary",
                    ),
                status =
                    MemoryContinuityStatus.FAILED,
                error =
                    createError(
                        traceId =
                            TraceId.from(
                                "trace-stage-103-result-other",
                            ),
                        code =
                            "MEMORY_CONTINUITY_STAGE_103_OTHER_TRACE",
                    ),
            )
        }
    }

    private fun createContinuityRecord(): MemoryContinuityRecord {
        return MemoryContinuityRecord.create(
            representation =
                LogicalMemoryRepresentation.create(
                    memoryId =
                        MemoryId.from(
                            "memory-stage-103-result",
                        ),
                    subjectIdentityId =
                        IdentityId.from(
                            "subject-stage-103-result",
                        ),
                    memoryClass =
                        MemoryClass.SEMANTIC,
                    sensitivity =
                        MemorySensitivity.PRIVATE,
                    confidence =
                        MemoryConfidence.from(91),
                    retention =
                        MemoryRetention.LONG_TERM,
                    source =
                        MemorySource.create(
                            sourceId =
                                "source-stage-103-result",
                            sourceType =
                                "explicit-stage-103-test-source",
                        ),
                    ownerVisibleReason =
                        OwnerVisibleMemoryReason.from(
                            "Stage 103 continuity result reason.",
                        ),
                    content =
                        "Stage 103 continuity result content.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    code,
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_755_000_103_100L,
                ),
            summary =
                "Stage 103 bounded continuity dependency failed.",
        )
    }
}
