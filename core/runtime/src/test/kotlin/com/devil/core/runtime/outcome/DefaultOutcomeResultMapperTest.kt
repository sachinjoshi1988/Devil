package com.devil.core.runtime.outcome

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
import com.devil.core.model.observation.ObservationRequest
import com.devil.core.model.outcome.OutcomeRequest
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.model.verification.VerificationRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultOutcomeResultMapperTest {

    @Test
    fun `map translates established evaluation into established result`() {
        val traceId = TraceId.from(
            "trace-outcome-result-mapper-001",
        )
        val request = createRequest(traceId)
        val mapper: OutcomeResultMapper =
            DefaultOutcomeResultMapper()

        val result = mapper.map(
            traceId = traceId,
            evaluation = OutcomeEvaluationResult.create(
                traceId = traceId,
                status = OutcomeEvaluationStatus.ESTABLISHED,
                request = request,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(OutcomeStatus.ESTABLISHED, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map translates unavailable evaluation into operational deferral`() {
        val traceId = TraceId.from(
            "trace-outcome-result-mapper-002",
        )

        val result = DefaultOutcomeResultMapper().map(
            traceId = traceId,
            evaluation = OutcomeEvaluationResult.create(
                traceId = traceId,
                status = OutcomeEvaluationStatus.UNAVAILABLE,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(OutcomeStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-outcome-result-mapper-003",
        )
        val error = createError(traceId)

        val result = DefaultOutcomeResultMapper().map(
            traceId = traceId,
            evaluation = OutcomeEvaluationResult.create(
                traceId = traceId,
                status = OutcomeEvaluationStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(OutcomeStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `map does not mutate world or task state`() {
        val traceId = TraceId.from(
            "trace-outcome-result-mapper-004",
        )

        val result = DefaultOutcomeResultMapper().map(
            traceId = traceId,
            evaluation = OutcomeEvaluationResult.create(
                traceId = traceId,
                status = OutcomeEvaluationStatus.ESTABLISHED,
                request = createRequest(traceId),
            ),
        )

        assertEquals(OutcomeStatus.ESTABLISHED, result.status)
        assertEquals(
            TaskState.CREATED,
            result.request
                ?.verification
                ?.observation
                ?.execution
                ?.plan
                ?.task
                ?.state,
        )
        assertEquals(
            PlanState.CREATED,
            result.request
                ?.verification
                ?.observation
                ?.execution
                ?.plan
                ?.state,
        )
        assertNull(result.error)
    }

    @Test
    fun `map rejects evaluation result from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultOutcomeResultMapper().map(
                traceId = TraceId.from(
                    "trace-outcome-result-mapper-005",
                ),
                evaluation = OutcomeEvaluationResult.create(
                    traceId = TraceId.from(
                        "trace-outcome-evaluation-other",
                    ),
                    status = OutcomeEvaluationStatus.UNAVAILABLE,
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): OutcomeRequest {
        return OutcomeRequest.create(
            verification = VerificationRequest.create(
                observation = ObservationRequest.create(
                    execution = ExecutionRequest.create(
                        plan = createPlan(traceId),
                        capability = createCapability(),
                    ),
                ),
            ),
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-camera",
            ),
            category = CapabilityCategory.ACTION,
            name = "Camera",
            description =
                "Performs one bounded registered camera action.",
        )
    }

    private fun createPlan(
        traceId: TraceId,
    ): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from(
                "plan-outcome-result-mapper",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-outcome-result-mapper",
                ),
                decision = DecisionRecord.create(
                    understanding =
                        UnderstandingRecord.create(
                            context = ContextEnvelope.create(
                                traceId = traceId,
                                schemaVersion =
                                    SchemaVersion.from(1),
                                source = ContextSource.TEXT,
                                trustLevel =
                                    ContextTrustLevel.VERIFIED,
                                securityLevel =
                                    ContextSecurityLevel.RESTRICTED,
                                observedAt =
                                    DevilTimestamp
                                        .fromEpochMilliseconds(
                                            1_754_000_134_000L,
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
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "OUTCOME_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_134_500L,
                ),
            summary =
                "Outcome evaluation failed.",
        )
    }
}
