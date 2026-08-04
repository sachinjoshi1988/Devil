package com.devil.core.model.owner

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class OwnerContextTest {

    @Test
    fun `create preserves owner and matching subject identities`() {
        val ownerIdentityId = IdentityId.from("owner-001")

        val context = OwnerContext.create(
            ownerIdentityId = ownerIdentityId,
            subjectIdentityId = ownerIdentityId,
        )

        assertEquals(ownerIdentityId, context.ownerIdentityId)
        assertEquals(ownerIdentityId, context.subjectIdentityId)
    }

    @Test
    fun `create preserves distinct owner and subject identities`() {
        val ownerIdentityId = IdentityId.from("owner-002")
        val subjectIdentityId = IdentityId.from("subject-child-001")

        val context = OwnerContext.create(
            ownerIdentityId = ownerIdentityId,
            subjectIdentityId = subjectIdentityId,
        )

        assertEquals(ownerIdentityId, context.ownerIdentityId)
        assertEquals(subjectIdentityId, context.subjectIdentityId)
    }
}
