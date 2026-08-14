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
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class Stage73WorldModelRepresentationEvaluationTest {

    @Test
    fun `established evidence becomes matching world model representation`() {
        val traceId =
            TraceId.from(
                "trace-stage73-world-model-representation-001",
            )
        val description =
            "The foreground application was genuinely established as Camera."

        val result =
            DefaultWorldModelUpdateEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(traceId),
                evidence =
                    WorldModelUpdateEvidenceResult.create(
                        traceId = traceId,
                        status =
                            WorldModelUpdateEvidenceStatus.ESTABLISHED,
                        capabilityId =
                            CapabilityId.from(
                                "capability-camera",
                            ),
                        description = description,
                    ),
            )

        assertEquals(
            WorldModelUpdateEvaluationStatus.APPLICABLE,
            result.status,
        )

        val representation =
            assertNotNull(result.representation)

        assertEquals(
            traceId,
            representation.traceId,
        )
        assertEquals(
            CapabilityId.from(
                "capability-camera",
            ),
            representation.capabilityId,
        )
        assertEquals(
            description,
            representation.description,
        )
    }

    @Test
    fun `evaluation rejects established evidence from another capability`() {
        val traceId =
            TraceId.from(
                "trace-stage73-world-model-representation-002",
            )

        assertFailsWith<IllegalArgumentException> {
            DefaultWorldModelUpdateEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(traceId),
                evidence =
                    WorldModelUpdateEvidenceResult.create(
                        traceId = traceId,
                        status =
                            WorldModelUpdateEvidenceStatus.ESTABLISHED,
                        capabilityId =
                            CapabilityId.from(
                                "capability-notifications",
                            ),
                        description =
                            "Unrelated notification evidence.",
                    ),
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
                                                createPlan(
                                                    traceId,
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
                                                        "Performs one bounded registered camera action.",
                                                ),
                                        ),
                                ),
                        ),
                ),
        )
    }

    private fun createPlan(
        traceId: TraceId,
    ): PlanRecord {
        return PlanRecord.create(
            planId =
                PlanId.from(
                    "plan-stage73-world-model-representation",
                ),
            task =
                TaskRecord.create(
                    taskId =
                        TaskId.from(
                            "task-stage73-world-model-representation",
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
                                                ContextSource.TEXT,
                                            trustLevel =
                                                ContextTrustLevel.VERIFIED,
                                            securityLevel =
                                                ContextSecurityLevel.RESTRICTED,
                                            observedAt =
                                                DevilTimestamp
                                                    .fromEpochMilliseconds(
                                                        1_754_000_145_000L,
                                                    ),
                                        ),
                                    state =
                                        UnderstandingState.COMPLETE,
                                    summary =
                                        "Open the Camera application.",
                                ),
                            state =
                                DecisionState.SELECTED,
                            summary =
                                "Use the authorized camera capability.",
                        ),
                    state =
                        TaskState.CREATED,
                    summary =
                        "Open the Camera application.",
                ),
            state =
                PlanState.CREATED,
            summary =
                "Use the constitutionally approved capability path.",
        )
    }
}
