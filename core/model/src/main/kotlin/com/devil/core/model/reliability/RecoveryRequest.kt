package com.devil.core.model.reliability

/**
 * One explicit Stage 45 bounded request for possible later recovery.
 *
 * A recovery request may be constructed only from a Part 1 assessment whose
 * disposition is RECOVERY_ELIGIBLE and from a non-exhausted finite attempt
 * budget.
 *
 * The request preserves:
 *
 * - the original reliability assessment;
 * - one explicit recovery strategy;
 * - one finite attempt budget.
 *
 * Creating this request does not:
 *
 * - grant authorization;
 * - consume an attempt;
 * - retry an operation;
 * - restart or reinitialize anything;
 * - reconnect anything;
 * - invoke UnifiedDevilRuntime;
 * - invoke an execution adapter;
 * - mutate capability health;
 * - erase the original error;
 * - persist logical memory;
 * - or claim recovery.
 */
@ConsistentCopyVisibility
data class RecoveryRequest private constructor(
    val assessment: ReliabilityAssessment,
    val strategy: RecoveryStrategy,
    val attemptBudget: RecoveryAttemptBudget,
) {
    companion object {

        fun create(
            assessment: ReliabilityAssessment,
            strategy: RecoveryStrategy,
            attemptBudget: RecoveryAttemptBudget,
        ): RecoveryRequest {
            require(
                assessment.disposition ==
                    RecoveryDisposition.RECOVERY_ELIGIBLE,
            ) {
                "Recovery request requires a RECOVERY_ELIGIBLE reliability assessment."
            }

            require(!attemptBudget.exhausted) {
                "Recovery request requires at least one remaining recovery attempt."
            }

            return RecoveryRequest(
                assessment = assessment,
                strategy = strategy,
                attemptBudget = attemptBudget,
            )
        }
    }
}
