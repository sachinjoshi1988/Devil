package com.devil.app.voice

import com.devil.core.model.education.LanguageEducationSessionRecord

/**
 * Stage 203 bounded Spoken Education Mode coordinator.
 *
 * It integrates only an already-established Stage 120 Language Education
 * session into a bounded voice-presentation mode.
 *
 * It does not:
 *
 * - generate lessons or curriculum;
 * - conduct teaching or conversation practice;
 * - assess pronunciation or proficiency;
 * - start speech recognition;
 * - invoke TextToSpeech;
 * - authenticate or authorize;
 * - implement Stage 204 Voice Production Validation.
 *
 * SPOKEN_EDUCATION_MODE != LESSON_GENERATION.
 * SPOKEN_EDUCATION_MODE != SPEECH_EXECUTED.
 * EDUCATION_CONTEXT != VERIFIED_PROFICIENCY.
 */
class AndroidSpokenEducationModeCoordinator {

    fun integrate(
        languageEducationSession: LanguageEducationSessionRecord?,
    ): AndroidSpokenEducationModeResult {
        if (languageEducationSession == null) {
            return AndroidSpokenEducationModeResult.create(
                status = AndroidSpokenEducationModeStatus.DEFERRED,
            )
        }

        return AndroidSpokenEducationModeResult.create(
            status = AndroidSpokenEducationModeStatus.AVAILABLE,
            languageEducationSession = languageEducationSession,
        )
    }
}
