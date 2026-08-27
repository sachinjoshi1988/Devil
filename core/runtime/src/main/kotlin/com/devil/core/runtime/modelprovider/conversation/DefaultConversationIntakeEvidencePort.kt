package com.devil.core.runtime.modelprovider.conversation

import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult

/**
 * Default Stage 313 passive conversation-intake evidence sink.
 *
 * The default implementation intentionally performs no external action.
 *
 * Supplying a different implementation may expose the already-established
 * intake result to a bounded downstream composition boundary, but does not
 * transfer or create constitutional authority.
 */
class DefaultConversationIntakeEvidencePort :
    ConversationIntakeEvidencePort {

    override fun observe(
        conversationIntake: ConversationIntakeAuthorityResult,
    ) {
        // Intentionally passive.
    }
}
