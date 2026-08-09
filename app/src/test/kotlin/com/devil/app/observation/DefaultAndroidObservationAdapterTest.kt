package com.devil.app.observation

import com.devil.app.execution.AndroidExecutionAttemptResult
import com.devil.app.execution.AndroidExecutionAttemptStatus
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidObservationAdapterTest {

    @Test
    fun `attempted execution may produce genuine observation from approved source`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-adapter-001",
            )
        val capabilityId =
            CapabilityId.from(
                "capability-android-observation-001",
            )

        val adapter =
            DefaultAndroidObservationAdapter(
                observationSource =
                    AndroidObservationSource {
                            sourceTraceId,
                            sourceCapabilityId,
                        ->
                        AndroidObservationResult.create(
                            traceId = sourceTraceId,
                            status =
                                AndroidObservationStatus.OBSERVED,
                            evidence =
                                AndroidObservationEvidence.create(
                                    capabilityId =
                                        sourceCapabilityId,
                                    description =
                                        "Approved observer detected the bounded platform effect.",
                                ),
                        )
                    },
            )

        val result =
            adapter.observe(
                attempted(
                    traceId = traceId,
                    capabilityId = capabilityId,
                ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            AndroidObservationStatus.OBSERVED,
            result.status,
        )
        assertEquals(
            capabilityId,
            result.evidence?.capabilityId,
        )
        assertNull(result.error)
    }

    @Test
    fun `default adapter does not reinterpret attempted execution as observed`() {
        val result =
            DefaultAndroidObservationAdapter().observe(
                attempted(
                    traceId =
                        TraceId.from(
                            "trace-android-observation-adapter-002",
                        ),
                    capabilityId =
                        CapabilityId.from(
                            "capability-android-observation-002",
                        ),
                ),
            )

        assertEquals(
            AndroidObservationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.evidence)
        assertNull(result.error)
    }

    @Test
    fun `deferred execution attempt does not invoke observation source`() {
        var sourceInvoked = false

        val adapter =
            DefaultAndroidObservationAdapter(
                observationSource =
                    AndroidObservationSource { traceId, capabilityId ->
                        sourceInvoked = true

                        AndroidObservationResult.create(
                            traceId = traceId,
                            status =
                                AndroidObservationStatus.OBSERVED,
                            evidence =
                                AndroidObservationEvidence.create(
                                    capabilityId = capabilityId,
                                    description =
                                        "This source must not be reached.",
                                ),
                        )
                    },
            )

        val result =
            adapter.observe(
                AndroidExecutionAttemptResult.create(
                    traceId =
                        TraceId.from(
                            "trace-android-observation-adapter-003",
                        ),
                    status =
                        AndroidExecutionAttemptStatus.DEFERRED,
                ),
            )

        assertEquals(false, sourceInvoked)
        assertEquals(
            AndroidObservationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `failed execution attempt preserves operational error without observing`() {
        var sourceInvoked = false
        val traceId =
            TraceId.from(
                "trace-android-observation-adapter-004",
            )
        val error = createError(traceId)

        val adapter =
            DefaultAndroidObservationAdapter(
                observationSource =
                    AndroidObservationSource { sourceTraceId, capabilityId ->
                        sourceInvoked = true

                        AndroidObservationResult.create(
                            traceId = sourceTraceId,
                            status =
                                AndroidObservationStatus.OBSERVED,
                            evidence =
                                AndroidObservationEvidence.create(
                                    capabilityId = capabilityId,
                                    description =
                                        "This source must not be reached.",
                                ),
                        )
                    },
            )

        val result =
            adapter.observe(
                AndroidExecutionAttemptResult.create(
                    traceId = traceId,
                    status =
                        AndroidExecutionAttemptStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(false, sourceInvoked)
        assertEquals(
            AndroidObservationStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.evidence)
    }

    @Test
    fun `adapter rejects observation result from another trace`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-adapter-005",
            )

        val adapter =
            DefaultAndroidObservationAdapter(
                observationSource =
                    AndroidObservationSource { _, capabilityId ->
                        AndroidObservationResult.create(
                            traceId =
                                TraceId.from(
                                    "trace-android-observation-adapter-other",
                                ),
                            status =
                                AndroidObservationStatus.OBSERVED,
                            evidence =
                                AndroidObservationEvidence.create(
                                    capabilityId = capabilityId,
                                    description =
                                        "Observation uses the wrong trace.",
                                ),
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            adapter.observe(
                attempted(
                    traceId = traceId,
                    capabilityId =
                        CapabilityId.from(
                            "capability-android-observation-005",
                        ),
                ),
            )
        }
    }

    @Test
    fun `adapter rejects observation evidence for another capability`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-adapter-006",
            )

        val adapter =
            DefaultAndroidObservationAdapter(
                observationSource =
                    AndroidObservationSource { sourceTraceId, _ ->
                        AndroidObservationResult.create(
                            traceId = sourceTraceId,
                            status =
                                AndroidObservationStatus.OBSERVED,
                            evidence =
                                AndroidObservationEvidence.create(
                                    capabilityId =
                                        CapabilityId.from(
                                            "capability-android-observation-other",
                                        ),
                                    description =
                                        "Observation belongs to another capability.",
                                ),
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            adapter.observe(
                attempted(
                    traceId = traceId,
                    capabilityId =
                        CapabilityId.from(
                            "capability-android-observation-006",
                        ),
                ),
            )
        }
    }

    private fun attempted(
        traceId: TraceId,
        capabilityId: CapabilityId,
    ): AndroidExecutionAttemptResult {
        return AndroidExecutionAttemptResult.create(
            traceId = traceId,
            status =
                AndroidExecutionAttemptStatus.ATTEMPTED,
            capabilityId = capabilityId,
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "ANDROID_EXECUTION_OR_OBSERVATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_232_000L,
                ),
            summary =
                "Bounded Android execution or observation failed.",
        )
    }
}
