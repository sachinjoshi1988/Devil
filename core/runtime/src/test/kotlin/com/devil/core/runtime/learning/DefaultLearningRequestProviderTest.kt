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

class DefaultLearningRequestProviderTest {

    @Test
    fun `provide returns available request for applicable World Model update`() {
        val traceId = TraceId.from(
            "trace-learning-request-provider-001",
        )
        val updateRequest = createWorldModelUpdateRequest(traceId)
        val provider: LearningRequestProvider =
            DefaultLearningRequestProvider()

        val result = provider.provide(
            worldModelUpdate = WorldModelUpdateResult.create(
                traceId = traceId,
                status = WorldModelUpdateStatus.APPLICABLE,
                request = updateRequest,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(LearningRequestStatus.AVAILABLE, result.status)
        assertEquals(
            updateRequest,
            result.request?.worldModelUpdate,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred World Model update`() {
        val traceId = TraceId.from(
            "trace-learning-request-provider-002",
        )

        val result =
            DefaultLearningRequestProvider().provide(
                worldModelUpdate = WorldModelUpdateResult.create(
                    traceId = traceId,
                    status = WorldModelUpdateStatus.DEFERRED,
                ),
            )

        assertEquals(LearningRequestStatus.UNAVAILABLE, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed World Model update error`() {
        val traceId = TraceId.from(
            "trace-learning-request-provider-003",
        )
        val error = createError(traceId)

        val result =
            DefaultLearningRequestProvider().provide(
                worldModelUpdate = WorldModelUpdateResult.create(
                    traceId = traceId,
                    status = WorldModelUpdateStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(LearningRequestStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide does not treat applicable update as created learning`() {
        val traceId = TraceId.from(
            "trace-learning-request-provider-004",
        )

        val result =
            DefaultLearningRequestProvider().provide(
                worldModelUpdate = WorldModelUpdateResult.create(
                    traceId = traceId,
                    status = WorldModelUpdateStatus.APPLICABLE,
                    request =
                        createWorldModelUpdateRequest(traceId),
                ),
            )

        assertEquals(LearningRequestStatus.AVAILABLE, result.status)
        assertEquals(
            PlanState.CREATED,
            result.request
                ?.worldModelUpdate
                ?.outcome
                ?.verification
                ?.observation
                ?.execution
                ?.plan
                ?.state,
        )
        assertEquals(
            "capability-camera",
            result.request
                ?.worldModelUpdate
                ?.outcome
                ?.verification
                ?.observation
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )
        assertNull(result.error)
    }

    private fun createWorldModelUpdateRequest(
        traceId: TraceId,
    ): WorldModelUpdateRequest {
        return WorldModelUpdateRequest.create(
            outcome = OutcomeRequest.create(
                verification = VerificationRequest.create(
                    observation = ObservationRequest.create(
                        execution = ExecutionRequest.create(
                            plan = createPlan(traceId),
                            capability = createCapability(),
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
                "plan-learning-request-provider",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-learning-request-provider",
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
                                            1_754_000_148_000L,
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
                "WORLD_MODEL_UPDATE_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_148_500L,
                ),
            summary =
                "Constitutional World Model update evaluation failed.",
        )
    }
}
