package com.devil.app.verification

import com.devil.app.observation.AndroidObservationEvidence
import com.devil.app.observation.AndroidObservationResult
import com.devil.app.observation.AndroidObservationStatus
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.error.ErrorCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidVerificationAdapterTest {

    @Test
    fun `deferred observation does not invoke verification source`() {
        var sourceInvoked = false

        val adapter =
            DefaultAndroidVerificationAdapter(
                verificationSource =
                    AndroidVerificationSource { _, _ ->
                        sourceInvoked = true
                        error("Verification source must not be invoked.")
                    },
            )

        val result =
            adapter.verify(
                AndroidObservationResult.create(
                    traceId = TraceId.from("trace-stage-32-deferred"),
                    status = AndroidObservationStatus.DEFERRED,
                ),
            )

        assertEquals(AndroidVerificationStatus.DEFERRED, result.status)
        assertEquals(false, sourceInvoked)
        assertNull(result.evidence)
        assertNull(result.error)
    }

    @Test
    fun `failed observation preserves matching failure without invoking source`() {
        val traceId = TraceId.from("trace-stage-32-failed")

        val error =
            UniversalErrorRecord.create(
                traceId = traceId,
                errorCode = ErrorCode.from("ANDROID_OBSERVATION_FAILED"),
                occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_320_500L),
                summary = "Observation failed.",
            )

        var sourceInvoked = false

        val adapter =
            DefaultAndroidVerificationAdapter(
                verificationSource =
                    AndroidVerificationSource { _, _ ->
                        sourceInvoked = true
                        error("Verification source must not be invoked.")
                    },
            )

        val result =
            adapter.verify(
                AndroidObservationResult.create(
                    traceId = traceId,
                    status = AndroidObservationStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(AndroidVerificationStatus.FAILED, result.status)
        assertEquals(error, result.error)
        assertEquals(false, sourceInvoked)
    }

    @Test
    fun `observed result approaches source without becoming verified automatically`() {
        val traceId = TraceId.from("trace-stage-32-observed")
        val capabilityId =
            CapabilityId.from("capability-stage-32")

        var sourceInvoked = false

        val adapter =
            DefaultAndroidVerificationAdapter(
                verificationSource =
                    AndroidVerificationSource {
                            sourceTraceId,
                            observationEvidence,
                        ->
                        sourceInvoked = true
                        assertEquals(traceId, sourceTraceId)
                        assertEquals(
                            capabilityId,
                            observationEvidence.capabilityId,
                        )

                        AndroidVerificationResult.create(
                            traceId = sourceTraceId,
                            status = AndroidVerificationStatus.DEFERRED,
                        )
                    },
            )

        val result =
            adapter.verify(
                AndroidObservationResult.create(
                    traceId = traceId,
                    status = AndroidObservationStatus.OBSERVED,
                    evidence =
                        AndroidObservationEvidence.create(
                            capabilityId = capabilityId,
                            description = "Observed bounded Android effect",
                        ),
                ),
            )

        assertEquals(true, sourceInvoked)
        assertEquals(AndroidVerificationStatus.DEFERRED, result.status)
        assertNull(result.evidence)
    }

    @Test
    fun `adapter preserves genuine independently produced verification evidence`() {
        val traceId = TraceId.from("trace-stage-32-verified")
        val capabilityId =
            CapabilityId.from("capability-stage-32")

        val verificationEvidence =
            AndroidVerificationEvidence.create(
                capabilityId = capabilityId,
                description = "Observed effect independently verified",
            )

        val adapter =
            DefaultAndroidVerificationAdapter(
                verificationSource =
                    AndroidVerificationSource { sourceTraceId, _ ->
                        AndroidVerificationResult.create(
                            traceId = sourceTraceId,
                            status = AndroidVerificationStatus.VERIFIED,
                            evidence = verificationEvidence,
                        )
                    },
            )

        val result =
            adapter.verify(
                AndroidObservationResult.create(
                    traceId = traceId,
                    status = AndroidObservationStatus.OBSERVED,
                    evidence =
                        AndroidObservationEvidence.create(
                            capabilityId = capabilityId,
                            description = "Observed bounded Android effect",
                        ),
                ),
            )

        assertEquals(AndroidVerificationStatus.VERIFIED, result.status)
        assertEquals(verificationEvidence, result.evidence)
    }

    @Test
    fun `adapter rejects verification result from another trace`() {
        val observation =
            AndroidObservationResult.create(
                traceId = TraceId.from("trace-stage-32-primary"),
                status = AndroidObservationStatus.OBSERVED,
                evidence =
                    AndroidObservationEvidence.create(
                        capabilityId =
                            CapabilityId.from("capability-stage-32"),
                        description = "Observed bounded Android effect",
                    ),
            )

        val adapter =
            DefaultAndroidVerificationAdapter(
                verificationSource =
                    AndroidVerificationSource { _, _ ->
                        AndroidVerificationResult.create(
                            traceId =
                                TraceId.from("trace-stage-32-other"),
                            status = AndroidVerificationStatus.DEFERRED,
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            adapter.verify(observation)
        }
    }

    @Test
    fun `adapter rejects verification evidence for another capability`() {
        val observation =
            AndroidObservationResult.create(
                traceId = TraceId.from("trace-stage-32-capability"),
                status = AndroidObservationStatus.OBSERVED,
                evidence =
                    AndroidObservationEvidence.create(
                        capabilityId =
                            CapabilityId.from("capability-stage-32"),
                        description = "Observed bounded Android effect",
                    ),
            )

        val adapter =
            DefaultAndroidVerificationAdapter(
                verificationSource =
                    AndroidVerificationSource { traceId, _ ->
                        AndroidVerificationResult.create(
                            traceId = traceId,
                            status = AndroidVerificationStatus.VERIFIED,
                            evidence =
                                AndroidVerificationEvidence.create(
                                    capabilityId =
                                        CapabilityId.from(
                                            "capability-stage-32-other",
                                        ),
                                    description =
                                        "Mismatched verification evidence",
                                ),
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            adapter.verify(observation)
        }
    }
}
