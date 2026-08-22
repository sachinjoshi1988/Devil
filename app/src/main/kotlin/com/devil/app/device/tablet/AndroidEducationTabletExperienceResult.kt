package com.devil.app.device.tablet

import com.devil.core.model.education.EducationSessionRecord

/**
 * Stage 215 bounded Education Tablet Experience result.
 *
 * AVAILABLE preserves one exact available Stage 214 Tablet Embodiment result and
 * one exact existing EducationSessionRecord.
 *
 * DEFERRED preserves both exact upstream objects without claiming an available
 * education-tablet experience.
 *
 * EDUCATION_TABLET_EXPERIENCE != LESSON_GENERATION.
 * EDUCATION_TABLET_EXPERIENCE != CURRICULUM.
 * EDUCATION_TABLET_EXPERIENCE != LEARNING_PROGRESS.
 * EDUCATION_SESSION != SECURITY_SESSION.
 * TABLET_CONTEXT != AUTHENTICATION.
 * TABLET_CONTEXT != AUTHORIZATION.
 * TABLET_CONTEXT != EXECUTION.
 * TABLET_CONTEXT != MEMORY_SYNC.
 * TABLET_EDUCATION != ANOTHER_INTELLIGENCE.
 */
@ConsistentCopyVisibility
data class AndroidEducationTabletExperienceResult private constructor(
    val status: AndroidEducationTabletExperienceStatus,
    val tabletEmbodiment: AndroidTabletEmbodimentResult,
    val educationSession: EducationSessionRecord,
) {
    companion object {
        fun create(
            status: AndroidEducationTabletExperienceStatus,
            tabletEmbodiment: AndroidTabletEmbodimentResult,
            educationSession: EducationSessionRecord,
        ): AndroidEducationTabletExperienceResult {
            when (status) {
                AndroidEducationTabletExperienceStatus.AVAILABLE -> {
                    require(
                        tabletEmbodiment.status ==
                            AndroidTabletEmbodimentStatus.AVAILABLE,
                    ) {
                        "Available Stage 215 Education Tablet Experience requires available Stage 214 Tablet Embodiment."
                    }
                }

                AndroidEducationTabletExperienceStatus.DEFERRED -> Unit
            }

            return AndroidEducationTabletExperienceResult(
                status = status,
                tabletEmbodiment = tabletEmbodiment,
                educationSession = educationSession,
            )
        }
    }
}
