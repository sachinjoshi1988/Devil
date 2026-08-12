package com.devil.core.runtime.execution

/**
 * Neutral execution-embodiment port between constitutional Execution approval
 * and constitutional Observation.
 *
 * The core runtime may approach this port only with the genuine ExecutionResult
 * produced by ExecutionAuthority.
 *
 * Implementations may attempt a bounded external or platform action only when
 * their own embodiment-specific gates are independently satisfied.
 *
 * This port grants no authority of its own.
 *
 * ExecutionStatus.APPROVED is necessary before an implementation may attempt an
 * action, but approval alone does not prove that an action was attempted.
 *
 * Implementations must return ATTEMPTED only after a genuine attempt occurred.
 *
 * This contract contains no Android dependency and creates no alternate Brain,
 * Executive, Planner, Security Authority, or execution authority.
 */
fun interface ExecutionAttemptPort {

    fun attempt(
        execution: ExecutionResult,
    ): ExecutionAttemptResult
}
