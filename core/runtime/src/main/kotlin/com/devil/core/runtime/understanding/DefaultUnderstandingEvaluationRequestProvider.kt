package com.devil.core.runtime.understanding

import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus

/**
 * Default Stage 6 understanding-evaluation request provider.
 *
 * An evaluation request is available only when Conversation Intake produced an
 * accepted bounded intake result. Deferred or rejected intake remains
 * unavailable. Conversation-intake failure propagates its matching error.
 *
 * This implementation does not interpret textual content, infer intent,
 * produce understanding, create memory, select decisions, plan work, execute
 * capabilities, observe results, or verify outcomes.
 */
class DefaultUnderstandingEvaluationRequestProvider :
    UnderstandingEvaluationRequestProvider {

    override fun provide(
        conversationIntake: ConversationIntakeAuthorityResult,
    ): UnderstandingEvaluationRequestResult {
        return when (conversationIntake.status) {
            ConversationIntakeAuthorityStatus.PRODUCED -> {
                val intake = requireNotNull(
                    conversationIntake.intake,
                )

                when (intake.record.state) {
                    ConversationIntakeState.ACCEPTED ->
                        UnderstandingEvaluationRequestResult.create(
                            traceId = conversationIntake.traceId,
                            status =
                                UnderstandingEvaluationRequestStatus.AVAILABLE,
                            request =
                                UnderstandingEvaluationRequest.create(
                                    conversationIntake = intake,
                                ),
                        )

                    ConversationIntakeState.DEFERRED,
                    ConversationIntakeState.REJECTED,
                    ->
                        UnderstandingEvaluationRequestResult.create(
                            traceId = conversationIntake.traceId,
                            status =
                                UnderstandingEvaluationRequestStatus.UNAVAILABLE,
                        )
                }
            }

            ConversationIntakeAuthorityStatus.DEFERRED ->
                UnderstandingEvaluationRequestResult.create(
                    traceId = conversationIntake.traceId,
                    status =
                        UnderstandingEvaluationRequestStatus.UNAVAILABLE,
                )

            ConversationIntakeAuthorityStatus.FAILED ->
                UnderstandingEvaluationRequestResult.create(
                    traceId = conversationIntake.traceId,
                    status =
                        UnderstandingEvaluationRequestStatus.FAILED,
                    error = requireNotNull(conversationIntake.error),
                )
        }
    }
}
