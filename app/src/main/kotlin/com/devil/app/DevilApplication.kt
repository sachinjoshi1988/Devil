package com.devil.app

import android.app.Application
import com.devil.app.capability.AndroidCapabilityRegistry
import com.devil.app.capability.AndroidCapabilityStateProvider
import com.devil.app.capability.DefaultAndroidCapabilityRegistry
import com.devil.app.capability.DefaultAndroidCapabilityAvailabilitySource
import com.devil.app.capability.DefaultAndroidCapabilityHealthSource
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
import com.devil.app.device.AndroidDeviceKnowledgeCoordinator
import com.devil.app.device.AndroidDeviceKnowledgeQueryCoordinator
import com.devil.app.vision.DefaultAndroidCameraInventorySource
import com.devil.app.vision.DefaultAndroidVisionFrameSource
import com.devil.app.vision.AndroidVisionFramePerceptionCoordinator
import com.devil.app.observation.AndroidObservationAdapter
import com.devil.app.observation.DefaultAndroidObservationAdapter
import com.devil.app.notification.AndroidNotificationAnalysisCoordinator
import com.devil.app.notification.AndroidNotificationPerceptionCoordinator
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
import com.devil.app.voice.HandsFreeProductionCoordinator
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
 * Stage 24 established Android conversation presentation.
 * Stage 27 established Android Capability Registry.
 * Stage 28 established capability availability and health.
 * Stage 29 established Android permission assessment.
 * Stage 30 established bounded Android execution.
 * Stage 31 established Android observation.
 * Stage 32 established Android verification.
 * Stage 33 established Android Outcome embodiment.
 * Stage 34 established typed-text runtime entry.
 * Stage 35 established bounded Android voice input.
 * Stage 36 established bounded Android voice output.
 *
 * Stage 37 adds wake and hands-free Android orchestration around those existing
 * input and output boundaries.
 *
 * Stage 37 does not create another runtime, another Brain, another Conversation
 * Domain, or another Security Authority.
 *
 * Approved wake phrases establish attention only.
 *
 * Wake != authentication.
 *
 * Recognition of "Code Red" requests the real authentication boundary only.
 *
 * Code Red != authentication.
 *
 * ContextSource.VOICE does not authenticate the speaker.
 *
 * Android RECORD_AUDIO permission is Android operating-system permission only.
 *
 * Android permission != Devil authorization.
 *
 * No Stage 37 composition may create ACTIVE_SESSION without genuine
 * authentication/session evidence.
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
        val cameraInventorySource =
            DefaultAndroidCameraInventorySource(
                context = applicationContext,
            )

        DefaultAndroidCapabilityStateProvider(
            availabilitySource =
                DefaultAndroidCapabilityAvailabilitySource(
                    visionCameraInventorySource =
                        cameraInventorySource,
                ),
            healthSource =
                DefaultAndroidCapabilityHealthSource(
                    visionCameraInventorySource =
                        cameraInventorySource,
                ),
        )
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

    /**
     * Stage 37 process-scoped wake/hands-free orchestration.
     *
     * The default authentication handoff remains fail-closed. Therefore this
     * coordinator cannot produce ACTIVE_SESSION in current production.
     */
    /**
     * Stage 39 process-scoped Android notification perception and analysis path.
     *
     * Notification listener connectivity and notification content grant no
     * constitutional authority.
     *
     * This coordinator does not create ConversationInput, invoke a separate
     * runtime, speak notification content, persist notification content, or
     * execute an action.
     */
    /**
     * Stage 40 process-scoped Android Device Knowledge embodiment.
     *
     * Device knowledge is bounded to directly observed, non-sensitive Android
     * platform facts approved by Stage 40.
     *
     * Reading device knowledge does not authenticate a subject, grant authority,
     * create logical memory, perform an Android action, or establish an Outcome.
     *
     * Device knowledge != device control.
     */
    val deviceKnowledgeCoordinator:
        AndroidDeviceKnowledgeCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidDeviceKnowledgeCoordinator()
    }

    /**
     * Stage 40 process-scoped bounded Device Knowledge query boundary.
     *
     * Queries are explicit typed requests only. This coordinator does not parse
     * conversation text, infer intent, invoke a separate runtime, or execute an
     * Android action.
     */
    val deviceKnowledgeQueryCoordinator:
        AndroidDeviceKnowledgeQueryCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidDeviceKnowledgeQueryCoordinator()
    }

    /**
     * Stage 41 process-scoped bounded real Camera2 frame-perception boundary.
     *
     * Camera capture is an Android input embodiment only.
     *
     * Captured frame != semantic understanding.
     * Captured frame != identity.
     * Captured frame != authentication.
     * Captured frame != authorization.
     * Captured frame != memory.
     * Captured frame != verified Outcome.
     *
     * The source must be invoked away from the Android main thread.
     */
    val visionFramePerceptionCoordinator:
        AndroidVisionFramePerceptionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidVisionFramePerceptionCoordinator(
            frameSource =
                DefaultAndroidVisionFrameSource(
                    context = applicationContext,
                ),
        )
    }

    val notificationAnalysisCoordinator:
        AndroidNotificationAnalysisCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidNotificationAnalysisCoordinator()
    }

    val notificationPerceptionCoordinator:
        AndroidNotificationPerceptionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        AndroidNotificationPerceptionCoordinator(
            analysisCoordinator =
                notificationAnalysisCoordinator,
        )
    }

    val handsFreeProductionCoordinator:
        HandsFreeProductionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        HandsFreeProductionCoordinator()
    }
}
