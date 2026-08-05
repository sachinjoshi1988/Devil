package com.devil.core.runtime.plan

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.plan.PlanId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PlanIdentityProvisionResultTest {

    @Test
    fun `create preserves available result with plan identity`() {
        val traceId = TraceId.from(
            "trace-plan-identity-result-001",
        )
        val planId = PlanId.from(
            "plan-identity-result-001",
        )

        val result = PlanIdentityProvisionResult.create(
            traceId = traceId,
            status = PlanIdentityProvisionStatus.AVAILABLE,
            planId = planId,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanIdentityProvisionStatus.AVAILABLE,
            result.status,
        )
        assertEquals(planId, result.planId)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without identity or error`() {
        val traceId = TraceId.from(
            "trace-plan-identity-result-002",
        )

        val result = PlanIdentityProvisionResult.create(
            traceId = traceId,
            status = PlanIdentityProvisionStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            PlanIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.planId)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-plan-identity-result-003",
        )
        val error = createError(traceId)

        val result = PlanIdentityProvisionResult.create(
            traceId = traceId,
            status = PlanIdentityProvisionStatus.FAILED,
            error = error,
        )

        assertEquals(
            PlanIdentityProvisionStatus.FAILED,
            result.status,
        )
        assertNull(result.planId)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without plan identity`() {
        assertFailsWith<IllegalArgumentException> {
            PlanIdentityProvisionResult.create(
                traceId = TraceId.from(
                    "trace-plan-identity-result-004",
                ),
                status = PlanIdentityProvisionStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available result with error`() {
        val traceId = TraceId.from(
            "trace-plan-identity-result-005",
        )

        assertFailsWith<IllegalArgumentException> {
            PlanIdentityProvisionResult.create(
                traceId = traceId,
                status = PlanIdentityProvisionStatus.AVAILABLE,
                planId = PlanId.from(
                    "plan-identity-result-005",
                ),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with plan identity`() {
        assertFailsWith<IllegalArgumentException> {
            PlanIdentityProvisionResult.create(
                traceId = TraceId.from(
                    "trace-plan-identity-result-006",
                ),
                status = PlanIdentityProvisionStatus.UNAVAILABLE,
                planId = PlanId.from(
                    "plan-identity-result-006",
                ),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            PlanIdentityProvisionResult.create(
                traceId = TraceId.from(
                    "trace-plan-identity-result-007",
                ),
                status = PlanIdentityProvisionStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            PlanIdentityProvisionResult.create(
                traceId = TraceId.from(
                    "trace-plan-identity-result-008",
                ),
                status = PlanIdentityProvisionStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-plan-identity-error-other",
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
                "PLAN_IDENTITY_PROVISION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_083_500L,
                ),
            summary = "Plan identity provision failed.",
        )
    }
}
