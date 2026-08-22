package com.devil.app.vision

/**
 * Stage 206 bounded Image Understanding coordinator.
 *
 * It associates one explicitly supplied bounded description with an already
 * available Stage 205 Vision Integration V2 result.
 *
 * It does not:
 *
 * - capture camera imagery;
 * - invoke a vision provider or model;
 * - perform object detection;
 * - perform OCR;
 * - identify a person or face;
 * - authenticate from visual evidence;
 * - establish factual truth;
 * - create Memory;
 * - establish constitutional Observation, Verification, or Outcome;
 * - implement Stage 207 Camera Understanding.
 *
 * IMAGE_UNDERSTOOD != VERIFIED_REALITY.
 * IMAGE_DESCRIPTION != OBJECT_DETECTION_EVIDENCE.
 * IMAGE_DESCRIPTION != PERSON_IDENTITY.
 * IMAGE_UNDERSTANDING != FACE_AUTHENTICATION.
 * IMAGE_UNDERSTANDING != OCR.
 * IMAGE_UNDERSTANDING != MEMORY.
 * IMAGE_UNDERSTANDING != CONSTITUTIONAL_VERIFICATION.
 */
class AndroidImageUnderstandingCoordinator {

    fun understand(
        visionIntegration: AndroidVisionIntegrationV2Result,
        description: String?,
    ): AndroidImageUnderstandingResult {
        if (
            visionIntegration.status !=
                AndroidVisionIntegrationV2Status.AVAILABLE ||
            description.isNullOrBlank()
        ) {
            return AndroidImageUnderstandingResult.create(
                status = AndroidImageUnderstandingStatus.DEFERRED,
                visionIntegration = visionIntegration,
            )
        }

        return AndroidImageUnderstandingResult.create(
            status = AndroidImageUnderstandingStatus.UNDERSTOOD,
            visionIntegration = visionIntegration,
            description = description,
        )
    }
}
