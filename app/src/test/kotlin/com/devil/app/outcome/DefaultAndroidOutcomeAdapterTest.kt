package com.devil.app.outcome

import com.devil.app.verification.AndroidVerificationEvidence
import com.devil.app.verification.AndroidVerificationResult
import com.devil.app.verification.AndroidVerificationStatus
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAndroidOutcomeAdapterTest {

    @Test
    fun `deferred verification does not invoke outcome source`() {
        var invoked = false

        val adapter =
            DefaultAndroidOutcomeAdapter(
                outcomeSource =
                    AndroidOutcomeSource { _, _ ->
                        invoked = true
                        error("Outcome source must not be invoked.")
                    },
            )

        val result =
            adapter.establish(
                AndroidVerificationResult.create(
                    traceId = TraceId.from("trace-stage-33-deferred"),
                    status = AndroidVerificationStatus.DEFERRED,
                ),
            )

        assertEquals(AndroidOutcomeStatus.DEFERRED, result.status)
        assertEquals(false, invoked)
        assertNull(result.evidence)
    }

    @Test
    fun `failed verification preserves failure without invoking source`() {
        val traceId = TraceId.from("trace-stage-33-failed")

        val error =
            UniversalErrorRecord.create(
                errorCode = ErrorCode.from("ANDROID_VERIFICATION_FAILED"),
                traceId = traceId,
                occurredAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_330_500L,
                    ),
                summary = "Android verification failed.",
            )

        var invoked = false

        val adapter =
            DefaultAndroidOutcomeAdapter(
                outcomeSource =
                    AndroidOutcomeSource { _, _ ->
                        invoked = true
                        error("Outcome source must not be invoked.")
                    },
            )

        val result =
            adapter.establish(
                AndroidVerificationResult.create(
                    traceId = traceId,
                    status = AndroidVerificationStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(AndroidOutcomeStatus.FAILED, result.status)
        assertEquals(error, result.error)
        assertEquals(false, invoked)
    }

    @Test
    fun `verified result approaches source without becoming outcome automatically`() {
        val traceId = TraceId.from("trace-stage-33-verified")
        val capabilityId = CapabilityId.from("capability-stage-33")

        var invoked = false

        val adapter =
            DefaultAndroidOutcomeAdapter(
                outcomeSource =
                    AndroidOutcomeSource {
                            sourceTraceId,
                            verificationEvidence,
                        ->
                        invoked = true
                        assertEquals(traceId, sourceTraceId)
                        assertEquals(
                            capabilityId,
                            verificationEvidence.capabilityId,
                        )

                        AndroidOutcomeResult.create(
                            traceId = sourceTraceId,
                            status = AndroidOutcomeStatus.DEFERRED,
                        )
                    },
            )

        val result =
            adapter.establish(
                AndroidVerificationResult.create(
                    traceId = traceId,
                    status = AndroidVerificationStatus.VERIFIED,
                    evidence =
                        AndroidVerificationEvidence.create(
                            capabilityId = capabilityId,
                            description =
                                "Observed effect independently verified",
                        ),
                ),
            )

        assertEquals(true, invoked)
        assertEquals(AndroidOutcomeStatus.DEFERRED, result.status)
        assertNull(result.evidence)
    }

    @Test
    fun `adapter preserves genuine independently produced outcome evidence`() {
        val traceId = TraceId.from("trace-stage-33-established")
        val capabilityId = CapabilityId.from("capability-stage-33")

        val outcomeEvidence =
            AndroidOutcomeEvidence.create(
                capabilityId = capabilityId,
                description =
                    "Bounded Android outcome independently established",
            )

        val adapter =
            DefaultAndroidOutcomeAdapter(
                outcomeSource =
                    AndroidOutcomeSource { sourceTraceId, _ ->
                        AndroidOutcomeResult.create(
                            traceId = sourceTraceId,
                            status = AndroidOutcomeStatus.ESTABLISHED,
                            evidence = outcomeEvidence,
                        )
                    },
            )

        val result =
            adapter.establish(
                AndroidVerificationResult.create(
                    traceId = traceId,
                    status = AndroidVerificationStatus.VERIFIED,
                    evidence =
                        AndroidVerificationEvidence.create(
                            capabilityId = capabilityId,
                            description =
                                "Observed effect independently verified",
                        ),
                ),
            )

        assertEquals(AndroidOutcomeStatus.ESTABLISHED, result.status)
        assertEquals(outcomeEvidence, result.evidence)
    }

    @Test
    fun `adapter rejects outcome result from another trace`() {
        val adapter =
            DefaultAndroidOutcomeAdapter(
                outcomeSource =
                    AndroidOutcomeSource { _, _ ->
                        AndroidOutcomeResult.create(
                            traceId =
                                TraceId.from("trace-stage-33-other"),
                            status = AndroidOutcomeStatus.DEFERRED,
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            adapter.establish(
                AndroidVerificationResult.create(
                    traceId = TraceId.from("trace-stage-33-primary"),
                    status = AndroidVerificationStatus.VERIFIED,
                    evidence =
                        AndroidVerificationEvidence.create(
                            capabilityId =
                                CapabilityId.from("capability-stage-33"),
                            description =
                                "Observed effect independently verified",
                        ),
                ),
            )
        }
    }

    @Test
    fun `adapter rejects outcome evidence for another capability`() {
        val traceId = TraceId.from("trace-stage-33-capability")

        val adapter =
            DefaultAndroidOutcomeAdapter(
                outcomeSource =
                    AndroidOutcomeSource { sourceTraceId, _ ->
                        AndroidOutcomeResult.create(
                            traceId = sourceTraceId,
                            status = AndroidOutcomeStatus.ESTABLISHED,
                            evidence =
                                AndroidOutcomeEvidence.create(
                                    capabilityId =
                                        CapabilityId.from(
                                            "capability-stage-33-other",
                                        ),
                                    description =
                                        "Mismatched Android outcome evidence",
                                ),
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            adapter.establish(
                AndroidVerificationResult.create(
                    traceId = traceId,
                    status = AndroidVerificationStatus.VERIFIED,
                    evidence =
                        AndroidVerificationEvidence.create(
                            capabilityId =
                                CapabilityId.from("capability-stage-33"),
                            description =
                                "Observed effect independently verified",
                        ),
                ),
            )
        }
    }
}
