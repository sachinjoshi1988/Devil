package com.devil.core.runtime.modelprovider.conversation

import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult

/**
 * Stage 313 passive evidence boundary for the exact constitutional
 * ConversationIntakeAuthorityResult produced by the Unified Devil Runtime.
 *
 * This port observes already-established conversation-intake evidence only.
 *
 * It does not:
 *
 * - perform conversation intake;
 * - reinterpret or replace Conversation Intake Authority;
 * - establish identity, trust, authentication, or authorization;
 * - select or invoke a model provider;
 * - perform model inference;
 * - execute capabilities;
 * - establish Observation, Verification, or Outcome;
 * - perform Learning;
 * - create, commit, persist, or recall Memory;
 * - or treat accepted conversation intake as verified model output.
 *
 * OBSERVATION OF INTAKE != CONVERSATION INTAKE AUTHORITY.
 * INTAKE ACCEPTED != MODEL AUTHORIZATION.
 * INTAKE ACCEPTED != MODEL OUTPUT VERIFIED.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 * GENERATED != VERIFIED.
 */
fun interface ConversationIntakeEvidencePort {

    fun observe(
        conversationIntake: ConversationIntakeAuthorityResult,
    )
}
