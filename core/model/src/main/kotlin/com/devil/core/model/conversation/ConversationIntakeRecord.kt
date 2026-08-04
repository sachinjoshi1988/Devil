package com.devil.core.model.conversation

/**
 * Records the bounded outcome of conversation intake for one supplied input.
 *
 * The record preserves the original conversation input, established intake
 * state, and a concise rationale. It does not interpret language, infer intent,
 * establish understanding, resolve identity, evaluate trust, grant
 * authorization, create memory, make decisions, create tasks, plan work,
 * execute capabilities, observe results, or verify outcomes.
 */
@ConsistentCopyVisibility
data class ConversationIntakeRecord private constructor(
    val input: ConversationInput,
    val state: ConversationIntakeState,
    val rationale: String,
) {
    companion object {
        fun create(
            input: ConversationInput,
            state: ConversationIntakeState,
            rationale: String,
        ): ConversationIntakeRecord {
            val normalizedRationale = rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Conversation intake rationale must not be blank."
            }

            return ConversationIntakeRecord(
                input = input,
                state = state,
                rationale = normalizedRationale,
            )
        }
    }
}
