package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId

/**
 * Immutable Stage 44 child-and-guardian policy context.
 *
 * This context combines:
 *
 * - the subject identity to which child policy may apply;
 * - an explicitly supplied child-policy classification;
 * - optional guardian-authority state supplied by an approved source.
 *
 * A guardian record, when present, must refer to the same subject identity.
 *
 * No guardian record is fabricated merely because the subject is classified
 * CHILD.
 *
 * Likewise, CHILD does not automatically imply that a guardian exists.
 *
 * ChildGuardianContext
 * != authentication
 * != trust
 * != guardian authentication
 * != Owner Mode
 * != authorization
 * != permission
 * != execution approval
 * != logical memory.
 */
@ConsistentCopyVisibility
data class ChildGuardianContext private constructor(
    val subjectIdentityId: IdentityId,
    val classification: ChildSubjectClassification,
    val guardianAuthority: GuardianAuthorityRecord?,
) {
    companion object {

        fun create(
            subjectIdentityId: IdentityId,
            classification: ChildSubjectClassification,
            guardianAuthority: GuardianAuthorityRecord? = null,
        ): ChildGuardianContext {
            require(
                guardianAuthority == null ||
                    guardianAuthority.childIdentityId == subjectIdentityId,
            ) {
                "Guardian authority record must belong to the supplied subject identity."
            }

            return ChildGuardianContext(
                subjectIdentityId = subjectIdentityId,
                classification = classification,
                guardianAuthority = guardianAuthority,
            )
        }
    }
}
