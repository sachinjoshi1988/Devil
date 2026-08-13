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

class DefaultVerificationEvidencePortTest {

    @Test
    fun `observed result remains deferred without configured verification embodiment`() {
        val traceId =
            TraceId.from(
                "trace-default-verification-evidence-port-001",
            )

        val result =
            DefaultVerificationEvidencePort().verify(
                observation =
                    observed(
                        traceId = traceId,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            VerificationEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `deferred observation remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-default-verification-evidence-port-002",
            )

        val result =
            DefaultVerificationEvidencePort().verify(
                observation =
                    ObservationResult.create(
                        traceId = traceId,
                        status = ObservationStatus.DEFERRED,
                    ),
            )

        assertEquals(
            VerificationEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed observation preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-default-verification-evidence-port-003",
            )

        val error = createError(traceId)

        val result =
            DefaultVerificationEvidencePort().verify(
                observation =
                    ObservationResult.create(
                        traceId = traceId,
                        status = ObservationStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(
            VerificationEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `default port never manufactures verified evidence`() {
        val traceId =
            TraceId.from(
                "trace-default-verification-evidence-port-004",
            )

        val result =
            DefaultVerificationEvidencePort().verify(
                observation =
                    observed(
                        traceId = traceId,
                    ),
            )

        assertEquals(
            VerificationEvidenceStatus.DEFERRED,
            result.status,
        )
    }

    private fun observed(
        traceId: TraceId,
    ): ObservationResult {
        return ObservationResult.create(
            traceId = traceId,
            status = ObservationStatus.OBSERVED,
            request =
                ObservationRequest.create(
                    execution =
                        ExecutionRequest.create(
                            plan =
                                PlanRecord.create(
                                    planId =
                                        PlanId.from(
                                            "plan-default-verification-evidence-port",
                                        ),
                                    task =
                                        TaskRecord.create(
                                            taskId =
                                                TaskId.from(
                                                    "task-default-verification-evidence-port",
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
                                                                                1_754_000_128_000L,
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
                                    capabilityId =
                                        CapabilityId.from(
                                            "capability-verification-evidence-port",
                                        ),
                                    category =
                                        CapabilityCategory.ACTION,
                                    name =
                                        "Verification Evidence Test Capability",
                                    description =
                                        "Represents one bounded capability for verification-evidence testing.",
                                ),
                        ),
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "OBSERVATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_128_500L,
                ),
            summary =
                "Bounded observation failed.",
        )
    }
}
