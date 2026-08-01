package com.devil.core.model.decision

import com.devil.core.model.understanding.UnderstandingRecord

/**
 * Represents the single constitutional decision selected by Devil.
 *
 * This record captures the selected decision only. It does not represent
 * planning, task execution, capability selection, or execution outcome.
 */
@ConsistentCopyVisibility
data class DecisionRecord private constructor(
    val understanding: UnderstandingRecord,
    val state: DecisionState,
    val summary: String,
) {
    companion object {
        fun create(
            understanding: UnderstandingRecord,
            state: DecisionState,
            summary: String,
        ): DecisionRecord {
            val normalizedSummary = summary.trim()

            require(normalizedSummary.isNotEmpty()) {
                "Decision summary must not be blank."
            }

            return DecisionRecord(
                understanding = understanding,
                state = state,
                summary = normalizedSummary,
            )
        }
    }
}
