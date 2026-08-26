package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

/**
 * Stage 296 direct unit coverage for the existing Stage 201
 * AndroidVoiceActivityResult contract.
 *
 * Factory preservation only. No production behavior is modified.
 * Stage 297 Integration Test Completion is out of scope.
 */
class Stage296AndroidVoiceActivityResultTest {

    @Test
    fun `result preserves exact classification and evidence`() {
        val evidence =
            AndroidVoiceActivityEvidence.create(
                rmsDb = 0.5f,
                silenceThresholdDb = 0.2f,
                voiceThresholdDb = 0.4f,
            )

        val result =
            AndroidVoiceActivityResult.create(
                classification = AndroidVoiceActivityClassification.VOICE_ACTIVITY,
                evidence = evidence,
            )

        assertEquals(
            AndroidVoiceActivityClassification.VOICE_ACTIVITY,
            result.classification,
        )
        assertSame(
            evidence,
            result.evidence,
        )
    }
}
