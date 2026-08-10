package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId

/**
 * One explicit Stage 43 request for bounded owner-profile information.
 *
 * PROFILE and PREFERRED_FORM_OF_ADDRESS require no subject identity.
 *
 * RELATIONSHIP_FOR_SUBJECT requires exactly one subject identity.
 *
 * The supplied subject identity is descriptive lookup identity only.
 *
 * Possessing or matching that identity does not authenticate the subject.
 */
@ConsistentCopyVisibility
data class OwnerProfileQuery private constructor(
    val type: OwnerProfileQueryType,
    val subjectIdentityId: IdentityId?,
) {
    companion object {

        fun profile(): OwnerProfileQuery {
            return OwnerProfileQuery(
                type = OwnerProfileQueryType.PROFILE,
                subjectIdentityId = null,
            )
        }

        fun preferredFormOfAddress(): OwnerProfileQuery {
            return OwnerProfileQuery(
                type =
                    OwnerProfileQueryType.PREFERRED_FORM_OF_ADDRESS,
                subjectIdentityId = null,
            )
        }

        fun relationshipForSubject(
            subjectIdentityId: IdentityId,
        ): OwnerProfileQuery {
            return OwnerProfileQuery(
                type =
                    OwnerProfileQueryType.RELATIONSHIP_FOR_SUBJECT,
                subjectIdentityId = subjectIdentityId,
            )
        }
    }
}
