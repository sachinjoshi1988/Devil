package com.devil.core.model.authorization

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthorizationAssessmentTest {

    @Test
    fun `create preserves and normalizes deferred assessment`() {
        val identityId = IdentityId.from(
            "subject-authorization-assessment-001",
        )

        val assessment = AuthorizationAssessment.create(
            subjectIdentityId = identityId,
            state = AuthorizationEvaluationState.DEFERRED,
            rationale = "  No authorization policy conclusion is available.  ",
        )

        assertEquals(identityId, assessment.subjectIdentityId)
        assertEquals(
            AuthorizationEvaluationState.DEFERRED,
            assessment.state,
        )
        assertEquals(
            "No authorization policy conclusion is available.",
            assessment.rationale,
        )
    }

    @Test
    fun `create preserves denied assessment without implying execution`() {
        val assessment = AuthorizationAssessment.create(
            subjectIdentityId = IdentityId.from(
                "subject-authorization-assessment-002",
            ),
            state = AuthorizationEvaluationState.DENIED,
            rationale = "Continuation is not permitted by policy.",
        )

        assertEquals(
            AuthorizationEvaluationState.DENIED,
            assessment.state,
        )
    }

    @Test
    fun `create preserves authorized continuation assessment`() {
        val assessment = AuthorizationAssessment.create(
            subjectIdentityId = IdentityId.from(
                "subject-authorization-assessment-003",
            ),
            state = AuthorizationEvaluationState.AUTHORIZED,
            rationale = "Constitutional continuation requirements were met.",
        )

        assertEquals(
            AuthorizationEvaluationState.AUTHORIZED,
            assessment.state,
        )
    }

    @Test
    fun `create rejects blank authorization rationale`() {
        assertFailsWith<IllegalArgumentException> {
            AuthorizationAssessment.create(
                subjectIdentityId = IdentityId.from(
                    "subject-authorization-assessment-004",
                ),
                state = AuthorizationEvaluationState.DEFERRED,
                rationale = "   ",
            )
        }
    }
}
