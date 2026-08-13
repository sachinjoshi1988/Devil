package com.devil.app.outcome

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
import com.devil.core.runtime.outcome.OutcomeEvidenceStatus
import com.devil.core.runtime.verification.VerificationEvidenceResult
import com.devil.core.runtime.verification.VerificationEvidenceStatus
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.verification.VerificationStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidOutcomeEvidencePortTest {

    @Test
    fun `deferred constitutional verification does not invoke Android outcome`() {
        val traceId =
            TraceId.from(
                "trace-stage-67b-deferred",
            )

        var invoked = false

        val port =
            DefaultAndroidOutcomeEvidencePort(
                outcomeAdapter =
                    AndroidOutcomeAdapter {
                        invoked = true
                        error(
                            "Android outcome must not be invoked.",
                        )
                    },
            )

        val result =
            port.establish(
                verification =
                    VerificationResult.create(
                        traceId = traceId,
                        status =
                            VerificationStatus.DEFERRED,
                    ),
                verificationEvidence =
                    VerificationEvidenceResult.create(
                        traceId = traceId,
                        status =
                            VerificationEvidenceStatus.DEFERRED,
                    ),
            )

        assertEquals(false, invoked)
        assertEquals(
            OutcomeEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed constitutional verification preserves failure without Android outcome`() {
        val traceId =
            TraceId.from(
                "trace-stage-67b-failed",
            )

        val error =
            createError(
                traceId = traceId,
                code = "VERIFICATION_FAILED",
            )

        var invoked = false

        val port =
            DefaultAndroidOutcomeEvidencePort(
                outcomeAdapter =
                    AndroidOutcomeAdapter {
                        invoked = true
                        error(
                            "Android outcome must not be invoked.",
                        )
                    },
            )

        val result =
            port.establish(
                verification =
                    VerificationResult.create(
                        traceId = traceId,
                        status =
                            VerificationStatus.FAILED,
                        error = error,
                    ),
                verificationEvidence =
                    VerificationEvidenceResult.create(
                        traceId = traceId,
                        status =
                            VerificationEvidenceStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(false, invoked)
        assertEquals(
            OutcomeEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `verified constitutional result approaches Android outcome without becoming established automatically`() {
        val traceId =
            TraceId.from(
                "trace-stage-67b-verified-deferred",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-camera",
            )

        var invoked = false

        val port =
            DefaultAndroidOutcomeEvidencePort(
                outcomeAdapter =
                    AndroidOutcomeAdapter { verification ->
                        invoked = true

                        assertEquals(
                            traceId,
                            verification.traceId,
                        )
                        assertEquals(
                            capabilityId,
                            verification.evidence
                                ?.capabilityId,
                        )

                        AndroidOutcomeResult.create(
                            traceId =
                                verification.traceId,
                            status =
                                AndroidOutcomeStatus.DEFERRED,
                        )
                    },
            )

        val result =
            port.establish(
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

        assertEquals(true, invoked)
        assertEquals(
            OutcomeEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `genuine Android outcome evidence becomes neutral outcome evidence`() {
        val traceId =
            TraceId.from(
                "trace-stage-67b-established",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-camera",
            )

        val port =
            DefaultAndroidOutcomeEvidencePort(
                outcomeAdapter =
                    AndroidOutcomeAdapter { verification ->
                        AndroidOutcomeResult.create(
                            traceId =
                                verification.traceId,
                            status =
                                AndroidOutcomeStatus.ESTABLISHED,
                            evidence =
                                AndroidOutcomeEvidence.create(
                                    capabilityId =
                                        capabilityId,
                                    description =
                                        "  Android independently established bounded outcome evidence.  ",
                                ),
                        )
                    },
            )

        val result =
            port.establish(
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
            OutcomeEvidenceStatus.ESTABLISHED,
            result.status,
        )
        assertEquals(
            capabilityId,
            result.capabilityId,
        )
        assertEquals(
            "Android independently established bounded outcome evidence.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `port rejects verification evidence from another trace`() {
        val traceId =
            TraceId.from(
                "trace-stage-67b-trace-primary",
            )

        assertFailsWith<IllegalArgumentException> {
            DefaultAndroidOutcomeEvidencePort(
                outcomeAdapter =
                    AndroidOutcomeAdapter {
                        error(
                            "Android outcome must not be invoked.",
                        )
                    },
            ).establish(
                verification =
                    verified(
                        traceId = traceId,
                    ),
                verificationEvidence =
                    verifiedEvidence(
                        traceId =
                            TraceId.from(
                                "trace-stage-67b-trace-other",
                            ),
                    ),
            )
        }
    }

    @Test
    fun `port rejects verification evidence for another capability`() {
        val traceId =
            TraceId.from(
                "trace-stage-67b-capability-input",
            )

        assertFailsWith<IllegalArgumentException> {
            DefaultAndroidOutcomeEvidencePort(
                outcomeAdapter =
                    AndroidOutcomeAdapter {
                        error(
                            "Android outcome must not be invoked.",
                        )
                    },
            ).establish(
                verification =
                    verified(
                        traceId = traceId,
                        capabilityId =
                            CapabilityId.from(
                                "capability-camera",
                            ),
                    ),
                verificationEvidence =
                    verifiedEvidence(
                        traceId = traceId,
                        capabilityId =
                            CapabilityId.from(
                                "capability-other",
                            ),
                    ),
            )
        }
    }

    @Test
    fun `port rejects Android outcome result from another trace`() {
        val traceId =
            TraceId.from(
                "trace-stage-67b-android-trace",
            )

        val port =
            DefaultAndroidOutcomeEvidencePort(
                outcomeAdapter =
                    AndroidOutcomeAdapter {
                        AndroidOutcomeResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage-67b-android-other",
                                ),
                            status =
                                AndroidOutcomeStatus.DEFERRED,
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            port.establish(
                verification =
                    verified(
                        traceId = traceId,
                    ),
                verificationEvidence =
                    verifiedEvidence(
                        traceId = traceId,
                    ),
            )
        }
    }

    @Test
    fun `port rejects Android outcome evidence for another capability`() {
        val traceId =
            TraceId.from(
                "trace-stage-67b-android-capability",
            )

        val port =
            DefaultAndroidOutcomeEvidencePort(
                outcomeAdapter =
                    AndroidOutcomeAdapter { verification ->
                        AndroidOutcomeResult.create(
                            traceId =
                                verification.traceId,
                            status =
                                AndroidOutcomeStatus.ESTABLISHED,
                            evidence =
                                AndroidOutcomeEvidence.create(
                                    capabilityId =
                                        CapabilityId.from(
                                            "capability-other",
                                        ),
                                    description =
                                        "Outcome evidence belongs to another capability.",
                                ),
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            port.establish(
                verification =
                    verified(
                        traceId = traceId,
                    ),
                verificationEvidence =
                    verifiedEvidence(
                        traceId = traceId,
                    ),
            )
        }
    }

    private fun verified(
        traceId: TraceId,
        capabilityId: CapabilityId =
            CapabilityId.from(
                "capability-camera",
            ),
    ): VerificationResult {
        return VerificationResult.create(
            traceId = traceId,
            status =
                VerificationStatus.VERIFIED,
            request =
                VerificationRequest.create(
                    observation =
                        ObservationRequest.create(
                            execution =
                                createExecutionRequest(
                                    traceId = traceId,
                                    capabilityId =
                                        capabilityId,
                                ),
                        ),
                ),
        )
    }

    private fun verifiedEvidence(
        traceId: TraceId,
        capabilityId: CapabilityId =
            CapabilityId.from(
                "capability-camera",
            ),
    ): VerificationEvidenceResult {
        return VerificationEvidenceResult.create(
            traceId = traceId,
            status =
                VerificationEvidenceStatus.VERIFIED,
            capabilityId = capabilityId,
            description =
                "Android independently verified the bounded observed effect.",
        )
    }

    private fun createExecutionRequest(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-stage-67b",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-stage-67b",
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
                                                        SchemaVersion.from(
                                                            1,
                                                        ),
                                                    source =
                                                        ContextSource.TEST,
                                                    trustLevel =
                                                        ContextTrustLevel.VERIFIED,
                                                    securityLevel =
                                                        ContextSecurityLevel.RESTRICTED,
                                                    observedAt =
                                                        DevilTimestamp
                                                            .fromEpochMilliseconds(
                                                                1_754_000_670_000L,
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
                            state =
                                TaskState.CREATED,
                            summary =
                                "A bounded constitutional task was created.",
                        ),
                    state =
                        PlanState.CREATED,
                    summary =
                        "Use the constitutionally approved capability path.",
                ),
            capability =
                CapabilityContract.create(
                    capabilityId = capabilityId,
                    category =
                        CapabilityCategory.ACTION,
                    name =
                        "Stage 67B Test Capability",
                    description =
                        "Represents one bounded capability for Android outcome-evidence testing.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    code,
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_670_500L,
                ),
            summary =
                "Bounded Stage 67B dependency failed.",
        )
    }
}
