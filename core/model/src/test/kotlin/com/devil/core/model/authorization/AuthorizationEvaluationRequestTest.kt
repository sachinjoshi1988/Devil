package com.devil.core.model.authorization

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.trust.SubjectTrustLevel
import com.devil.core.model.trust.TrustAssessment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthorizationEvaluationRequestTest {

    @Test
    fun `create preserves context subject and matching trust assessment`() {
        val context = createContext()
        val identityId = IdentityId.from(
            "subject-authorization-request-001",
        )
        val trustAssessment = createTrustAssessment(
            identityId = identityId,
        )

        val request = AuthorizationEvaluationRequest.create(
            context = context,
            subjectIdentityId = identityId,
            trustAssessment = trustAssessment,
        )

        assertEquals(context, request.context)
        assertEquals(identityId, request.subjectIdentityId)
        assertEquals(
            trustAssessment,
            request.trustAssessment,
        )
    }

    @Test
    fun `create rejects trust assessment for a different subject`() {
        assertFailsWith<IllegalArgumentException> {
            AuthorizationEvaluationRequest.create(
                context = createContext(),
                subjectIdentityId = IdentityId.from(
                    "subject-authorization-request-002",
                ),
                trustAssessment = createTrustAssessment(
                    identityId = IdentityId.from(
                        "subject-authorization-request-other",
                    ),
                ),
            )
        }
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(
                "trace-authorization-evaluation-request-001",
            ),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_053_000L,
            ),
        )
    }

    private fun createTrustAssessment(
        identityId: IdentityId,
    ): TrustAssessment {
        return TrustAssessment.create(
            subjectIdentityId = identityId,
            level = SubjectTrustLevel.UNESTABLISHED,
            rationale = "No subject trust conclusion is available.",
        )
    }
}
