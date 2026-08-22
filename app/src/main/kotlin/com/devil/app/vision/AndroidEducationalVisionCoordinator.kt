package com.devil.app.vision

import com.devil.core.model.education.EducationSessionRecord

/**
 * Stage 211 bounded Educational Vision coordinator.
 *
 * It integrates one exact existing EducationSessionRecord with one exact
 * Stage 206 Image Understanding result.
 *
 * It does not:
 *
 * - generate or execute lessons;
 * - solve, complete, or submit homework;
 * - infer what the learner should study;
 * - assess correctness, mastery, grade, or progress;
 * - perform OCR or new image analysis;
 * - infer educational meaning beyond supplied Stage 206 description;
 * - modify the education session;
 * - invoke models or providers;
 * - create Memory;
 * - establish constitutional Learning, Observation, Verification, or Outcome;
 * - implement Stage 212 Multimodal Evidence Governance.
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
class AndroidEducationalVisionCoordinator {

    fun integrate(
        educationSession: EducationSessionRecord,
        imageUnderstanding: AndroidImageUnderstandingResult,
    ): AndroidEducationalVisionResult {
        val status =
            if (
                imageUnderstanding.status ==
                    AndroidImageUnderstandingStatus.UNDERSTOOD
            ) {
                AndroidEducationalVisionStatus.AVAILABLE
            } else {
                AndroidEducationalVisionStatus.DEFERRED
            }

        return AndroidEducationalVisionResult.create(
            status = status,
            educationSession = educationSession,
            imageUnderstanding = imageUnderstanding,
        )
    }
}
