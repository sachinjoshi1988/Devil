package com.devil.app.device.tablet

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage214TabletEmbodimentTest {

    @Test
    fun `tablet assessment becomes available and preserves exact provenance`() {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage214:tablet",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage214-tablet"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.TABLET,
                embodiment = embodiment,
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 600,
                    ),
            )

        val result =
            AndroidTabletEmbodimentCoordinator()
                .integrate(assessment)

        assertEquals(
            AndroidTabletEmbodimentStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            assessment,
            result.formFactorAssessment,
        )
        assertSame(
            embodiment,
            result.embodiment,
        )
    }

    @Test
    fun `non tablet assessment remains deferred`() {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage214:phone",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage214-phone"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.NON_TABLET,
                embodiment = embodiment,
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 411,
                    ),
            )

        val result =
            AndroidTabletEmbodimentCoordinator()
                .integrate(assessment)

        assertEquals(
            AndroidTabletEmbodimentStatus.DEFERRED,
            result.status,
        )
        assertSame(
            assessment,
            result.formFactorAssessment,
        )
        assertSame(
            embodiment,
            result.embodiment,
        )
    }

    @Test
    fun `deferred Stage 82 assessment remains deferred`() {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage214:deferred",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage214-deferred"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.DEFERRED,
                embodiment = embodiment,
            )

        val result =
            AndroidTabletEmbodimentCoordinator()
                .integrate(assessment)

        assertEquals(
            AndroidTabletEmbodimentStatus.DEFERRED,
            result.status,
        )
        assertSame(
            assessment,
            result.formFactorAssessment,
        )
        assertSame(
            embodiment,
            result.embodiment,
        )
    }

    @Test
    fun `available result requires Stage 82 tablet assessment`() {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage214:invalid-status",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage214-invalid-status"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.NON_TABLET,
                embodiment = embodiment,
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 400,
                    ),
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidTabletEmbodimentResult.create(
                status = AndroidTabletEmbodimentStatus.AVAILABLE,
                formFactorAssessment = assessment,
                embodiment = embodiment,
            )
        }
    }

    @Test
    fun `result rejects reconstructed embodiment provenance`() {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage214:source",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage214-provenance"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.TABLET,
                embodiment = embodiment,
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 720,
                    ),
            )

        val reconstructed =
            androidEmbodiment(
                id = "embodiment:stage214:source",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidTabletEmbodimentResult.create(
                status = AndroidTabletEmbodimentStatus.AVAILABLE,
                formFactorAssessment = assessment,
                embodiment = reconstructed,
            )
        }
    }

    private fun androidEmbodiment(
        id: String,
    ): EmbodimentRecord {
        return EmbodimentRecord.create(
            embodimentId = EmbodimentId.from(id),
            platformId = EmbodimentPlatformId.from("android"),
            description = "Stage 214 bounded Android embodiment.",
        )
    }
}
