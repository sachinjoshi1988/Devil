package com.devil.core.model.education

/**
 * Immutable Stage 142 representation of one bounded Language Education
 * Production Validation context.
 *
 * This record preserves:
 *
 * - one existing Stage 133 Multilingual Teaching context;
 * - the exact Stage 120 target language preserved by that context;
 * - one explicitly supplied nonblank validation focus;
 * - one explicitly supplied nonblank validation evidence description.
 *
 * This is Education Domain structural validation only.
 *
 * It does not:
 *
 * - teach a language;
 * - perform translation;
 * - conduct conversation;
 * - recognize or synthesize speech;
 * - assess pronunciation;
 * - infer or verify proficiency;
 * - execute curriculum;
 * - invoke models or providers;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create Memory;
 * - persist learner progress;
 * - or establish Android or real-device production validation.
 *
 * LANGUAGE_EDUCATION_VALIDATED != CONSTITUTIONAL_VERIFICATION.
 * LANGUAGE_EDUCATION_VALIDATED != VERIFIED_PROFICIENCY.
 * LANGUAGE_EDUCATION_VALIDATED != LANGUAGE_TAUGHT.
 * LANGUAGE_EDUCATION_VALIDATED != CONVERSATION_COMPLETED.
 * LANGUAGE_EDUCATION_VALIDATED != PRODUCTION_RUNTIME_EXECUTED.
 * LANGUAGE_EDUCATION_VALIDATED != REAL_DEVICE_VALIDATED.
 */
@ConsistentCopyVisibility
data class LanguageEducationProductionValidationRecord private constructor(
    val multilingualTeaching: MultilingualTeachingRecord,
    val validationFocus: String,
    val validationEvidenceDescription: String,
) {
    companion object {

        fun create(
            multilingualTeaching: MultilingualTeachingRecord,
            validationFocus: String,
            validationEvidenceDescription: String,
        ): LanguageEducationProductionValidationRecord {
            val normalizedValidationFocus =
                validationFocus.trim()

            val normalizedValidationEvidenceDescription =
                validationEvidenceDescription.trim()

            require(normalizedValidationFocus.isNotEmpty()) {
                "Language Education Production Validation focus must not be blank."
            }

            require(normalizedValidationEvidenceDescription.isNotEmpty()) {
                "Language Education Production Validation evidence description must not be blank."
            }

            return LanguageEducationProductionValidationRecord(
                multilingualTeaching = multilingualTeaching,
                validationFocus = normalizedValidationFocus,
                validationEvidenceDescription =
                    normalizedValidationEvidenceDescription,
            )
        }
    }
}
