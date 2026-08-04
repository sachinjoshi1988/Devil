package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationEvaluationRequest
import com.devil.core.model.authorization.AuthorizationEvaluationState
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

class DefaultAuthorizationEvaluationResolverTest {

    @Test
    fun `evaluate preserves subject and defers without authorization policy`() {
        val request = createRequest()
        val resolver: AuthorizationEvaluationResolver =
            DefaultAuthorizationEvaluationResolver()

        val assessment = resolver.evaluate(request)

        assertEquals(
            request.subjectIdentityId,
            assessment.subjectIdentityId,
        )
        assertEquals(
            AuthorizationEvaluationState.DEFERRED,
            assessment.state,
        )
        assertEquals(
            "No constitutional authorization policy is available.",
            assessment.rationale,
        )
    }

    @Test
    fun `evaluate does not derive authorization from trust classification`() {
        val request = createRequest(
            trustLevel = SubjectTrustLevel.TRUSTED,
        )
        val resolver: AuthorizationEvaluationResolver =
            DefaultAuthorizationEvaluationResolver()

        val assessment = resolver.evaluate(request)

        assertEquals(
            SubjectTrustLevel.TRUSTED,
            request.trustAssessment.level,
        )
        assertEquals(
            AuthorizationEvaluationState.DEFERRED,
            assessment.state,
        )
    }

    private fun createRequest(
        trustLevel: SubjectTrustLevel =
            SubjectTrustLevel.UNESTABLISHED,
    ): AuthorizationEvaluationRequest {
        val identityId = IdentityId.from(
            "subject-default-authorization-resolver-001",
        )

        return AuthorizationEvaluationRequest.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from(
                    "trace-default-authorization-resolver-001",
                ),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEST,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_056_000L,
                ),
            ),
            subjectIdentityId = identityId,
            trustAssessment = TrustAssessment.create(
                subjectIdentityId = identityId,
                level = trustLevel,
                rationale = "Bounded subject trust assessment.",
            ),
        )
    }
}
