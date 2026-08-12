package com.devil.core.runtime.execution

/**
 * Default fail-closed core execution-attempt port.
 *
 * No production execution embodiment is configured inside core runtime.
 *
 * Therefore:
 *
 * - APPROVED constitutional execution remains DEFERRED rather than being
 *   fabricated as ATTEMPTED;
 * - DEFERRED remains DEFERRED;
 * - FAILED preserves its matching operational error.
 *
 * A platform embodiment may later implement ExecutionAttemptPort outside core
 * and be injected through the normal Unified Devil Runtime composition boundary.
 *
 * This default performs no platform action and produces no observation evidence.
 */
class DefaultExecutionAttemptPort : ExecutionAttemptPort {

    override fun attempt(
        execution: ExecutionResult,
    ): ExecutionAttemptResult {
        return when (execution.status) {
            ExecutionStatus.APPROVED ->
                ExecutionAttemptResult.create(
                    traceId = execution.traceId,
                    status = ExecutionAttemptStatus.DEFERRED,
                )

            ExecutionStatus.DEFERRED ->
                ExecutionAttemptResult.create(
                    traceId = execution.traceId,
                    status = ExecutionAttemptStatus.DEFERRED,
                )

            ExecutionStatus.FAILED ->
                ExecutionAttemptResult.create(
                    traceId = execution.traceId,
                    status = ExecutionAttemptStatus.FAILED,
                    error = requireNotNull(execution.error),
                )
        }
    }
}
