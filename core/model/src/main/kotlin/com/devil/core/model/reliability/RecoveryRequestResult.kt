package com.devil.core.model.reliability

/**
 * Stage 45 result of bounded recovery-request evaluation.
 *
 * AVAILABLE contains exactly one RecoveryRequest.
 *
 * UNAVAILABLE and EXHAUSTED contain no RecoveryRequest and one bounded reason.
 *
 * This result does not authorize or execute recovery.
 */
@ConsistentCopyVisibility
data class RecoveryRequestResult private constructor(
    val status: RecoveryRequestStatus,
    val request: RecoveryRequest?,
    val reason: String?,
) {
    companion object {

        fun available(
            request: RecoveryRequest,
        ): RecoveryRequestResult {
            return RecoveryRequestResult(
                status = RecoveryRequestStatus.AVAILABLE,
                request = request,
                reason = null,
            )
        }

        fun unavailable(
            reason: String,
        ): RecoveryRequestResult {
            return withoutRequest(
                status = RecoveryRequestStatus.UNAVAILABLE,
                reason = reason,
            )
        }

        fun exhausted(
            reason: String,
        ): RecoveryRequestResult {
            return withoutRequest(
                status = RecoveryRequestStatus.EXHAUSTED,
                reason = reason,
            )
        }

        private fun withoutRequest(
            status: RecoveryRequestStatus,
            reason: String,
        ): RecoveryRequestResult {
            val normalizedReason =
                reason.trim()

            require(normalizedReason.isNotEmpty()) {
                "Unavailable recovery-request result requires a reason."
            }

            return RecoveryRequestResult(
                status = status,
                request = null,
                reason = normalizedReason,
            )
        }
    }
}
