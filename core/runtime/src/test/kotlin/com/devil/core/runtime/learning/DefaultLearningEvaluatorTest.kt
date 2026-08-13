package com.devil.core.runtime.learning

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
import com.devil.core.model.learning.LearningRequest
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
import com.devil.core.model.worldmodel.WorldModelUpdateRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultLearningEvaluatorTest {

    @Test
    fun `evaluate returns unavailable without creating learning`() {
        val traceId = TraceId.from(
            "trace-default-learning-evaluator-001",
        )
        val evaluator: LearningEvaluator =
            DefaultLearningEvaluator()

        val result = evaluator.evaluate(
            traceId = traceId,
            evidence = createDeferredLearningEvidence(traceId),
            request = createRequest(traceId),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            LearningEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate does not treat applicable update as completed learning`() {
        val traceId = TraceId.from(
            "trace-default-learning-evaluator-002",
        )
        val request = createRequest(traceId)

        val result = DefaultLearningEvaluator().evaluate(
            traceId = traceId,
                evidence = createDeferredLearningEvidence(traceId),
            request = request,
        )

        assertEquals(
            "capability-camera",
            request.worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .capability
                .capabilityId
                .value,
        )
        assertEquals(
            PlanState.CREATED,
            request.worldModelUpdate
                .outcome
                .verification
                .observation
                .execution
                .plan
                .state,
        )
        assertEquals(
            LearningEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultLearningEvaluator().evaluate(
                traceId = TraceId.from(
                    "trace-default-learning-evaluator-003",
                ),
                evidence = createDeferredLearningEvidence(
                    TraceId.from(
                        "trace-default-learning-evaluator-003",
                    ),
                ),
                request = createRequest(
                    TraceId.from(
                        "trace-default-learning-request-other",
                    ),
                ),
            )
        }
    }

    private fun createDeferredLearningEvidence(
        traceId: TraceId,
    ): LearningEvidenceResult {
        return LearningEvidenceResult.create(
            traceId = traceId,
            status = LearningEvidenceStatus.DEFERRED,
        )
    }


    private fun createRequest(
        traceId: TraceId,
    ): LearningRequest {
        return LearningRequest.create(
            worldModelUpdate =
                WorldModelUpdateRequest.create(
                    outcome = OutcomeRequest.create(
                        verification =
                            VerificationRequest.create(
                                observation =
                                    ObservationRequest.create(
                                        execution =
                                            ExecutionRequest.create(
                                                plan = createPlan(traceId),
                                                capability =
                                                    createCapability(),
                                            ),
                                    ),
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
                "plan-default-learning-evaluator",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-default-learning-evaluator",
                ),
                decision = DecisionRecord.create(
                    understanding =
                        UnderstandingRecord.create(
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
                                            1_754_000_150_000L,
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
}
