package com.devil.core.runtime.embodiment

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import com.devil.core.model.embodiment.PcEmbodimentEvidence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage83PcEmbodimentGovernanceTest {

    @Test
    fun `pc embodiment may be classified without creating another intelligence`() {
        val embodiment =
            pcEmbodiment()

        val evidence =
            PcEmbodimentEvidence.create(
                operatingSystemFamily = "Windows",
            )

        val result =
            PcEmbodimentCoordinator().assess(
                traceId =
                    TraceId.from(
                        "trace-stage83-pc-001",
                    ),
                embodiment = embodiment,
                evidence = evidence,
            )

        assertEquals(
            PcEmbodimentAssessmentStatus.PC,
            result.status,
        )

        assertSame(
            embodiment,
            result.embodiment,
        )

        assertSame(
            evidence,
            result.evidence,
        )

        assertEquals(
            "Windows",
            requireNotNull(result.evidence)
                .operatingSystemFamily,
        )
    }

    @Test
    fun `pc evidence normalizes operating system family`() {
        val evidence =
            PcEmbodimentEvidence.create(
                operatingSystemFamily =
                    "  Linux  ",
            )

        assertEquals(
            "Linux",
            evidence.operatingSystemFamily,
        )
    }

    @Test
    fun `pc evidence requires nonblank operating system family`() {
        assertFailsWith<IllegalArgumentException> {
            PcEmbodimentEvidence.create(
                operatingSystemFamily = "   ",
            )
        }
    }

    @Test
    fun `non pc platform identity is sufficient for non pc classification`() {
        val embodiment =
            androidEmbodiment()

        val result =
            PcEmbodimentCoordinator().assess(
                traceId =
                    TraceId.from(
                        "trace-stage83-pc-002",
                    ),
                embodiment = embodiment,
                evidence = null,
            )

        assertEquals(
            PcEmbodimentAssessmentStatus.NON_PC,
            result.status,
        )

        assertSame(
            embodiment,
            result.embodiment,
        )

        assertNull(result.evidence)
    }

    @Test
    fun `non pc embodiment does not preserve contradictory pc evidence`() {
        val embodiment =
            androidEmbodiment()

        val result =
            PcEmbodimentCoordinator().assess(
                traceId =
                    TraceId.from(
                        "trace-stage83-pc-003",
                    ),
                embodiment = embodiment,
                evidence =
                    PcEmbodimentEvidence.create(
                        operatingSystemFamily = "Linux",
                    ),
            )

        assertEquals(
            PcEmbodimentAssessmentStatus.NON_PC,
            result.status,
        )

        assertNull(result.evidence)
    }

    @Test
    fun `pc embodiment without genuine pc evidence remains deferred`() {
        val embodiment =
            pcEmbodiment()

        val result =
            PcEmbodimentCoordinator().assess(
                traceId =
                    TraceId.from(
                        "trace-stage83-pc-004",
                    ),
                embodiment = embodiment,
                evidence = null,
            )

        assertEquals(
            PcEmbodimentAssessmentStatus.DEFERRED,
            result.status,
        )

        assertSame(
            embodiment,
            result.embodiment,
        )

        assertNull(result.evidence)
    }

    @Test
    fun `pc platform matching is case insensitive without changing represented identity`() {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:pc-case-test",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "PC",
                    ),
                description =
                    "Bounded PC embodiment.",
            )

        val result =
            PcEmbodimentCoordinator().assess(
                traceId =
                    TraceId.from(
                        "trace-stage83-pc-005",
                    ),
                embodiment = embodiment,
                evidence =
                    PcEmbodimentEvidence.create(
                        operatingSystemFamily =
                            "future-desktop-os",
                    ),
            )

        assertEquals(
            PcEmbodimentAssessmentStatus.PC,
            result.status,
        )

        assertEquals(
            "PC",
            result.embodiment.platformId.value,
        )
    }

    @Test
    fun `determined pc result cannot exist without evidence`() {
        assertFailsWith<IllegalArgumentException> {
            PcEmbodimentAssessmentResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage83-result-001",
                    ),
                status =
                    PcEmbodimentAssessmentStatus.PC,
                embodiment =
                    pcEmbodiment(),
            )
        }
    }

    @Test
    fun `non pc result cannot smuggle pc evidence`() {
        assertFailsWith<IllegalArgumentException> {
            PcEmbodimentAssessmentResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage83-result-002",
                    ),
                status =
                    PcEmbodimentAssessmentStatus.NON_PC,
                embodiment =
                    androidEmbodiment(),
                evidence =
                    PcEmbodimentEvidence.create(
                        operatingSystemFamily =
                            "Linux",
                    ),
            )
        }
    }

    @Test
    fun `deferred pc result cannot smuggle evidence`() {
        assertFailsWith<IllegalArgumentException> {
            PcEmbodimentAssessmentResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage83-result-003",
                    ),
                status =
                    PcEmbodimentAssessmentStatus.DEFERRED,
                embodiment =
                    pcEmbodiment(),
                evidence =
                    PcEmbodimentEvidence.create(
                        operatingSystemFamily =
                            "Linux",
                    ),
            )
        }
    }

    private fun pcEmbodiment(): EmbodimentRecord {
        return EmbodimentRecord.create(
            embodimentId =
                EmbodimentId.from(
                    "embodiment:pc-primary",
                ),
            platformId =
                EmbodimentPlatformId.from(
                    PcEmbodimentCoordinator.PC_PLATFORM_ID,
                ),
            description =
                "Primary bounded PC embodiment of the unified Devil runtime.",
        )
    }

    private fun androidEmbodiment(): EmbodimentRecord {
        return EmbodimentRecord.create(
            embodimentId =
                EmbodimentId.from(
                    "embodiment:android-primary",
                ),
            platformId =
                EmbodimentPlatformId.from(
                    "android",
                ),
            description =
                "Primary Android embodiment of the unified Devil runtime.",
        )
    }
}
