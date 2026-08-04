package com.devil.core.model.trust

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class TrustEvaluationRequestTest {

    @Test
    fun `create preserves constitutional context and resolved subject identity`() {
        val context = createContext()
        val identityId = IdentityId.from(
            "subject-trust-evaluation-request-001",
        )

        val request = TrustEvaluationRequest.create(
            context = context,
            subjectIdentityId = identityId,
        )

        assertEquals(context, request.context)
        assertEquals(identityId, request.subjectIdentityId)
    }

    @Test
    fun `context trust classification remains separate from subject trust evaluation`() {
        val context = createContext(
            trustLevel = ContextTrustLevel.TRUSTED,
        )

        val request = TrustEvaluationRequest.create(
            context = context,
            subjectIdentityId = IdentityId.from(
                "subject-trust-evaluation-request-002",
            ),
        )

        assertEquals(
            ContextTrustLevel.TRUSTED,
            request.context.trustLevel,
        )
        assertEquals(
            "subject-trust-evaluation-request-002",
            request.subjectIdentityId.value,
        )
    }

    private fun createContext(
        trustLevel: ContextTrustLevel =
            ContextTrustLevel.VERIFIED,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(
                "trace-trust-evaluation-request-001",
            ),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = trustLevel,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_048_000L,
            ),
        )
    }
}
