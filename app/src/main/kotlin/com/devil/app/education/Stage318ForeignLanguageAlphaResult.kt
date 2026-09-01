package com.devil.app.education

import com.devil.core.model.education.FrenchEducationRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualTeachingRecord

/**
 * Stage 318 bounded Foreign Language Alpha result.
 *
 * This result preserves existing Education Domain records for Android Alpha
 * presentation only.
 *
 * FOREIGN_LANGUAGE_ALPHA != ANOTHER_INTELLIGENCE.
 * FOREIGN_LANGUAGE_ALPHA != NEW_LANGUAGE_ARCHITECTURE.
 * STAGE318_FRENCH_ALPHA != ONLY_SUPPORTED_FOREIGN_LANGUAGE.
 * MULTILINGUAL_CONTEXT != TRANSLATION_ENGINE.
 * AVAILABLE != FRENCH_TAUGHT.
 * AVAILABLE != LESSON_COMPLETED.
 * AVAILABLE != TRANSLATION_PERFORMED.
 * AVAILABLE != CONVERSATION_OCCURRED.
 * AVAILABLE != SPEECH_RECOGNIZED.
 * AVAILABLE != PRONUNCIATION_VERIFIED.
 * AVAILABLE != PROFICIENCY_VERIFIED.
 * AVAILABLE != LEARNING_VERIFIED.
 * USER_LANGUAGE_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 */
@ConsistentCopyVisibility
data class Stage318ForeignLanguageAlphaResult private constructor(
    val status: Stage318ForeignLanguageAlphaStatus,
    val languageSession: LanguageEducationSessionRecord?,
    val multilingualTeaching: MultilingualTeachingRecord?,
    val frenchEducation: FrenchEducationRecord?,
) {
    companion object {
        fun create(
            status: Stage318ForeignLanguageAlphaStatus,
            languageSession: LanguageEducationSessionRecord? = null,
            multilingualTeaching: MultilingualTeachingRecord? = null,
            frenchEducation: FrenchEducationRecord? = null,
        ): Stage318ForeignLanguageAlphaResult {
            when (status) {
                Stage318ForeignLanguageAlphaStatus.AVAILABLE -> {
                    require(languageSession != null) {
                        "Available Stage 318 Foreign Language Alpha requires a language session."
                    }
                    require(multilingualTeaching != null) {
                        "Available Stage 318 Foreign Language Alpha requires multilingual teaching context."
                    }
                    require(frenchEducation != null) {
                        "Available Stage 318 Foreign Language Alpha requires French Education context."
                    }
                }

                Stage318ForeignLanguageAlphaStatus.DEFERRED -> {
                    require(languageSession == null)
                    require(multilingualTeaching == null)
                    require(frenchEducation == null)
                }
            }

            return Stage318ForeignLanguageAlphaResult(
                status = status,
                languageSession = languageSession,
                multilingualTeaching = multilingualTeaching,
                frenchEducation = frenchEducation,
            )
        }
    }
}
