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

class ExecutiveReadinessEvaluationResultTest {

    @Test
    fun `create preserves ready result with matching request`() {
        val traceId = TraceId.from(
            "trace-executive-evaluation-result-001",
        )
        val request = createRequest(traceId)

        val result = ExecutiveReadinessEvaluationResult.create(
            traceId = traceId,
            status = ExecutiveReadinessEvaluationStatus.READY,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ExecutiveReadinessEvaluationStatus.READY,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from(
            "trace-executive-evaluation-result-002",
        )

        val result = ExecutiveReadinessEvaluationResult.create(
            traceId = traceId,
            status = ExecutiveReadinessEvaluationStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ExecutiveReadinessEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-executive-evaluation-result-003",
        )
        val error = createError(traceId)

        val result = ExecutiveReadinessEvaluationResult.create(
            traceId = traceId,
            status = ExecutiveReadinessEvaluationStatus.FAILED,
            error = error,
        )

        assertEquals(
            ExecutiveReadinessEvaluationStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects ready result without request`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-executive-evaluation-result-004",
                ),
                status = ExecutiveReadinessEvaluationStatus.READY,
            )
        }
    }

    @Test
    fun `create rejects ready request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-executive-evaluation-result-005",
                ),
                status = ExecutiveReadinessEvaluationStatus.READY,
                request = createRequest(
                    TraceId.from(
                        "trace-executive-evaluation-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-executive-evaluation-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessEvaluationResult.create(
                traceId = traceId,
                status =
                    ExecutiveReadinessEvaluationStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-executive-evaluation-result-007",
                ),
                status = ExecutiveReadinessEvaluationStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutiveReadinessEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-executive-evaluation-result-008",
                ),
                status = ExecutiveReadinessEvaluationStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-executive-evaluation-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ExecutiveReadinessRequest {
        return ExecutiveReadinessRequest.create(
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

    private fun createPlan(traceId: TraceId): PlanRecord {
        return PlanRecord.create(
            planId = PlanId.from(
                "plan-executive-evaluation-result",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-executive-evaluation-result",
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
                                    1_754_000_097_000L,
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
                "EXECUTIVE_READINESS_EVALUATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_097_500L,
                ),
            summary =
                "Executive readiness evaluation failed.",
        )
    }
}
