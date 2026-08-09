package com.devil.app.execution

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidExecutionAttemptResultTest {

    @Test
    fun `attempted result preserves capability identity without claiming outcome`() {
        val traceId = TraceId.from("trace-android-execution-result-001")
        val capabilityId =
            CapabilityId.from("capability-android-execution-result")

        val result =
            AndroidExecutionAttemptResult.create(
                traceId = traceId,
                status = AndroidExecutionAttemptStatus.ATTEMPTED,
                capabilityId = capabilityId,
            )

        assertEquals(AndroidExecutionAttemptStatus.ATTEMPTED, result.status)
        assertEquals(capabilityId, result.capabilityId)
        assertNull(result.error)
    }

    @Test
    fun `deferred result contains no capability or error`() {
        val result =
            AndroidExecutionAttemptResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-execution-result-002",
                    ),
                status = AndroidExecutionAttemptStatus.DEFERRED,
            )

        assertNull(result.capabilityId)
        assertNull(result.error)
    }

    @Test
    fun `failed result preserves matching error`() {
        val traceId =
            TraceId.from(
                "trace-android-execution-result-003",
            )
        val error = createError(traceId)

        val result =
            AndroidExecutionAttemptResult.create(
                traceId = traceId,
                status = AndroidExecutionAttemptStatus.FAILED,
                error = error,
            )

        assertEquals(AndroidExecutionAttemptStatus.FAILED, result.status)
        assertEquals(error, result.error)
    }

    @Test
    fun `attempted result requires capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidExecutionAttemptResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-execution-result-004",
                    ),
                status = AndroidExecutionAttemptStatus.ATTEMPTED,
            )
        }
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidExecutionAttemptResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-execution-result-005",
                    ),
                status = AndroidExecutionAttemptStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-android-execution-result-other",
                        ),
                    ),
            )
        }
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "ANDROID_EXECUTION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_230_500L,
                ),
            summary =
                "Bounded Android execution failed.",
        )
    }
}
