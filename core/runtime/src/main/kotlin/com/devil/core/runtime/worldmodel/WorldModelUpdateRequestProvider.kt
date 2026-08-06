package com.devil.core.runtime.worldmodel

import com.devil.core.runtime.outcome.OutcomeResult

/**
 * Supplies one structured constitutional World Model update request when a
 * genuine constitutional outcome has been established.
 *
 * This provider does not mutate world state, claim that world state changed,
 * change task or plan state, create memory or learning, communicate
 * externally, or produce a runtime result.
 */
interface WorldModelUpdateRequestProvider {

    fun provide(
        outcome: OutcomeResult,
    ): WorldModelUpdateRequestResult
}
