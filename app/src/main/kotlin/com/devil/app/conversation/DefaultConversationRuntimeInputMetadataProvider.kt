package com.devil.app.conversation

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel

/**
 * Default Stage 34 production metadata provider for typed Android conversation
 * input.
 *
 * This provider establishes only metadata that is truthfully known at the
 * bounded typed-text entry point:
 *
 * - schema version 1 identifies the current conversation/runtime contract;
 * - TEXT identifies the actual Android typed-input origin;
 * - UNVERIFIED conservatively describes supplied-context trust;
 * - RESTRICTED conservatively classifies typed conversation content.
 *
 * ContextTrustLevel.UNVERIFIED does not authenticate or distrust a subject.
 *
 * ContextSecurityLevel.RESTRICTED is supplied-context sensitivity only. It is
 * not Devil SecurityStage and grants no authority.
 *
 * Producing this metadata does not resolve identity, establish subject trust,
 * authenticate an owner, create a session, grant Devil authorization, grant
 * Android permission, select a capability, permit execution, or establish an
 * outcome.
 */
class DefaultConversationRuntimeInputMetadataProvider :
    ConversationRuntimeInputMetadataProvider {

    override fun provide(): ConversationRuntimeInputMetadataResult {
        return ConversationRuntimeInputMetadataResult.available(
            metadata =
                ConversationRuntimeInputMetadata(
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEXT,
                    trustLevel = ContextTrustLevel.UNVERIFIED,
                    securityLevel = ContextSecurityLevel.RESTRICTED,
                ),
        )
    }
}
