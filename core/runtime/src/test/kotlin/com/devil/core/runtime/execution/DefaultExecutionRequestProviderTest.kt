package com.devil.core.runtime.execution

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
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.executive.ExecutiveReadinessStatus
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultExecutionRequestProviderTest {

    @Test
    fun `provide returns available request for ready approved inputs`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-001",
        )
        val plan = createPlanResult(traceId)
        val capability = createCapabilityResult(traceId)
        val provider: ExecutionRequestProvider =
            DefaultExecutionRequestProvider()

        val result = provider.provide(
            plan = plan,
            capability = capability,
            readiness = ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.READY,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionRequestStatus.AVAILABLE, result.status)
        assertEquals(plan.plan, result.request?.plan)
        assertEquals(
            capability.capability,
            result.request?.capability,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide does not create execution request for selected ready knowledge capability`() {
        val traceId =
            TraceId.from(
                "trace-stage337m-knowledge-no-execution",
            )

        val result =
            DefaultExecutionRequestProvider().provide(
                plan = createPlanResult(traceId),
                capability =
                    createCapabilityResult(
                        traceId = traceId,
                        category = CapabilityCategory.KNOWLEDGE,
                    ),
                readiness =
                    ExecutiveReadinessResult.create(
                        traceId = traceId,
                        status = ExecutiveReadinessStatus.READY,
                    ),
            )

        assertEquals(
            ExecutionRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred readiness`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-002",
        )

        val result = DefaultExecutionRequestProvider().provide(
            plan = createPlanResult(traceId),
            capability = createCapabilityResult(traceId),
            readiness = ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.DEFERRED,
            ),
        )

        assertEquals(ExecutionRequestStatus.UNAVAILABLE, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred capability selection`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-003",
        )

        val result = DefaultExecutionRequestProvider().provide(
            plan = createPlanResult(traceId),
            capability = CapabilitySelectionResult.create(
                traceId = traceId,
                status = CapabilitySelectionStatus.DEFERRED,
            ),
            readiness = ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.DEFERRED,
            ),
        )

        assertEquals(ExecutionRequestStatus.UNAVAILABLE, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred plan authority`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-004",
        )

        val result = DefaultExecutionRequestProvider().provide(
            plan = PlanAuthorityResult.create(
                traceId = traceId,
                status = PlanAuthorityStatus.DEFERRED,
            ),
            capability = CapabilitySelectionResult.create(
                traceId = traceId,
                status = CapabilitySelectionStatus.DEFERRED,
            ),
            readiness = ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.DEFERRED,
            ),
        )

        assertEquals(ExecutionRequestStatus.UNAVAILABLE, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed readiness error`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-005",
        )
        val error = createError(
            traceId = traceId,
            code = "EXECUTIVE_READINESS_FAILED",
        )

        val result = DefaultExecutionRequestProvider().provide(
            plan = createPlanResult(traceId),
            capability = createCapabilityResult(traceId),
            readiness = ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(ExecutionRequestStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide preserves failed capability selection error`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-006",
        )
        val error = createError(
            traceId = traceId,
            code = "CAPABILITY_SELECTION_FAILED",
        )

        val result = DefaultExecutionRequestProvider().provide(
            plan = createPlanResult(traceId),
            capability = CapabilitySelectionResult.create(
                traceId = traceId,
                status = CapabilitySelectionStatus.FAILED,
                error = error,
            ),
            readiness = ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.DEFERRED,
            ),
        )

        assertEquals(ExecutionRequestStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide preserves failed plan error`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-007",
        )
        val error = createError(
            traceId = traceId,
            code = "PLAN_CREATION_FAILED",
        )

        val result = DefaultExecutionRequestProvider().provide(
            plan = PlanAuthorityResult.create(
                traceId = traceId,
                status = PlanAuthorityStatus.FAILED,
                error = error,
            ),
            capability = CapabilitySelectionResult.create(
                traceId = traceId,
                status = CapabilitySelectionStatus.DEFERRED,
            ),
            readiness = ExecutiveReadinessResult.create(
                traceId = traceId,
                status = ExecutiveReadinessStatus.DEFERRED,
            ),
        )

        assertEquals(ExecutionRequestStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide rejects capability result from a different trace`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-008",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultExecutionRequestProvider().provide(
                plan = createPlanResult(traceId),
                capability = CapabilitySelectionResult.create(
                    traceId = TraceId.from(
                        "trace-execution-capability-other",
                    ),
                    status = CapabilitySelectionStatus.DEFERRED,
                ),
                readiness = ExecutiveReadinessResult.create(
                    traceId = traceId,
                    status = ExecutiveReadinessStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `provide rejects readiness result from a different trace`() {
        val traceId = TraceId.from(
            "trace-execution-request-provider-009",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultExecutionRequestProvider().provide(
                plan = createPlanResult(traceId),
                capability = createCapabilityResult(traceId),
                readiness = ExecutiveReadinessResult.create(
                    traceId = TraceId.from(
                        "trace-execution-readiness-other",
                    ),
                    status = ExecutiveReadinessStatus.DEFERRED,
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
                    "plan-execution-request-provider",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-execution-request-provider",
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
                                            1_754_000_103_000L,
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
        category: CapabilityCategory = CapabilityCategory.ACTION,
    ): CapabilitySelectionResult {
        return CapabilitySelectionResult.create(
            traceId = traceId,
            status = CapabilitySelectionStatus.SELECTED,
            capability = CapabilityContract.create(
                capabilityId = CapabilityId.from(
                    "capability-camera",
                ),
                category = category,
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
                    1_754_000_103_500L,
                ),
            summary =
                "Execution request dependency failed.",
        )
    }
}
