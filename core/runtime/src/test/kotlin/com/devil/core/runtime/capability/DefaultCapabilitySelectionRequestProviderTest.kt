package com.devil.core.runtime.capability

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultCapabilitySelectionRequestProviderTest {

    @Test
    fun `provide returns available request for created plan`() {
        val plan = createCreatedPlan(
            traceValue = "trace-capability-provider-001",
            state = PlanState.CREATED,
        )
        val provider: CapabilitySelectionRequestProvider =
            DefaultCapabilitySelectionRequestProvider()

        val result = provider.provide(plan)

        assertEquals(plan.traceId, result.traceId)
        assertEquals(
            CapabilitySelectionRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            requireNotNull(plan.plan),
            requireNotNull(result.request).plan,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for waiting plan`() {
        val result =
            DefaultCapabilitySelectionRequestProvider().provide(
                createCreatedPlan(
                    traceValue = "trace-capability-provider-002",
                    state = PlanState.WAITING,
                ),
            )

        assertEquals(
            CapabilitySelectionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for ready plan`() {
        val result =
            DefaultCapabilitySelectionRequestProvider().provide(
                createCreatedPlan(
                    traceValue = "trace-capability-provider-003",
                    state = PlanState.READY,
                ),
            )

        assertEquals(
            CapabilitySelectionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for active plan`() {
        val result =
            DefaultCapabilitySelectionRequestProvider().provide(
                createCreatedPlan(
                    traceValue = "trace-capability-provider-004",
                    state = PlanState.ACTIVE,
                ),
            )

        assertEquals(
            CapabilitySelectionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for completed plan`() {
        val result =
            DefaultCapabilitySelectionRequestProvider().provide(
                createCreatedPlan(
                    traceValue = "trace-capability-provider-005",
                    state = PlanState.COMPLETED,
                ),
            )

        assertEquals(
            CapabilitySelectionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for cancelled plan`() {
        val result =
            DefaultCapabilitySelectionRequestProvider().provide(
                createCreatedPlan(
                    traceValue = "trace-capability-provider-006",
                    state = PlanState.CANCELLED,
                ),
            )

        assertEquals(
            CapabilitySelectionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for failed plan lifecycle`() {
        val result =
            DefaultCapabilitySelectionRequestProvider().provide(
                createCreatedPlan(
                    traceValue = "trace-capability-provider-007",
                    state = PlanState.FAILED,
                ),
            )

        assertEquals(
            CapabilitySelectionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred plan authority result`() {
        val traceId = TraceId.from(
            "trace-capability-provider-008",
        )

        val result =
            DefaultCapabilitySelectionRequestProvider().provide(
                PlanAuthorityResult.create(
                    traceId = traceId,
                    status = PlanAuthorityStatus.DEFERRED,
                ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed plan error`() {
        val traceId = TraceId.from(
            "trace-capability-provider-009",
        )
        val error = createError(traceId)

        val result =
            DefaultCapabilitySelectionRequestProvider().provide(
                PlanAuthorityResult.create(
                    traceId = traceId,
                    status = PlanAuthorityStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(
            CapabilitySelectionRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    private fun createCreatedPlan(
        traceValue: String,
        state: PlanState,
    ): PlanAuthorityResult {
        val traceId = TraceId.from(traceValue)

        return PlanAuthorityResult.create(
            traceId = traceId,
            status = PlanAuthorityStatus.CREATED,
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-capability-provider-001",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-capability-provider-001",
                    ),
                    decision = DecisionRecord.create(
                        understanding = UnderstandingRecord.create(
                            context = ContextEnvelope.create(
                                traceId = traceId,
                                schemaVersion = SchemaVersion.from(1),
                                source = ContextSource.TEXT,
                                trustLevel =
                                    ContextTrustLevel.VERIFIED,
                                securityLevel =
                                    ContextSecurityLevel.RESTRICTED,
                                observedAt =
                                    DevilTimestamp
                                        .fromEpochMilliseconds(
                                            1_754_000_089_000L,
                                        ),
                            ),
                            state = UnderstandingState.COMPLETE,
                            summary =
                                "Bounded understanding was produced.",
                        ),
                        state = DecisionState.SELECTED,
                        summary =
                            "A constitutional decision was selected.",
                    ),
                    state = TaskState.CREATED,
                    summary = "A bounded task was created.",
                ),
                state = state,
                summary =
                    "Use the constitutionally approved capability path.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "PLAN_CREATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_089_500L,
                ),
            summary = "Plan creation failed.",
        )
    }
}
