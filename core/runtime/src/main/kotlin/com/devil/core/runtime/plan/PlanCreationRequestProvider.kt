package com.devil.core.runtime.plan

import com.devil.core.runtime.task.TaskAuthorityResult

/**
 * Supplies one structured constitutional plan-creation request when a bounded
 * TaskRecord has been created.
 *
 * This provider does not create planning strategy, generate plan identity,
 * create plans, bind or authorize capabilities, execute actions, observe
 * results, verify outcomes, or report final outcomes.
 */
interface PlanCreationRequestProvider {

    fun provide(
        task: TaskAuthorityResult,
    ): PlanCreationRequestResult
}
