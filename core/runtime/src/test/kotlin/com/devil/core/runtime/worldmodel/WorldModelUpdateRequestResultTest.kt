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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class WorldModelUpdateRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId = TraceId.from(
            "trace-world-model-update-request-result-001",
        )
        val request = createRequest(traceId)

        val result = WorldModelUpdateRequestResult.create(
            traceId = traceId,
            status = WorldModelUpdateRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            WorldModelUpdateRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result`() {
        val traceId = TraceId.from(
            "trace-world-model-update-request-result-002",
        )

        val result = WorldModelUpdateRequestResult.create(
            traceId = traceId,
            status = WorldModelUpdateRequestStatus.UNAVAILABLE,
        )

        assertEquals(
            WorldModelUpdateRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-world-model-update-request-result-003",
        )
        val error = createError(traceId)

        val result = WorldModelUpdateRequestResult.create(
            traceId = traceId,
            status = WorldModelUpdateRequestStatus.FAILED,
            error = error,
        )

        assertEquals(
            WorldModelUpdateRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateRequestResult.create(
                traceId = TraceId.from(
                    "trace-world-model-update-request-result-004",
                ),
                status = WorldModelUpdateRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateRequestResult.create(
                traceId = TraceId.from(
                    "trace-world-model-update-request-result-005",
                ),
                status = WorldModelUpdateRequestStatus.AVAILABLE,
                request = createRequest(
                    TraceId.from(
                        "trace-world-model-update-request-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId = TraceId.from(
            "trace-world-model-update-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateRequestResult.create(
                traceId = traceId,
                status = WorldModelUpdateRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateRequestResult.create(
                traceId = TraceId.from(
                    "trace-world-model-update-request-result-007",
                ),
                status = WorldModelUpdateRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateRequestResult.create(
                traceId = TraceId.from(
                    "trace-world-model-update-request-result-008",
                ),
                status = WorldModelUpdateRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-world-model-update-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
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
                "plan-world-model-update-request-result",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-world-model-update-request-result",
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
                                            1_754_000_138_000L,
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
                "WORLD_MODEL_UPDATE_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_138_500L,
                ),
            summary =
                "World Model update request preparation failed.",
        )
    }
}
