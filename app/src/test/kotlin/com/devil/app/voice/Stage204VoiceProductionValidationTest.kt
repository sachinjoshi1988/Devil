package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage204VoiceProductionValidationTest {

    @Test
    fun `available Stage 195 architecture produces validated structural context`() {
        val architecture =
            availableVoiceArchitecture()

        val result =
            AndroidVoiceProductionValidationCoordinator()
                .prepare(
                    voiceArchitecture = architecture,
                    validationFocus = "  Voice architecture readiness  ",
                    validationEvidenceDescription =
                        "  Stages 195 through 203 preserve bounded voice-domain contracts.  ",
                )

        assertEquals(
            AndroidVoiceProductionValidationStatus.VALIDATED,
            result.status,
        )
        assertSame(
            architecture,
            result.voiceArchitecture,
        )
        assertEquals(
            "Voice architecture readiness",
            result.validationFocus,
        )
        assertEquals(
            "Stages 195 through 203 preserve bounded voice-domain contracts.",
            result.validationEvidenceDescription,
        )
    }

    @Test
    fun `deferred Stage 195 architecture remains deferred`() {
        val architecture =
            AndroidVoiceArchitectureV2Coordinator()
                .integrate(
                    inputSource = null,
                    outputSource = null,
                )

        val result =
            AndroidVoiceProductionValidationCoordinator()
                .prepare(
                    voiceArchitecture = architecture,
                    validationFocus = "Voice readiness",
                    validationEvidenceDescription = "Bounded structural evidence.",
                )

        assertEquals(
            AndroidVoiceProductionValidationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.voiceArchitecture)
        assertNull(result.validationFocus)
        assertNull(result.validationEvidenceDescription)
    }

    @Test
    fun `blank validation focus remains deferred`() {
        val result =
            AndroidVoiceProductionValidationCoordinator()
                .prepare(
                    voiceArchitecture = availableVoiceArchitecture(),
                    validationFocus = "   ",
                    validationEvidenceDescription = "Bounded evidence.",
                )

        assertEquals(
            AndroidVoiceProductionValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `blank validation evidence remains deferred`() {
        val result =
            AndroidVoiceProductionValidationCoordinator()
                .prepare(
                    voiceArchitecture = availableVoiceArchitecture(),
                    validationFocus = "Voice readiness",
                    validationEvidenceDescription = "   ",
                )

        assertEquals(
            AndroidVoiceProductionValidationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `validated result rejects unavailable Stage 195 architecture`() {
        val architecture =
            AndroidVoiceArchitectureV2Coordinator()
                .integrate(
                    inputSource = null,
                    outputSource = null,
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceProductionValidationResult.create(
                status = AndroidVoiceProductionValidationStatus.VALIDATED,
                voiceArchitecture = architecture,
                validationFocus = "Voice readiness",
                validationEvidenceDescription = "Bounded evidence.",
            )
        }
    }

    @Test
    fun `deferred result rejects validation metadata`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceProductionValidationResult.create(
                status = AndroidVoiceProductionValidationStatus.DEFERRED,
                validationFocus = "Voice readiness",
            )
        }
    }

    private fun availableVoiceArchitecture(): AndroidVoiceArchitectureV2Result {
        return AndroidVoiceArchitectureV2Coordinator()
            .integrate(
                inputSource = testInputSource(),
                outputSource = testOutputSource(),
            )
    }

    private fun testInputSource(): AndroidVoiceInputSource {
        return object : AndroidVoiceInputSource {
            override fun startListening(
                listener: AndroidVoiceInputListener,
            ) = Unit

            override fun cancel() = Unit

            override fun release() = Unit
        }
    }

    private fun testOutputSource(): AndroidVoiceOutputSource {
        return object : AndroidVoiceOutputSource {
            override fun speak(
                text: String,
                listener: AndroidVoiceOutputListener,
            ) = Unit

            override fun stop() = Unit

            override fun release() = Unit
        }
    }
}
