package com.devil.core.model.understanding

import com.devil.core.model.context.ContextEnvelope

/**
 * Represents Devil's current understanding of an interaction.
 *
 * This record captures understanding only. It does not contain decisions,
 * planning, execution, memory, or capability selection.
 */
@ConsistentCopyVisibility
data class UnderstandingRecord private constructor(
    val context: ContextEnvelope,
    val state: UnderstandingState,
    val summary: String,
) {
    companion object {
        fun create(
            context: ContextEnvelope,
            state: UnderstandingState,
            summary: String,
        ): UnderstandingRecord {
            val normalizedSummary = summary.trim()

            require(normalizedSummary.isNotEmpty()) {
                "Understanding summary must not be blank."
            }

            return UnderstandingRecord(
                context = context,
                state = state,
                summary = normalizedSummary,
            )
        }
    }
}
