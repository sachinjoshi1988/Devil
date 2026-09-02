package com.devil.app.education

import com.devil.core.model.common.TraceId
import com.devil.core.runtime.education.AdaptiveLanguageCurriculumCoordinator
import com.devil.core.runtime.education.AdaptiveLanguageCurriculumPreparationStatus
import com.devil.core.runtime.education.LanguageEducationProductionValidationCoordinator
import com.devil.core.runtime.education.LanguageEducationProductionValidationPreparationStatus

/**
 * Stage 326 bounded Language Curriculum Validation coordinator.
 *
 * This coordinator consumes one existing Stage 318 Foreign Language Alpha
 * result and delegates only through the already-established Stage 131 Adaptive
 * Language Curriculum and Stage 142 Language Education Production Validation
 * boundaries.
 *
 * The curriculum focus, adaptation rationale, validation focus, and validation
 * evidence description are explicit supplied inputs. They are not inferred
 * learner evidence or constitutional evidence.
 *
 * CURRICULUM_PREPARED != CURRICULUM_EXECUTED.
 * CURRICULUM_PREPARED != LESSON_GENERATED.
 * ADAPTATION_RATIONALE != VERIFIED_LEARNER_ASSESSMENT.
 * CURRICULUM_PRESENTED != EDUCATION_DELIVERED.
 * CURRICULUM_VALIDATED != VERIFIED_PROFICIENCY.
 * CURRICULUM_VALIDATED != VERIFIED_MASTERY.
 * LANGUAGE_EDUCATION_VALIDATED != CONSTITUTIONAL_VERIFICATION.
 * USER_LANGUAGE_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 * STAGE_326 != STAGE_327_ACCESSIBILITY_TESTING.
 */
class Stage326LanguageCurriculumValidationCoordinator(
    private val adaptiveLanguageCurriculumCoordinator:
        AdaptiveLanguageCurriculumCoordinator =
        AdaptiveLanguageCurriculumCoordinator(),
    private val languageEducationProductionValidationCoordinator:
        LanguageEducationProductionValidationCoordinator =
        LanguageEducationProductionValidationCoordinator(),
) {
    fun validate(
        traceId: TraceId,
        foreignLanguageAlphaResult: Stage318ForeignLanguageAlphaResult,
        curriculumFocus: String,
        adaptationRationale: String,
        validationFocus: String,
        validationEvidenceDescription: String,
    ): Stage326LanguageCurriculumValidationResult {
        if (
            foreignLanguageAlphaResult.status !=
                Stage318ForeignLanguageAlphaStatus.AVAILABLE
        ) {
            return deferred(foreignLanguageAlphaResult)
        }

        val languageSession =
            foreignLanguageAlphaResult.languageSession
                ?: return deferred(foreignLanguageAlphaResult)

        val multilingualTeaching =
            foreignLanguageAlphaResult.multilingualTeaching
                ?: return deferred(foreignLanguageAlphaResult)

        val curriculumPreparation =
            adaptiveLanguageCurriculumCoordinator.prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                curriculumFocus = curriculumFocus,
                adaptationRationale = adaptationRationale,
            )

        if (
            curriculumPreparation.status !=
                AdaptiveLanguageCurriculumPreparationStatus.PREPARED
        ) {
            return deferred(foreignLanguageAlphaResult)
        }

        val languageValidation =
            languageEducationProductionValidationCoordinator.prepare(
                traceId = traceId,
                multilingualTeaching = multilingualTeaching,
                validationFocus = validationFocus,
                validationEvidenceDescription =
                    validationEvidenceDescription,
            )

        if (
            languageValidation.status !=
                LanguageEducationProductionValidationPreparationStatus.VALIDATED
        ) {
            return deferred(foreignLanguageAlphaResult)
        }

        return Stage326LanguageCurriculumValidationResult.create(
            status =
                Stage326LanguageCurriculumValidationStatus.VALIDATED,
            foreignLanguageAlphaResult = foreignLanguageAlphaResult,
            curriculumPreparation = curriculumPreparation,
            languageEducationValidation = languageValidation,
        )
    }

    private fun deferred(
        foreignLanguageAlphaResult: Stage318ForeignLanguageAlphaResult,
    ): Stage326LanguageCurriculumValidationResult =
        Stage326LanguageCurriculumValidationResult.create(
            status =
                Stage326LanguageCurriculumValidationStatus.DEFERRED,
            foreignLanguageAlphaResult = foreignLanguageAlphaResult,
        )
}
