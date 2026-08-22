package com.devil.app.background

/**
 * Stage 193 bounded Android background-operation result.
 *
 * READY contains exactly one explicitly supplied request.
 * DEFERRED contains no request.
 *
 * BACKGROUND_READY != SCHEDULED.
 * SCHEDULED != EXECUTED.
 */
@ConsistentCopyVisibility
data class AndroidBackgroundOperationResult private constructor(
    val status: AndroidBackgroundOperationStatus,
    val request: AndroidBackgroundOperationRequest?,
) {
    companion object {
        fun create(
            status: AndroidBackgroundOperationStatus,
            request: AndroidBackgroundOperationRequest? = null,
        ): AndroidBackgroundOperationResult {
            when (status) {
                AndroidBackgroundOperationStatus.READY ->
                    require(request != null) {
                        "Ready Android background operation requires one request."
                    }

                AndroidBackgroundOperationStatus.DEFERRED ->
                    require(request == null) {
                        "Deferred Android background operation must not contain a request."
                    }
            }

            return AndroidBackgroundOperationResult(
                status = status,
                request = request,
            )
        }
    }
}
