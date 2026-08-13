package com.devil.app.observation

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
import com.devil.core.model.plan.PlanId
import com.devil.core.model.plan.PlanRecord
import com.devil.core.model.plan.PlanState
import com.devil.core.model.task.TaskId
import com.devil.core.model.task.TaskRecord
import com.devil.core.model.task.TaskState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.execution.ExecutionAttemptResult
import com.devil.core.runtime.execution.ExecutionAttemptStatus
import com.devil.core.runtime.observation.ObservationEvidenceStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidObservationEvidencePortTest {

    @Test
    fun `attempted execution with genuine Android evidence becomes neutral observed evidence`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-evidence-port-001",
            )
        val request =
            createExecutionRequest(traceId)

        val port =
            DefaultAndroidObservationEvidencePort(
                observationAdapter =
                    AndroidObservationAdapter { androidAttempt ->
                        assertEquals(
                            traceId,
                            androidAttempt.traceId,
                        )
                        assertEquals(
                            request.capability.capabilityId,
                            androidAttempt.capabilityId,
                        )

                        AndroidObservationResult.create(
                            traceId = androidAttempt.traceId,
                            status =
                                AndroidObservationStatus.OBSERVED,
                            evidence =
                                AndroidObservationEvidence.create(
                                    capabilityId =
                                        requireNotNull(
                                            androidAttempt.capabilityId,
                                        ),
                                    description =
                                        "The approved Android observer detected the bounded platform effect.",
                                ),
                        )
                    },
            )

        val result =
            port.observe(
                executionAttempt =
                    attempted(
                        traceId = traceId,
                        request = request,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ObservationEvidenceStatus.OBSERVED,
            result.status,
        )
        assertEquals(
            request.capability.capabilityId,
            result.capabilityId,
        )
        assertEquals(
            "The approved Android observer detected the bounded platform effect.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `attempted execution remains deferred when Android observation is deferred`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-evidence-port-002",
            )

        val port =
            DefaultAndroidObservationEvidencePort(
                observationAdapter =
                    AndroidObservationAdapter { androidAttempt ->
                        AndroidObservationResult.create(
                            traceId = androidAttempt.traceId,
                            status =
                                AndroidObservationStatus.DEFERRED,
                        )
                    },
            )

        val result =
            port.observe(
                executionAttempt =
                    attempted(
                        traceId = traceId,
                        request =
                            createExecutionRequest(traceId),
                    ),
            )

        assertEquals(
            ObservationEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `Android observation failure becomes neutral evidence failure`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-evidence-port-003",
            )
        val error =
            createError(
                traceId = traceId,
                code = "ANDROID_OBSERVATION_FAILED",
            )

        val port =
            DefaultAndroidObservationEvidencePort(
                observationAdapter =
                    AndroidObservationAdapter { androidAttempt ->
                        AndroidObservationResult.create(
                            traceId = androidAttempt.traceId,
                            status =
                                AndroidObservationStatus.FAILED,
                            error = error,
                        )
                    },
            )

        val result =
            port.observe(
                executionAttempt =
                    attempted(
                        traceId = traceId,
                        request =
                            createExecutionRequest(traceId),
                    ),
            )

        assertEquals(
            ObservationEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `deferred constitutional execution attempt never approaches Android observation`() {
        var calls = 0

        val port =
            DefaultAndroidObservationEvidencePort(
                observationAdapter =
                    AndroidObservationAdapter {
                        calls += 1

                        AndroidObservationResult.create(
                            traceId = it.traceId,
                            status =
                                AndroidObservationStatus.DEFERRED,
                        )
                    },
            )

        val result =
            port.observe(
                executionAttempt =
                    ExecutionAttemptResult.create(
                        traceId =
                            TraceId.from(
                                "trace-android-observation-evidence-port-004",
                            ),
                        status =
                            ExecutionAttemptStatus.DEFERRED,
                    ),
            )

        assertEquals(0, calls)
        assertEquals(
            ObservationEvidenceStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `failed constitutional execution attempt never approaches Android observation`() {
        var calls = 0
        val traceId =
            TraceId.from(
                "trace-android-observation-evidence-port-005",
            )
        val error =
            createError(
                traceId = traceId,
                code = "EXECUTION_ATTEMPT_FAILED",
            )

        val port =
            DefaultAndroidObservationEvidencePort(
                observationAdapter =
                    AndroidObservationAdapter {
                        calls += 1

                        AndroidObservationResult.create(
                            traceId = it.traceId,
                            status =
                                AndroidObservationStatus.DEFERRED,
                        )
                    },
            )

        val result =
            port.observe(
                executionAttempt =
                    ExecutionAttemptResult.create(
                        traceId = traceId,
                        status =
                            ExecutionAttemptStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(0, calls)
        assertEquals(
            ObservationEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
    }

    @Test
    fun `port rejects Android observation from another trace`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-evidence-port-006",
            )

        val port =
            DefaultAndroidObservationEvidencePort(
                observationAdapter =
                    AndroidObservationAdapter {
                        AndroidObservationResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-android-observation-evidence-other",
                                ),
                            status =
                                AndroidObservationStatus.DEFERRED,
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            port.observe(
                executionAttempt =
                    attempted(
                        traceId = traceId,
                        request =
                            createExecutionRequest(traceId),
                    ),
            )
        }
    }

    @Test
    fun `port rejects Android observation evidence for another capability`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-evidence-port-007",
            )

        val port =
            DefaultAndroidObservationEvidencePort(
                observationAdapter =
                    AndroidObservationAdapter { androidAttempt ->
                        AndroidObservationResult.create(
                            traceId = androidAttempt.traceId,
                            status =
                                AndroidObservationStatus.OBSERVED,
                            evidence =
                                AndroidObservationEvidence.create(
                                    capabilityId =
                                        CapabilityId.from(
                                            "capability-observation-other",
                                        ),
                                    description =
                                        "Evidence belongs to another capability.",
                                ),
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            port.observe(
                executionAttempt =
                    attempted(
                        traceId = traceId,
                        request =
                            createExecutionRequest(traceId),
                    ),
            )
        }
    }

    private fun attempted(
        traceId: TraceId,
        request: ExecutionRequest,
    ): ExecutionAttemptResult {
        return ExecutionAttemptResult.create(
            traceId = traceId,
            status = ExecutionAttemptStatus.ATTEMPTED,
            request = request,
        )
    }

    private fun createExecutionRequest(
        traceId: TraceId,
    ): ExecutionRequest {
        return ExecutionRequest.create(
            plan =
                PlanRecord.create(
                    planId =
                        PlanId.from(
                            "plan-android-observation-evidence-port",
                        ),
                    task =
                        TaskRecord.create(
                            taskId =
                                TaskId.from(
                                    "task-android-observation-evidence-port",
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
                                                                1_754_000_119_000L,
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
                    capabilityId =
                        CapabilityId.from(
                            "capability-android-observation-evidence",
                        ),
                    category =
                        CapabilityCategory.ACTION,
                    name =
                        "Android Observation Evidence Test Capability",
                    description =
                        "Represents one bounded Android observation-evidence capability.",
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(code),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_119_500L,
                ),
            summary =
                "Bounded execution-attempt or observation operation failed.",
        )
    }
}
