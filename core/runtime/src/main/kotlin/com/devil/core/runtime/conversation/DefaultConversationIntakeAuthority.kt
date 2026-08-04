package com.devil.core.runtime.conversation

import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Default Stage 5 implementation of bounded conversation intake.
 *
 * The authority preserves the supplied ConversationInput and derives only the
 * intake state allowed by the established constitutional authorization result.
 * It does not inspect or interpret the textual content.
 *
 * Authorized continuation produces ACCEPTED intake. Denied continuation
 * produces REJECTED intake. Deferred continuation produces DEFERRED intake.
 * Failed authorization propagates its matching error.
 *
 * This implementation performs no identity resolution, trust evaluation,
 * authorization evaluation, understanding, memory creation, decision-making,
 * task creation, planning, execution, observation, or verification.
 */
class DefaultConversationIntakeAuthority :
    ConversationIntakeAuthority {

    override fun intake(
        input: ConversationInput,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
    ): ConversationIntakeAuthorityResult {
        val traceId = input.context.traceId

        require(identity.traceId == traceId) {
            "Conversation input and identity result must use the same trace identity."
        }

        require(trust.traceId == traceId) {
            "Conversation input and trust result must use the same trace identity."
        }

        require(authorization.traceId == traceId) {
            "Conversation input and authorization result must use the same trace identity."
        }

        if (authorization.status == AuthorizationStatus.FAILED) {
            return ConversationIntakeAuthorityResult.create(
                traceId = traceId,
                status = ConversationIntakeAuthorityStatus.FAILED,
                error = requireNotNull(authorization.error),
            )
        }

        val state: ConversationIntakeState
        val rationale: String

        when (authorization.status) {
            AuthorizationStatus.AUTHORIZED -> {
                state = ConversationIntakeState.ACCEPTED
                rationale =
                    "Constitutional authorization permits conversation intake."
            }

            AuthorizationStatus.DENIED -> {
                state = ConversationIntakeState.REJECTED
                rationale =
                    "Constitutional authorization denies conversation intake."
            }

            AuthorizationStatus.DEFERRED -> {
                state = ConversationIntakeState.DEFERRED
                rationale =
                    "Conversation intake is deferred pending constitutional authorization."
            }

            AuthorizationStatus.FAILED ->
                error("Failed authorization was handled before intake-state mapping.")
        }

        val intake = ConversationIntakeResult.create(
            record = ConversationIntakeRecord.create(
                input = input,
                state = state,
                rationale = rationale,
            ),
        )

        return ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status = ConversationIntakeAuthorityStatus.PRODUCED,
            intake = intake,
        )
    }
}
