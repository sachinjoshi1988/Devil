package com.devil.core.model.owner

/**
 * Stage 43 bounded coordinator for owner-profile and relationship information.
 *
 * The coordinator obtains one snapshot from the supplied source and returns it
 * unchanged.
 *
 * It is not another Brain, Identity Authority, Trust Authority, Security
 * Authority, Authorization Authority, Memory Authority, or execution path.
 *
 * Reading owner-profile information does not authenticate anyone and does not
 * authorize any action.
 */
class OwnerProfileCoordinator(
    private val source: OwnerProfileSource,
) {

    fun snapshot(): OwnerProfileSnapshot {
        return source.snapshot()
    }
}
