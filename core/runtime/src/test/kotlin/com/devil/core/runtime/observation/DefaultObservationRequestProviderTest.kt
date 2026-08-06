package com.devil.core.runtime.observation

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
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultObservationRequestProviderTest {

    @Test
    fun `provide returns available request for approved execution`() {
        val traceId = TraceId.from(
            "trace-observation-request-provider-001",
        )
        val executionRequest =
            createExecutionRequest(traceId)
        val provider: ObservationRequestProvider =
            DefaultObservationRequestProvider()

        val result = provider.provide(
            execution = ExecutionResult.create(
                traceId = traceId,
                status = ExecutionStatus.APPROVED,
                request = executionRequest,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ObservationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            executionRequest,
            result.request?.execution,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred execution`() {
        val traceId = TraceId.from(
            "trace-observation-request-provider-002",
        )

        val result =
            DefaultObservationRequestProvider().provide(
                execution = ExecutionResult.create(
                    traceId = traceId,
                    status = ExecutionStatus.DEFERRED,
                ),
            )

        assertEquals(
            ObservationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed execution error`() {
        val traceId = TraceId.from(
            "trace-observation-request-provider-003",
        )
        val error = createError(traceId)

        val result =
            DefaultObservationRequestProvider().provide(
                execution = ExecutionResult.create(
                    traceId = traceId,
                    status = ExecutionStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(
            ObservationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide does not claim that approved execution was attempted`() {
        val traceId = TraceId.from(
            "trace-observation-request-provider-004",
        )

        val result =
            DefaultObservationRequestProvider().provide(
                execution = ExecutionResult.create(
                    traceId = traceId,
                    status = ExecutionStatus.APPROVED,
                    request =
                        createExecutionRequest(traceId),
                ),
            )

        assertEquals(
            ObservationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            "capability-camera",
            result.request
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )
        assertNull(result.error)
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-observation-request-provider",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-observation-request-provider",
                    ),
                    decision = DecisionRecord.create(
                        understanding =
                            UnderstandingRecord.create(
                                context =
                                    ContextEnvelope.create(
                                        traceId = traceId,
                                        schemaVersion =
                                            SchemaVersion.from(1),
                                        source =
                                            ContextSource.TEXT,
                                        trustLevel =
                                            ContextTrustLevel.VERIFIED,
                                        securityLevel =
                                            ContextSecurityLevel.RESTRICTED,
                                        observedAt =
                                            DevilTimestamp
                                                .fromEpochMilliseconds(
                                                    1_754_000_112_000L,
                                                ),
                                    ),
                                state =
                                    UnderstandingState.COMPLETE,
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
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "EXECUTION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_112_500L,
                ),
            summary =
                "Constitutional execution evaluation failed.",
        )
    }
}
