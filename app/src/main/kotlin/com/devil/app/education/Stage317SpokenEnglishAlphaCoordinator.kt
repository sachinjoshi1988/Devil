package com.devil.app.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.runtime.education.LanguageEducationFoundationCoordinator
import com.devil.core.runtime.education.LanguageEducationFoundationStatus
import com.devil.core.runtime.education.PronunciationIntelligenceCoordinator
import com.devil.core.runtime.education.PronunciationIntelligencePreparationStatus
import com.devil.core.runtime.education.SpokenEnglishBeginnerCoordinator
import com.devil.core.runtime.education.SpokenEnglishBeginnerPreparationStatus
import com.devil.core.runtime.education.SpokenEnglishConversationCoordinator
import com.devil.core.runtime.education.SpokenEnglishConversationPreparationStatus

/**
 * Stage 317 bounded Spoken English Alpha coordinator.
 *
 * This Android-side Alpha composition consumes an already prepared Stage 85
 * education session and delegates strictly through the existing Stage 120,
 * Stage 121, Stage 122, and Stage 123 Education architecture.
 *
 * It does not create another education architecture, conduct teaching,
 * complete conversation, capture audio, recognize speech, score or verify
 * pronunciation, infer proficiency, authenticate, authorize, execute,
 * perform constitutional Learning, commit Memory, or persist learner state.
 *
 * SPOKEN_ENGLISH_ALPHA != ANOTHER_INTELLIGENCE.
 * EDUCATION_CONVERSATION_PRACTICE != CONVERSATION_DOMAIN.
 * PREPARED != TAUGHT.
 * PREPARED != CONVERSATION_COMPLETED.
 * PRONUNCIATION_INTELLIGENCE != SPEECH_RECOGNITION.
 * PREPARED != PRONUNCIATION_VERIFIED.
 * PREPARED != PROFICIENCY_VERIFIED.
 */
class Stage317SpokenEnglishAlphaCoordinator(
    private val languageEducationFoundationCoordinator:
        LanguageEducationFoundationCoordinator =
        LanguageEducationFoundationCoordinator(),
    private val spokenEnglishBeginnerCoordinator:
        SpokenEnglishBeginnerCoordinator =
        SpokenEnglishBeginnerCoordinator(),
    private val spokenEnglishConversationCoordinator:
        SpokenEnglishConversationCoordinator =
        SpokenEnglishConversationCoordinator(),
    private val pronunciationIntelligenceCoordinator:
        PronunciationIntelligenceCoordinator =
        PronunciationIntelligenceCoordinator(),
) {
    fun prepare(
        traceId: TraceId,
        educationSession: EducationSessionRecord,
        targetLanguage: String,
        conversationTopic: String,
        pronunciationTarget: String,
    ): Stage317SpokenEnglishAlphaResult {
        val languagePreparation =
            languageEducationFoundationCoordinator.prepare(
                traceId = traceId,
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        if (
            languagePreparation.status !=
            LanguageEducationFoundationStatus.PREPARED
        ) {
            return deferred()
        }

        val languageSession =
            requireNotNull(languagePreparation.languageSession)

        val beginnerPreparation =
            spokenEnglishBeginnerCoordinator.prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
            )

        if (
            beginnerPreparation.status !=
            SpokenEnglishBeginnerPreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val beginnerSession =
            requireNotNull(beginnerPreparation.beginnerSession)

        val conversationPreparation =
            spokenEnglishConversationCoordinator.prepare(
                traceId = traceId,
                beginnerSession = beginnerSession,
                topic = conversationTopic,
            )

        if (
            conversationPreparation.status !=
            SpokenEnglishConversationPreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val conversationPractice =
            requireNotNull(conversationPreparation.practice)

        val pronunciationPreparation =
            pronunciationIntelligenceCoordinator.prepare(
                traceId = traceId,
                conversationPractice = conversationPractice,
                target = pronunciationTarget,
            )

        if (
            pronunciationPreparation.status !=
            PronunciationIntelligencePreparationStatus.PREPARED
        ) {
            return deferred()
        }

        val pronunciationPractice =
            requireNotNull(pronunciationPreparation.practice)

        return Stage317SpokenEnglishAlphaResult.create(
            status = Stage317SpokenEnglishAlphaStatus.AVAILABLE,
            languageSession = languageSession,
            beginnerSession = beginnerSession,
            conversationPractice = conversationPractice,
            pronunciationPractice = pronunciationPractice,
        )
    }

    private fun deferred(): Stage317SpokenEnglishAlphaResult =
        Stage317SpokenEnglishAlphaResult.create(
            status = Stage317SpokenEnglishAlphaStatus.DEFERRED,
        )
}
