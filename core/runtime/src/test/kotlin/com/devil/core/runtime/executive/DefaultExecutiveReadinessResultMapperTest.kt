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
import com.devil.core.model.executive.ExecutiveReadinessRequest
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

class DefaultExecutiveReadinessResultMapperTest {

    @Test
    fun `map translates affirmative evaluation into ready result`() {
        val traceId = TraceId.from(
            "trace-executive-result-mapper-001",
        )
        val mapper: ExecutiveReadinessResultMapper =
            DefaultExecutiveReadinessResultMapper()

        val result = mapper.map(
            traceId = traceId,
            evaluation = ExecutiveReadinessEvaluationResult.create(
                traceId = traceId,
                status = ExecutiveReadinessEvaluationStatus.READY,
                request = createRequest(traceId),
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ExecutiveReadinessStatus.READY,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `map translates unavailable evaluation into operational deferral`() {
        val traceId = TraceId.from(
            "trace-executive-result-mapper-002",
        )

        val result =
            DefaultExecutiveReadinessResultMapper().map(
                traceId = traceId,
                evaluation =
                    ExecutiveReadinessEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ExecutiveReadinessEvaluationStatus.UNAVAILABLE,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ExecutiveReadinessStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-executive-result-mapper-003",
        )
        val error = createError(traceId)

        val result =
            DefaultExecutiveReadinessResultMapper().map(
                traceId = traceId,
                evaluation =
                    ExecutiveReadinessEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ExecutiveReadinessEvaluationStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ExecutiveReadinessStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
    }

    @Test
    fun `map does not treat readiness as execution success`() {
        val traceId = TraceId.from(
            "trace-executive-result-mapper-004",
        )

        val result =
            DefaultExecutiveReadinessResultMapper().map(
                traceId = traceId,
                evaluation =
                    ExecutiveReadinessEvaluationResult.create(
                        traceId = traceId,
                        status =
                            ExecutiveReadinessEvaluationStatus.READY,
                        request = createRequest(traceId),
                    ),
            )

        assertEquals(
            ExecutiveReadinessStatus.READY,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `map rejects evaluation result from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultExecutiveReadinessResultMapper().map(
                traceId = TraceId.from(
                    "trace-executive-result-mapper-005",
                ),
                evaluation =
                    ExecutiveReadinessEvaluationResult.create(
                        traceId = TraceId.from(
                            "trace-executive-evaluation-other",
                        ),
                        status =
                            ExecutiveReadinessEvaluationStatus.UNAVAILABLE,
                    ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ExecutiveReadinessRequest {
        return ExecutiveReadinessRequest.create(
            plan = PlanRecord.create(
                planId = PlanId.from(
                    "plan-executive-result-mapper",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-executive-result-mapper",
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
                                            1_754_000_099_000L,
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
                "EXECUTIVE_READINESS_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_099_500L,
                ),
            summary =
                "Executive readiness evaluation failed.",
        )
    }
}
