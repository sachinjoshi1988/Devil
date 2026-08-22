package com.devil.app.vision

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.app.accessibility.AndroidScreenUnderstandingResult
import com.devil.app.accessibility.AndroidScreenUnderstandingStatus
import com.devil.app.voice.AndroidMultilingualSpeechRecognitionCoordinator
import com.devil.app.voice.AndroidMultilingualSpeechRecognitionResult
import com.devil.app.voice.AndroidMultilingualSpeechRecognitionStatus
import com.devil.app.voice.AndroidSpeechRecognitionV2Coordinator
import com.devil.app.voice.AndroidVoiceInputResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage210VoiceVisionInteractionTest {

    @Test
    fun `available voice and screen vision produce available interaction`() {
        val speechRecognition =
            availableMultilingualSpeech()

        val screenVision =
            availableScreenVision()

        val result =
            AndroidVoiceVisionInteractionCoordinator()
                .integrate(
                    speechRecognition = speechRecognition,
                    screenVision = screenVision,
                )

        assertEquals(
            AndroidVoiceVisionInteractionStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            speechRecognition,
            result.speechRecognition,
        )
        assertSame(
            screenVision,
            result.screenVision,
        )
    }

    @Test
    fun `deferred multilingual speech remains deferred`() {
        val speechRecognition =
            AndroidMultilingualSpeechRecognitionCoordinator()
                .integrate(
                    speechRecognition =
                        AndroidSpeechRecognitionV2Coordinator()
                            .integrate(
                                AndroidVoiceInputResult.noMatch(),
                            ),
                    languageTag = "en-US",
                )

        val result =
            AndroidVoiceVisionInteractionCoordinator()
                .integrate(
                    speechRecognition = speechRecognition,
                    screenVision = availableScreenVision(),
                )

        assertEquals(
            AndroidVoiceVisionInteractionStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `deferred screen vision remains deferred`() {
        val screenUnderstanding =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.SCREEN_UNAVAILABLE,
            )

        val screenVision =
            AndroidScreenVisionCoordinator()
                .integrate(
                    screenUnderstanding = screenUnderstanding,
                    imageUnderstanding = understoodImage(),
                )

        val result =
            AndroidVoiceVisionInteractionCoordinator()
                .integrate(
                    speechRecognition = availableMultilingualSpeech(),
                    screenVision = screenVision,
                )

        assertEquals(
            AndroidVoiceVisionInteractionStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `available result requires available multilingual speech`() {
        val speechRecognition =
            AndroidMultilingualSpeechRecognitionCoordinator()
                .integrate(
                    speechRecognition =
                        AndroidSpeechRecognitionV2Coordinator()
                            .integrate(
                                AndroidVoiceInputResult.noMatch(),
                            ),
                    languageTag = "en-US",
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceVisionInteractionResult.create(
                status = AndroidVoiceVisionInteractionStatus.AVAILABLE,
                speechRecognition = speechRecognition,
                screenVision = availableScreenVision(),
            )
        }
    }

    @Test
    fun `available result requires available screen vision`() {
        val screenUnderstanding =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.SCREEN_UNAVAILABLE,
            )

        val screenVision =
            AndroidScreenVisionCoordinator()
                .integrate(
                    screenUnderstanding = screenUnderstanding,
                    imageUnderstanding = understoodImage(),
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceVisionInteractionResult.create(
                status = AndroidVoiceVisionInteractionStatus.AVAILABLE,
                speechRecognition = availableMultilingualSpeech(),
                screenVision = screenVision,
            )
        }
    }

    private fun availableMultilingualSpeech():
        AndroidMultilingualSpeechRecognitionResult {
        val voiceInput =
            AndroidVoiceInputResult.recognized(
                transcript = "What is on the screen?",
            )

        val speechRecognition =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(voiceInput)

        return AndroidMultilingualSpeechRecognitionCoordinator()
            .integrate(
                speechRecognition = speechRecognition,
                languageTag = "en-US",
            )
            .also {
                assertEquals(
                    AndroidMultilingualSpeechRecognitionStatus.AVAILABLE,
                    it.status,
                )
            }
    }

    private fun availableScreenVision(): AndroidScreenVisionResult {
        val screenUnderstanding =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.AVAILABLE,
                elements =
                    listOf(
                        AndroidScreenElementRecord.create(
                            position = 0,
                            text = "Devil",
                            contentDescription = null,
                        ),
                    ),
            )

        return AndroidScreenVisionCoordinator()
            .integrate(
                screenUnderstanding = screenUnderstanding,
                imageUnderstanding = understoodImage(),
            )
    }

    private fun understoodImage(): AndroidImageUnderstandingResult {
        val frame =
            AndroidVisionFrame.create(
                cameraId = "camera:stage210",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 210L,
                width = 1,
                height = 1,
                encodedBytes = byteArrayOf(2, 1, 0),
            )

        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.captured(
                    frame = frame,
                ),
            )

        val integration =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        return AndroidImageUnderstandingCoordinator()
            .understand(
                visionIntegration = integration,
                description = "Bounded supplied screen-image description.",
            )
    }
}
