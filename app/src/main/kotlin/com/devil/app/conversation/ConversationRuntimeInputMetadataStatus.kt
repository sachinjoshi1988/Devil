package com.devil.app.conversation

/**
 * Describes whether complete constitutional metadata is currently available for
 * one conversation runtime submission.
 *
 * AVAILABLE means all metadata required by AndroidRuntimeInputCoordinator has
 * been supplied by an approved upstream mechanism.
 *
 * UNAVAILABLE means the required metadata cannot currently be established and
 * runtime submission must not occur.
 */
enum class ConversationRuntimeInputMetadataStatus {
    AVAILABLE,
    UNAVAILABLE,
}
