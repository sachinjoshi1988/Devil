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

class ExecutionAttemptResultTest {

    @Test
    fun `create preserves genuine attempted execution request`() {
        val traceId =
            TraceId.from(
                "trace-execution-attempt-result-001",
            )
        val request = createExecutionRequest(traceId)

        val result =
            ExecutionAttemptResult.create(
                traceId = traceId,
                status = ExecutionAttemptStatus.ATTEMPTED,
                request = request,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionAttemptStatus.ATTEMPTED, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred execution attempt without fabricated request`() {
        val traceId =
            TraceId.from(
                "trace-execution-attempt-result-002",
            )

        val result =
            ExecutionAttemptResult.create(
                traceId = traceId,
                status = ExecutionAttemptStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionAttemptStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed execution attempt with matching error`() {
        val traceId =
            TraceId.from(
                "trace-execution-attempt-result-003",
            )
        val error = createError(traceId)

        val result =
            ExecutionAttemptResult.create(
                traceId = traceId,
                status = ExecutionAttemptStatus.FAILED,
                error = error,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionAttemptStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects attempted result without execution request`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionAttemptResult.create(
                traceId =
                    TraceId.from(
                        "trace-execution-attempt-result-004",
                    ),
                status = ExecutionAttemptStatus.ATTEMPTED,
            )
        }
    }

    @Test
    fun `create rejects deferred result containing execution request`() {
        val traceId =
            TraceId.from(
                "trace-execution-attempt-result-005",
            )

        assertFailsWith<IllegalArgumentException> {
            ExecutionAttemptResult.create(
                traceId = traceId,
                status = ExecutionAttemptStatus.DEFERRED,
                request = createExecutionRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionAttemptResult.create(
                traceId =
                    TraceId.from(
                        "trace-execution-attempt-result-006",
                    ),
                status = ExecutionAttemptStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects execution request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionAttemptResult.create(
                traceId =
                    TraceId.from(
                        "trace-execution-attempt-result-007",
                    ),
                status = ExecutionAttemptStatus.ATTEMPTED,
                request =
                    createExecutionRequest(
                        TraceId.from(
                            "trace-execution-attempt-result-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `create rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionAttemptResult.create(
                traceId =
                    TraceId.from(
                        "trace-execution-attempt-result-008",
                    ),
                status = ExecutionAttemptStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-execution-attempt-result-error-other",
                        ),
                    ),
            )
        }
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-execution-attempt-result",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-execution-attempt-result",
                                ),
                            decision =
                                DecisionRecord.create(
                                    understanding =
                                        UnderstandingRecord.create(
                                            context =
                                                ContextEnvelope.create(
                                                    traceId = traceId,
                                                    schemaVersion =
                                                        SchemaVersion.from(1),
                                                    source =
                                                        ContextSource.TEST,
                                                    trustLevel =
                                                        ContextTrustLevel.VERIFIED,
                                                    securityLevel =
                                                        ContextSecurityLevel.RESTRICTED,
                                                    observedAt =
                                                        DevilTimestamp
                                                            .fromEpochMilliseconds(
                                                                1_754_000_263_000L,
                                                            ),
                                                ),
                                            state =
                                                UnderstandingState.COMPLETE,
                                            summary =
                                                "Bounded understanding was produced.",
                                        ),
                                    state =
                                        DecisionState.SELECTED,
                                    summary =
                                        "A constitutional decision was selected.",
                                ),
                            state = TaskState.CREATED,
                            summary =
                                "A bounded execution task was created.",
                        ),
                    state = PlanState.CREATED,
                    summary =
                        "Attempt one bounded execution embodiment.",
                ),
            capability =
                CapabilityContract.create(
                    capabilityId =
                        CapabilityId.from(
                            "capability-execution-attempt-result",
                        ),
                    category = CapabilityCategory.ACTION,
                    name =
                        "Execution Attempt Result Capability",
                    description =
                        "Represents one bounded execution-attempt result test capability.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "EXECUTION_ATTEMPT_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_263_500L,
                ),
            summary =
                "The bounded execution attempt failed.",
        )
    }
}
