package com.devil.app.verification

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.error.ErrorCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidVerificationResultTest {

    @Test
    fun `verified result preserves genuine evidence`() {
        val traceId = TraceId.from("trace-stage-32-verified")
        val evidence =
            AndroidVerificationEvidence.create(
                capabilityId = CapabilityId.from("capability-stage-32"),
                description = "Observed effect independently verified",
            )

        val result =
            AndroidVerificationResult.create(
                traceId = traceId,
                status = AndroidVerificationStatus.VERIFIED,
                evidence = evidence,
            )

        assertEquals(AndroidVerificationStatus.VERIFIED, result.status)
        assertEquals(evidence, result.evidence)
        assertNull(result.error)
    }

    @Test
    fun `deferred result contains no evidence or error`() {
        val result =
            AndroidVerificationResult.create(
                traceId = TraceId.from("trace-stage-32-deferred"),
                status = AndroidVerificationStatus.DEFERRED,
            )

        assertEquals(AndroidVerificationStatus.DEFERRED, result.status)
        assertNull(result.evidence)
        assertNull(result.error)
    }

    @Test
    fun `verified result requires evidence`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVerificationResult.create(
                traceId = TraceId.from("trace-stage-32-missing-evidence"),
                status = AndroidVerificationStatus.VERIFIED,
            )
        }
    }

    @Test
    fun `deferred result rejects evidence`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVerificationResult.create(
                traceId = TraceId.from("trace-stage-32-deferred-evidence"),
                status = AndroidVerificationStatus.DEFERRED,
                evidence =
                    AndroidVerificationEvidence.create(
                        capabilityId =
                            CapabilityId.from("capability-stage-32"),
                        description = "Evidence must not survive deferral",
                    ),
            )
        }
    }

    @Test
    fun `verification evidence rejects blank description`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVerificationEvidence.create(
                capabilityId = CapabilityId.from("capability-stage-32"),
                description = "   ",
            )
        }
    }

    @Test
    fun `failed result requires matching trace error`() {
        val traceId = TraceId.from("trace-stage-32-failed")

        val error =
            UniversalErrorRecord.create(
                traceId = traceId,
                errorCode = ErrorCode.from("ANDROID_VERIFICATION_FAILED"),
                occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_320_000L),
                summary = "Verification failed.",
            )

        val result =
            AndroidVerificationResult.create(
                traceId = traceId,
                status = AndroidVerificationStatus.FAILED,
                error = error,
            )

        assertEquals(AndroidVerificationStatus.FAILED, result.status)
        assertEquals(error, result.error)
        assertNull(result.evidence)
    }

    @Test
    fun `verification result rejects error from another trace`() {
        val error =
            UniversalErrorRecord.create(
                traceId = TraceId.from("trace-stage-32-other"),
                errorCode = ErrorCode.from("ANDROID_VERIFICATION_FAILED"),
                occurredAt = DevilTimestamp.fromEpochMilliseconds(1_754_000_320_000L),
                summary = "Verification failed.",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidVerificationResult.create(
                traceId = TraceId.from("trace-stage-32-primary"),
                status = AndroidVerificationStatus.FAILED,
                error = error,
            )
        }
    }
}
