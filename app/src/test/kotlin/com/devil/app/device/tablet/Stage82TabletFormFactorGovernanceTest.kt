package com.devil.app.device.tablet

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage82TabletFormFactorGovernanceTest {

    @Test
    fun `android embodiment at 600dp is classified as tablet`() {
        val embodiment =
            androidEmbodiment()

        val evidence =
            AndroidTabletFormFactorEvidence.create(
                smallestScreenWidthDp = 600,
            )

        val result =
            AndroidTabletFormFactorCoordinator(
                evidenceSource =
                    AndroidTabletFormFactorEvidenceSource {
                        evidence
                    },
            ).assess(
                traceId =
                    TraceId.from(
                        "trace-stage82-tablet-001",
                    ),
                embodiment = embodiment,
            )

        assertEquals(
            AndroidTabletFormFactorAssessmentStatus.TABLET,
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
    }

    @Test
    fun `android embodiment above tablet threshold remains tablet`() {
        val result =
            coordinatorWithWidth(
                smallestScreenWidthDp = 840,
            ).assess(
                traceId =
                    TraceId.from(
                        "trace-stage82-tablet-002",
                    ),
                embodiment =
                    androidEmbodiment(),
            )

        assertEquals(
            AndroidTabletFormFactorAssessmentStatus.TABLET,
            result.status,
        )
    }

    @Test
    fun `android embodiment below 600dp is classified as non tablet`() {
        val result =
            coordinatorWithWidth(
                smallestScreenWidthDp = 599,
            ).assess(
                traceId =
                    TraceId.from(
                        "trace-stage82-tablet-003",
                    ),
                embodiment =
                    androidEmbodiment(),
            )

        assertEquals(
            AndroidTabletFormFactorAssessmentStatus.NON_TABLET,
            result.status,
        )
        assertEquals(
            599,
            requireNotNull(result.evidence)
                .smallestScreenWidthDp,
        )
    }

    @Test
    fun `missing genuine configuration evidence remains deferred`() {
        val result =
            AndroidTabletFormFactorCoordinator(
                evidenceSource =
                    AndroidTabletFormFactorEvidenceSource {
                        null
                    },
            ).assess(
                traceId =
                    TraceId.from(
                        "trace-stage82-tablet-004",
                    ),
                embodiment =
                    androidEmbodiment(),
            )

        assertEquals(
            AndroidTabletFormFactorAssessmentStatus.DEFERRED,
            result.status,
        )
        assertNull(result.evidence)
    }

    @Test
    fun `non android embodiment is deferred without consulting android evidence`() {
        var sourceInvoked = false

        val result =
            AndroidTabletFormFactorCoordinator(
                evidenceSource =
                    AndroidTabletFormFactorEvidenceSource {
                        sourceInvoked = true

                        AndroidTabletFormFactorEvidence.create(
                            smallestScreenWidthDp = 800,
                        )
                    },
            ).assess(
                traceId =
                    TraceId.from(
                        "trace-stage82-tablet-005",
                    ),
                embodiment =
                    EmbodimentRecord.create(
                        embodimentId =
                            EmbodimentId.from(
                                "embodiment:pc-test",
                            ),
                        platformId =
                            EmbodimentPlatformId.from(
                                "pc",
                            ),
                        description =
                            "Bounded non-Android embodiment.",
                    ),
            )

        assertEquals(
            AndroidTabletFormFactorAssessmentStatus.DEFERRED,
            result.status,
        )
        assertEquals(
            false,
            sourceInvoked,
        )
        assertNull(result.evidence)
    }

    @Test
    fun `tablet evidence requires positive smallest screen width`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidTabletFormFactorEvidence.create(
                smallestScreenWidthDp = 0,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            AndroidTabletFormFactorEvidence.create(
                smallestScreenWidthDp = -1,
            )
        }
    }

    @Test
    fun `determined tablet result cannot exist without evidence`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidTabletFormFactorAssessmentResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage82-result-001",
                    ),
                status =
                    AndroidTabletFormFactorAssessmentStatus.TABLET,
                embodiment =
                    androidEmbodiment(),
            )
        }
    }

    @Test
    fun `deferred tablet result cannot smuggle evidence`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidTabletFormFactorAssessmentResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage82-result-002",
                    ),
                status =
                    AndroidTabletFormFactorAssessmentStatus.DEFERRED,
                embodiment =
                    androidEmbodiment(),
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 700,
                    ),
            )
        }
    }

    private fun coordinatorWithWidth(
        smallestScreenWidthDp: Int,
    ): AndroidTabletFormFactorCoordinator {
        return AndroidTabletFormFactorCoordinator(
            evidenceSource =
                AndroidTabletFormFactorEvidenceSource {
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp =
                            smallestScreenWidthDp,
                    )
                },
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
