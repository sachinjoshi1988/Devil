package com.devil.core.model.understanding

import com.devil.core.model.conversation.ConversationIntakeResult

/**
 * Represents one structured request for bounded understanding evaluation.
 *
 * The request preserves the completed conversation-intake result. Its
 * authoritative constitutional context and textual input remain owned by the
 * ConversationInput inside that result and are not duplicated here.
 *
 * This request does not interpret language, infer intent, create memory, select
 * decisions, create tasks, plan work, authorize capabilities, execute actions,
 * observe results, or verify outcomes.
 */
@ConsistentCopyVisibility
data class UnderstandingEvaluationRequest private constructor(
    val conversationIntake: ConversationIntakeResult,
) {
    companion object {
        fun create(
            conversationIntake: ConversationIntakeResult,
        ): UnderstandingEvaluationRequest {
            return UnderstandingEvaluationRequest(
                conversationIntake = conversationIntake,
            )
        }
    }
}
