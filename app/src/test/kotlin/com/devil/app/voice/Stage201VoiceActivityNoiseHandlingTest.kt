package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage201VoiceActivityNoiseHandlingTest {

    private val coordinator =
        AndroidVoiceActivityCoordinator()

    @Test
    fun `audio at silence threshold is classified as silence`() {
        val evidence =
            AndroidVoiceActivityEvidence.create(
                rmsDb = -45.0f,
                silenceThresholdDb = -45.0f,
                voiceThresholdDb = -20.0f,
            )

        val result =
            coordinator.classify(evidence)

        assertEquals(
            AndroidVoiceActivityClassification.SILENCE,
            result.classification,
        )
        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `audio between thresholds is classified as noise`() {
        val evidence =
            AndroidVoiceActivityEvidence.create(
                rmsDb = -30.0f,
                silenceThresholdDb = -45.0f,
                voiceThresholdDb = -20.0f,
            )

        val result =
            coordinator.classify(evidence)

        assertEquals(
            AndroidVoiceActivityClassification.NOISE,
            result.classification,
        )
        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `audio at voice threshold is classified as voice activity`() {
        val evidence =
            AndroidVoiceActivityEvidence.create(
                rmsDb = -20.0f,
                silenceThresholdDb = -45.0f,
                voiceThresholdDb = -20.0f,
            )

        val result =
            coordinator.classify(evidence)

        assertEquals(
            AndroidVoiceActivityClassification.VOICE_ACTIVITY,
            result.classification,
        )
        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `audio above voice threshold remains voice activity`() {
        val result =
            coordinator.classify(
                AndroidVoiceActivityEvidence.create(
                    rmsDb = -8.0f,
                    silenceThresholdDb = -45.0f,
                    voiceThresholdDb = -20.0f,
                ),
            )

        assertEquals(
            AndroidVoiceActivityClassification.VOICE_ACTIVITY,
            result.classification,
        )
    }

    @Test
    fun `evidence rejects equal thresholds`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceActivityEvidence.create(
                rmsDb = -30.0f,
                silenceThresholdDb = -20.0f,
                voiceThresholdDb = -20.0f,
            )
        }
    }

    @Test
    fun `evidence rejects reversed thresholds`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceActivityEvidence.create(
                rmsDb = -30.0f,
                silenceThresholdDb = -10.0f,
                voiceThresholdDb = -20.0f,
            )
        }
    }

    @Test
    fun `evidence rejects non finite rms`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceActivityEvidence.create(
                rmsDb = Float.NaN,
                silenceThresholdDb = -45.0f,
                voiceThresholdDb = -20.0f,
            )
        }
    }
}
