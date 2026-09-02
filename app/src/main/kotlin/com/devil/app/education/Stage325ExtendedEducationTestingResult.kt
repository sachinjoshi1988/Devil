package com.devil.app.education

import com.devil.app.device.tablet.AndroidEducationTabletExperienceStatus
import com.devil.app.vision.AndroidEducationalVisionStatus
import com.devil.app.voice.AndroidSpokenEducationModeStatus

/**
 * Stage 325 bounded Extended Education Testing result.
 *
 * This result preserves:
 *
 * - one exact existing Stage 316 Education Alpha result;
 * - one optionally supplied existing Spoken Education status signal;
 * - one optionally supplied existing Educational Vision status signal;
 * - one optionally supplied existing Education Tablet Experience status signal.
 *
 * These integration statuses are evidence inputs only. Stage 325 does not
 * manufacture their underlying integrations or reinterpret AVAILABLE as proof
 * that teaching, speech execution, correctness, progress, or mastery occurred.
 *
 * AVAILABLE requires an AVAILABLE Stage 316 result containing its existing
 * EducationSessionRecord.
 *
 * DEFERRED requires a DEFERRED Stage 316 result and therefore preserves no
 * education session through the Stage 316 boundary.
 *
 * EDUCATION_SESSION_PREPARED != EDUCATION_DELIVERED.
 * EDUCATION_UI_VISIBLE != EDUCATION_AUTHORITY.
 * AVAILABLE != TAUGHT.
 * LEARNER_EVIDENCE_PRESENTED != VERIFIED_MASTERY.
 * USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 * SPOKEN_EDUCATION_AVAILABLE != SPEECH_EXECUTED.
 * EDUCATIONAL_VISION_AVAILABLE != VERIFIED_CORRECTNESS.
 * TABLET_EDUCATION_AVAILABLE != LEARNING_PROGRESS.
 * STAGE_325 != STAGE_326_LANGUAGE_CURRICULUM_VALIDATION.
 */
@ConsistentCopyVisibility
data class Stage325ExtendedEducationTestingResult private constructor(
    val status: Stage325ExtendedEducationTestingStatus,
    val educationAlphaResult: Stage316EducationAlphaResult,
    val spokenEducationStatus: AndroidSpokenEducationModeStatus?,
    val educationalVisionStatus: AndroidEducationalVisionStatus?,
    val tabletEducationStatus: AndroidEducationTabletExperienceStatus?,
) {
    companion object {
        fun create(
            status: Stage325ExtendedEducationTestingStatus,
            educationAlphaResult: Stage316EducationAlphaResult,
            spokenEducationStatus: AndroidSpokenEducationModeStatus? = null,
            educationalVisionStatus: AndroidEducationalVisionStatus? = null,
            tabletEducationStatus: AndroidEducationTabletExperienceStatus? = null,
        ): Stage325ExtendedEducationTestingResult {
            when (status) {
                Stage325ExtendedEducationTestingStatus.AVAILABLE -> {
                    require(
                        educationAlphaResult.status ==
                            Stage316EducationAlphaStatus.AVAILABLE,
                    ) {
                        "Available Stage 325 Extended Education Testing requires an available Stage 316 Education Alpha result."
                    }

                    require(educationAlphaResult.session != null) {
                        "Available Stage 325 Extended Education Testing requires the existing Stage 316 education session."
                    }
                }

                Stage325ExtendedEducationTestingStatus.DEFERRED -> {
                    require(
                        educationAlphaResult.status ==
                            Stage316EducationAlphaStatus.DEFERRED,
                    ) {
                        "Deferred Stage 325 Extended Education Testing requires a deferred Stage 316 Education Alpha result."
                    }

                    require(educationAlphaResult.session == null) {
                        "Deferred Stage 325 Extended Education Testing must not contain a Stage 316 education session."
                    }
                }
            }

            return Stage325ExtendedEducationTestingResult(
                status = status,
                educationAlphaResult = educationAlphaResult,
                spokenEducationStatus = spokenEducationStatus,
                educationalVisionStatus = educationalVisionStatus,
                tabletEducationStatus = tabletEducationStatus,
            )
        }
    }
}
