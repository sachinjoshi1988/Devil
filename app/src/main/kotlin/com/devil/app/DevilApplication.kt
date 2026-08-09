package com.devil.app

import android.app.Application
import com.devil.app.capability.AndroidCapabilityRegistry
import com.devil.app.capability.AndroidCapabilityStateProvider
import com.devil.app.capability.DefaultAndroidCapabilityRegistry
import com.devil.app.capability.DefaultAndroidCapabilityStateProvider
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.ConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.DefaultConversationEntryIdProvider
import com.devil.app.conversation.DefaultConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.DefaultConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.DefaultConversationSubmissionFlowCoordinator
import com.devil.app.conversation.VoiceConversationRuntimeInputMetadataProvider
import com.devil.app.execution.AndroidExecutionAdapter
import com.devil.app.execution.DefaultAndroidExecutionAdapter
import com.devil.app.observation.AndroidObservationAdapter
import com.devil.app.observation.DefaultAndroidObservationAdapter
import com.devil.app.outcome.AndroidOutcomeAdapter
import com.devil.app.outcome.DefaultAndroidOutcomeAdapter
import com.devil.app.runtime.AndroidRuntimeInputCoordinator
import com.devil.app.runtime.DefaultAndroidContextEnvelopeProvider
import com.devil.app.runtime.DefaultAndroidRuntimeGateway
import com.devil.app.runtime.DefaultAndroidRuntimeInputCoordinator
import com.devil.app.verification.AndroidVerificationAdapter
import com.devil.app.verification.DefaultAndroidVerificationAdapter
import com.devil.app.voice.AndroidVoiceOutputSource
import com.devil.app.voice.DefaultAndroidVoiceOutputSource
import com.devil.app.voice.VoiceConversationOutputCoordinator
import com.devil.app.voice.VoiceConversationResultCoordinator
import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import com.devil.core.runtime.UnifiedDevilRuntime

/**
 * Android process bootstrap for Devil.
 *
 * The Android application owns one process-scoped reference to the single
 * UnifiedDevilRuntime and bounded Android embodiment adapters around it.
 *
 * Stage 24 established the Android conversation-submission presentation path.
 *
 * Stage 27 established the Android Capability Registry boundary.
 *
 * Stage 28 established capability availability and health.
 *
 * Stage 29 established Android runtime-permission assessment.
 *
 * Stage 30 established the first safe Android execution adapter.
 *
 * Stage 31 established Android execution observation.
 *
 * Stage 32 established Android verification.
 *
 * Stage 33 established the Android Outcome embodiment boundary.
 *
 * Stage 34 established production typed-text runtime entry.
 *
 * Stage 35 adds voice-derived textual input while preserving the same
 * Conversation Domain and Unified Devil Runtime.
 *
 * Stage 36 adds bounded Android voice output for already-established runtime
 * presentation truth.
 *
 * Voice input is another bounded input provenance. It is not another Devil.
 *
 * Voice output is another bounded presentation embodiment. It is not another
 * Devil and does not generate conversational meaning.
 *
 * ContextSource.VOICE does not authenticate the speaker.
 *
 * Android RECORD_AUDIO permission is Android operating-system permission only.
 * Android permission != Devil authorization.
 *
 * Spoken runtime presentation != understanding.
 * Spoken runtime presentation != execution.
 * Spoken runtime presentation != verified Outcome.
 * Spoken runtime presentation != completion.
 *
 * No authority is granted and no runtime work is performed merely because the
 * Android process was created.
 */
class DevilApplication : Application() {

    val runtime: UnifiedDevilRuntime by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultUnifiedDevilRuntime()
    }

    val capabilityRegistry: AndroidCapabilityRegistry by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidCapabilityRegistry()
    }

    val capabilityStateProvider: AndroidCapabilityStateProvider by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidCapabilityStateProvider()
    }

    val executionAdapter: AndroidExecutionAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidExecutionAdapter()
    }

    val observationAdapter: AndroidObservationAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidObservationAdapter()
    }

    val verificationAdapter: AndroidVerificationAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidVerificationAdapter()
    }

    val outcomeAdapter: AndroidOutcomeAdapter by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidOutcomeAdapter()
    }

    val runtimeInputCoordinator: AndroidRuntimeInputCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidRuntimeInputCoordinator(
            contextEnvelopeProvider =
                DefaultAndroidContextEnvelopeProvider(),
            runtimeGateway =
                DefaultAndroidRuntimeGateway(
                    runtime = runtime,
                ),
        )
    }

    val conversationInteractionCoordinator:
        ConversationInteractionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        ConversationInteractionCoordinator()
    }

    private val conversationEntryIdProvider:
        ConversationEntryIdProvider by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationEntryIdProvider()
    }

    private val conversationRuntimeInputMetadataProvider:
        ConversationRuntimeInputMetadataProvider by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationRuntimeInputMetadataProvider()
    }

    private val conversationRuntimeSubmissionCoordinator:
        ConversationRuntimeSubmissionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationRuntimeSubmissionCoordinator(
            metadataProvider =
                conversationRuntimeInputMetadataProvider,
            runtimeInputCoordinator =
                runtimeInputCoordinator,
        )
    }

    val conversationSubmissionFlowCoordinator:
        ConversationSubmissionFlowCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationSubmissionFlowCoordinator(
            interactionCoordinator =
                conversationInteractionCoordinator,
            entryIdProvider =
                conversationEntryIdProvider,
            runtimeSubmissionCoordinator =
                conversationRuntimeSubmissionCoordinator,
        )
    }

    private val voiceConversationRuntimeInputMetadataProvider:
        ConversationRuntimeInputMetadataProvider by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        VoiceConversationRuntimeInputMetadataProvider()
    }

    private val voiceConversationRuntimeSubmissionCoordinator:
        ConversationRuntimeSubmissionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationRuntimeSubmissionCoordinator(
            metadataProvider =
                voiceConversationRuntimeInputMetadataProvider,
            runtimeInputCoordinator =
                runtimeInputCoordinator,
        )
    }

    val voiceConversationSubmissionFlowCoordinator:
        ConversationSubmissionFlowCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultConversationSubmissionFlowCoordinator(
            interactionCoordinator =
                conversationInteractionCoordinator,
            entryIdProvider =
                conversationEntryIdProvider,
            runtimeSubmissionCoordinator =
                voiceConversationRuntimeSubmissionCoordinator,
        )
    }

    val voiceConversationResultCoordinator:
        VoiceConversationResultCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        VoiceConversationResultCoordinator(
            interactionCoordinator =
                conversationInteractionCoordinator,
            submissionFlowCoordinator =
                voiceConversationSubmissionFlowCoordinator,
        )
    }

    val voiceOutputSource: AndroidVoiceOutputSource by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        DefaultAndroidVoiceOutputSource(
            context = applicationContext,
        )
    }

    val voiceConversationOutputCoordinator:
        VoiceConversationOutputCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        VoiceConversationOutputCoordinator(
            outputSource =
                voiceOutputSource,
        )
    }
}
