package com.devil.app.conversation

import java.util.UUID

/**
 * Default Stage 24 provider of UI-only conversation presentation identities.
 *
 * UUID generation is used only to distinguish presentation entries in the
 * Android conversation timeline.
 *
 * These values never substitute for constitutional TraceId and carry no runtime,
 * security, authorization, persistence, execution, or outcome meaning.
 */
class DefaultConversationEntryIdProvider(
    private val rawIdProvider: () -> String = {
        UUID.randomUUID().toString()
    },
) : ConversationEntryIdProvider {

    override fun provide(): ConversationEntryId {
        return ConversationEntryId.from(
            rawValue = rawIdProvider(),
        )
    }
}
