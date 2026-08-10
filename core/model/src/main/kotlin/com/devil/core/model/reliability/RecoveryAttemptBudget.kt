package com.devil.core.model.reliability

/**
 * Stage 45 finite budget constraining bounded recovery attempts.
 *
 * The budget exists to prevent uncontrolled or infinite recovery loops.
 *
 * maximumAttempts must be strictly positive.
 *
 * attemptsAlreadyUsed must never be negative and must never exceed
 * maximumAttempts.
 *
 * Remaining budget
 * != authorization to consume it.
 *
 * Possessing an unused attempt
 * != permission to retry.
 */
@ConsistentCopyVisibility
data class RecoveryAttemptBudget private constructor(
    val maximumAttempts: Int,
    val attemptsAlreadyUsed: Int,
) {

    val remainingAttempts: Int
        get() =
            maximumAttempts - attemptsAlreadyUsed

    val exhausted: Boolean
        get() =
            remainingAttempts == 0

    companion object {

        fun create(
            maximumAttempts: Int,
            attemptsAlreadyUsed: Int = 0,
        ): RecoveryAttemptBudget {
            require(maximumAttempts > 0) {
                "Recovery maximum attempts must be greater than zero."
            }

            require(attemptsAlreadyUsed >= 0) {
                "Recovery attempts already used must not be negative."
            }

            require(attemptsAlreadyUsed <= maximumAttempts) {
                "Recovery attempts already used must not exceed maximum attempts."
            }

            return RecoveryAttemptBudget(
                maximumAttempts = maximumAttempts,
                attemptsAlreadyUsed = attemptsAlreadyUsed,
            )
        }
    }
}
