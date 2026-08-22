package com.devil.app.vision

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.app.accessibility.AndroidScreenUnderstandingResult
import com.devil.app.accessibility.AndroidScreenUnderstandingStatus
import com.devil.app.voice.AndroidMultilingualSpeechRecognitionCoordinator
import com.devil.app.voice.AndroidSpeechRecognitionV2Coordinator
import com.devil.app.voice.AndroidVoiceInputResult
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage212MultimodalEvidenceGovernanceTest {

    @Test
    fun `available multimodal contexts become governed and preserve exact provenance`() {
        val voiceVision =
            availableVoiceVision()

        val educationalVision =
            availableEducationalVision()

        val result =
            AndroidMultimodalEvidenceGovernanceCoordinator()
                .govern(
                    voiceVisionInteraction = voiceVision,
                    educationalVision = educationalVision,
                )

        assertEquals(
            AndroidMultimodalEvidenceGovernanceStatus.GOVERNED,
            result.status,
        )
        assertSame(
            voiceVision,
            result.voiceVisionInteraction,
        )
        assertSame(
            educationalVision,
            result.educationalVision,
        )
    }

    @Test
    fun `deferred voice vision keeps multimodal governance deferred`() {
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

        val speechRecognition =
            availableMultilingualSpeech()

        val voiceVision =
            AndroidVoiceVisionInteractionCoordinator()
                .integrate(
                    speechRecognition = speechRecognition,
                    screenVision = screenVision,
                )

        val result =
            AndroidMultimodalEvidenceGovernanceCoordinator()
                .govern(
                    voiceVisionInteraction = voiceVision,
                    educationalVision = availableEducationalVision(),
                )

        assertEquals(
            AndroidMultimodalEvidenceGovernanceStatus.DEFERRED,
            result.status,
        )
        assertSame(
            voiceVision,
            result.voiceVisionInteraction,
        )
    }

    @Test
    fun `deferred educational vision keeps multimodal governance deferred`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.failed(),
            )

        val integration =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        val imageUnderstanding =
            AndroidImageUnderstandingCoordinator()
                .understand(
                    visionIntegration = integration,
                    description = "Supplied educational description.",
                )

        val educationalVision =
            AndroidEducationalVisionCoordinator()
                .integrate(
                    educationSession = educationSession(),
                    imageUnderstanding = imageUnderstanding,
                )

        val result =
            AndroidMultimodalEvidenceGovernanceCoordinator()
                .govern(
                    voiceVisionInteraction = availableVoiceVision(),
                    educationalVision = educationalVision,
                )

        assertEquals(
            AndroidMultimodalEvidenceGovernanceStatus.DEFERRED,
            result.status,
        )
        assertSame(
            educationalVision,
            result.educationalVision,
        )
    }

    @Test
    fun `governed result requires available Stage 210 interaction`() {
        val screenUnderstanding =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.SCREEN_UNAVAILABLE,
            )

        val deferredScreenVision =
            AndroidScreenVisionCoordinator()
                .integrate(
                    screenUnderstanding = screenUnderstanding,
                    imageUnderstanding = understoodImage(),
                )

        val deferredVoiceVision =
            AndroidVoiceVisionInteractionCoordinator()
                .integrate(
                    speechRecognition = availableMultilingualSpeech(),
                    screenVision = deferredScreenVision,
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidMultimodalEvidenceGovernanceResult.create(
                status =
                    AndroidMultimodalEvidenceGovernanceStatus.GOVERNED,
                voiceVisionInteraction = deferredVoiceVision,
                educationalVision = availableEducationalVision(),
            )
        }
    }

    @Test
    fun `governed result requires available Stage 211 educational vision`() {
        val perception =
            AndroidVisionFramePerceptionResult.fromCapture(
                AndroidVisionFrameCaptureResult.permissionUnavailable(),
            )

        val integration =
            AndroidVisionIntegrationV2Coordinator()
                .integrate(perception)

        val imageUnderstanding =
            AndroidImageUnderstandingCoordinator()
                .understand(
                    visionIntegration = integration,
                    description = "Supplied educational description.",
                )

        val deferredEducationalVision =
            AndroidEducationalVisionCoordinator()
                .integrate(
                    educationSession = educationSession(),
                    imageUnderstanding = imageUnderstanding,
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidMultimodalEvidenceGovernanceResult.create(
                status =
                    AndroidMultimodalEvidenceGovernanceStatus.GOVERNED,
                voiceVisionInteraction = availableVoiceVision(),
                educationalVision = deferredEducationalVision,
            )
        }
    }

    private fun availableVoiceVision():
        AndroidVoiceVisionInteractionResult {
        return AndroidVoiceVisionInteractionCoordinator()
            .integrate(
                speechRecognition = availableMultilingualSpeech(),
                screenVision = availableScreenVision(),
            )
    }

    private fun availableMultilingualSpeech() =
        AndroidMultilingualSpeechRecognitionCoordinator()
            .integrate(
                speechRecognition =
                    AndroidSpeechRecognitionV2Coordinator()
                        .integrate(
                            AndroidVoiceInputResult.recognized(
                                transcript =
                                    "Explain what I am looking at.",
                            ),
                        ),
                languageTag = "en-US",
            )

    private fun availableScreenVision(): AndroidScreenVisionResult {
        val screenUnderstanding =
            AndroidScreenUnderstandingResult.create(
                status =
                    AndroidScreenUnderstandingStatus.AVAILABLE,
                elements =
                    listOf(
                        AndroidScreenElementRecord.create(
                            position = 0,
                            text = "Educational material",
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

    private fun availableEducationalVision():
        AndroidEducationalVisionResult {
        return AndroidEducationalVisionCoordinator()
            .integrate(
                educationSession = educationSession(),
                imageUnderstanding = understoodImage(),
            )
    }

    private fun educationSession(): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage212",
                ),
            subjectIdentityId =
                IdentityId.from(
                    "identity:stage212-learner",
                ),
            objective =
                EducationObjective.create(
                    subject = "Multimodal Education",
                    objective =
                        "Preserve bounded multimodal educational context.",
                ),
        )
    }

    private fun understoodImage(): AndroidImageUnderstandingResult {
        val frame =
            AndroidVisionFrame.create(
                cameraId = "camera:stage212",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 212L,
                width = 1,
                height = 1,
                encodedBytes = byteArrayOf(2, 1, 2),
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
                description =
                    "Bounded supplied multimodal image description.",
            )
    }
}
