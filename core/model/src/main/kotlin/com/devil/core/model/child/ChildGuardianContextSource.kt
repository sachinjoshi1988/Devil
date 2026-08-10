package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId

/**
 * Supplies bounded Stage 44 child-and-guardian policy context for one explicitly
 * identified subject.
 *
 * Implementations must expose only information already established by their
 * approved source.
 *
 * A source must not:
 *
 * - infer child status from appearance or behavior;
 * - treat FAMILY as guardian authority;
 * - authenticate a subject;
 * - authenticate a guardian;
 * - establish trust;
 * - enter Owner Mode;
 * - grant authorization;
 * - persist logical memory;
 * - or execute an action.
 */
fun interface ChildGuardianContextSource {

    fun contextFor(
        subjectIdentityId: IdentityId,
    ): ChildGuardianContext
}
