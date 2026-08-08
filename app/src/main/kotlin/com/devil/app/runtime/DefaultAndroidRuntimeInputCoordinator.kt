package com.devil.app.runtime

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.runtime.RuntimeResult

/**
 * Default bounded Android runtime-input coordinator.
 *
 * Context creation remains delegated to AndroidContextEnvelopeProvider.
 * Conversation adaptation and constitutional runtime submission remain
 * delegated to AndroidRuntimeGateway.
 *
 * This implementation preserves the supplied constitutional classifications
 * without reinterpretation and introduces no independent authority.
 */
class DefaultAndroidRuntimeInputCoordinator(
    private val contextEnvelopeProvider: AndroidContextEnvelopeProvider,
    private val runtimeGateway: AndroidRuntimeGateway,
) : AndroidRuntimeInputCoordinator {

    override fun submit(
        schemaVersion: SchemaVersion,
        source: ContextSource,
        trustLevel: ContextTrustLevel,
        securityLevel: ContextSecurityLevel,
        content: String,
    ): RuntimeResult {
        val context = contextEnvelopeProvider.provide(
            schemaVersion = schemaVersion,
            source = source,
            trustLevel = trustLevel,
            securityLevel = securityLevel,
        )

        val result = runtimeGateway.submit(
            context = context,
            content = content,
        )

        require(result.traceId == context.traceId) {
            "Android runtime input coordinator context and runtime result must use the same trace identity."
        }

        return result
    }
}
