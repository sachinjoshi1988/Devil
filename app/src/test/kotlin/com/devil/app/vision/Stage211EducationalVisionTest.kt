package com.devil.app.vision

import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage211EducationalVisionTest {

    @Test
    fun `understood image becomes available educational vision and preserves provenance`() {
        val educationSession =
            educationSession()

        val imageUnderstanding =
            understoodImage()

        val result =
            AndroidEducationalVisionCoordinator()
                .integrate(
                    educationSession = educationSession,
                    imageUnderstanding = imageUnderstanding,
                )

        assertEquals(
            AndroidEducationalVisionStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            educationSession,
            result.educationSession,
        )
        assertSame(
            imageUnderstanding,
            result.imageUnderstanding,
        )
    }

    @Test
    fun `deferred image understanding remains deferred and preserves education session`() {
        val educationSession =
            educationSession()

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
                    description = "Supplied educational image description.",
                )

        val result =
            AndroidEducationalVisionCoordinator()
                .integrate(
                    educationSession = educationSession,
                    imageUnderstanding = imageUnderstanding,
                )

        assertEquals(
            AndroidEducationalVisionStatus.DEFERRED,
            result.status,
        )
        assertSame(
            educationSession,
            result.educationSession,
        )
        assertSame(
            imageUnderstanding,
            result.imageUnderstanding,
        )
    }

    @Test
    fun `available result requires understood Stage 206 image`() {
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
                    description = "Supplied educational image description.",
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidEducationalVisionResult.create(
                status = AndroidEducationalVisionStatus.AVAILABLE,
                educationSession = educationSession(),
                imageUnderstanding = imageUnderstanding,
            )
        }
    }

    private fun educationSession(): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage211",
                ),
            subjectIdentityId =
                IdentityId.from(
                    "identity:stage211-learner",
                ),
            objective =
                EducationObjective.create(
                    subject = "Visual Education",
                    objective =
                        "Preserve bounded educational visual context.",
                ),
        )
    }

    private fun understoodImage(): AndroidImageUnderstandingResult {
        val frame =
            AndroidVisionFrame.create(
                cameraId = "camera:stage211",
                format = AndroidVisionFrameFormat.JPEG,
                capturedAtEpochMilliseconds = 211L,
                width = 1,
                height = 1,
                encodedBytes = byteArrayOf(2, 1, 1),
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
                    "Bounded supplied educational image description.",
            )
    }
}
