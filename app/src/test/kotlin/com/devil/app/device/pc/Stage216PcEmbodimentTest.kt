package com.devil.app.device.pc

import com.devil.core.model.common.TraceId
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import com.devil.core.model.embodiment.PcEmbodimentEvidence
import com.devil.core.runtime.embodiment.PcEmbodimentAssessmentResult
import com.devil.core.runtime.embodiment.PcEmbodimentAssessmentStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage216PcEmbodimentTest {

    @Test
    fun `pc assessment becomes available and preserves exact provenance`() {
        val embodiment =
            pcEmbodiment(
                id = "embodiment:stage216:pc",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId = TraceId.from("trace-stage216-pc"),
                status = PcEmbodimentAssessmentStatus.PC,
                embodiment = embodiment,
                evidence =
                    PcEmbodimentEvidence.create(
                        operatingSystemFamily = "Linux",
                    ),
            )

        val result =
            AndroidPcEmbodimentCoordinator()
                .integrate(assessment)

        assertEquals(
            AndroidPcEmbodimentStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            assessment,
            result.pcAssessment,
        )
        assertSame(
            embodiment,
            result.embodiment,
        )
    }

    @Test
    fun `non pc assessment remains deferred`() {
        val embodiment =
            EmbodimentRecord.create(
                embodimentId =
                    EmbodimentId.from(
                        "embodiment:stage216:android",
                    ),
                platformId =
                    EmbodimentPlatformId.from(
                        "android",
                    ),
                description =
                    "Stage 216 bounded non-PC embodiment.",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId = TraceId.from("trace-stage216-non-pc"),
                status = PcEmbodimentAssessmentStatus.NON_PC,
                embodiment = embodiment,
            )

        val result =
            AndroidPcEmbodimentCoordinator()
                .integrate(assessment)

        assertEquals(
            AndroidPcEmbodimentStatus.DEFERRED,
            result.status,
        )
        assertSame(
            assessment,
            result.pcAssessment,
        )
        assertSame(
            embodiment,
            result.embodiment,
        )
    }

    @Test
    fun `deferred Stage 83 assessment remains deferred`() {
        val embodiment =
            pcEmbodiment(
                id = "embodiment:stage216:deferred",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId = TraceId.from("trace-stage216-deferred"),
                status = PcEmbodimentAssessmentStatus.DEFERRED,
                embodiment = embodiment,
            )

        val result =
            AndroidPcEmbodimentCoordinator()
                .integrate(assessment)

        assertEquals(
            AndroidPcEmbodimentStatus.DEFERRED,
            result.status,
        )
        assertSame(
            assessment,
            result.pcAssessment,
        )
        assertSame(
            embodiment,
            result.embodiment,
        )
    }

    @Test
    fun `available result requires Stage 83 pc assessment`() {
        val embodiment =
            pcEmbodiment(
                id = "embodiment:stage216:invalid-status",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId = TraceId.from("trace-stage216-invalid-status"),
                status = PcEmbodimentAssessmentStatus.DEFERRED,
                embodiment = embodiment,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidPcEmbodimentResult.create(
                status = AndroidPcEmbodimentStatus.AVAILABLE,
                pcAssessment = assessment,
                embodiment = embodiment,
            )
        }
    }

    @Test
    fun `result rejects reconstructed embodiment provenance`() {
        val embodiment =
            pcEmbodiment(
                id = "embodiment:stage216:source",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId = TraceId.from("trace-stage216-provenance"),
                status = PcEmbodimentAssessmentStatus.PC,
                embodiment = embodiment,
                evidence =
                    PcEmbodimentEvidence.create(
                        operatingSystemFamily = "Windows",
                    ),
            )

        val reconstructed =
            pcEmbodiment(
                id = "embodiment:stage216:source",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidPcEmbodimentResult.create(
                status = AndroidPcEmbodimentStatus.AVAILABLE,
                pcAssessment = assessment,
                embodiment = reconstructed,
            )
        }
    }

    private fun pcEmbodiment(
        id: String,
    ): EmbodimentRecord {
        return EmbodimentRecord.create(
            embodimentId = EmbodimentId.from(id),
            platformId = EmbodimentPlatformId.from("pc"),
            description = "Stage 216 bounded PC embodiment.",
        )
    }
}
