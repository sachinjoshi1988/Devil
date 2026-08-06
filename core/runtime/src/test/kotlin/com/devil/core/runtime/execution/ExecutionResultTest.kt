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

class ExecutionResultTest {

    @Test
    fun `create preserves approved result with matching request`() {
        val traceId = TraceId.from(
            "trace-execution-result-001",
        )
        val request = createRequest(traceId)

        val result = ExecutionResult.create(
            traceId = traceId,
            status = ExecutionStatus.APPROVED,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionStatus.APPROVED, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without request or error`() {
        val traceId = TraceId.from(
            "trace-execution-result-002",
        )

        val result = ExecutionResult.create(
            traceId = traceId,
            status = ExecutionStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-execution-result-003",
        )
        val error = createError(traceId)

        val result = ExecutionResult.create(
            traceId = traceId,
            status = ExecutionStatus.FAILED,
            error = error,
        )

        assertEquals(ExecutionStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects approved result without request`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionResult.create(
                traceId = TraceId.from(
                    "trace-execution-result-004",
                ),
                status = ExecutionStatus.APPROVED,
            )
        }
    }

    @Test
    fun `create rejects approved request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionResult.create(
                traceId = TraceId.from(
                    "trace-execution-result-005",
                ),
                status = ExecutionStatus.APPROVED,
                request = createRequest(
                    TraceId.from(
                        "trace-execution-result-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects deferred result with request`() {
        val traceId = TraceId.from(
            "trace-execution-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            ExecutionResult.create(
                traceId = traceId,
                status = ExecutionStatus.DEFERRED,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionResult.create(
                traceId = TraceId.from(
                    "trace-execution-result-007",
                ),
                status = ExecutionStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionResult.create(
                traceId = TraceId.from(
                    "trace-execution-result-008",
                ),
                status = ExecutionStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-execution-result-error-other",
                    ),
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
                "plan-execution-result",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-execution-result",
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
                                    1_754_000_106_000L,
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
                "EXECUTION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_106_500L,
                ),
            summary =
                "Constitutional execution evaluation failed.",
        )
    }
}
