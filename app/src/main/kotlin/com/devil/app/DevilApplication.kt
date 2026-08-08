package com.devil.app

import android.app.Application
import com.devil.app.conversation.ConversationEntryIdProvider
import com.devil.app.conversation.ConversationInteractionCoordinator
import com.devil.app.conversation.ConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.ConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.ConversationSubmissionFlowCoordinator
import com.devil.app.conversation.DefaultConversationEntryIdProvider
import com.devil.app.conversation.DefaultConversationRuntimeInputMetadataProvider
import com.devil.app.conversation.DefaultConversationRuntimeSubmissionCoordinator
import com.devil.app.conversation.DefaultConversationSubmissionFlowCoordinator
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
 * UnifiedDevilRuntime and one bounded AndroidRuntimeInputCoordinator that
 * delegates all constitutional processing into that same runtime instance.
 *
 * Stage 24 also composes the Android conversation-submission presentation path
 * around that existing runtime boundary.
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

    val conversationInteractionCoordinator: ConversationInteractionCoordinator by lazy(
        LazyThreadSafetyMode.SYNCHRONIZED,
    ) {
        ConversationInteractionCoordinator()
    }

    private val conversationEntryIdProvider: ConversationEntryIdProvider by lazy(
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
