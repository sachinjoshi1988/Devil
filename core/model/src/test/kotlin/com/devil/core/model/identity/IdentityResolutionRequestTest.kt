package com.devil.core.model.identity

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import kotlin.test.Test
import kotlin.test.assertEquals

class IdentityResolutionRequestTest {

    @Test
    fun `create preserves context and identity evidence set`() {
        val context = createContext()
        val identityId = IdentityId.from(
            "subject-resolution-request-001",
        )
        val evidenceSet = IdentityEvidenceSet.create(
            claimedIdentityId = identityId,
            evidence = listOf(
                IdentityEvidence.create(
                    claimedIdentityId = identityId,
                    source = IdentityEvidenceSource.DEVICE,
                    observedAt = DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_031_000L,
                    ),
                    reference = "device-profile-resolution-001",
                ),
            ),
        )

        val request = IdentityResolutionRequest.create(
            context = context,
            evidenceSet = evidenceSet,
        )

        assertEquals(context, request.context)
        assertEquals(evidenceSet, request.evidenceSet)
        assertEquals(
            identityId,
            request.evidenceSet.claimedIdentityId,
        )
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(
                "trace-identity-resolution-request-001",
            ),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_030_000L,
            ),
        )
    }
}
