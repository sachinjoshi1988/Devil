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

class DefaultExecutiveReadinessEvaluatorTest {

    @Test
    fun `evaluate returns ready for bounded readiness request`() {
        val traceId = TraceId.from(
            "trace-default-executive-evaluator-001",
        )
        val request = createRequest(traceId)

        val result =
            DefaultExecutiveReadinessEvaluator().evaluate(
                traceId = traceId,
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
    fun `evaluate preserves selected capability without claiming execution`() {
        val traceId = TraceId.from(
            "trace-default-executive-evaluator-002",
        )
        val request = createRequest(traceId)

        val result =
            DefaultExecutiveReadinessEvaluator().evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            "capability-camera",
            request.capability.capabilityId.value,
        )
        assertEquals(
            ExecutiveReadinessEvaluationStatus.READY,
            result.status,
        )
        assertEquals(
            "capability-camera",
            result.request?.capability?.capabilityId?.value,
        )
    }

    @Test
    fun `evaluate rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultExecutiveReadinessEvaluator().evaluate(
                traceId = TraceId.from(
                    "trace-default-executive-evaluator-003",
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-default-executive-request-other",
                    ),
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
                    "plan-default-executive-evaluator",
                ),
                task = TaskRecord.create(
                    taskId = TaskId.from(
                        "task-default-executive-evaluator",
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
                                                    1_754_000_098_000L,
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
}
