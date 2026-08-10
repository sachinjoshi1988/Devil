package com.devil.core.model.owner

/**
 * Stage 43 bounded coordinator for descriptive owner-profile queries.
 *
 * Flow:
 *
 * OwnerProfileQuery
 * -> OwnerProfileSource
 * -> OwnerProfileSnapshot
 * -> OwnerProfileQueryPolicy
 * -> OwnerProfileQueryResult.
 *
 * This coordinator is not another Brain, Identity Authority, Trust Authority,
 * Security Authority, Authorization Authority, Memory Authority, Planner,
 * Executive, runtime, or execution mechanism.
 *
 * It does not authenticate a matching subject and does not persist query data.
 */
class OwnerProfileQueryCoordinator(
    private val source: OwnerProfileSource,
    private val policy: OwnerProfileQueryPolicy =
        OwnerProfileQueryPolicy(),
) {

    fun query(
        request: OwnerProfileQuery,
    ): OwnerProfileQueryResult {
        val snapshot =
            source.snapshot()

        return policy.evaluate(
            query = request,
            snapshot = snapshot,
        )
    }
}
