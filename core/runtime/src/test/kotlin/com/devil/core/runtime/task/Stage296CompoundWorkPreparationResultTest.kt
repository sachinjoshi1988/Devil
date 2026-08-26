package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * Stage 296 direct unit coverage for the existing Stage 77
 * CompoundWorkPreparationResult contract.
 *
 * Factory invariants only. No production behavior is modified.
 * Stage 297 Integration Test Completion is out of scope.
 */
class Stage296CompoundWorkPreparationResultTest {

    @Test
    fun `deferred result preserves trace and contains no request`() {
        val traceId =
            TraceId.from(
                "trace-stage296-compound-work-deferred",
            )

        val result =
            CompoundWorkPreparationResult.create(
                traceId = traceId,
                status = CompoundWorkPreparationStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CompoundWorkPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
    }

    @Test
    fun `prepared result rejects missing request`() {
        assertFailsWith<IllegalArgumentException> {
            CompoundWorkPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage296-compound-work-invalid",
                    ),
                status = CompoundWorkPreparationStatus.PREPARED,
            )
        }
    }
}
