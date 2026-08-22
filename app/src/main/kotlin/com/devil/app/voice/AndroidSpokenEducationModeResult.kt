package com.devil.app.voice

import com.devil.core.model.education.LanguageEducationSessionRecord

/**
 * Stage 203 bounded Spoken Education Mode result.
 *
 * AVAILABLE preserves exactly one existing Stage 120 Language Education
 * session.
 *
 * DEFERRED preserves no education session.
 *
 * SPOKEN_EDUCATION_MODE != LESSON_GENERATION.
 * SPOKEN_EDUCATION_MODE != SPEECH_EXECUTED.
 * EDUCATION_CONTEXT != VERIFIED_PROFICIENCY.
 */
@ConsistentCopyVisibility
data class AndroidSpokenEducationModeResult private constructor(
    val status: AndroidSpokenEducationModeStatus,
    val languageEducationSession: LanguageEducationSessionRecord?,
) {
    companion object {
        fun create(
            status: AndroidSpokenEducationModeStatus,
            languageEducationSession: LanguageEducationSessionRecord? = null,
        ): AndroidSpokenEducationModeResult {
            when (status) {
                AndroidSpokenEducationModeStatus.AVAILABLE ->
                    require(languageEducationSession != null) {
                        "Available Stage 203 Spoken Education Mode requires one Stage 120 Language Education session."
                    }

                AndroidSpokenEducationModeStatus.DEFERRED ->
                    require(languageEducationSession == null) {
                        "Deferred Stage 203 Spoken Education Mode must not contain a Language Education session."
                    }
            }

            return AndroidSpokenEducationModeResult(
                status = status,
                languageEducationSession = languageEducationSession,
            )
        }
    }
}
