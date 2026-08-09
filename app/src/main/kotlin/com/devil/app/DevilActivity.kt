package com.devil.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationScreen
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.app.conversation.ConversationUiState
import com.devil.app.voice.AndroidVoiceInputListener
import com.devil.app.voice.AndroidVoiceInputResult
import com.devil.app.voice.AndroidVoiceInputSource
import com.devil.app.voice.AndroidVoiceOutputListener
import com.devil.app.voice.AndroidVoiceOutputResult
import com.devil.app.voice.AndroidVoiceOutputStatus
import com.devil.app.voice.DefaultAndroidVoiceInputSource

/**
 * Android launcher surface for Devil.
 *
 * Typed text and voice-derived text enter the same Conversation Domain and
 * Unified Devil Runtime.
 *
 * Stage 35 owns the Android presentation/lifecycle boundary around microphone
 * permission and bounded speech recognition.
 *
 * Stage 36 may speak only already-established RUNTIME presentation entries.
 *
 * Voice output does not generate a Devil answer, reinterpret runtime status,
 * establish execution success, or establish final Outcome.
 *
 * Android microphone permission != Devil authorization.
 *
 * Spoken runtime presentation != task completion.
 */
class DevilActivity : ComponentActivity() {

    private var conversationState by
        mutableStateOf(
            ConversationUiState(),
        )

    private var isVoiceListening by
        mutableStateOf(false)

    private var voiceInputMessage by
        mutableStateOf<String?>(null)

    private var isVoiceSpeaking by
        mutableStateOf(false)

    private var voiceOutputMessage by
        mutableStateOf<String?>(null)

    private lateinit var voiceInputSource:
        AndroidVoiceInputSource

    private val recordAudioPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            if (granted) {
                startVoiceInput()
            } else {
                isVoiceListening = false
                voiceInputMessage =
                    "Microphone permission is required for voice input."
            }
        }

    private val voiceInputListener =
        object : AndroidVoiceInputListener {

            override fun onReady() {
                isVoiceListening = true
                voiceInputMessage =
                    "Listening…"
            }

            override fun onResult(
                result: AndroidVoiceInputResult,
            ) {
                isVoiceListening = false

                val devilApplication =
                    application as DevilApplication

                val previousEntryCount =
                    conversationState.entries.size

                val handled =
                    devilApplication
                        .voiceConversationResultCoordinator
                        .handle(
                            state =
                                conversationState,
                            result =
                                result,
                        )

                conversationState =
                    handled.state

                voiceInputMessage =
                    handled.message

                speakNewestRuntimeEntry(
                    previousEntryCount =
                        previousEntryCount,
                )
            }
        }

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(
            savedInstanceState,
        )

        val devilApplication =
            application as DevilApplication

        voiceInputSource =
            DefaultAndroidVoiceInputSource(
                context =
                    applicationContext,
            )

        setContent {
            MaterialTheme {
                ConversationScreen(
                    state =
                        conversationState,
                    onDraftChange = {
                        updatedDraft ->
                        conversationState =
                            devilApplication
                                .conversationInteractionCoordinator
                                .updateDraft(
                                    state =
                                        conversationState,
                                    draft =
                                        updatedDraft,
                                )

                        voiceInputMessage = null
                        voiceOutputMessage = null
                    },
                    onSubmit = {
                        val previousEntryCount =
                            conversationState
                                .entries
                                .size

                        conversationState =
                            devilApplication
                                .conversationSubmissionFlowCoordinator
                                .submit(
                                    state =
                                        conversationState,
                                )

                        voiceInputMessage = null
                        voiceOutputMessage = null

                        speakNewestRuntimeEntry(
                            previousEntryCount =
                                previousEntryCount,
                        )
                    },
                    onVoiceInput = {
                        requestVoiceInput()
                    },
                    isVoiceListening =
                        isVoiceListening,
                    voiceInputEnabled =
                        !isVoiceSpeaking,
                    voiceInputMessage =
                        voiceInputMessage,
                    isVoiceSpeaking =
                        isVoiceSpeaking,
                    voiceOutputMessage =
                        voiceOutputMessage,
                )
            }
        }
    }

    override fun onDestroy() {
        if (
            ::voiceInputSource
                .isInitialized
        ) {
            voiceInputSource.release()
        }

        val devilApplication =
            application as DevilApplication

        if (
            devilApplication
                .voiceOutputSource
                .let { true }
        ) {
            devilApplication
                .voiceConversationOutputCoordinator
                .release()
        }

        super.onDestroy()
    }

    private fun requestVoiceInput() {
        voiceInputMessage = null

        if (
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        if (
            checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            startVoiceInput()
        } else {
            recordAudioPermissionLauncher.launch(
                Manifest.permission.RECORD_AUDIO,
            )
        }
    }

    private fun startVoiceInput() {
        if (
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        try {
            isVoiceListening = true

            voiceInputMessage =
                "Starting voice input…"

            voiceInputSource.startListening(
                listener =
                    voiceInputListener,
            )
        } catch (
            throwable: RuntimeException,
        ) {
            isVoiceListening = false

            voiceInputMessage =
                "Voice input is unavailable."
        }
    }

    private fun speakNewestRuntimeEntry(
        previousEntryCount: Int,
    ) {
        val newEntries =
            conversationState
                .entries
                .drop(
                    previousEntryCount,
                )

        val runtimeEntry =
            newEntries.lastOrNull {
                it.role ==
                    ConversationEntryRole.RUNTIME
            }

        if (runtimeEntry != null) {
            speakRuntimeEntry(
                entry =
                    runtimeEntry,
            )
        }
    }

    private fun speakRuntimeEntry(
        entry: ConversationTimelineEntry,
    ) {
        if (isVoiceSpeaking) {
            return
        }

        val devilApplication =
            application as DevilApplication

        isVoiceSpeaking = true

        voiceOutputMessage =
            "Speaking runtime status…"

        devilApplication
            .voiceConversationOutputCoordinator
            .speak(
                entry =
                    entry,
                listener =
                    AndroidVoiceOutputListener {
                        result ->
                        handleVoiceOutputResult(
                            result =
                                result,
                        )
                    },
            )
    }

    private fun handleVoiceOutputResult(
        result: AndroidVoiceOutputResult,
    ) {
        isVoiceSpeaking = false

        voiceOutputMessage =
            when (result.status) {
                AndroidVoiceOutputStatus.SPOKEN ->
                    null

                AndroidVoiceOutputStatus.UNAVAILABLE ->
                    "Voice output is unavailable."

                AndroidVoiceOutputStatus.CANCELLED ->
                    "Voice output cancelled."

                AndroidVoiceOutputStatus.FAILED ->
                    "Voice output failed."
            }
    }
}
