package com.devil.app.device.pc

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
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

class Stage217PcCapabilityAdaptersTest {

    @Test
    fun `available pc embodiment and adapter id produce available adapter`() {
        val pcEmbodiment =
            availablePcEmbodiment()

        val capability =
            capability()

        val result =
            AndroidPcCapabilityAdapterCoordinator()
                .integrate(
                    pcEmbodiment = pcEmbodiment,
                    capability = capability,
                    adapterId = "  pc.files.read  ",
                )

        assertEquals(
            AndroidPcCapabilityAdapterStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            pcEmbodiment,
            result.pcEmbodiment,
        )
        assertSame(
            capability,
            result.capability,
        )
        assertEquals(
            "pc.files.read",
            result.adapterId,
        )
    }

    @Test
    fun `blank adapter identifier remains deferred`() {
        val pcEmbodiment =
            availablePcEmbodiment()

        val capability =
            capability()

        val result =
            AndroidPcCapabilityAdapterCoordinator()
                .integrate(
                    pcEmbodiment = pcEmbodiment,
                    capability = capability,
                    adapterId = "   ",
                )

        assertEquals(
            AndroidPcCapabilityAdapterStatus.DEFERRED,
            result.status,
        )
        assertSame(
            pcEmbodiment,
            result.pcEmbodiment,
        )
        assertSame(
            capability,
            result.capability,
        )
        assertEquals(
            null,
            result.adapterId,
        )
    }

    @Test
    fun `deferred pc embodiment keeps pc capability adapter deferred`() {
        val embodiment =
            pcEmbodimentRecord(
                id = "embodiment:stage217:deferred",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId = TraceId.from("trace-stage217-deferred"),
                status = PcEmbodimentAssessmentStatus.DEFERRED,
                embodiment = embodiment,
            )

        val pcEmbodiment =
            AndroidPcEmbodimentCoordinator()
                .integrate(assessment)

        val result =
            AndroidPcCapabilityAdapterCoordinator()
                .integrate(
                    pcEmbodiment = pcEmbodiment,
                    capability = capability(),
                    adapterId = "pc.files.read",
                )

        assertEquals(
            AndroidPcCapabilityAdapterStatus.DEFERRED,
            result.status,
        )
        assertEquals(
            null,
            result.adapterId,
        )
    }

    @Test
    fun `available result requires available Stage 216 pc embodiment`() {
        val embodiment =
            pcEmbodimentRecord(
                id = "embodiment:stage217:invalid",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId = TraceId.from("trace-stage217-invalid"),
                status = PcEmbodimentAssessmentStatus.DEFERRED,
                embodiment = embodiment,
            )

        val pcEmbodiment =
            AndroidPcEmbodimentCoordinator()
                .integrate(assessment)

        assertFailsWith<IllegalArgumentException> {
            AndroidPcCapabilityAdapterResult.create(
                status = AndroidPcCapabilityAdapterStatus.AVAILABLE,
                pcEmbodiment = pcEmbodiment,
                capability = capability(),
                adapterId = "pc.files.read",
            )
        }
    }

    @Test
    fun `available result rejects blank adapter identifier`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidPcCapabilityAdapterResult.create(
                status = AndroidPcCapabilityAdapterStatus.AVAILABLE,
                pcEmbodiment = availablePcEmbodiment(),
                capability = capability(),
                adapterId = " ",
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle adapter identifier`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidPcCapabilityAdapterResult.create(
                status = AndroidPcCapabilityAdapterStatus.DEFERRED,
                pcEmbodiment = availablePcEmbodiment(),
                capability = capability(),
                adapterId = "pc.files.read",
            )
        }
    }

    private fun availablePcEmbodiment(): AndroidPcEmbodimentResult {
        val embodiment =
            pcEmbodimentRecord(
                id = "embodiment:stage217:pc",
            )

        val assessment =
            PcEmbodimentAssessmentResult.create(
                traceId = TraceId.from("trace-stage217-pc"),
                status = PcEmbodimentAssessmentStatus.PC,
                embodiment = embodiment,
                evidence =
                    PcEmbodimentEvidence.create(
                        operatingSystemFamily = "Linux",
                    ),
            )

        return AndroidPcEmbodimentCoordinator()
            .integrate(assessment)
    }

    private fun pcEmbodimentRecord(
        id: String,
    ): EmbodimentRecord {
        return EmbodimentRecord.create(
            embodimentId = EmbodimentId.from(id),
            platformId = EmbodimentPlatformId.from("pc"),
            description = "Stage 217 bounded PC embodiment.",
        )
    }

    private fun capability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability:stage217:files-read",
                ),
            category = CapabilityCategory.ACTION,
            name = "PC Files Read",
            description =
                "Bounded Stage 217 capability contract used only for adapter representation.",
        )
    }
}
