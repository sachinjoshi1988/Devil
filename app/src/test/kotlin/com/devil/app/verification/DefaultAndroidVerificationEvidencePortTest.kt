package com.devil.app.verification

import com.devil.app.observation.AndroidObservationResult
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
import com.devil.core.runtime.verification.VerificationEvidenceStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidVerificationEvidencePortTest {

    @Test
    fun `deferred constitutional observation does not invoke Android verification`() {
        var invoked = false

        val port =
            DefaultAndroidVerificationEvidencePort(
                verificationAdapter =
                    AndroidVerificationAdapter {
                        invoked = true
                        error("Android verification must not be invoked.")
                    },
            )

        val result =
            port.verify(
                observation =
                    ObservationResult.create(
                        traceId =
                            TraceId.from(
                                "trace-stage-66b-deferred",
                            ),
                        status = ObservationStatus.DEFERRED,
                    ),
            )

        assertEquals(false, invoked)
        assertEquals(
            VerificationEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed constitutional observation preserves failure without Android verification`() {
        val traceId =
            TraceId.from(
                "trace-stage-66b-failed",
            )
        val error =
            createError(
                traceId = traceId,
                code = "OBSERVATION_FAILED",
            )

        var invoked = false

        val port =
            DefaultAndroidVerificationEvidencePort(
                verificationAdapter =
                    AndroidVerificationAdapter {
                        invoked = true
                        error("Android verification must not be invoked.")
                    },
            )

        val result =
            port.verify(
                observation =
                    ObservationResult.create(
                        traceId = traceId,
                        status = ObservationStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(false, invoked)
        assertEquals(
            VerificationEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
    }

    @Test
    fun `observed constitutional result does not become verified when Android verification defers`() {
        var invoked = false

        val port =
            DefaultAndroidVerificationEvidencePort(
                verificationAdapter =
                    AndroidVerificationAdapter { androidObservation ->
                        invoked = true

                        AndroidVerificationResult.create(
                            traceId = androidObservation.traceId,
                            status = AndroidVerificationStatus.DEFERRED,
                        )
                    },
            )

        val result =
            port.verify(
                observation =
                    observed(
                        traceId =
                            TraceId.from(
                                "trace-stage-66b-observed-deferred",
                            ),
                    ),
            )

        assertEquals(true, invoked)
        assertEquals(
            VerificationEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `independently verified Android evidence becomes neutral verification evidence`() {
        val traceId =
            TraceId.from(
                "trace-stage-66b-verified",
            )
        val capabilityId =
            CapabilityId.from(
                "capability-camera",
            )

        val port =
            DefaultAndroidVerificationEvidencePort(
                verificationAdapter =
                    AndroidVerificationAdapter { androidObservation ->
                        AndroidVerificationResult.create(
                            traceId = androidObservation.traceId,
                            status = AndroidVerificationStatus.VERIFIED,
                            evidence =
                                AndroidVerificationEvidence.create(
                                    capabilityId = capabilityId,
                                    description =
                                        "  Android independently verified the bounded observed effect.  ",
                                ),
                        )
                    },
            )

        val result =
            port.verify(
                observation =
                    observed(
                        traceId = traceId,
                        capabilityId = capabilityId,
                    ),
            )

        assertEquals(
            VerificationEvidenceStatus.VERIFIED,
            result.status,
        )
        assertEquals(capabilityId, result.capabilityId)
        assertEquals(
            "Android independently verified the bounded observed effect.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `port rejects Android verification result from another trace`() {
        val observation =
            observed(
                traceId =
                    TraceId.from(
                        "trace-stage-66b-trace-primary",
                    ),
            )

        val port =
            DefaultAndroidVerificationEvidencePort(
                verificationAdapter =
                    AndroidVerificationAdapter {
                        AndroidVerificationResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-stage-66b-trace-other",
                                ),
                            status = AndroidVerificationStatus.DEFERRED,
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            port.verify(observation)
        }
    }

    @Test
    fun `port rejects Android verification evidence for another capability`() {
        val observation =
            observed(
                traceId =
                    TraceId.from(
                        "trace-stage-66b-capability",
                    ),
                capabilityId =
                    CapabilityId.from(
                        "capability-camera",
                    ),
            )

        val port =
            DefaultAndroidVerificationEvidencePort(
                verificationAdapter =
                    AndroidVerificationAdapter { androidObservation ->
                        AndroidVerificationResult.create(
                            traceId = androidObservation.traceId,
                            status = AndroidVerificationStatus.VERIFIED,
                            evidence =
                                AndroidVerificationEvidence.create(
                                    capabilityId =
                                        CapabilityId.from(
                                            "capability-other",
                                        ),
                                    description =
                                        "Evidence belongs to another capability.",
                                ),
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            port.verify(observation)
        }
    }

    private fun observed(
        traceId: TraceId,
        capabilityId: CapabilityId =
            CapabilityId.from(
                "capability-camera",
            ),
    ): ObservationResult {
        return ObservationResult.create(
            traceId = traceId,
            status = ObservationStatus.OBSERVED,
            request =
                ObservationRequest.create(
                    execution =
                        createExecutionRequest(
                            traceId = traceId,
                            capabilityId = capabilityId,
                        ),
                ),
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
                            "plan-stage-66b",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-stage-66b",
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
                                                                1_754_000_660_000L,
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
                    name = "Camera",
                    description =
                        "Performs one bounded registered camera action.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_660_500L,
                ),
            summary =
                "Bounded Stage 66B dependency failed.",
        )
    }
}
