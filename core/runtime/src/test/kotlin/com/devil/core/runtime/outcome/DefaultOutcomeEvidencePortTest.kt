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
import com.devil.core.runtime.verification.VerificationEvidenceResult
import com.devil.core.runtime.verification.VerificationEvidenceStatus
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.verification.VerificationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultOutcomeEvidencePortTest {

    @Test
    fun `verified result remains deferred without configured outcome embodiment`() {
        val traceId =
            TraceId.from(
                "trace-default-outcome-evidence-port-001",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-outcome-evidence-port",
            )

        val result =
            DefaultOutcomeEvidencePort().establish(
                verification =
                    verified(
                        traceId = traceId,
                        capabilityId = capabilityId,
                    ),
                verificationEvidence =
                    verifiedEvidence(
                        traceId = traceId,
                        capabilityId = capabilityId,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            OutcomeEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `deferred verification remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-default-outcome-evidence-port-002",
            )

        val result =
            DefaultOutcomeEvidencePort().establish(
                verification =
                    VerificationResult.create(
                        traceId = traceId,
                        status = VerificationStatus.DEFERRED,
                    ),
                verificationEvidence =
                    VerificationEvidenceResult.create(
                        traceId = traceId,
                        status = VerificationEvidenceStatus.DEFERRED,
                    ),
            )

        assertEquals(
            OutcomeEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed verification preserves matching operational failure`() {
        val traceId =
            TraceId.from(
                "trace-default-outcome-evidence-port-003",
            )

        val error = createError(traceId)

        val result =
            DefaultOutcomeEvidencePort().establish(
                verification =
                    VerificationResult.create(
                        traceId = traceId,
                        status = VerificationStatus.FAILED,
                        error = error,
                    ),
                verificationEvidence =
                    VerificationEvidenceResult.create(
                        traceId = traceId,
                        status = VerificationEvidenceStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(
            OutcomeEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `default port never manufactures established outcome evidence`() {
        val traceId =
            TraceId.from(
                "trace-default-outcome-evidence-port-004",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-outcome-evidence-port",
            )

        val result =
            DefaultOutcomeEvidencePort().establish(
                verification =
                    verified(
                        traceId = traceId,
                        capabilityId = capabilityId,
                    ),
                verificationEvidence =
                    verifiedEvidence(
                        traceId = traceId,
                        capabilityId = capabilityId,
                    ),
            )

        assertEquals(
            OutcomeEvidenceStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `port rejects verification evidence from another trace`() {
        val traceId =
            TraceId.from(
                "trace-default-outcome-evidence-port-005",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-outcome-evidence-port",
            )

        assertFailsWith<IllegalArgumentException> {
            DefaultOutcomeEvidencePort().establish(
                verification =
                    verified(
                        traceId = traceId,
                        capabilityId = capabilityId,
                    ),
                verificationEvidence =
                    verifiedEvidence(
                        traceId =
                            TraceId.from(
                                "trace-default-outcome-evidence-port-other",
                            ),
                        capabilityId = capabilityId,
                    ),
            )
        }
    }

    @Test
    fun `verified result requires genuinely verified evidence`() {
        val traceId =
            TraceId.from(
                "trace-default-outcome-evidence-port-006",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-outcome-evidence-port",
            )

        assertFailsWith<IllegalArgumentException> {
            DefaultOutcomeEvidencePort().establish(
                verification =
                    verified(
                        traceId = traceId,
                        capabilityId = capabilityId,
                    ),
                verificationEvidence =
                    VerificationEvidenceResult.create(
                        traceId = traceId,
                        status = VerificationEvidenceStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `verified result rejects verification evidence for another capability`() {
        val traceId =
            TraceId.from(
                "trace-default-outcome-evidence-port-007",
            )

        assertFailsWith<IllegalArgumentException> {
            DefaultOutcomeEvidencePort().establish(
                verification =
                    verified(
                        traceId = traceId,
                        capabilityId =
                            CapabilityId.from(
                                "capability-outcome-evidence-port",
                            ),
                    ),
                verificationEvidence =
                    verifiedEvidence(
                        traceId = traceId,
                        capabilityId =
                            CapabilityId.from(
                                "capability-outcome-evidence-port-other",
                            ),
                    ),
            )
        }
    }

    private fun verified(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): VerificationResult {
        return VerificationResult.create(
            traceId = traceId,
            status = VerificationStatus.VERIFIED,
            request =
                VerificationRequest.create(
                    observation =
                        ObservationRequest.create(
                            execution =
                                executionRequest(
                                    traceId = traceId,
                                    capabilityId = capabilityId,
                                ),
                        ),
                ),
        )
    }

    private fun verifiedEvidence(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): VerificationEvidenceResult {
        return VerificationEvidenceResult.create(
            traceId = traceId,
            status = VerificationEvidenceStatus.VERIFIED,
            capabilityId = capabilityId,
            description =
                "Genuine bounded verification evidence.",
        )
    }

    private fun executionRequest(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-default-outcome-evidence-port",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-default-outcome-evidence-port",
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
                                                                1_754_000_136_000L,
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
                ),
            capability =
                CapabilityContract.create(
                    capabilityId = capabilityId,
                    category = CapabilityCategory.ACTION,
                    name =
                        "Outcome Evidence Test Capability",
                    description =
                        "Represents one bounded capability for outcome-evidence testing.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "VERIFICATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_136_500L,
                ),
            summary =
                "Bounded verification failed.",
        )
    }
}
