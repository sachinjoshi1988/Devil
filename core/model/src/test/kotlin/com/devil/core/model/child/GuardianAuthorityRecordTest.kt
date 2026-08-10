package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GuardianAuthorityRecordTest {

    @Test
    fun `record preserves explicitly supplied guardian authority context`() {
        val childIdentityId =
            IdentityId.from(
                "identity-stage-44-child-001",
            )

        val guardianIdentityId =
            IdentityId.from(
                "identity-stage-44-guardian-001",
            )

        val record =
            GuardianAuthorityRecord.create(
                childIdentityId = childIdentityId,
                guardianIdentityId = guardianIdentityId,
                status = GuardianAuthorityStatus.ESTABLISHED,
            )

        assertEquals(
            childIdentityId,
            record.childIdentityId,
        )
        assertEquals(
            guardianIdentityId,
            record.guardianIdentityId,
        )
        assertEquals(
            GuardianAuthorityStatus.ESTABLISHED,
            record.status,
        )
    }

    @Test
    fun `guardian and child identities must remain distinct`() {
        val identityId =
            IdentityId.from(
                "identity-stage-44-same-001",
            )

        assertFailsWith<IllegalArgumentException> {
            GuardianAuthorityRecord.create(
                childIdentityId = identityId,
                guardianIdentityId = identityId,
                status = GuardianAuthorityStatus.ESTABLISHED,
            )
        }
    }
}
