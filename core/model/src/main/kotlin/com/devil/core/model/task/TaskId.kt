package com.devil.core.model.task

/**
 * Identifies one task throughout its complete lifecycle.
 *
 * Task creation belongs to the task authority. This type only validates and
 * represents an existing task identity.
 */
@ConsistentCopyVisibility
data class TaskId private constructor(
    val value: String,
) {
    companion object {
        fun from(rawValue: String): TaskId {
            val normalizedValue = rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Task identity must not be blank."
            }

            return TaskId(normalizedValue)
        }
    }
}
