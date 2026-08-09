package com.devil.app.conversation

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel

/**
 * Stage 35 metadata provider for recognized Android voice input.
 *
 * This provider establishes only metadata truthfully known after one bounded
 * Android voice-input mechanism has produced textual content:
 *
 * - schema version 1 identifies the current conversation/runtime contract;
 * - VOICE preserves the genuine input provenance;
 * - UNVERIFIED conservatively represents supplied-context trust;
 * - RESTRICTED conservatively represents conversation-content sensitivity.
 *
 * ContextSource.VOICE does not identify or authenticate the speaker.
 *
 * ContextTrustLevel.UNVERIFIED describes supplied-context trust only and must
 * not be reinterpreted as SubjectTrustLevel.
 *
 * ContextSecurityLevel.RESTRICTED describes supplied-context sensitivity only
 * and must not be reinterpreted as SecurityStage.
 *
 * This provider does not request microphone permission, perform speech
 * recognition, resolve identity, authenticate an owner, create a session,
 * grant Devil authorization, grant Android permission, select a capability,
 * permit execution, or establish an outcome.
 */
class VoiceConversationRuntimeInputMetadataProvider :
    ConversationRuntimeInputMetadataProvider {

    override fun provide(): ConversationRuntimeInputMetadataResult {
        return ConversationRuntimeInputMetadataResult.available(
            metadata =
                ConversationRuntimeInputMetadata(
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.VOICE,
                    trustLevel = ContextTrustLevel.UNVERIFIED,
                    securityLevel = ContextSecurityLevel.RESTRICTED,
                ),
        )
    }
}
