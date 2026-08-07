package com.devil.app.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel

/**
 * Default bounded Android ContextEnvelope composer.
 *
 * This implementation preserves already-established constitutional values and
 * delegates immutable envelope construction to the existing core model contract.
 *
 * It generates no trace identity, reads no clock, assigns no constitutional
 * classification, and introduces no independent runtime authority.
 */
class DefaultAndroidContextEnvelopeFactory :
    AndroidContextEnvelopeFactory {

    override fun create(
        traceId: TraceId,
        schemaVersion: SchemaVersion,
        source: ContextSource,
        trustLevel: ContextTrustLevel,
        securityLevel: ContextSecurityLevel,
        observedAt: DevilTimestamp,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = traceId,
            schemaVersion = schemaVersion,
            source = source,
            trustLevel = trustLevel,
            securityLevel = securityLevel,
            observedAt = observedAt,
        )
    }
}
