package com.devil.core.runtime.worldmodel

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
import com.devil.core.model.worldmodel.WorldModelUpdateRequest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class Stage73WorldModelRepresentationFailClosedTest {

    @Test
    fun `default mapper rejects applicable evaluation without representation`() {
        val traceId =
            TraceId.from(
                "trace-stage73-world-model-fail-closed-001",
            )

        val evaluation =
            WorldModelUpdateEvaluationResult.create(
                traceId = traceId,
                status =
                    WorldModelUpdateEvaluationStatus.APPLICABLE,
                request = createRequest(traceId),
            )

        assertFailsWith<IllegalArgumentException> {
            DefaultWorldModelUpdateResultMapper().map(
                traceId = traceId,
                evaluation = evaluation,
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): WorldModelUpdateRequest {
        return WorldModelUpdateRequest.create(
            outcome =
                OutcomeRequest.create(
                    verification =
                        VerificationRequest.create(
                            observation =
                                ObservationRequest.create(
                                    execution =
                                        ExecutionRequest.create(
                                            plan =
                                                PlanRecord.create(
                                                    planId =
                                                        PlanId.from(
                                                            "plan-stage73-fail-closed",
                                                        ),
                                                    task =
                                                        TaskRecord.create(
                                                            taskId =
                                                                TaskId.from(
                                                                    "task-stage73-fail-closed",
                                                                ),
                                                            decision =
                                                                DecisionRecord.create(
                                                                    understanding =
                                                                        UnderstandingRecord.create(
                                                                            context =
                                                                                ContextEnvelope.create(
                                                                                    traceId =
                                                                                        traceId,
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
                                                                                                1_754_000_147_000L,
                                                                                            ),
                                                                                ),
                                                                            state =
                                                                                UnderstandingState.COMPLETE,
                                                                            summary =
                                                                                "Bounded understanding.",
                                                                        ),
                                                                    state =
                                                                        DecisionState.SELECTED,
                                                                    summary =
                                                                        "Bounded decision.",
                                                                ),
                                                            state =
                                                                TaskState.CREATED,
                                                            summary =
                                                                "Bounded task.",
                                                        ),
                                                    state =
                                                        PlanState.CREATED,
                                                    summary =
                                                        "Bounded plan.",
                                                ),
                                            capability =
                                                CapabilityContract.create(
                                                    capabilityId =
                                                        CapabilityId.from(
                                                            "capability-camera",
                                                        ),
                                                    category =
                                                        CapabilityCategory.ACTION,
                                                    name =
                                                        "Camera",
                                                    description =
                                                        "Bounded camera capability.",
                                                ),
                                        ),
                                ),
                        ),
                ),
        )
    }
}
