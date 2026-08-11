package com.devil.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.devil.app.accessibility.AndroidAccessibilityServiceDiagnosticStatus
import com.devil.app.accessibility.DefaultAndroidAccessibilityServiceDiagnosticSource
import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationScreen
import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.app.conversation.ConversationUiState
import com.devil.app.ui.launch.DevilAwakeningScreen
import com.devil.app.ui.theme.DevilTheme
import com.devil.app.voice.AndroidVoiceInputListener
import com.devil.app.voice.AndroidVoiceInputResult
import com.devil.app.voice.AndroidVoiceInputSource
import com.devil.app.voice.AndroidVoiceInputStatus
import com.devil.app.voice.AndroidVoiceInteractionMode
import com.devil.app.voice.AndroidVoiceOutputListener
import com.devil.app.voice.AndroidVoiceOutputResult
import com.devil.app.voice.AndroidVoiceOutputStatus
import com.devil.app.voice.DefaultAndroidVoiceInputSource
import com.devil.app.voice.HandsFreeConversationState
import com.devil.app.voice.HandsFreeProductionAction
import com.devil.app.voice.HandsFreeProductionResult

/**
 * Android launcher surface for Devil.
 *
 * Typed text and voice-derived text enter the same Conversation Domain and
 * Unified Devil Runtime.
 *
 * Stage 35 owns bounded Android speech-recognition presentation/lifecycle.
 *
 * Stage 36 owns bounded Android TextToSpeech presentation/lifecycle.
 *
 * Stage 37 adds explicit wake and hands-free orchestration.
 *
 * Approved wake phrases establish attention only.
 *
 * Wake != authentication.
 *
 * "Code Red" requests the authentication handoff only.
 *
 * Code Red != authentication.
 *
 * The current default Stage 37 authentication handoff is fail-closed and cannot
 * create ACTIVE_SESSION.
 *
 * Android microphone permission != Devil authorization.
 *
 * This Activity does not grant authority, authenticate a speaker, create a
 * session, enter Owner Mode, bypass the constitutional runtime, execute a
 * capability, or fabricate success.
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

    private var handsFreeEnabled by
        mutableStateOf(false)

    private var handsFreeMessage by
        mutableStateOf<String?>(null)

    private var accessibilityDiagnosticMessage by
        mutableStateOf<String?>(null)

    private var handsFreeState by
        mutableStateOf(
            HandsFreeConversationState.IDLE,
        )

    private var pendingVoiceInteractionMode:
        AndroidVoiceInteractionMode? = null

    private var activeVoiceInteractionMode:
        AndroidVoiceInteractionMode? = null

    private var resumeHandsFreeAfterVoiceOutput:
        Boolean = false

    private lateinit var accessibilityDiagnosticSource:
        DefaultAndroidAccessibilityServiceDiagnosticSource

    private lateinit var voiceInputSource:
        AndroidVoiceInputSource

    private val recordAudioPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            val requestedMode =
                pendingVoiceInteractionMode

            pendingVoiceInteractionMode = null

            if (granted && requestedMode != null) {
                startVoiceInput(
                    mode = requestedMode,
                )
            } else {
                isVoiceListening = false
                activeVoiceInteractionMode = null

                if (
                    requestedMode ==
                    AndroidVoiceInteractionMode.HANDS_FREE
                ) {
                    handsFreeEnabled = false
                    handsFreeState =
                        HandsFreeConversationState.IDLE
                    handsFreeMessage =
                        "Microphone permission is required for hands-free voice input."
                } else {
                    voiceInputMessage =
                        "Microphone permission is required for voice input."
                }
            }
        }

    private val voiceInputListener =
        object : AndroidVoiceInputListener {

            override fun onReady() {
                isVoiceListening = true

                if (
                    activeVoiceInteractionMode ==
                    AndroidVoiceInteractionMode.HANDS_FREE
                ) {
                    handsFreeMessage =
                        when (handsFreeState) {
                            HandsFreeConversationState.IDLE ->
                                "Listening for Devil."

                            HandsFreeConversationState.AWAITING_AUTHENTICATION_PHRASE ->
                                "Listening for Code Red."

                            HandsFreeConversationState.AUTHENTICATION_REQUESTED ->
                                "Authentication is required."

                            HandsFreeConversationState.ACTIVE_SESSION ->
                                "Listening."
                        }
                } else {
                    voiceInputMessage =
                        "Listening…"
                }
            }

            override fun onResult(
                result: AndroidVoiceInputResult,
            ) {
                isVoiceListening = false

                val completedMode =
                    activeVoiceInteractionMode

                activeVoiceInteractionMode = null

                when (completedMode) {
                    AndroidVoiceInteractionMode.HANDS_FREE ->
                        handleHandsFreeVoiceResult(
                            result = result,
                        )

                    AndroidVoiceInteractionMode.MANUAL ->
                        handleManualVoiceResult(
                            result = result,
                        )

                    null -> Unit
                }
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

        accessibilityDiagnosticSource =
            DefaultAndroidAccessibilityServiceDiagnosticSource(
                context = applicationContext,
            )

        refreshAccessibilityDiagnostic()

        setContent {
            DevilTheme {
                var showAwakening by remember {
                    mutableStateOf(true)
                }

                if (showAwakening) {
                    DevilAwakeningScreen(
                        onComplete = {
                            showAwakening = false
                        },
                    )
                } else {
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
                            resumeHandsFree = false,
                        )
                    },
                    onVoiceInput = {
                        requestVoiceInput(
                            mode =
                                AndroidVoiceInteractionMode.MANUAL,
                        )
                    },
                    isVoiceListening =
                        isVoiceListening,
                    voiceInputEnabled =
                        !isVoiceSpeaking &&
                            !handsFreeEnabled,
                    voiceInputMessage =
                        voiceInputMessage,
                    isVoiceSpeaking =
                        isVoiceSpeaking,
                    voiceOutputMessage =
                        voiceOutputMessage,
                    onHandsFreeToggle = {
                        toggleHandsFree()
                    },
                    handsFreeEnabled =
                        handsFreeEnabled,
                    handsFreeMessage =
                        handsFreeMessage,
                      accessibilityDiagnosticMessage =
                        accessibilityDiagnosticMessage,
                )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (::accessibilityDiagnosticSource.isInitialized) {
            refreshAccessibilityDiagnostic()
        }
    }

    private fun refreshAccessibilityDiagnostic() {
        val diagnostic =
            accessibilityDiagnosticSource.diagnose()

        accessibilityDiagnosticMessage =
            if (
                diagnostic.status ==
                AndroidAccessibilityServiceDiagnosticStatus.CONNECTED
            ) {
                null
            } else {
                diagnostic.message
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

        devilApplication
            .voiceConversationOutputCoordinator
            .release()

        super.onDestroy()
    }

    private fun handleManualVoiceResult(
        result: AndroidVoiceInputResult,
    ) {
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
            resumeHandsFree = false,
        )
    }

    private fun toggleHandsFree() {
        if (handsFreeEnabled) {
            stopHandsFree()
        } else {
            startHandsFree()
        }
    }

    private fun startHandsFree() {
        if (
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        val devilApplication =
            application as DevilApplication

        val reset =
            devilApplication
                .handsFreeProductionCoordinator
                .reset()

        handsFreeState =
            reset.state

        handsFreeEnabled = true
        handsFreeMessage =
            "Hands-Free ready."

        requestVoiceInput(
            mode =
                AndroidVoiceInteractionMode.HANDS_FREE,
        )
    }

    private fun stopHandsFree() {
        handsFreeEnabled = false
        pendingVoiceInteractionMode = null
        resumeHandsFreeAfterVoiceOutput = false

        if (
            activeVoiceInteractionMode ==
            AndroidVoiceInteractionMode.HANDS_FREE &&
            ::voiceInputSource.isInitialized
        ) {
            voiceInputSource.cancel()
        }

        activeVoiceInteractionMode = null
        isVoiceListening = false

        val devilApplication =
            application as DevilApplication

        handsFreeState =
            devilApplication
                .handsFreeProductionCoordinator
                .reset()
                .state

        handsFreeMessage =
            "Hands-Free stopped."
    }

    private fun requestVoiceInput(
        mode: AndroidVoiceInteractionMode,
    ) {
        if (
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        if (
            mode ==
                AndroidVoiceInteractionMode.HANDS_FREE &&
            !handsFreeEnabled
        ) {
            return
        }

        pendingVoiceInteractionMode =
            mode

        if (
            checkSelfPermission(
                Manifest.permission.RECORD_AUDIO,
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            pendingVoiceInteractionMode = null

            startVoiceInput(
                mode = mode,
            )
        } else {
            recordAudioPermissionLauncher.launch(
                Manifest.permission.RECORD_AUDIO,
            )
        }
    }

    private fun startVoiceInput(
        mode: AndroidVoiceInteractionMode,
    ) {
        if (
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        if (
            mode ==
                AndroidVoiceInteractionMode.HANDS_FREE &&
            !handsFreeEnabled
        ) {
            return
        }

        try {
            activeVoiceInteractionMode =
                mode

            isVoiceListening = true

            when (mode) {
                AndroidVoiceInteractionMode.MANUAL ->
                    voiceInputMessage =
                        "Starting voice input…"

                AndroidVoiceInteractionMode.HANDS_FREE ->
                    handsFreeMessage =
                        when (handsFreeState) {
                            HandsFreeConversationState.IDLE ->
                                "Listening for Devil."

                            HandsFreeConversationState.AWAITING_AUTHENTICATION_PHRASE ->
                                "Listening for Code Red."

                            HandsFreeConversationState.AUTHENTICATION_REQUESTED ->
                                "Authentication is required."

                            HandsFreeConversationState.ACTIVE_SESSION ->
                                "Listening."
                        }
            }

            voiceInputSource.startListening(
                listener =
                    voiceInputListener,
            )
        } catch (
            throwable: RuntimeException,
        ) {
            activeVoiceInteractionMode = null
            isVoiceListening = false

            when (mode) {
                AndroidVoiceInteractionMode.MANUAL ->
                    voiceInputMessage =
                        "Voice input is unavailable."

                AndroidVoiceInteractionMode.HANDS_FREE -> {
                    handsFreeEnabled = false
                    handsFreeState =
                        HandsFreeConversationState.IDLE
                    handsFreeMessage =
                        "Hands-Free voice input is unavailable."
                }
            }
        }
    }

    private fun handleHandsFreeVoiceResult(
        result: AndroidVoiceInputResult,
    ) {
        if (!handsFreeEnabled) {
            return
        }

        when (result.status) {
            AndroidVoiceInputStatus.RECOGNIZED -> {
                val transcript =
                    requireNotNull(
                        result.transcript,
                    )

                val devilApplication =
                    application as DevilApplication

                val productionResult =
                    devilApplication
                        .handsFreeProductionCoordinator
                        .handleRecognizedTranscript(
                            state =
                                handsFreeState,
                            transcript =
                                transcript,
                        )

                handsFreeState =
                    productionResult.state

                handleHandsFreeProductionResult(
                    result =
                        productionResult,
                )
            }

            AndroidVoiceInputStatus.NO_MATCH -> {
                handsFreeMessage =
                    "No speech recognized."

                resumeHandsFreeListening()
            }

            AndroidVoiceInputStatus.CANCELLED -> {
                if (handsFreeEnabled) {
                    handsFreeMessage =
                        "Hands-Free listening cancelled."
                }
            }

            AndroidVoiceInputStatus.FAILED -> {
                handsFreeEnabled = false
                handsFreeState =
                    HandsFreeConversationState.IDLE
                handsFreeMessage =
                    "Hands-Free voice input failed."
            }
        }
    }

    private fun handleHandsFreeProductionResult(
        result: HandsFreeProductionResult,
    ) {
        handsFreeMessage =
            result.message

        when (result.action) {
            HandsFreeProductionAction.NONE ->
                Unit

            HandsFreeProductionAction.LISTEN ->
                resumeHandsFreeListening()

            HandsFreeProductionAction.SPEAK_AND_LISTEN -> {
                val message =
                    requireNotNull(
                        result.message,
                    )

                speakHandsFreeMessage(
                    message = message,
                    resumeListening = true,
                )
            }

            HandsFreeProductionAction.AUTHENTICATION_HANDOFF -> {
                val message =
                    requireNotNull(
                        result.message,
                    )

                /*
                 * Current production authentication handoff is fail-closed.
                 *
                 * No ACTIVE_SESSION is created from Code Red.
                 */
                handsFreeEnabled = false

                speakHandsFreeMessage(
                    message = message,
                    resumeListening = false,
                )
            }

            HandsFreeProductionAction.SUBMIT_CONVERSATION -> {
                val transcript =
                    requireNotNull(
                        result.runtimeTranscript,
                    )

                submitAuthenticatedHandsFreeConversation(
                    transcript =
                        transcript,
                )
            }
        }
    }

    private fun resumeHandsFreeListening() {
        if (
            !handsFreeEnabled ||
            isVoiceListening ||
            isVoiceSpeaking
        ) {
            return
        }

        startVoiceInput(
            mode =
                AndroidVoiceInteractionMode.HANDS_FREE,
        )
    }

    private fun speakHandsFreeMessage(
        message: String,
        resumeListening: Boolean,
    ) {
        val normalizedMessage =
            message.trim()

        require(
            normalizedMessage.isNotEmpty(),
        ) {
            "Hands-free spoken presentation must not be blank."
        }

        if (isVoiceSpeaking) {
            return
        }

        val devilApplication =
            application as DevilApplication

        isVoiceSpeaking = true

        resumeHandsFreeAfterVoiceOutput =
            resumeListening

        voiceOutputMessage =
            "Speaking hands-free status…"

        devilApplication
            .voiceOutputSource
            .speak(
                text =
                    normalizedMessage,
                listener =
                    AndroidVoiceOutputListener {
                        result ->

                        handleVoiceOutputResult(
                            result = result,
                        )
                    },
            )
    }

    private fun submitAuthenticatedHandsFreeConversation(
        transcript: String,
    ) {
        require(
            handsFreeState ==
                HandsFreeConversationState.ACTIVE_SESSION,
        ) {
            "Hands-free conversation submission requires ACTIVE_SESSION."
        }

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
                        AndroidVoiceInputResult.recognized(
                            transcript =
                                transcript,
                        ),
                )

        conversationState =
            handled.state

        handsFreeMessage =
            handled.message

        speakNewestRuntimeEntry(
            previousEntryCount =
                previousEntryCount,
            resumeHandsFree = true,
        )
    }

    private fun speakNewestRuntimeEntry(
        previousEntryCount: Int,
        resumeHandsFree: Boolean,
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
                resumeHandsFree =
                    resumeHandsFree,
            )
        } else if (
            resumeHandsFree
        ) {
            resumeHandsFreeListening()
        }
    }

    private fun speakRuntimeEntry(
        entry: ConversationTimelineEntry,
        resumeHandsFree: Boolean,
    ) {
        if (isVoiceSpeaking) {
            return
        }

        val devilApplication =
            application as DevilApplication

        isVoiceSpeaking = true

        resumeHandsFreeAfterVoiceOutput =
            resumeHandsFree

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

        val shouldResumeHandsFree =
            resumeHandsFreeAfterVoiceOutput

        resumeHandsFreeAfterVoiceOutput =
            false

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

        if (
            shouldResumeHandsFree &&
            handsFreeEnabled &&
            result.status ==
                AndroidVoiceOutputStatus.SPOKEN
        ) {
            resumeHandsFreeListening()
        }
    }
}
