package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * Stage 296 direct unit coverage for the existing Stage 119
 * CompoundWorkExecutiveReentryResult contract.
 *
 * Factory invariants only. No production behavior is modified.
 * Stage 297 Integration Test Completion is out of scope.
 */
class Stage296CompoundWorkExecutiveReentryResultTest {

    @Test
    fun `deferred result preserves trace and contains no record`() {
        val traceId =
            TraceId.from(
                "trace-stage296-executive-reentry-deferred",
            )

        val result =
            CompoundWorkExecutiveReentryResult.create(
                traceId = traceId,
                status = CompoundWorkExecutiveReentryStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CompoundWorkExecutiveReentryStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
    }

    @Test
    fun `prepared result rejects missing executive reentry record`() {
        assertFailsWith<IllegalArgumentException> {
            CompoundWorkExecutiveReentryResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage296-executive-reentry-invalid",
                    ),
                status = CompoundWorkExecutiveReentryStatus.PREPARED,
            )
        }
    }
}
