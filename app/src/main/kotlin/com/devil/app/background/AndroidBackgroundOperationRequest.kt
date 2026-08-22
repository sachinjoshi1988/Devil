package com.devil.app.background

/**
 * Stage 193 bounded Android background-operation request.
 *
 * This record preserves only an explicitly supplied operation identifier.
 *
 * It does not schedule, execute, retry, persist, or authorize background work.
 *
 * BACKGROUND_READY != SCHEDULED.
 */
@ConsistentCopyVisibility
data class AndroidBackgroundOperationRequest private constructor(
    val operationId: String,
) {
    companion object {
        fun create(
            operationId: String,
        ): AndroidBackgroundOperationRequest {
            val normalizedOperationId = operationId.trim()

            require(normalizedOperationId.isNotEmpty()) {
                "Android background operation identity must not be blank."
            }

            return AndroidBackgroundOperationRequest(
                operationId = normalizedOperationId,
            )
        }
    }
}
