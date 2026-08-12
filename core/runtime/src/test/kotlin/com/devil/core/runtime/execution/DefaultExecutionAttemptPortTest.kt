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
import kotlin.test.assertNull

class DefaultExecutionAttemptPortTest {

    @Test
    fun `approved execution remains deferred when no embodiment is configured`() {
        val traceId =
            TraceId.from(
                "trace-default-execution-attempt-port-001",
            )

        val result =
            DefaultExecutionAttemptPort().attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.APPROVED,
                        request = createExecutionRequest(traceId),
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionAttemptStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `deferred constitutional execution remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-default-execution-attempt-port-002",
            )

        val result =
            DefaultExecutionAttemptPort().attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.DEFERRED,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionAttemptStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `failed constitutional execution preserves matching failure`() {
        val traceId =
            TraceId.from(
                "trace-default-execution-attempt-port-003",
            )
        val error = createError(traceId)

        val result =
            DefaultExecutionAttemptPort().attempt(
                execution =
                    ExecutionResult.create(
                        traceId = traceId,
                        status = ExecutionStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(ExecutionAttemptStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-default-execution-attempt-port",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-default-execution-attempt-port",
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
                                                                1_754_000_264_000L,
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
                        "Approach one execution embodiment.",
                ),
            capability =
                CapabilityContract.create(
                    capabilityId =
                        CapabilityId.from(
                            "capability-default-execution-attempt-port",
                        ),
                    category = CapabilityCategory.ACTION,
                    name =
                        "Default Execution Attempt Port Capability",
                    description =
                        "Represents one bounded default-port test capability.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "CONSTITUTIONAL_EXECUTION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_264_500L,
                ),
            summary =
                "Constitutional execution evaluation failed.",
        )
    }
}
