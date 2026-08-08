package com.devil.app.runtime

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel

/**
 * Supplies one fresh bounded constitutional ContextEnvelope for Android input.
 *
 * Trace identity and observation time may be obtained from their bounded Android
 * runtime providers.
 *
 * Schema version, provenance, trust classification, and security classification
 * must already have been established by the proper upstream caller and are
 * therefore supplied explicitly.
 *
 * This provider does not choose schema version, infer provenance, assign trust,
 * assign security classification, resolve identity, grant authorization,
 * interpret input, make decisions, plan work, select or execute capabilities,
 * create memory, persist memory, or invoke the UnifiedDevilRuntime.
 *
 * It grants no authority.
 */
interface AndroidContextEnvelopeProvider {

    fun provide(
        schemaVersion: SchemaVersion,
        source: ContextSource,
        trustLevel: ContextTrustLevel,
        securityLevel: ContextSecurityLevel,
    ): ContextEnvelope
}
