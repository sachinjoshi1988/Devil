package com.devil.app.outcome

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidOutcomeResultTest {

    @Test
    fun `established result preserves genuine evidence`() {
        val evidence =
            AndroidOutcomeEvidence.create(
                capabilityId = CapabilityId.from("capability-stage-33"),
                description = "Bounded Android outcome independently established",
            )

        val result =
            AndroidOutcomeResult.create(
                traceId = TraceId.from("trace-stage-33-established"),
                status = AndroidOutcomeStatus.ESTABLISHED,
                evidence = evidence,
            )

        assertEquals(AndroidOutcomeStatus.ESTABLISHED, result.status)
        assertEquals(evidence, result.evidence)
        assertNull(result.error)
    }

    @Test
    fun `deferred result contains no evidence or error`() {
        val result =
            AndroidOutcomeResult.create(
                traceId = TraceId.from("trace-stage-33-deferred"),
                status = AndroidOutcomeStatus.DEFERRED,
            )

        assertEquals(AndroidOutcomeStatus.DEFERRED, result.status)
        assertNull(result.evidence)
        assertNull(result.error)
    }

    @Test
    fun `established result requires evidence`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidOutcomeResult.create(
                traceId = TraceId.from("trace-stage-33-missing-evidence"),
                status = AndroidOutcomeStatus.ESTABLISHED,
            )
        }
    }

    @Test
    fun `outcome evidence rejects blank description`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidOutcomeEvidence.create(
                capabilityId = CapabilityId.from("capability-stage-33"),
                description = "   ",
            )
        }
    }

    @Test
    fun `failed result preserves matching error`() {
        val traceId = TraceId.from("trace-stage-33-failed")

        val error =
            UniversalErrorRecord.create(
                errorCode = ErrorCode.from("ANDROID_OUTCOME_FAILED"),
                traceId = traceId,
                occurredAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_330_000L,
                    ),
                summary = "Android outcome determination failed.",
            )

        val result =
            AndroidOutcomeResult.create(
                traceId = traceId,
                status = AndroidOutcomeStatus.FAILED,
                error = error,
            )

        assertEquals(AndroidOutcomeStatus.FAILED, result.status)
        assertEquals(error, result.error)
        assertNull(result.evidence)
    }
}
