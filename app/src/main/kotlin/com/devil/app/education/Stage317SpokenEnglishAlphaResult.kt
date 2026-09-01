package com.devil.app.education

import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.PronunciationPracticeRecord
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord

/**
 * Stage 317 bounded Spoken English Alpha result.
 *
 * This result preserves existing Education Domain records for Android Alpha
 * presentation only.
 *
 * SPOKEN_ENGLISH_ALPHA != ANOTHER_INTELLIGENCE.
 * EDUCATION_CONVERSATION_PRACTICE != CONVERSATION_DOMAIN.
 * AVAILABLE != TAUGHT.
 * AVAILABLE != CONVERSATION_COMPLETED.
 * AVAILABLE != SPEECH_RECOGNIZED.
 * AVAILABLE != PRONUNCIATION_VERIFIED.
 * AVAILABLE != PROFICIENCY_VERIFIED.
 * USER_LANGUAGE_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 */
@ConsistentCopyVisibility
data class Stage317SpokenEnglishAlphaResult private constructor(
    val status: Stage317SpokenEnglishAlphaStatus,
    val languageSession: LanguageEducationSessionRecord?,
    val beginnerSession: SpokenEnglishBeginnerSessionRecord?,
    val conversationPractice: SpokenEnglishConversationPracticeRecord?,
    val pronunciationPractice: PronunciationPracticeRecord?,
) {
    companion object {
        fun create(
            status: Stage317SpokenEnglishAlphaStatus,
            languageSession: LanguageEducationSessionRecord? = null,
            beginnerSession: SpokenEnglishBeginnerSessionRecord? = null,
            conversationPractice: SpokenEnglishConversationPracticeRecord? = null,
            pronunciationPractice: PronunciationPracticeRecord? = null,
        ): Stage317SpokenEnglishAlphaResult {
            when (status) {
                Stage317SpokenEnglishAlphaStatus.AVAILABLE -> {
                    require(languageSession != null) {
                        "Available Stage 317 Spoken English Alpha requires a language session."
                    }
                    require(beginnerSession != null) {
                        "Available Stage 317 Spoken English Alpha requires a beginner session."
                    }
                    require(conversationPractice != null) {
                        "Available Stage 317 Spoken English Alpha requires conversation practice."
                    }
                    require(pronunciationPractice != null) {
                        "Available Stage 317 Spoken English Alpha requires pronunciation practice."
                    }
                }

                Stage317SpokenEnglishAlphaStatus.DEFERRED -> {
                    require(languageSession == null)
                    require(beginnerSession == null)
                    require(conversationPractice == null)
                    require(pronunciationPractice == null)
                }
            }

            return Stage317SpokenEnglishAlphaResult(
                status = status,
                languageSession = languageSession,
                beginnerSession = beginnerSession,
                conversationPractice = conversationPractice,
                pronunciationPractice = pronunciationPractice,
            )
        }
    }
}
