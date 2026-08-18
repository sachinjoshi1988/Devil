package com.devil.core.runtime.memory

/**
 * Supplies one bounded constitutional logical-memory recall request only when
 * Stage 104 has already established recall eligibility.
 *
 * This provider does not establish recall eligibility itself.
 *
 * It does not read storage, retrieve logical memory, recall logical memory, expose
 * content, disclose content, establish disclosure permission, persist memory,
 * mutate memory, invoke platform storage, or execute any action.
 */
interface MemoryRecallRequestProvider {

    fun provide(
        eligibility: MemoryRecallEligibilityResult,
    ): MemoryRecallRequestResult
}
