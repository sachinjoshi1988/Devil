package com.devil.app.conversation

/**
 * Default Stage 24 conversation runtime-input metadata provider.
 *
 * No production mechanism currently establishes the complete schema,
 * supplied-context trust classification, and supplied-context security
 * classification required by AndroidRuntimeInputCoordinator.
 *
 * Therefore this provider truthfully reports UNAVAILABLE.
 *
 * It deliberately does not hard-code SchemaVersion, ContextTrustLevel,
 * ContextSecurityLevel, or ContextSource merely to make UI submission possible.
 */
class DefaultConversationRuntimeInputMetadataProvider :
    ConversationRuntimeInputMetadataProvider {

    override fun provide(): ConversationRuntimeInputMetadataResult {
        return ConversationRuntimeInputMetadataResult.unavailable()
    }
}
