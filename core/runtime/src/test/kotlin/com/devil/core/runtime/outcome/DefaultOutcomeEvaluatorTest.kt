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

class DefaultOutcomeEvaluatorTest {

    @Test
    fun `evaluate returns unavailable without inventing an outcome`() {
        val traceId = TraceId.from(
            "trace-default-outcome-evaluator-001",
        )
        val evaluator: OutcomeEvaluator =
            DefaultOutcomeEvaluator()

        val result = evaluator.evaluate(
            traceId = traceId,
            request = createRequest(traceId),
            evidence = createDeferredOutcomeEvidence(traceId),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            OutcomeEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate does not treat verification as final task success`() {
        val traceId = TraceId.from(
            "trace-default-outcome-evaluator-002",
        )
        val request = createRequest(traceId)

        val result = DefaultOutcomeEvaluator().evaluate(
            traceId = traceId,
            request = request,
            evidence = createDeferredOutcomeEvidence(traceId),
        )

        assertEquals(
            "capability-camera",
            request.verification
                .observation
                .execution
                .capability
                .capabilityId
                .value,
        )
        assertEquals(
            OutcomeEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultOutcomeEvaluator().evaluate(
                traceId = TraceId.from(
                    "trace-default-outcome-evaluator-003",
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-default-outcome-request-other",
                    ),
                ),
                evidence = createDeferredOutcomeEvidence(
                    TraceId.from(
                        "trace-default-outcome-evaluator-003",
                    ),
                ),
            )
        }
    }

    private fun createDeferredOutcomeEvidence(
        traceId: TraceId,
    ): OutcomeEvidenceResult {
        return OutcomeEvidenceResult.create(
            traceId = traceId,
            status = OutcomeEvidenceStatus.DEFERRED,
        )
    }

    private fun createRequest(
        traceId: TraceId,
    ): OutcomeRequest {
        return OutcomeRequest.create(
            verification = VerificationRequest.create(
                observation = ObservationRequest.create(
                    execution = ExecutionRequest.create(
                        plan = PlanRecord.create(
                            planId = PlanId.from(
                                "plan-default-outcome-evaluator",
                            ),
                            task = TaskRecord.create(
                                taskId = TaskId.from(
                                    "task-default-outcome-evaluator",
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
                                                                1_754_000_132_000L,
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
                    ),
                ),
            ),
        )
    }
}
