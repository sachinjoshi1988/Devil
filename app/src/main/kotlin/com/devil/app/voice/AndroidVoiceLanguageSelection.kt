package com.devil.app.voice

import java.util.Locale

/**
 * Stage 337H bounded process-local multilingual voice configuration.
 *
 * A voice-language selection configures Android speech I/O only.
 *
 * It does not establish detected or verified conversation language, speaker
 * identity, authentication, authorization, semantic understanding, execution,
 * verification, Outcome, translation, transliteration, or persistent Memory.
 *
 * VOICE_LANGUAGE_SELECTION != DETECTED_LANGUAGE.
 * RECOGNITION_LOCALE != UNDERSTANDING_LANGUAGE_TRUTH.
 * TTS_LOCALE != RESPONSE_LANGUAGE_TRUTH.
 * VOICE_SOURCE != SPEAKER_AUTHENTICATED.
 */
enum class AndroidVoiceLanguageSelection(
    val uiLabel: String,
    val languageTag: String,
) {
    ENGLISH(
        uiLabel = "EN",
        languageTag = "en-IN",
    ),
    HINDI(
        uiLabel = "HI",
        languageTag = "hi-IN",
    ),
    MARATHI(
        uiLabel = "MR",
        languageTag = "mr-IN",
    ),
    ;

    companion object {
        /**
         * Device locale establishes only the initial process-local voice
         * configuration.
         *
         * DEVICE_LOCALE != DETECTED_CONVERSATION_LANGUAGE.
         */
        fun fromDeviceLanguageTag(
            languageTag: String?,
        ): AndroidVoiceLanguageSelection {
            val normalized =
                languageTag
                    ?.trim()
                    ?.lowercase(Locale.ROOT)
                    .orEmpty()

            return when {
                normalized == "hi" ||
                    normalized.startsWith("hi-") ->
                    HINDI

                normalized == "mr" ||
                    normalized.startsWith("mr-") ->
                    MARATHI

                else ->
                    ENGLISH
            }
        }
    }
}

/**
 * Read-only process-local Stage 337H voice-language source.
 */
fun interface AndroidVoiceLanguageSelectionProvider {
    fun current(): AndroidVoiceLanguageSelection
}

/**
 * Small process-local mutable Stage 337H preference holder.
 *
 * This is deliberately not Memory Authority and performs no persistence.
 */
class MutableAndroidVoiceLanguageSelectionProvider(
    initialSelection: AndroidVoiceLanguageSelection,
) : AndroidVoiceLanguageSelectionProvider {

    private var selection =
        initialSelection

    override fun current(): AndroidVoiceLanguageSelection {
        return selection
    }

    fun select(
        selection: AndroidVoiceLanguageSelection,
    ) {
        this.selection =
            selection
    }
}

/**
 * Determines whether one recognition attempt may receive the currently
 * selected multilingual conversation locale.
 *
 * Manual conversation and authenticated ACTIVE_SESSION hands-free
 * conversation may use the selected locale.
 *
 * Wake/authentication-oriented hands-free states deliberately receive no
 * multilingual locale override.
 *
 * MULTILINGUAL_CONVERSATION != MULTILINGUAL_AUTHENTICATION.
 * VOICE_LANGUAGE_SELECTION != WAKE_LANGUAGE_AUTHORITY.
 */
class AndroidVoiceLanguagePolicy {

    fun recognitionLanguageTag(
        selection: AndroidVoiceLanguageSelection,
        mode: AndroidVoiceInteractionMode?,
        handsFreeState: HandsFreeConversationState,
    ): String? {
        return when {
            mode ==
                AndroidVoiceInteractionMode.MANUAL ->
                selection.languageTag

            mode ==
                AndroidVoiceInteractionMode.HANDS_FREE &&
                handsFreeState ==
                HandsFreeConversationState.ACTIVE_SESSION ->
                selection.languageTag

            else ->
                null
        }
    }
}
