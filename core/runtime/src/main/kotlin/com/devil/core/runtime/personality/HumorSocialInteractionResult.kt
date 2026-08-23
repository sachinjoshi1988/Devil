package com.devil.core.runtime.personality

import com.devil.core.model.personality.HumorSocialInteractionRecord

/**
 * Stage 248 bounded Humor & Social Interaction result.
 *
 * ESTABLISHED contains exactly one HumorSocialInteractionRecord.
 *
 * DEFERRED contains no humor/social-interaction record.
 *
 * This result does not:
 *
 * - generate responses;
 * - establish a conversation;
 * - infer emotional state;
 * - prove relationships;
 * - learn preferences;
 * - create or persist Memory;
 * - create another Devil, Brain, Constitution, or authority;
 * - make decisions;
 * - authorize or execute actions;
 * - establish verified truth;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * HUMOR_SOCIAL_INTERACTION != AUTHORITY.
 * HUMOR_SOCIAL_INTERACTION != DECISION.
 * HUMOR_SOCIAL_INTERACTION != AUTHORIZATION.
 * HUMOR_SOCIAL_INTERACTION != EXECUTION.
 * HUMOR_SOCIAL_INTERACTION != VERIFICATION.
 * HUMOR_SOCIAL_INTERACTION != MEMORY.
 */
@ConsistentCopyVisibility
data class HumorSocialInteractionResult private constructor(
    val status: HumorSocialInteractionStatus,
    val interaction: HumorSocialInteractionRecord?,
) {
    companion object {

        fun create(
            status: HumorSocialInteractionStatus,
            interaction: HumorSocialInteractionRecord? = null,
        ): HumorSocialInteractionResult {
            return when (status) {
                HumorSocialInteractionStatus.ESTABLISHED -> {
                    requireNotNull(interaction) {
                        "Established Stage 248 Humor & Social Interaction requires an interaction record."
                    }

                    HumorSocialInteractionResult(
                        status = status,
                        interaction = interaction,
                    )
                }

                HumorSocialInteractionStatus.DEFERRED -> {
                    require(interaction == null) {
                        "Deferred Stage 248 Humor & Social Interaction must not contain an interaction record."
                    }

                    HumorSocialInteractionResult(
                        status = status,
                        interaction = null,
                    )
                }
            }
        }
    }
}
