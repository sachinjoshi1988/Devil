package com.devil.core.model.reliability

/**
 * Stage 45 bounded recovery disposition derived from reliability evidence.
 *
 * RECOVERY_NOT_REQUIRED means the supplied evidence does not establish a need
 * for recovery.
 *
 * RECOVERY_ELIGIBLE means a later authorized recovery mechanism may consider
 * recovery.
 *
 * MANUAL_INTERVENTION_REQUIRED means Stage 45 must not automatically proceed.
 *
 * NOT_RECOVERABLE means the supplied evidence establishes that bounded recovery
 * must not be attempted through this policy.
 *
 * UNAVAILABLE means there is insufficient evidence to determine a safe recovery
 * disposition.
 *
 * RECOVERY_ELIGIBLE
 * != retry authorized
 * != execution approved
 * != recovery started
 * != recovered
 * != capability READY.
 */
enum class RecoveryDisposition {
    RECOVERY_NOT_REQUIRED,
    RECOVERY_ELIGIBLE,
    MANUAL_INTERVENTION_REQUIRED,
    NOT_RECOVERABLE,
    UNAVAILABLE,
}
