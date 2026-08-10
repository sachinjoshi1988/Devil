package com.devil.core.model.owner

/**
 * Stage 43 bounded coordinator for explicit owner-profile structural updates.
 *
 * Flow:
 *
 * OwnerProfileUpdateRequest
 * -> OwnerProfileUpdatePolicy
 * -> OwnerProfileUpdateResult.
 *
 * This coordinator does not modify an OwnerProfileSource and does not persist
 * the resulting snapshot.
 *
 * It is not an Identity Authority, Trust Authority, Security Authority,
 * Authorization Authority, Memory Authority, Brain, Planner, Executive, or
 * execution mechanism.
 */
class OwnerProfileUpdateCoordinator(
    private val policy: OwnerProfileUpdatePolicy =
        OwnerProfileUpdatePolicy(),
) {

    fun update(
        request: OwnerProfileUpdateRequest,
    ): OwnerProfileUpdateResult {
        return policy.evaluate(
            request = request,
        )
    }
}
