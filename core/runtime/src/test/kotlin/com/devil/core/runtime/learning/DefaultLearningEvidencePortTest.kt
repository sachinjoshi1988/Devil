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
import com.devil.core.model.worldmodel.WorldModelUpdateRequest
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultLearningEvidencePortTest {

    @Test
    fun `applicable world model update remains deferred without configured learning evidence mechanism`() {
        val traceId =
            TraceId.from(
                "trace-default-learning-evidence-port-001",
            )

        val result =
            DefaultLearningEvidencePort().establish(
                worldModelUpdate =
                    applicableWorldModelUpdate(
                        traceId = traceId,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            LearningEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `deferred world model update remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-default-learning-evidence-port-002",
            )

        val result =
            DefaultLearningEvidencePort().establish(
                worldModelUpdate =
                    WorldModelUpdateResult.create(
                        traceId = traceId,
                        status = WorldModelUpdateStatus.DEFERRED,
                    ),
            )

        assertEquals(
            LearningEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed world model update preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-default-learning-evidence-port-003",
            )
        val error = createError(traceId)

        val result =
            DefaultLearningEvidencePort().establish(
                worldModelUpdate =
                    WorldModelUpdateResult.create(
                        traceId = traceId,
                        status = WorldModelUpdateStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(
            LearningEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `default port never manufactures established learning evidence`() {
        val result =
            DefaultLearningEvidencePort().establish(
                worldModelUpdate =
                    applicableWorldModelUpdate(
                        traceId =
                            TraceId.from(
                                "trace-default-learning-evidence-port-004",
                            ),
                    ),
            )

        assertEquals(
            LearningEvidenceStatus.DEFERRED,
            result.status,
        )
    }

    private fun applicableWorldModelUpdate(
        traceId: TraceId,
    ): WorldModelUpdateResult {
        return WorldModelUpdateResult.create(
            traceId = traceId,
            status = WorldModelUpdateStatus.APPLICABLE,
            request =
                WorldModelUpdateRequest.create(
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
                                                            traceId = traceId,
                                                        ),
                                                    capability =
                                                        CapabilityContract.create(
                                                            capabilityId =
                                                                CapabilityId.from(
                                                                    "capability-learning-evidence-port",
                                                                ),
                                                            category =
                                                                CapabilityCategory.ACTION,
                                                            name =
                                                                "Learning Evidence Test Capability",
                                                            description =
                                                                "Represents one bounded capability for Learning-evidence testing.",
                                                        ),
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
                    "plan-default-learning-evidence-port",
                ),
            task =
                TaskRecord.create(
                    taskId =
                        TaskId.from(
                            "task-default-learning-evidence-port",
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
                                                        1_754_000_690_000L,
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
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "WORLD_MODEL_UPDATE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_690_500L,
                ),
            summary =
                "Constitutional World Model update failed before Learning evidence.",
        )
    }
}
