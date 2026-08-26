package com.devil.core.runtime.goal

import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * Stage 296 direct unit coverage for the existing Stage 78
 * LongRunningGoalPreparationResult contract.
 *
 * Factory invariants only. No production behavior is modified.
 * Stage 297 Integration Test Completion is out of scope.
 */
class Stage296LongRunningGoalPreparationResultTest {

    @Test
    fun `deferred result preserves trace and contains no goal`() {
        val traceId =
            TraceId.from(
                "trace-stage296-long-running-goal-deferred",
            )

        val result =
            LongRunningGoalPreparationResult.create(
                traceId = traceId,
                status = LongRunningGoalPreparationStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            LongRunningGoalPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.goal)
    }

    @Test
    fun `prepared result rejects missing goal`() {
        assertFailsWith<IllegalArgumentException> {
            LongRunningGoalPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage296-long-running-goal-invalid",
                    ),
                status = LongRunningGoalPreparationStatus.PREPARED,
            )
        }
    }
}
