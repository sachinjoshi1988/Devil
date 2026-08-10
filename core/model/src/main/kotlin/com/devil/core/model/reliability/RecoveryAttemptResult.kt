package com.devil.core.model.reliability

/**
 * Stage 45 result of bounded recovery-attempt accounting.
 *
 * RECORDED contains exactly one RecoveryAttemptRecord and no reason.
 *
 * EXHAUSTED or UNAVAILABLE contains no attempt record and one bounded reason.
 *
 * This result represents accounting only.
 *
 * It is not evidence that the recovery strategy was executed or succeeded.
 */
@ConsistentCopyVisibility
data class RecoveryAttemptResult private constructor(
    val status: RecoveryAttemptStatus,
    val record: RecoveryAttemptRecord?,
    val reason: String?,
) {
    companion object {

        fun recorded(
            record: RecoveryAttemptRecord,
        ): RecoveryAttemptResult {
            return RecoveryAttemptResult(
                status = RecoveryAttemptStatus.RECORDED,
                record = record,
                reason = null,
            )
        }

        fun exhausted(
            reason: String,
        ): RecoveryAttemptResult {
            return withoutRecord(
                status = RecoveryAttemptStatus.EXHAUSTED,
                reason = reason,
            )
        }

        fun unavailable(
            reason: String,
        ): RecoveryAttemptResult {
            return withoutRecord(
                status = RecoveryAttemptStatus.UNAVAILABLE,
                reason = reason,
            )
        }

        private fun withoutRecord(
            status: RecoveryAttemptStatus,
            reason: String,
        ): RecoveryAttemptResult {
            val normalizedReason =
                reason.trim()

            require(normalizedReason.isNotEmpty()) {
                "Recovery attempt result without a record requires a reason."
            }

            return RecoveryAttemptResult(
                status = status,
                record = null,
                reason = normalizedReason,
            )
        }
    }
}
