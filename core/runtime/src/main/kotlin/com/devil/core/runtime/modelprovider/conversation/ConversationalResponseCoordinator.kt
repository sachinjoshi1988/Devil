package com.devil.core.runtime.modelprovider.conversation

import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus

/**
 * Stage 313 bounded conversational-response coordinator.
 *
 * This coordinator permits conversational model inference only after the
 * existing constitutional ConversationIntakeAuthority has produced one
 * accepted conversation intake.
 *
 * The exact upstream authority result remains authoritative.
 *
 * It does not:
 *
 * - perform identity resolution;
 * - authenticate a subject;
 * - establish trust;
 * - grant authorization;
 * - reinterpret constitutional admission;
 * - select a provider;
 * - perform networking or credential handling;
 * - execute capabilities;
 * - establish constitutional Observation, Verification, or Outcome;
 * - perform Learning;
 * - commit or persist Memory;
 * - or treat generated model output as verified truth.
 *
 * CONVERSATION_INTAKE_ACCEPTED != MODEL_OUTPUT_VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 * GENERATED != VERIFIED.
 */
class ConversationalResponseCoordinator(
    private val inferencePort: ConversationalModelInferencePort,
) {

    fun generate(
        conversationIntake: ConversationIntakeAuthorityResult,
        content: String,
    ): ConversationalModelInferenceResult {
        require(
            conversationIntake.status ==
                ConversationIntakeAuthorityStatus.PRODUCED,
        ) {
            "Conversational model inference requires a produced constitutional conversation-intake result."
        }

        val intake =
            requireNotNull(conversationIntake.intake) {
                "Produced conversation-intake authority result requires its preserved intake."
            }

        require(
            intake.record.state ==
                ConversationIntakeState.ACCEPTED,
        ) {
            "Conversational model inference requires constitutionally accepted conversation intake."
        }

        val request =
            ConversationalModelInferenceRequest.create(
                traceId = conversationIntake.traceId,
                content = content,
            )

        val inference =
            inferencePort.infer(
                request = request,
            )

        require(
            inference.traceId ==
                conversationIntake.traceId,
        ) {
            "Conversation intake and conversational model inference must use the same trace identity."
        }

        return inference
    }
}
