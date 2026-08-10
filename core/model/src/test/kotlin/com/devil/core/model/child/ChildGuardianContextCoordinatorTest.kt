package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ChildGuardianContextCoordinatorTest {

    @Test
    fun `coordinator preserves bounded context from approved source`() {
        val subjectIdentityId =
            IdentityId.from(
                "identity-stage-44-coordinator-001",
            )

        val expected =
            ChildGuardianContext.create(
                subjectIdentityId = subjectIdentityId,
                classification = ChildSubjectClassification.UNKNOWN,
            )

        val coordinator =
            ChildGuardianContextCoordinator(
                source =
                    ChildGuardianContextSource {
                        expected
                    },
            )

        assertEquals(
            expected,
            coordinator.contextFor(
                subjectIdentityId = subjectIdentityId,
            ),
        )
    }

    @Test
    fun `coordinator rejects source response for a different subject`() {
        val requestedIdentityId =
            IdentityId.from(
                "identity-stage-44-coordinator-002",
            )

        val differentIdentityId =
            IdentityId.from(
                "identity-stage-44-coordinator-003",
            )

        val coordinator =
            ChildGuardianContextCoordinator(
                source =
                    ChildGuardianContextSource {
                        ChildGuardianContext.create(
                            subjectIdentityId = differentIdentityId,
                            classification = ChildSubjectClassification.UNKNOWN,
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.contextFor(
                subjectIdentityId = requestedIdentityId,
            )
        }
    }
}
