package com.devil.core.runtime.personality

import com.devil.core.model.personality.PersonaPresentationRecord

/**
 * Stage 249 bounded Persona Presentation result.
 *
 * ESTABLISHED contains exactly one PersonaPresentationRecord.
 *
 * DEFERRED contains no persona-presentation record.
 *
 * This result does not:
 *
 * - create or replace Devil identity;
 * - authenticate or authorize;
 * - make Brain decisions;
 * - generate responses;
 * - synthesize voice;
 * - render UI;
 * - execute actions;
 * - establish verified truth;
 * - create or persist Memory;
 * - implement Stage 250 Owner Experience.
 *
 * PERSONA_PRESENTATION != AUTHORITY.
 * PERSONA_PRESENTATION != DECISION.
 * PERSONA_PRESENTATION != AUTHORIZATION.
 * PERSONA_PRESENTATION != EXECUTION.
 * PERSONA_PRESENTATION != VERIFICATION.
 * PERSONA_PRESENTATION != MEMORY.
 * PERSONA_PRESENTATION != OWNER_EXPERIENCE.
 */
@ConsistentCopyVisibility
data class PersonaPresentationResult private constructor(
    val status: PersonaPresentationStatus,
    val presentation: PersonaPresentationRecord?,
) {
    companion object {

        fun create(
            status: PersonaPresentationStatus,
            presentation: PersonaPresentationRecord? = null,
        ): PersonaPresentationResult {
            return when (status) {
                PersonaPresentationStatus.ESTABLISHED -> {
                    requireNotNull(presentation) {
                        "Established Stage 249 Persona Presentation requires a presentation record."
                    }

                    PersonaPresentationResult(
                        status = status,
                        presentation = presentation,
                    )
                }

                PersonaPresentationStatus.DEFERRED -> {
                    require(presentation == null) {
                        "Deferred Stage 249 Persona Presentation must not contain a presentation record."
                    }

                    PersonaPresentationResult(
                        status = status,
                        presentation = null,
                    )
                }
            }
        }
    }
}
