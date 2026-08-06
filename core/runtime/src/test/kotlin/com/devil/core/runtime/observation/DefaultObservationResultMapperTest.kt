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
import com.devil.core.model.observation.ObservationRequest
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

class DefaultObservationResultMapperTest {

    @Test
    fun `map translates observed evaluation into observed result`() {
        val traceId = TraceId.from(
            "trace-observation-result-mapper-001",
        )
        val request = createRequest(traceId)
        val mapper: ObservationResultMapper =
            DefaultObservationResultMapper()

        val result = mapper.map(
            traceId = traceId,
            evaluation = ObservationEvaluationResult.create(
                traceId = traceId,
                status = ObservationEvaluationStatus.OBSERVED,
                request = request,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ObservationStatus.OBSERVED, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map translates unavailable evaluation into operational deferral`() {
        val traceId = TraceId.from(
            "trace-observation-result-mapper-002",
        )

        val result = DefaultObservationResultMapper().map(
            traceId = traceId,
            evaluation = ObservationEvaluationResult.create(
                traceId = traceId,
                status = ObservationEvaluationStatus.UNAVAILABLE,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ObservationStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-observation-result-mapper-003",
        )
        val error = createError(traceId)

        val result = DefaultObservationResultMapper().map(
            traceId = traceId,
            evaluation = ObservationEvaluationResult.create(
                traceId = traceId,
                status = ObservationEvaluationStatus.FAILED,
                error = error,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(ObservationStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `map does not treat observation as verification or final success`() {
        val traceId = TraceId.from(
            "trace-observation-result-mapper-004",
        )

        val result = DefaultObservationResultMapper().map(
            traceId = traceId,
            evaluation = ObservationEvaluationResult.create(
                traceId = traceId,
                status = ObservationEvaluationStatus.OBSERVED,
                request = createRequest(traceId),
            ),
        )

        assertEquals(ObservationStatus.OBSERVED, result.status)
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

    @Test
    fun `map rejects evaluation result from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultObservationResultMapper().map(
                traceId = TraceId.from(
                    "trace-observation-result-mapper-005",
                ),
                evaluation = ObservationEvaluationResult.create(
                    traceId = TraceId.from(
                        "trace-observation-evaluation-other",
                    ),
                    status =
                        ObservationEvaluationStatus.UNAVAILABLE,
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ObservationRequest {
        return ObservationRequest.create(
            execution = ExecutionRequest.create(
                plan = createPlan(traceId),
                capability = createCapability(),
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
                "plan-observation-result-mapper",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-observation-result-mapper",
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
                                            1_754_000_116_000L,
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
                "OBSERVATION_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_116_500L,
                ),
            summary =
                "Observation evaluation failed.",
        )
    }
}
