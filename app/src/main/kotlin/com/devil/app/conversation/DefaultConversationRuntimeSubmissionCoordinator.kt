package com.devil.app.conversation

import com.devil.app.runtime.AndroidRuntimeInputCoordinator

/**
 * Default Stage 24 bridge from prepared conversation text to the existing
 * AndroidRuntimeInputCoordinator.
 *
 * Runtime submission occurs only when the supplied metadata provider returns a
 * complete AVAILABLE result.
 *
 * If metadata is unavailable, this coordinator returns METADATA_UNAVAILABLE
 * without invoking AndroidRuntimeInputCoordinator and without fabricating a
 * TraceId or RuntimeResult.
 *
 * When submission does occur, the genuine RuntimeResult is converted through
 * ConversationRuntimePresentation so ACCEPTED, DEFERRED, and REJECTED meanings
 * remain unchanged.
 */
class DefaultConversationRuntimeSubmissionCoordinator(
    private val metadataProvider: ConversationRuntimeInputMetadataProvider,
    private val runtimeInputCoordinator: AndroidRuntimeInputCoordinator,
) : ConversationRuntimeSubmissionCoordinator {

    override fun submit(
        content: String,
    ): ConversationRuntimeSubmissionResult {
        val normalizedContent = content.trim()

        require(normalizedContent.isNotEmpty()) {
            "Conversation runtime submission content must not be blank."
        }

        val metadataResult = metadataProvider.provide()

        return when (metadataResult.status) {
            ConversationRuntimeInputMetadataStatus.UNAVAILABLE ->
                ConversationRuntimeSubmissionResult.metadataUnavailable()

            ConversationRuntimeInputMetadataStatus.AVAILABLE -> {
                val metadata = requireNotNull(metadataResult.metadata)

                val runtimeResult =
                    runtimeInputCoordinator.submit(
                        schemaVersion = metadata.schemaVersion,
                        source = metadata.source,
                        trustLevel = metadata.trustLevel,
                        securityLevel = metadata.securityLevel,
                        content = normalizedContent,
                    )

                ConversationRuntimeSubmissionResult.submitted(
                    presentation =
                        ConversationRuntimePresentation.from(
                            runtimeResult,
                        ),
                )
            }
        }
    }
}
