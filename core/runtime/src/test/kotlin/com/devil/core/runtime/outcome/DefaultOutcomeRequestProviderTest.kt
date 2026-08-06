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
import com.devil.core.model.verification.VerificationRequest
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.verification.VerificationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultOutcomeRequestProviderTest {

    @Test
    fun `provide returns available request for verified result`() {
        val traceId = TraceId.from(
            "trace-outcome-request-provider-001",
        )
        val verificationRequest =
            createVerificationRequest(traceId)
        val provider: OutcomeRequestProvider =
            DefaultOutcomeRequestProvider()

        val result = provider.provide(
            verification = VerificationResult.create(
                traceId = traceId,
                status = VerificationStatus.VERIFIED,
                request = verificationRequest,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            OutcomeRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            verificationRequest,
            result.request?.verification,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred verification`() {
        val traceId = TraceId.from(
            "trace-outcome-request-provider-002",
        )

        val result =
            DefaultOutcomeRequestProvider().provide(
                verification = VerificationResult.create(
                    traceId = traceId,
                    status = VerificationStatus.DEFERRED,
                ),
            )

        assertEquals(
            OutcomeRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed verification error`() {
        val traceId = TraceId.from(
            "trace-outcome-request-provider-003",
        )
        val error = createError(traceId)

        val result =
            DefaultOutcomeRequestProvider().provide(
                verification = VerificationResult.create(
                    traceId = traceId,
                    status = VerificationStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(
            OutcomeRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `provide does not treat verification as final task outcome`() {
        val traceId = TraceId.from(
            "trace-outcome-request-provider-004",
        )

        val result =
            DefaultOutcomeRequestProvider().provide(
                verification = VerificationResult.create(
                    traceId = traceId,
                    status = VerificationStatus.VERIFIED,
                    request =
                        createVerificationRequest(traceId),
                ),
            )

        assertEquals(
            OutcomeRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            "capability-camera",
            result.request
                ?.verification
                ?.observation
                ?.execution
                ?.capability
                ?.capabilityId
                ?.value,
        )
        assertNull(result.error)
    }

    private fun createVerificationRequest(
        traceId: TraceId,
    ): VerificationRequest {
        return VerificationRequest.create(
            observation = ObservationRequest.create(
                execution = ExecutionRequest.create(
                    plan = createPlan(traceId),
                    capability = createCapability(),
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
                "plan-outcome-request-provider",
            ),
            task = TaskRecord.create(
                taskId = TaskId.from(
                    "task-outcome-request-provider",
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
                                                1_754_000_130_000L,
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
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "VERIFICATION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_130_500L,
                ),
            summary =
                "Constitutional verification evaluation failed.",
        )
    }
}
