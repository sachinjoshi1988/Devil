package com.devil.core.runtime.trust

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.trust.SubjectTrustLevel
import com.devil.core.model.trust.TrustEvaluationRequest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultTrustEvaluationResolverTest {

    @Test
    fun `evaluate preserves subject and returns unestablished trust`() {
        val request = createRequest(
            trustLevel = ContextTrustLevel.TRUSTED,
        )
        val resolver: TrustEvaluationResolver =
            DefaultTrustEvaluationResolver()

        val assessment = resolver.evaluate(request)

        assertEquals(
            request.subjectIdentityId,
            assessment.subjectIdentityId,
        )
        assertEquals(
            SubjectTrustLevel.UNESTABLISHED,
            assessment.level,
        )
        assertEquals(
            "No subject trust evaluation policy is available.",
            assessment.rationale,
        )
    }

    @Test
    fun `evaluate does not copy context trust into subject trust`() {
        val request = createRequest(
            trustLevel = ContextTrustLevel.TRUSTED,
        )
        val resolver: TrustEvaluationResolver =
            DefaultTrustEvaluationResolver()

        val assessment = resolver.evaluate(request)

        assertEquals(
            ContextTrustLevel.TRUSTED,
            request.context.trustLevel,
        )
        assertEquals(
            SubjectTrustLevel.UNESTABLISHED,
            assessment.level,
        )
    }

    private fun createRequest(
        trustLevel: ContextTrustLevel,
    ): TrustEvaluationRequest {
        return TrustEvaluationRequest.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from(
                    "trace-default-trust-evaluation-resolver-001",
                ),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEST,
                trustLevel = trustLevel,
                securityLevel = ContextSecurityLevel.RESTRICTED,
                observedAt = DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_051_000L,
                ),
            ),
            subjectIdentityId = IdentityId.from(
                "subject-default-trust-evaluation-resolver-001",
            ),
        )
    }
}
