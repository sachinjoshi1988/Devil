package com.devil.app.conversation

/**
 * Describes the presentation role of one conversation timeline entry.
 *
 * USER represents text visibly supplied by the user.
 *
 * RUNTIME represents truthful immediate runtime status information. It does not
 * represent a fabricated Devil answer or verified task outcome.
 *
 * ASSISTANT represents generated assistant-facing conversational text produced
 * through the bounded conversational-model path.
 *
 * ASSISTANT does not represent RuntimeResult, constitutional Verification,
 * verified truth, verified Outcome, execution success, Learning, or Memory.
 *
 * ASSISTANT != RUNTIME.
 * GENERATED != VERIFIED.
 */
enum class ConversationEntryRole {
    USER,
    RUNTIME,
    ASSISTANT,
}
