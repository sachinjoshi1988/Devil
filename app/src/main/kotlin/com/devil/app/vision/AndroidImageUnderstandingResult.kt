package com.devil.app.vision

/**
 * Stage 206 bounded Image Understanding result.
 *
 * UNDERSTOOD preserves the exact available Stage 205 Vision Integration V2
 * result together with one normalized explicitly supplied bounded description.
 *
 * DEFERRED preserves the exact Stage 205 integration result and no description.
 *
 * IMAGE_UNDERSTOOD != VERIFIED_REALITY.
 * IMAGE_DESCRIPTION != OBJECT_DETECTION_EVIDENCE.
 * IMAGE_DESCRIPTION != PERSON_IDENTITY.
 * IMAGE_UNDERSTANDING != FACE_AUTHENTICATION.
 * IMAGE_UNDERSTANDING != OCR.
 * IMAGE_UNDERSTANDING != MEMORY.
 * IMAGE_UNDERSTANDING != CONSTITUTIONAL_VERIFICATION.
 */
@ConsistentCopyVisibility
data class AndroidImageUnderstandingResult private constructor(
    val status: AndroidImageUnderstandingStatus,
    val visionIntegration: AndroidVisionIntegrationV2Result,
    val description: String?,
) {
    companion object {
        fun create(
            status: AndroidImageUnderstandingStatus,
            visionIntegration: AndroidVisionIntegrationV2Result,
            description: String? = null,
        ): AndroidImageUnderstandingResult {
            return when (status) {
                AndroidImageUnderstandingStatus.UNDERSTOOD -> {
                    require(
                        visionIntegration.status ==
                            AndroidVisionIntegrationV2Status.AVAILABLE,
                    ) {
                        "Understood Stage 206 image requires available Stage 205 vision integration."
                    }

                    val normalizedDescription =
                        requireNotNull(description)
                            .trim()

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 206 image description must not be blank."
                    }

                    AndroidImageUnderstandingResult(
                        status = status,
                        visionIntegration = visionIntegration,
                        description = normalizedDescription,
                    )
                }

                AndroidImageUnderstandingStatus.DEFERRED -> {
                    require(description == null) {
                        "Deferred Stage 206 image understanding must not contain a description."
                    }

                    AndroidImageUnderstandingResult(
                        status = status,
                        visionIntegration = visionIntegration,
                        description = null,
                    )
                }
            }
        }
    }
}
