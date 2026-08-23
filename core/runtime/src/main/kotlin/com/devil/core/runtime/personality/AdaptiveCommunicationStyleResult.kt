package com.devil.core.runtime.personality

import com.devil.core.model.personality.AdaptiveCommunicationStyleRecord

/**
 * Stage 247 bounded Adaptive Communication Style result.
 *
 * ESTABLISHED contains exactly one AdaptiveCommunicationStyleRecord.
 *
 * DEFERRED contains no adaptive communication-style record.
 *
 * This result does not:
 *
 * - generate responses;
 * - learn preferences;
 * - create or persist Memory;
 * - infer emotional state;
 * - create another Devil, Brain, Constitution, or authority;
 * - make decisions;
 * - authorize or execute actions;
 * - establish verified truth;
 * - implement Stage 248 Humor & Social Interaction;
 * - implement Stage 249 Persona Presentation;
 * - implement Stage 250 Owner Experience.
 *
 * ADAPTIVE_COMMUNICATION_STYLE != AUTHORITY.
 * ADAPTIVE_COMMUNICATION_STYLE != DECISION.
 * ADAPTIVE_COMMUNICATION_STYLE != AUTHORIZATION.
 * ADAPTIVE_COMMUNICATION_STYLE != EXECUTION.
 * ADAPTIVE_COMMUNICATION_STYLE != VERIFICATION.
 * ADAPTIVE_COMMUNICATION_STYLE != MEMORY.
 */
@ConsistentCopyVisibility
data class AdaptiveCommunicationStyleResult private constructor(
    val status: AdaptiveCommunicationStyleStatus,
    val style: AdaptiveCommunicationStyleRecord?,
) {
    companion object {

        fun create(
            status: AdaptiveCommunicationStyleStatus,
            style: AdaptiveCommunicationStyleRecord? = null,
        ): AdaptiveCommunicationStyleResult {
            return when (status) {
                AdaptiveCommunicationStyleStatus.ESTABLISHED -> {
                    requireNotNull(style) {
                        "Established Stage 247 Adaptive Communication Style requires a style record."
                    }

                    AdaptiveCommunicationStyleResult(
                        status = status,
                        style = style,
                    )
                }

                AdaptiveCommunicationStyleStatus.DEFERRED -> {
                    require(style == null) {
                        "Deferred Stage 247 Adaptive Communication Style must not contain a style record."
                    }

                    AdaptiveCommunicationStyleResult(
                        status = status,
                        style = null,
                    )
                }
            }
        }
    }
}
