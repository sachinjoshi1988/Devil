package com.devil.app.voice

/**
 * Distinguishes the Android presentation reason for one bounded speech-recognition
 * attempt.
 *
 * MANUAL
 *     Stage 35 user-requested one-shot voice input.
 *
 * HANDS_FREE
 *     Stage 37 wake/hands-free control listening.
 *
 * The mode is Android orchestration state only.
 *
 * It does not authenticate a speaker, establish a session, grant authorization,
 * enter Owner Mode, or alter the constitutional meaning of recognized speech.
 */
enum class AndroidVoiceInteractionMode {
    MANUAL,
    HANDS_FREE,
}
