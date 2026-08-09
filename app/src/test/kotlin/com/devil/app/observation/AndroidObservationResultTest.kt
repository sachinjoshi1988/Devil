package com.devil.app.observation

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidObservationResultTest {

    @Test
    fun `observed result preserves genuine bounded evidence`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-result-001",
            )

        val evidence =
            AndroidObservationEvidence.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-android-observation",
                    ),
                description =
                    "The bounded Android observer detected the explicitly supported effect.",
            )

        val result =
            AndroidObservationResult.create(
                traceId = traceId,
                status = AndroidObservationStatus.OBSERVED,
                evidence = evidence,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(AndroidObservationStatus.OBSERVED, result.status)
        assertEquals(evidence, result.evidence)
        assertNull(result.error)
    }

    @Test
    fun `deferred result contains no fabricated evidence`() {
        val result =
            AndroidObservationResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-observation-result-002",
                    ),
                status = AndroidObservationStatus.DEFERRED,
            )

        assertEquals(AndroidObservationStatus.DEFERRED, result.status)
        assertNull(result.evidence)
        assertNull(result.error)
    }

    @Test
    fun `failed result preserves matching error`() {
        val traceId =
            TraceId.from(
                "trace-android-observation-result-003",
            )
        val error = createError(traceId)

        val result =
            AndroidObservationResult.create(
                traceId = traceId,
                status = AndroidObservationStatus.FAILED,
                error = error,
            )

        assertEquals(AndroidObservationStatus.FAILED, result.status)
        assertNull(result.evidence)
        assertEquals(error, result.error)
    }

    @Test
    fun `observed result requires evidence`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidObservationResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-observation-result-004",
                    ),
                status = AndroidObservationStatus.OBSERVED,
            )
        }
    }

    @Test
    fun `deferred result rejects fabricated evidence`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidObservationResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-observation-result-005",
                    ),
                status = AndroidObservationStatus.DEFERRED,
                evidence =
                    AndroidObservationEvidence.create(
                        capabilityId =
                            CapabilityId.from(
                                "capability-observation-invalid",
                            ),
                        description =
                            "This evidence must not be attached to a deferred result.",
                    ),
            )
        }
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidObservationResult.create(
                traceId =
                    TraceId.from(
                        "trace-android-observation-result-006",
                    ),
                status = AndroidObservationStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-android-observation-result-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `evidence requires nonblank description`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidObservationEvidence.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-observation-description",
                    ),
                description = "   ",
            )
        }
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "ANDROID_OBSERVATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_231_500L,
                ),
            summary =
                "Bounded Android observation failed.",
        )
    }
}
