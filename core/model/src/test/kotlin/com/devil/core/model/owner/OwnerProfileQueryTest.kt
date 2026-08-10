package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OwnerProfileQueryTest {

    @Test
    fun `profile query requires no subject identity`() {
        val query =
            OwnerProfileQuery.profile()

        assertEquals(
            OwnerProfileQueryType.PROFILE,
            query.type,
        )
        assertNull(query.subjectIdentityId)
    }

    @Test
    fun `relationship query preserves explicit subject identity`() {
        val subjectIdentityId =
            IdentityId.from(
                "identity-stage-43-query-subject-001",
            )

        val query =
            OwnerProfileQuery.relationshipForSubject(
                subjectIdentityId = subjectIdentityId,
            )

        assertEquals(
            OwnerProfileQueryType.RELATIONSHIP_FOR_SUBJECT,
            query.type,
        )
        assertEquals(
            subjectIdentityId,
            query.subjectIdentityId,
        )
    }
}
