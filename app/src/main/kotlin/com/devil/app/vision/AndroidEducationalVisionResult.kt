package com.devil.app.vision

import com.devil.core.model.education.EducationSessionRecord

/**
 * Stage 211 bounded Educational Vision result.
 *
 * AVAILABLE preserves one exact existing EducationSessionRecord together with
 * one exact understood Stage 206 Image Understanding result.
 *
 * DEFERRED preserves the exact EducationSessionRecord and exact Stage 206 result
 * without claiming educational-vision availability.
 *
 * EDUCATIONAL_VISION != LESSON_GENERATION.
 * EDUCATIONAL_VISION != HOMEWORK_COMPLETION.
 * EDUCATIONAL_VISION != VERIFIED_CORRECTNESS.
 * EDUCATIONAL_VISION != LEARNING_PROGRESS.
 * IMAGE_DESCRIPTION != VERIFIED_EDUCATIONAL_CONTENT.
 * EDUCATIONAL_VISION != OCR.
 * EDUCATIONAL_VISION != CONSTITUTIONAL_LEARNING.
 * EDUCATIONAL_VISION != CONSTITUTIONAL_VERIFICATION.
 */
@ConsistentCopyVisibility
data class AndroidEducationalVisionResult private constructor(
    val status: AndroidEducationalVisionStatus,
    val educationSession: EducationSessionRecord,
    val imageUnderstanding: AndroidImageUnderstandingResult,
) {
    companion object {
        fun create(
            status: AndroidEducationalVisionStatus,
            educationSession: EducationSessionRecord,
            imageUnderstanding: AndroidImageUnderstandingResult,
        ): AndroidEducationalVisionResult {
            when (status) {
                AndroidEducationalVisionStatus.AVAILABLE -> {
                    require(
                        imageUnderstanding.status ==
                            AndroidImageUnderstandingStatus.UNDERSTOOD,
                    ) {
                        "Available Stage 211 Educational Vision requires understood Stage 206 image understanding."
                    }
                }

                AndroidEducationalVisionStatus.DEFERRED -> Unit
            }

            return AndroidEducationalVisionResult(
                status = status,
                educationSession = educationSession,
                imageUnderstanding = imageUnderstanding,
            )
        }
    }
}
