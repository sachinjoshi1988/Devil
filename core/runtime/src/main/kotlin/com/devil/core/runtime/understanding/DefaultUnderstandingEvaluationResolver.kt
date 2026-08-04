package com.devil.core.runtime.understanding

import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState

/**
 * Default Stage 6 structured-understanding evaluation resolver.
 *
 * No structured language-understanding policy is available yet. This resolver
 * therefore preserves the authoritative context and returns UNSUPPORTED rather
 * than inferring intent or fabricating semantic understanding from input text.
 *
 * It performs no conversation intake, memory creation, decision selection,
 * task creation, planning, capability authorization, execution, observation,
 * or verification.
 */
class DefaultUnderstandingEvaluationResolver :
    UnderstandingEvaluationResolver {

    override fun evaluate(
        request: UnderstandingEvaluationRequest,
    ): UnderstandingRecord {
        return UnderstandingRecord.create(
            context =
                request.conversationIntake
                    .record
                    .input
                    .context,
            state = UnderstandingState.UNSUPPORTED,
            summary =
                "No structured language-understanding policy is available.",
        )
    }
}
