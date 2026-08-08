package com.devil.app.conversation

/**
 * Represents the bounded result of requesting metadata required for one Android
 * conversation runtime submission.
 *
 * AVAILABLE requires one complete metadata record.
 *
 * UNAVAILABLE carries no metadata and therefore cannot be used to invoke
 * AndroidRuntimeInputCoordinator.
 *
 * This result does not invent classifications, create constitutional context,
 * invoke the runtime, or grant authority.
 */
@ConsistentCopyVisibility
data class ConversationRuntimeInputMetadataResult private constructor(
    val status: ConversationRuntimeInputMetadataStatus,
    val metadata: ConversationRuntimeInputMetadata?,
) {
    companion object {

        fun available(
            metadata: ConversationRuntimeInputMetadata,
        ): ConversationRuntimeInputMetadataResult {
            return ConversationRuntimeInputMetadataResult(
                status =
                    ConversationRuntimeInputMetadataStatus.AVAILABLE,
                metadata = metadata,
            )
        }

        fun unavailable(): ConversationRuntimeInputMetadataResult {
            return ConversationRuntimeInputMetadataResult(
                status =
                    ConversationRuntimeInputMetadataStatus.UNAVAILABLE,
                metadata = null,
            )
        }
    }
}
