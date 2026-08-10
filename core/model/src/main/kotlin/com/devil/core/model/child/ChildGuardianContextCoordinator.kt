package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId

/**
 * Stage 44 bounded coordinator for child-and-guardian policy context.
 *
 * Flow:
 *
 * explicit subject IdentityId
 * -> ChildGuardianContextSource
 * -> ChildGuardianContext.
 *
 * The coordinator performs no child inference and no guardian-authority
 * derivation.
 *
 * It is not another Brain, Identity Authority, Trust Authority, Security
 * Authority, Authorization Authority, Memory Authority, Planner, Executive, or
 * runtime.
 *
 * It does not invoke UnifiedDevilRuntime and does not execute actions.
 */
class ChildGuardianContextCoordinator(
    private val source: ChildGuardianContextSource,
) {

    fun contextFor(
        subjectIdentityId: IdentityId,
    ): ChildGuardianContext {
        val context =
            source.contextFor(
                subjectIdentityId = subjectIdentityId,
            )

        require(context.subjectIdentityId == subjectIdentityId) {
            "Child/guardian context source returned a different subject identity."
        }

        return context
    }
}
