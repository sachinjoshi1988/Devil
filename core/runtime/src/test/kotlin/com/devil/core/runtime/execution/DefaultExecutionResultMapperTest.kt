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
import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultExecutionResultMapperTest {

    @Test
    fun `map translates approved evaluation into approved execution result`() {
        val traceId = TraceId.from(
            "trace-execution-result-mapper-001",
        )
        val request = createRequest(traceId)
        val mapper: ExecutionResultMapper =
            DefaultExecutionResultMapper()

        val result = mapper.map(
            traceId = traceId,
            evaluation = ExecutionEvaluationResult.create(
                traceId = traceId,
                status = ExecutionEvaluationStatus.APPROVED,
                request = request,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionStatus.APPROVED, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map translates unavailable evaluation into operational deferral`() {
        val traceId = TraceId.from(
            "trace-execution-result-mapper-002",
        )

        val result = DefaultExecutionResultMapper().map(
            traceId = traceId,
            evaluation = ExecutionEvaluationResult.create(
                traceId = traceId,
                status = ExecutionEvaluationStatus.UNAVAILABLE,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-execution-result-mapper-003",
        )
        val error = createError(traceId)

        val result = DefaultExecutionResultMapper().map(
            traceId = traceId,
            evaluation = ExecutionEvaluationResult.create(
                traceId = traceId,
                status = ExecutionEvaluationStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `map does not claim that approved execution was attempted or completed`() {
        val traceId = TraceId.from(
            "trace-execution-result-mapper-004",
        )

        val result = DefaultExecutionResultMapper().map(
            traceId = traceId,
            evaluation = ExecutionEvaluationResult.create(
                traceId = traceId,
                status = ExecutionEvaluationStatus.APPROVED,
                request = createRequest(traceId),
            ),
        )

        assertEquals(ExecutionStatus.APPROVED, result.status)
        assertEquals(
            "capability-camera",
            result.request?.capability?.capabilityId?.value,
        )
        assertNull(result.error)
    }

    @Test
    fun `map rejects evaluation result from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultExecutionResultMapper().map(
                traceId = TraceId.from(
                    "trace-execution-result-mapper-005",
                ),
                evaluation = ExecutionEvaluationResult.create(
                    traceId = TraceId.from(
                        "trace-execution-evaluation-other",
                    ),
                    status = ExecutionEvaluationStatus.UNAVAILABLE,
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan = createPlan(traceId),
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

    private fun createPlan(
        traceId: TraceId,
    ): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from(
                "plan-execution-result-mapper",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-execution-result-mapper",
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
                                DevilTimestamp.fromEpochMilliseconds(
                                    1_754_000_107_000L,
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
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "EXECUTION_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_107_500L,
                ),
            summary =
                "Execution evaluation failed.",
        )
    }
}
