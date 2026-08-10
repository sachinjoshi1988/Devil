package com.devil.core.model.reliability

/**
 * Stage 45 pure policy for finite recovery-attempt accounting.
 *
 * The policy may consume exactly one attempt from the budget embedded in one
 * valid RecoveryRequest.
 *
 * It performs no recovery operation.
 *
 * It does not:
 *
 * - retry an operation;
 * - restart or reinitialize a component;
 * - reconnect a source;
 * - invoke UnifiedDevilRuntime;
 * - invoke an execution adapter;
 * - grant authorization;
 * - alter capability health;
 * - erase failure evidence;
 * - persist memory;
 * - or claim recovery success.
 *
 * Accounting an attempt is intentionally distinct from executing that attempt.
 */
class RecoveryAttemptPolicy {

    fun record(
        requestResult: RecoveryRequestResult,
    ): RecoveryAttemptResult {
        return when (requestResult.status) {
            RecoveryRequestStatus.AVAILABLE -> {
                val request =
                    requestResult.request
                        ?: return RecoveryAttemptResult.unavailable(
                            reason =
                                "Available recovery-request result did not contain a request.",
                        )

                if (request.attemptBudget.exhausted) {
                    RecoveryAttemptResult.exhausted(
                        reason =
                            "Recovery attempt cannot be recorded because the finite recovery budget is exhausted.",
                    )
                } else {
                    RecoveryAttemptResult.recorded(
                        record =
                            RecoveryAttemptRecord.create(
                                request = request,
                            ),
                    )
                }
            }

            RecoveryRequestStatus.EXHAUSTED ->
                RecoveryAttemptResult.exhausted(
                    reason =
                        requestResult.reason
                            ?: "Recovery request budget is exhausted.",
                )

            RecoveryRequestStatus.UNAVAILABLE ->
                RecoveryAttemptResult.unavailable(
                    reason =
                        requestResult.reason
                            ?: "Recovery request is unavailable.",
                )
        }
    }
}
