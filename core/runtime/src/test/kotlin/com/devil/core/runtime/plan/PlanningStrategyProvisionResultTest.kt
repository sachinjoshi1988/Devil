package com.devil.core.runtime.plan

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PlanningStrategyProvisionResultTest {

    @Test
    fun `create preserves and normalizes available strategy`() {
        val traceId = TraceId.from(
            "trace-planning-strategy-result-001",
        )

        val result = PlanningStrategyProvisionResult.create(
            traceId = traceId,
            status = PlanningStrategyProvisionStatus.AVAILABLE,
            strategy =
                "  Use the constitutionally approved capability path.  ",
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanningStrategyProvisionStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            "Use the constitutionally approved capability path.",
            result.strategy,
        )
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without strategy or error`() {
        val traceId = TraceId.from(
            "trace-planning-strategy-result-002",
        )

        val result = PlanningStrategyProvisionResult.create(
            traceId = traceId,
            status = PlanningStrategyProvisionStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanningStrategyProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.strategy)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-planning-strategy-result-003",
        )
        val error = createError(traceId)

        val result = PlanningStrategyProvisionResult.create(
            traceId = traceId,
            status = PlanningStrategyProvisionStatus.FAILED,
            error = error,
        )

        assertEquals(
            PlanningStrategyProvisionStatus.FAILED,
            result.status,
        )
        assertNull(result.strategy)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without strategy`() {
        assertFailsWith<IllegalArgumentException> {
            PlanningStrategyProvisionResult.create(
                traceId = TraceId.from(
                    "trace-planning-strategy-result-004",
                ),
                status = PlanningStrategyProvisionStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available result with blank strategy`() {
        assertFailsWith<IllegalArgumentException> {
            PlanningStrategyProvisionResult.create(
                traceId = TraceId.from(
                    "trace-planning-strategy-result-005",
                ),
                status = PlanningStrategyProvisionStatus.AVAILABLE,
                strategy = "   ",
            )
        }
    }

    @Test
    fun `create rejects available result with error`() {
        val traceId = TraceId.from(
            "trace-planning-strategy-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            PlanningStrategyProvisionResult.create(
                traceId = traceId,
                status = PlanningStrategyProvisionStatus.AVAILABLE,
                strategy = "Use the approved capability path.",
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with strategy`() {
        assertFailsWith<IllegalArgumentException> {
            PlanningStrategyProvisionResult.create(
                traceId = TraceId.from(
                    "trace-planning-strategy-result-007",
                ),
                status = PlanningStrategyProvisionStatus.UNAVAILABLE,
                strategy = "Use the approved capability path.",
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            PlanningStrategyProvisionResult.create(
                traceId = TraceId.from(
                    "trace-planning-strategy-result-008",
                ),
                status = PlanningStrategyProvisionStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            PlanningStrategyProvisionResult.create(
                traceId = TraceId.from(
                    "trace-planning-strategy-result-009",
                ),
                status = PlanningStrategyProvisionStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-planning-strategy-error-other",
                    ),
                ),
            )
        }
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "PLANNING_STRATEGY_PROVISION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_082_500L,
                ),
            summary = "Planning strategy provision failed.",
        )
    }
}
