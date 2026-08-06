package com.devil.core.runtime.executive

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
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
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultExecutiveReadinessRequestProviderTest {

    @Test
    fun `provide returns available request for created plan and selected capability`() {
        val traceId = TraceId.from(
            "trace-executive-request-provider-001",
        )
        val plan = createPlanResult(traceId)
        val capability = createCapabilityResult(traceId)
        val provider: ExecutiveReadinessRequestProvider =
            DefaultExecutiveReadinessRequestProvider()

        val result = provider.provide(
            plan = plan,
            capability = capability,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ExecutiveReadinessRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(plan.plan, result.request?.plan)
        assertEquals(
            capability.capability,
            result.request?.capability,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred capability selection`() {
        val traceId = TraceId.from(
            "trace-executive-request-provider-002",
        )

        val result =
            DefaultExecutiveReadinessRequestProvider().provide(
                plan = createPlanResult(traceId),
                capability = CapabilitySelectionResult.create(
                    traceId = traceId,
                    status = CapabilitySelectionStatus.DEFERRED,
                ),
            )

        assertEquals(
            ExecutiveReadinessRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred plan authority`() {
        val traceId = TraceId.from(
            "trace-executive-request-provider-003",
        )

        val result =
            DefaultExecutiveReadinessRequestProvider().provide(
                plan = PlanAuthorityResult.create(
                    traceId = traceId,
                    status = PlanAuthorityStatus.DEFERRED,
                ),
                capability = CapabilitySelectionResult.create(
                    traceId = traceId,
                    status = CapabilitySelectionStatus.DEFERRED,
                ),
            )

        assertEquals(
            ExecutiveReadinessRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed capability selection error`() {
        val traceId = TraceId.from(
            "trace-executive-request-provider-004",
        )
        val error = createError(
            traceId = traceId,
            code = "CAPABILITY_SELECTION_FAILED",
        )

        val result =
            DefaultExecutiveReadinessRequestProvider().provide(
                plan = createPlanResult(traceId),
                capability = CapabilitySelectionResult.create(
                    traceId = traceId,
                    status = CapabilitySelectionStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(
            ExecutiveReadinessRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide preserves failed plan error`() {
        val traceId = TraceId.from(
            "trace-executive-request-provider-005",
        )
        val error = createError(
            traceId = traceId,
            code = "PLAN_CREATION_FAILED",
        )

        val result =
            DefaultExecutiveReadinessRequestProvider().provide(
                plan = PlanAuthorityResult.create(
                    traceId = traceId,
                    status = PlanAuthorityStatus.FAILED,
                    error = error,
                ),
                capability = CapabilitySelectionResult.create(
                    traceId = traceId,
                    status = CapabilitySelectionStatus.DEFERRED,
                ),
            )

        assertEquals(
            ExecutiveReadinessRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide rejects dependencies from different traces`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultExecutiveReadinessRequestProvider().provide(
                plan = PlanAuthorityResult.create(
                    traceId = TraceId.from(
                        "trace-executive-request-provider-006",
                    ),
                    status = PlanAuthorityStatus.DEFERRED,
                ),
                capability = CapabilitySelectionResult.create(
                    traceId = TraceId.from(
                        "trace-executive-capability-other",
                    ),
                    status = CapabilitySelectionStatus.DEFERRED,
                ),
            )
        }
    }

    private fun createPlanResult(
        traceId: TraceId,
    ): PlanAuthorityResult {
        return PlanAuthorityResult.create(
            traceId = traceId,
            status = PlanAuthorityStatus.CREATED,
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-executive-request-provider",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-executive-request-provider",
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
                                            1_754_000_096_000L,
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
                    summary =
                        "A bounded constitutional task was created.",
                ),
                state = PlanState.CREATED,
                summary =
                    "Use the constitutionally approved capability path.",
            ),
        )
    }

    private fun createCapabilityResult(
        traceId: TraceId,
    ): CapabilitySelectionResult {
        return CapabilitySelectionResult.create(
            traceId = traceId,
            status = CapabilitySelectionStatus.SELECTED,
            capability = CapabilityContract.create(
                capabilityId = CapabilityId.from(
                    "capability-camera",
                ),
                category = CapabilityCategory.ACTION,
                name = "Camera",
                description =
                    "Performs one bounded registered camera action.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_096_500L,
                ),
            summary =
                "Executive readiness request dependency failed.",
        )
    }
}
