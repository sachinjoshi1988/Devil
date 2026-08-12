package com.devil.core.model.understanding

import com.devil.core.model.context.ContextEnvelope

/**
 * Represents Devil's current understanding of an interaction.
 *
 * This record captures understanding only. It does not contain decisions,
 * planning, execution, memory, or capability selection.
 *
 * Structured semantics may be present when a bounded understanding policy
 * established meaning. Their presence does not grant authorization or imply
 * that any action should or can be executed.
 */
@ConsistentCopyVisibility
data class UnderstandingRecord private constructor(
    val context: ContextEnvelope,
    val state: UnderstandingState,
    val summary: String,
    val semantics: UnderstandingSemantics?,
) {

    companion object {

        fun create(
            context: ContextEnvelope,
            state: UnderstandingState,
            summary: String,
            semantics: UnderstandingSemantics? = null,
        ): UnderstandingRecord {
            val normalizedSummary = summary.trim()

            require(normalizedSummary.isNotEmpty()) {
                "Understanding summary must not be blank."
            }

            return UnderstandingRecord(
                context = context,
                state = state,
                summary = normalizedSummary,
                semantics = semantics,
            )
        }
    }
}
