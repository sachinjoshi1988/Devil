package com.devil.core.model.trust

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TrustAssessmentTest {

    @Test
    fun `create preserves and normalizes trusted subject assessment`() {
        val identityId = IdentityId.from(
            "subject-trust-assessment-001",
        )

        val assessment = TrustAssessment.create(
            subjectIdentityId = identityId,
            level = SubjectTrustLevel.TRUSTED,
            rationale = "  Verified evidence supports bounded subject trust.  ",
        )

        assertEquals(identityId, assessment.subjectIdentityId)
        assertEquals(SubjectTrustLevel.TRUSTED, assessment.level)
        assertEquals(
            "Verified evidence supports bounded subject trust.",
            assessment.rationale,
        )
    }

    @Test
    fun `create preserves unestablished trust without granting authority`() {
        val assessment = TrustAssessment.create(
            subjectIdentityId = IdentityId.from(
                "subject-trust-assessment-002",
            ),
            level = SubjectTrustLevel.UNESTABLISHED,
            rationale = "No trust policy conclusion is available.",
        )

        assertEquals(
            SubjectTrustLevel.UNESTABLISHED,
            assessment.level,
        )
    }

    @Test
    fun `create preserves restricted subject trust`() {
        val assessment = TrustAssessment.create(
            subjectIdentityId = IdentityId.from(
                "subject-trust-assessment-003",
            ),
            level = SubjectTrustLevel.RESTRICTED,
            rationale = "The available evidence supports restricted trust only.",
        )

        assertEquals(SubjectTrustLevel.RESTRICTED, assessment.level)
    }

    @Test
    fun `create rejects blank trust rationale`() {
        assertFailsWith<IllegalArgumentException> {
            TrustAssessment.create(
                subjectIdentityId = IdentityId.from(
                    "subject-trust-assessment-004",
                ),
                level = SubjectTrustLevel.UNESTABLISHED,
                rationale = "   ",
            )
        }
    }
}
