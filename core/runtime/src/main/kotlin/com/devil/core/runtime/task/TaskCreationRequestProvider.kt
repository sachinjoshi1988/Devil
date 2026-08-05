package com.devil.core.runtime.task

import com.devil.core.runtime.decision.DecisionAuthorityResult

/**
 * Supplies one structured constitutional task-creation request when a bounded
 * DecisionRecord has been produced.
 *
 * This provider does not create tasks, reinterpret decisions, select plans,
 * authorize capabilities, execute actions, observe results, or verify
 * outcomes.
 */
interface TaskCreationRequestProvider {

    fun provide(
        decision: DecisionAuthorityResult,
    ): TaskCreationRequestResult
}
