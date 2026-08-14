package com.devil.core.runtime.surveillance

import com.devil.core.model.common.TraceId
import com.devil.core.model.surveillance.SecuritySurveillanceRecord
import com.devil.core.model.surveillance.SecuritySurveillanceSignal
import com.devil.core.model.surveillance.SecuritySurveillanceSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage90SecuritySurveillanceFoundationGovernanceTest {

    @Test
    fun `bounded surveillance signal may be prepared without creating security response`() {
        val traceId =
            TraceId.from(
                "trace-stage90-surveillance-001",
            )

        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId = traceId,
                sourceId = "camera:warehouse-entry",
                sourceType = "network-camera",
                occurredAtEpochMilliseconds = 1000L,
                description =
                    "Supplied source reports bounded movement information.",
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            SecuritySurveillancePreparationStatus.PREPARED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertEquals(
            "camera:warehouse-entry",
            record.source.sourceId,
        )

        assertEquals(
            "network-camera",
            record.source.sourceType,
        )

        assertSame(
            record.source,
            record.signal.source,
        )

        assertEquals(
            1000L,
            record.signal.occurredAtEpochMilliseconds,
        )

        assertEquals(
            "Supplied source reports bounded movement information.",
            record.signal.description,
        )
    }

    @Test
    fun `surveillance source metadata is normalized and required`() {
        val source =
            SecuritySurveillanceSource.create(
                sourceId =
                    "  camera:front-gate  ",
                sourceType =
                    "  CCTV  ",
            )

        assertEquals(
            "camera:front-gate",
            source.sourceId,
        )

        assertEquals(
            "CCTV",
            source.sourceType,
        )

        assertFailsWith<IllegalArgumentException> {
            SecuritySurveillanceSource.create(
                sourceId = "   ",
                sourceType = "camera",
            )
        }

        assertFailsWith<IllegalArgumentException> {
            SecuritySurveillanceSource.create(
                sourceId = "camera:1",
                sourceType = "   ",
            )
        }
    }

    @Test
    fun `surveillance source type remains extensible descriptive metadata`() {
        val source =
            SecuritySurveillanceSource.create(
                sourceId = "future-source:001",
                sourceType =
                    "future-authorized-surveillance-embodiment",
            )

        assertEquals(
            "future-authorized-surveillance-embodiment",
            source.sourceType,
        )
    }

    @Test
    fun `surveillance signal description is normalized`() {
        val source =
            source()

        val signal =
            SecuritySurveillanceSignal.create(
                source = source,
                occurredAtEpochMilliseconds = 50L,
                description =
                    "  Supplied bounded signal.  ",
            )

        assertSame(
            source,
            signal.source,
        )

        assertEquals(
            "Supplied bounded signal.",
            signal.description,
        )
    }

    @Test
    fun `surveillance signal rejects negative time`() {
        assertFailsWith<IllegalArgumentException> {
            SecuritySurveillanceSignal.create(
                source = source(),
                occurredAtEpochMilliseconds = -1L,
                description =
                    "Supplied bounded signal.",
            )
        }
    }

    @Test
    fun `surveillance signal rejects blank description`() {
        assertFailsWith<IllegalArgumentException> {
            SecuritySurveillanceSignal.create(
                source = source(),
                occurredAtEpochMilliseconds = 0L,
                description = "   ",
            )
        }
    }

    @Test
    fun `surveillance record requires source consistency`() {
        val sourceOne =
            SecuritySurveillanceSource.create(
                sourceId = "camera:one",
                sourceType = "camera",
            )

        val sourceTwo =
            SecuritySurveillanceSource.create(
                sourceId = "camera:two",
                sourceType = "camera",
            )

        val signal =
            SecuritySurveillanceSignal.create(
                source = sourceOne,
                occurredAtEpochMilliseconds = 100L,
                description =
                    "Supplied signal from camera one.",
            )

        assertFailsWith<IllegalArgumentException> {
            SecuritySurveillanceRecord.create(
                source = sourceTwo,
                signal = signal,
            )
        }
    }

    @Test
    fun `surveillance record preserves supplied source and signal only`() {
        val source =
            source()

        val signal =
            SecuritySurveillanceSignal.create(
                source = source,
                occurredAtEpochMilliseconds = 100L,
                description =
                    "Supplied bounded signal.",
            )

        val record =
            SecuritySurveillanceRecord.create(
                source = source,
                signal = signal,
            )

        assertSame(
            source,
            record.source,
        )

        assertSame(
            signal,
            record.signal,
        )
    }

    @Test
    fun `blank source identity remains deferred`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-surveillance-002",
                    ),
                sourceId = "   ",
                sourceType = "camera",
                occurredAtEpochMilliseconds = 10L,
                description =
                    "Supplied bounded signal.",
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank source type remains deferred`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-surveillance-003",
                    ),
                sourceId = "camera:1",
                sourceType = "   ",
                occurredAtEpochMilliseconds = 10L,
                description =
                    "Supplied bounded signal.",
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `negative supplied surveillance time remains deferred`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-surveillance-004",
                    ),
                sourceId = "camera:1",
                sourceType = "camera",
                occurredAtEpochMilliseconds = -1L,
                description =
                    "Supplied bounded signal.",
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `blank supplied signal remains deferred rather than being invented`() {
        val result =
            SecuritySurveillanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage90-surveillance-005",
                    ),
                sourceId = "camera:1",
                sourceType = "camera",
                occurredAtEpochMilliseconds = 10L,
                description = "   ",
            )

        assertEquals(
            SecuritySurveillancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.record)
    }

    @Test
    fun `prepared result requires one surveillance record`() {
        assertFailsWith<IllegalArgumentException> {
            SecuritySurveillancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage90-result-001",
                    ),
                status =
                    SecuritySurveillancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle surveillance record`() {
        val source =
            source()

        val record =
            SecuritySurveillanceRecord.create(
                source = source,
                signal =
                    SecuritySurveillanceSignal.create(
                        source = source,
                        occurredAtEpochMilliseconds = 10L,
                        description =
                            "Supplied bounded signal.",
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            SecuritySurveillancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage90-result-002",
                    ),
                status =
                    SecuritySurveillancePreparationStatus.DEFERRED,
                record = record,
            )
        }
    }

    @Test
    fun `deferred result contains no surveillance record`() {
        val result =
            SecuritySurveillancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage90-result-003",
                    ),
                status =
                    SecuritySurveillancePreparationStatus.DEFERRED,
            )

        assertNull(result.record)
    }

    private fun source(): SecuritySurveillanceSource {
        return SecuritySurveillanceSource.create(
            sourceId = "camera:stage90-test",
            sourceType = "camera",
        )
    }
}
