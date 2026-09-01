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
 * OUTCOME represents presentation of one already-established trace-backed
 * outcome. It does not reinterpret RuntimeResult or claim task completion,
 * World Model update, Learning, Memory, or persistence.
 *
 * ASSISTANT != RUNTIME.
 * GENERATED != VERIFIED.
 * OUTCOME != RUNTIME.
 * OUTCOME_ESTABLISHED != TASK_COMPLETED.
 */
enum class ConversationEntryRole {
    USER,
    RUNTIME,
    ASSISTANT,
    OUTCOME,
}
