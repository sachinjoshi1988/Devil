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
import com.devil.app.conversation.ConversationScreen
import com.devil.app.conversation.ConversationUiState
import com.devil.app.voice.AndroidVoiceInputListener
import com.devil.app.voice.AndroidVoiceInputResult
import com.devil.app.voice.AndroidVoiceInputSource
import com.devil.app.voice.AndroidVoiceInputStatus
import com.devil.app.voice.DefaultAndroidVoiceInputSource

/**
 * Android launcher surface for Devil.
 *
 * Typed and voice-derived text both enter the same conversation and Unified
 * Devil Runtime architecture.
 *
 * This Activity owns only Android presentation/lifecycle responsibilities around
 * microphone permission and one bounded SpeechRecognizer source.
 *
 * Microphone permission does not authenticate a speaker and does not grant Devil
 * authorization.
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

    private lateinit var voiceInputSource: AndroidVoiceInputSource

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
                voiceInputMessage = "Listening…"
            }

            override fun onResult(
                result: AndroidVoiceInputResult,
            ) {
                isVoiceListening = false

                val devilApplication =
                    application as DevilApplication

                val handled =
                    devilApplication
                        .voiceConversationResultCoordinator
                        .handle(
                            state = conversationState,
                            result = result,
                        )

                conversationState = handled.state
                voiceInputMessage = handled.message
            }
        }

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        val devilApplication =
            application as DevilApplication

        voiceInputSource =
            DefaultAndroidVoiceInputSource(
                context = applicationContext,
            )

        setContent {
            MaterialTheme {
                ConversationScreen(
                    state = conversationState,
                    onDraftChange = { updatedDraft ->
                        conversationState =
                            devilApplication
                                .conversationInteractionCoordinator
                                .updateDraft(
                                    state = conversationState,
                                    draft = updatedDraft,
                                )

                        voiceInputMessage = null
                    },
                    onSubmit = {
                        conversationState =
                            devilApplication
                                .conversationSubmissionFlowCoordinator
                                .submit(
                                    state = conversationState,
                                )

                        voiceInputMessage = null
                    },
                    onVoiceInput = {
                        requestVoiceInput()
                    },
                    isVoiceListening = isVoiceListening,
                    voiceInputEnabled = true,
                    voiceInputMessage = voiceInputMessage,
                )
            }
        }
    }

    override fun onDestroy() {
        if (::voiceInputSource.isInitialized) {
            voiceInputSource.release()
        }

        super.onDestroy()
    }

    private fun requestVoiceInput() {
        voiceInputMessage = null

        if (
            checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startVoiceInput()
        } else {
            recordAudioPermissionLauncher.launch(
                Manifest.permission.RECORD_AUDIO,
            )
        }
    }

    private fun startVoiceInput() {
        if (isVoiceListening) {
            return
        }

        try {
            isVoiceListening = true
            voiceInputMessage = "Starting voice input…"

            voiceInputSource.startListening(
                listener = voiceInputListener,
            )
        } catch (
            throwable: RuntimeException,
        ) {
            isVoiceListening = false
            voiceInputMessage =
                "Voice input is unavailable."
        } catch (
            throwable: IllegalStateException,
        ) {
            isVoiceListening = false
            voiceInputMessage =
                "Voice input is unavailable."
        }
    }
}
