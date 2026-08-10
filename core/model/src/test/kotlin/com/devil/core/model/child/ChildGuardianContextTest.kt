package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ChildGuardianContextTest {

    @Test
    fun `child classification does not fabricate guardian authority`() {
        val subjectIdentityId =
            IdentityId.from(
                "identity-stage-44-child-context-001",
            )

        val context =
            ChildGuardianContext.create(
                subjectIdentityId = subjectIdentityId,
                classification = ChildSubjectClassification.CHILD,
            )

        assertEquals(
            subjectIdentityId,
            context.subjectIdentityId,
        )
        assertEquals(
            ChildSubjectClassification.CHILD,
            context.classification,
        )
        assertNull(context.guardianAuthority)
    }

    @Test
    fun `context preserves matching explicit guardian authority`() {
        val childIdentityId =
            IdentityId.from(
                "identity-stage-44-child-context-002",
            )

        val guardian =
            GuardianAuthorityRecord.create(
                childIdentityId = childIdentityId,
                guardianIdentityId =
                    IdentityId.from(
                        "identity-stage-44-guardian-context-002",
                    ),
                status = GuardianAuthorityStatus.ESTABLISHED,
            )

        val context =
            ChildGuardianContext.create(
                subjectIdentityId = childIdentityId,
                classification = ChildSubjectClassification.CHILD,
                guardianAuthority = guardian,
            )

        assertEquals(
            guardian,
            context.guardianAuthority,
        )
    }

    @Test
    fun `context rejects guardian authority belonging to another child`() {
        val requestedSubject =
            IdentityId.from(
                "identity-stage-44-child-context-003",
            )

        val differentChild =
            IdentityId.from(
                "identity-stage-44-child-context-004",
            )

        val guardian =
            GuardianAuthorityRecord.create(
                childIdentityId = differentChild,
                guardianIdentityId =
                    IdentityId.from(
                        "identity-stage-44-guardian-context-003",
                    ),
                status = GuardianAuthorityStatus.ESTABLISHED,
            )

        assertFailsWith<IllegalArgumentException> {
            ChildGuardianContext.create(
                subjectIdentityId = requestedSubject,
                classification = ChildSubjectClassification.CHILD,
                guardianAuthority = guardian,
            )
        }
    }
}
