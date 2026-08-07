package com.devil.app.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel

/**
 * Composes one bounded constitutional ContextEnvelope for Android runtime input.
 *
 * Every constitutional value must already have been established by its proper
 * upstream source before reaching this factory.
 *
 * This factory does not generate trace identity, access a clock, choose schema
 * version, infer provenance, assign trust, assign security classification,
 * resolve identity, grant authorization, interpret input, make decisions,
 * execute capabilities, or create or persist logical memory.
 *
 * It grants no authority.
 */
interface AndroidContextEnvelopeFactory {

    fun create(
        traceId: TraceId,
        schemaVersion: SchemaVersion,
        source: ContextSource,
        trustLevel: ContextTrustLevel,
        securityLevel: ContextSecurityLevel,
        observedAt: DevilTimestamp,
    ): ContextEnvelope
}
