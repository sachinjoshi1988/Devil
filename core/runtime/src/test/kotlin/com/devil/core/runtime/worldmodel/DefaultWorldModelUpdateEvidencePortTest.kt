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
import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.outcome.OutcomeStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultWorldModelUpdateEvidencePortTest {

    @Test
    fun `established outcome remains deferred without configured world model evidence embodiment`() {
        val traceId =
            TraceId.from(
                "trace-default-world-model-update-evidence-port-001",
            )

        val result =
            DefaultWorldModelUpdateEvidencePort().establish(
                outcome =
                    establishedOutcome(
                        traceId = traceId,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            WorldModelUpdateEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `deferred outcome remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-default-world-model-update-evidence-port-002",
            )

        val result =
            DefaultWorldModelUpdateEvidencePort().establish(
                outcome =
                    OutcomeResult.create(
                        traceId = traceId,
                        status = OutcomeStatus.DEFERRED,
                    ),
            )

        assertEquals(
            WorldModelUpdateEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed outcome preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-default-world-model-update-evidence-port-003",
            )

        val error =
            createError(traceId)

        val result =
            DefaultWorldModelUpdateEvidencePort().establish(
                outcome =
                    OutcomeResult.create(
                        traceId = traceId,
                        status = OutcomeStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(
            WorldModelUpdateEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `default port never manufactures established world model update evidence`() {
        val result =
            DefaultWorldModelUpdateEvidencePort().establish(
                outcome =
                    establishedOutcome(
                        traceId =
                            TraceId.from(
                                "trace-default-world-model-update-evidence-port-004",
                            ),
                    ),
            )

        assertEquals(
            WorldModelUpdateEvidenceStatus.DEFERRED,
            result.status,
        )
    }

    private fun establishedOutcome(
        traceId: TraceId,
    ): OutcomeResult {
        return OutcomeResult.create(
            traceId = traceId,
            status = OutcomeStatus.ESTABLISHED,
            request =
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
                                                            "capability-world-model-update-evidence-port",
                                                        ),
                                                    category =
                                                        CapabilityCategory.ACTION,
                                                    name =
                                                        "World Model Evidence Test Capability",
                                                    description =
                                                        "Represents one bounded capability for World Model update-evidence testing.",
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
                    "plan-default-world-model-update-evidence-port",
                ),
            task =
                TaskRecord.create(
                    taskId =
                        TaskId.from(
                            "task-default-world-model-update-evidence-port",
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
                                                        1_754_000_680_000L,
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
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "OUTCOME_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_680_500L,
                ),
            summary =
                "Constitutional Outcome failed before World Model update evidence.",
        )
    }
}
