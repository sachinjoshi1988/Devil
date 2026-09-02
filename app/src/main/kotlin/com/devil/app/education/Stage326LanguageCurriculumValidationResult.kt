package com.devil.app.education

import com.devil.core.runtime.education.AdaptiveLanguageCurriculumPreparationResult
import com.devil.core.runtime.education.AdaptiveLanguageCurriculumPreparationStatus
import com.devil.core.runtime.education.LanguageEducationProductionValidationPreparationResult
import com.devil.core.runtime.education.LanguageEducationProductionValidationPreparationStatus

/**
 * Stage 326 bounded Language Curriculum Validation result.
 *
 * This result preserves the exact existing Stage 318 Foreign Language Alpha
 * result together with existing Stage 131 curriculum-preparation and Stage 142
 * structural language-validation results.
 *
 * No lesson, executed curriculum, learner assessment, verified proficiency,
 * verified mastery, constitutional Observation/Verification/Outcome, Learning,
 * Memory commitment, or persistence is created here.
 */
@ConsistentCopyVisibility
data class Stage326LanguageCurriculumValidationResult private constructor(
    val status: Stage326LanguageCurriculumValidationStatus,
    val foreignLanguageAlphaResult: Stage318ForeignLanguageAlphaResult,
    val curriculumPreparation:
        AdaptiveLanguageCurriculumPreparationResult?,
    val languageEducationValidation:
        LanguageEducationProductionValidationPreparationResult?,
) {
    companion object {
        fun create(
            status: Stage326LanguageCurriculumValidationStatus,
            foreignLanguageAlphaResult: Stage318ForeignLanguageAlphaResult,
            curriculumPreparation:
                AdaptiveLanguageCurriculumPreparationResult? = null,
            languageEducationValidation:
                LanguageEducationProductionValidationPreparationResult? = null,
        ): Stage326LanguageCurriculumValidationResult {
            when (status) {
                Stage326LanguageCurriculumValidationStatus.VALIDATED -> {
                    require(
                        foreignLanguageAlphaResult.status ==
                            Stage318ForeignLanguageAlphaStatus.AVAILABLE,
                    ) {
                        "Validated Stage 326 results require an available Stage 318 result."
                    }

                    val preparedCurriculum =
                        requireNotNull(curriculumPreparation) {
                            "Validated Stage 326 results require curriculum preparation."
                        }

                    require(
                        preparedCurriculum.status ==
                            AdaptiveLanguageCurriculumPreparationStatus.PREPARED,
                    ) {
                        "Validated Stage 326 results require prepared curriculum context."
                    }

                    val curriculum =
                        requireNotNull(preparedCurriculum.curriculum) {
                            "Validated Stage 326 results require prepared curriculum context."
                        }

                    val structuralValidation =
                        requireNotNull(languageEducationValidation) {
                            "Validated Stage 326 results require structural language validation."
                        }

                    require(
                        structuralValidation.status ==
                            LanguageEducationProductionValidationPreparationStatus.VALIDATED,
                    ) {
                        "Validated Stage 326 results require structural language validation."
                    }

                    val validation =
                        requireNotNull(structuralValidation.validation) {
                            "Validated Stage 326 results require structural language validation."
                        }

                    require(
                        curriculum.languageEducationSession ===
                            foreignLanguageAlphaResult.languageSession,
                    ) {
                        "Stage 326 curriculum must preserve the exact Stage 318 language session."
                    }

                    require(
                        validation.multilingualTeaching ===
                            foreignLanguageAlphaResult.multilingualTeaching,
                    ) {
                        "Stage 326 validation must preserve the exact Stage 318 multilingual context."
                    }
                }

                Stage326LanguageCurriculumValidationStatus.DEFERRED -> {
                    require(curriculumPreparation == null)
                    require(languageEducationValidation == null)
                }
            }

            return Stage326LanguageCurriculumValidationResult(
                status = status,
                foreignLanguageAlphaResult = foreignLanguageAlphaResult,
                curriculumPreparation = curriculumPreparation,
                languageEducationValidation = languageEducationValidation,
            )
        }
    }
}
