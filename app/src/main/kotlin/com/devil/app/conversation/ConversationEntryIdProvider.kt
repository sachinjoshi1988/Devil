package com.devil.app.conversation

/**
 * Supplies one fresh presentation identity for a Stage 24 conversation timeline
 * entry.
 *
 * ConversationEntryId belongs only to Android conversation presentation state.
 *
 * It is not:
 *
 * - a constitutional TraceId,
 * - a task identity,
 * - a plan identity,
 * - a security-session identity,
 * - a conversation-persistence identity,
 * - proof of runtime submission,
 * - or proof of execution or outcome.
 *
 * Implementations must not create constitutional trace identity, invoke the
 * Unified Devil Runtime, choose runtime-input metadata, grant authority, or
 * persist conversation state.
 */
interface ConversationEntryIdProvider {

    fun provide(): ConversationEntryId
}
