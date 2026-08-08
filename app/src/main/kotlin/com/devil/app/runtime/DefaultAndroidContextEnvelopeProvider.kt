package com.devil.app.runtime

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel

/**
 * Default bounded Android ContextEnvelope provider.
 *
 * This implementation obtains only those values that belong at the Android
 * runtime boundary: one fresh trace identity and one observation timestamp.
 *
 * Constitutional schema, provenance, trust, and security values remain explicit
 * inputs and are preserved without reinterpretation.
 *
 * Final immutable ContextEnvelope construction is delegated to
 * AndroidContextEnvelopeFactory.
 *
 * This provider creates no independent constitutional authority.
 */
class DefaultAndroidContextEnvelopeProvider(
    private val traceIdProvider: AndroidTraceIdProvider =
        DefaultAndroidTraceIdProvider(),
    private val observationTimeProvider: AndroidObservationTimeProvider =
        DefaultAndroidObservationTimeProvider(),
    private val contextEnvelopeFactory: AndroidContextEnvelopeFactory =
        DefaultAndroidContextEnvelopeFactory(),
) : AndroidContextEnvelopeProvider {

    override fun provide(
        schemaVersion: SchemaVersion,
        source: ContextSource,
        trustLevel: ContextTrustLevel,
        securityLevel: ContextSecurityLevel,
    ): ContextEnvelope {
        val traceId = traceIdProvider.provide()
        val observedAt = observationTimeProvider.observe()

        return contextEnvelopeFactory.create(
            traceId = traceId,
            schemaVersion = schemaVersion,
            source = source,
            trustLevel = trustLevel,
            securityLevel = securityLevel,
            observedAt = observedAt,
        )
    }
}
