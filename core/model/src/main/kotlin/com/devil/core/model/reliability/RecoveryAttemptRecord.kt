package com.devil.core.model.reliability

/**
 * Immutable Stage 45 accounting record for exactly one bounded recovery attempt.
 *
 * The record preserves:
 *
 * - the RecoveryRequest whose budget is being consumed;
 * - the ordinal number of the accounted attempt;
 * - the finite budget after exactly one attempt was consumed.
 *
 * Creating this record does not perform the recovery strategy.
 *
 * Attempt recorded
 * != recovery executed
 * != recovery succeeded
 * != authorization
 * != Executive readiness
 * != execution approval.
 */
@ConsistentCopyVisibility
data class RecoveryAttemptRecord private constructor(
    val request: RecoveryRequest,
    val attemptNumber: Int,
    val remainingBudget: RecoveryAttemptBudget,
) {
    companion object {

        fun create(
            request: RecoveryRequest,
        ): RecoveryAttemptRecord {
            val currentBudget =
                request.attemptBudget

            require(!currentBudget.exhausted) {
                "Recovery attempt cannot be recorded against an exhausted budget."
            }

            val nextAttemptsUsed =
                currentBudget.attemptsAlreadyUsed + 1

            val nextBudget =
                RecoveryAttemptBudget.create(
                    maximumAttempts =
                        currentBudget.maximumAttempts,
                    attemptsAlreadyUsed =
                        nextAttemptsUsed,
                )

            return RecoveryAttemptRecord(
                request = request,
                attemptNumber = nextAttemptsUsed,
                remainingBudget = nextBudget,
            )
        }
    }
}
