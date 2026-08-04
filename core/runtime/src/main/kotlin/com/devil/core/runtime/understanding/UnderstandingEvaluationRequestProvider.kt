package com.devil.core.runtime.understanding

import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult

/**
 * Supplies a structured understanding-evaluation request when bounded,
 * accepted conversation intake is available.
 *
 * This provider does not interpret language, infer intent, produce
 * understanding, create memory, select decisions, plan work, execute
 * capabilities, or verify outcomes.
 */
interface UnderstandingEvaluationRequestProvider {

    fun provide(
        conversationIntake: ConversationIntakeAuthorityResult,
    ): UnderstandingEvaluationRequestResult
}
