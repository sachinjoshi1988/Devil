package com.devil.core.model.context

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId

/**
 * Carries the immutable constitutional header for context entering Devil.
 *
 * This contract describes provenance, classification, and observation time.
 * It does not contain understanding, decisions, memory, or execution state.
 */
@ConsistentCopyVisibility
data class ContextEnvelope private constructor(
    val traceId: TraceId,
    val schemaVersion: SchemaVersion,
    val source: ContextSource,
    val trustLevel: ContextTrustLevel,
    val securityLevel: ContextSecurityLevel,
    val observedAt: DevilTimestamp,
) {
    companion object {
        fun create(
            traceId: TraceId,
            schemaVersion: SchemaVersion,
            source: ContextSource,
            trustLevel: ContextTrustLevel,
            securityLevel: ContextSecurityLevel,
            observedAt: DevilTimestamp,
        ): ContextEnvelope {
            return ContextEnvelope(
                traceId = traceId,
                schemaVersion = schemaVersion,
                source = source,
                trustLevel = trustLevel,
                securityLevel = securityLevel,
                observedAt = observedAt,
            )
        }
    }
}
