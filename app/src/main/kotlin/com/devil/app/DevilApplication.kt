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
import com.devil.app.execution.AndroidExecutionAdapter
import com.devil.app.execution.DefaultAndroidExecutionAdapter
import com.devil.app.observation.AndroidObservationAdapter
import com.devil.app.observation.DefaultAndroidObservationAdapter
import com.devil.app.runtime.AndroidRuntimeInputCoordinator
import com.devil.app.runtime.DefaultAndroidContextEnvelopeProvider
import com.devil.app.runtime.DefaultAndroidRuntimeGateway
import com.devil.app.runtime.DefaultAndroidRuntimeInputCoordinator
import com.devil.core.runtime.DefaultUnifiedDevilRuntime
import com.devil.core.runtime.UnifiedDevilRuntime

/**
 * Android process bootstrap for Devil.
 *
 * The Android application owns one process-scoped reference to the single
 * UnifiedDevilRuntime and bounded Android embodiment adapters around it.
 *
 * Stage 24 composes the Android conversation-submission presentation path.
 *
 * Stage 27 adds one process-scoped Android Capability Registry boundary.
 *
 * Stage 28 adds one process-scoped Android capability availability-and-health
 * boundary around already registered CapabilityContract values.
 *
 * Stage 29 establishes the bounded Android runtime-permission assessment
 * boundary.
 *
 * Stage 30 establishes one process-scoped first-safe-execution adapter boundary.
 *
 * Stage 31 establishes one process-scoped Android execution-observation boundary.
 *
 * Registration, availability, health, Devil authorization, Executive readiness,
 * Android permission, execution approval, execution attempt, observation,
 * verification, and outcome remain constitutionally distinct.
 *
 * CapabilityHealthState.READY is capability health only. It is not Executive
 * readiness and grants no execution authority.
 *
 * Android permission is operating-system state only. It is not Devil
 * authorization.
 *
 * The Stage 30 default execution performer deliberately performs no platform
 * action until an explicitly registered and approved capability-to-platform
 * implementation exists.
 *
 * No capability or execution attempt is fabricated merely because Android
 * exposes an API, permission, application component, service, or hardware
 * feature.
 *
 * Conversation composition does not itself create conversation input,
 * constitutional context, trace identity, timestamps, decisions, plans,
 * capabilities, execution requests, memory, or persistence.
 *
 * This class chooses no schema version, provenance, trust classification, or
 * security classification. The default conversation metadata provider remains
 * truthfully unavailable until those values are established by their proper
 * production mechanisms.
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
}
