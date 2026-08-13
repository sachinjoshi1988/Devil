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
import com.devil.core.runtime.execution.ExecutionAttemptResult
import com.devil.core.runtime.execution.ExecutionAttemptStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultObservationEvidencePortTest {

    @Test
    fun `attempted execution remains deferred without configured observation embodiment`() {
        val traceId =
            TraceId.from(
                "trace-default-observation-evidence-port-001",
            )
        val request = createExecutionRequest(traceId)

        val result =
            DefaultObservationEvidencePort().observe(
                executionAttempt =
                    ExecutionAttemptResult.create(
                        traceId = traceId,
                        status =
                            ExecutionAttemptStatus.ATTEMPTED,
                        request = request,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ObservationEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `deferred execution attempt remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-default-observation-evidence-port-002",
            )

        val result =
            DefaultObservationEvidencePort().observe(
                executionAttempt =
                    ExecutionAttemptResult.create(
                        traceId = traceId,
                        status =
                            ExecutionAttemptStatus.DEFERRED,
                    ),
            )

        assertEquals(
            ObservationEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed execution attempt preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-default-observation-evidence-port-003",
            )
        val error = createError(traceId)

        val result =
            DefaultObservationEvidencePort().observe(
                executionAttempt =
                    ExecutionAttemptResult.create(
                        traceId = traceId,
                        status =
                            ExecutionAttemptStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(
            ObservationEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `default port never manufactures observed evidence`() {
        val traceId =
            TraceId.from(
                "trace-default-observation-evidence-port-004",
            )

        val result =
            DefaultObservationEvidencePort().observe(
                executionAttempt =
                    ExecutionAttemptResult.create(
                        traceId = traceId,
                        status =
                            ExecutionAttemptStatus.ATTEMPTED,
                        request =
                            createExecutionRequest(traceId),
                    ),
            )

        assertEquals(
            ObservationEvidenceStatus.DEFERRED,
            result.status,
        )
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-default-observation-evidence-port",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-default-observation-evidence-port",
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
                                                                1_754_000_118_000L,
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
                                "A bounded constitutional task was created.",
                        ),
                    state = PlanState.CREATED,
                    summary =
                        "Use the constitutionally approved capability path.",
                ),
            capability =
                CapabilityContract.create(
                    capabilityId =
                        CapabilityId.from(
                            "capability-observation-evidence-port",
                        ),
                    category = CapabilityCategory.ACTION,
                    name =
                        "Observation Evidence Test Capability",
                    description =
                        "Represents one bounded capability for observation-evidence testing.",
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
                    1_754_000_118_500L,
                ),
            summary =
                "Bounded execution attempt failed.",
        )
    }
}
