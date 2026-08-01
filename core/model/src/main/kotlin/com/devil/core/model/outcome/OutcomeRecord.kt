package com.devil.core.model.outcome

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.task.TaskRecord

/**
 * Represents the constitutionally verified result of one task.
 *
 * This record states what verification established. It does not contain
 * execution history, raw observations, capability internals, or retry logic.
 */
@ConsistentCopyVisibility
data class OutcomeRecord private constructor(
    val task: TaskRecord,
    val state: OutcomeState,
    val verifiedAt: DevilTimestamp,
    val summary: String,
) {
    companion object {
        fun create(
            task: TaskRecord,
            state: OutcomeState,
            verifiedAt: DevilTimestamp,
            summary: String,
        ): OutcomeRecord {
            val normalizedSummary = summary.trim()

            require(normalizedSummary.isNotEmpty()) {
                "Outcome summary must not be blank."
            }

            return OutcomeRecord(
                task = task,
                state = state,
                verifiedAt = verifiedAt,
                summary = normalizedSummary,
            )
        }
    }
}
