package com.devil.core.runtime.verification

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
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.observation.ObservationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultVerificationRequestProviderTest {

    @Test
    fun `provide returns available request for observed result`() {
        val traceId = TraceId.from(
            "trace-verification-request-provider-001",
        )
        val observationRequest =
            createObservationRequest(traceId)
        val provider: VerificationRequestProvider =
            DefaultVerificationRequestProvider()

        val result = provider.provide(
            observation = ObservationResult.create(
                traceId = traceId,
                status = ObservationStatus.OBSERVED,
                request = observationRequest,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            VerificationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            observationRequest,
            result.request?.observation,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred observation`() {
        val traceId = TraceId.from(
            "trace-verification-request-provider-002",
        )

        val result =
            DefaultVerificationRequestProvider().provide(
                observation = ObservationResult.create(
                    traceId = traceId,
                    status = ObservationStatus.DEFERRED,
                ),
            )

        assertEquals(
            VerificationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed observation error`() {
        val traceId = TraceId.from(
            "trace-verification-request-provider-003",
        )
        val error = createError(traceId)

        val result =
            DefaultVerificationRequestProvider().provide(
                observation = ObservationResult.create(
                    traceId = traceId,
                    status = ObservationStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(
            VerificationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide does not treat observation as verified success`() {
        val traceId = TraceId.from(
            "trace-verification-request-provider-004",
        )

        val result =
            DefaultVerificationRequestProvider().provide(
                observation = ObservationResult.create(
                    traceId = traceId,
                    status = ObservationStatus.OBSERVED,
                    request =
                        createObservationRequest(traceId),
                ),
            )

        assertEquals(
            VerificationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            "capability-camera",
            result.request
                ?.observation
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )
        assertNull(result.error)
    }

    private fun createObservationRequest(
        traceId: TraceId,
    ): ObservationRequest {
        return ObservationRequest.create(
            execution = ExecutionRequest.create(
                plan = PlanRecord.create(
                    planId = PlanId.from(
                        "plan-verification-request-provider",
                    ),
                    task = TaskRecord.create(
                        taskId = TaskId.from(
                            "task-verification-request-provider",
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
                                                        1_754_000_121_000L,
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
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "OBSERVATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_121_500L,
                ),
            summary =
                "Constitutional observation evaluation failed.",
        )
    }
}
