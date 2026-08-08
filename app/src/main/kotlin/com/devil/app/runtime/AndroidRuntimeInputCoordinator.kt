package com.devil.app.runtime

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.runtime.RuntimeResult

/**
 * Coordinates one bounded Android textual input into the single Unified Devil
 * Runtime path.
 *
 * Schema version, provenance, trust classification, and security classification
 * must already have been established by their proper upstream authorities.
 *
 * This coordinator requests one ContextEnvelope from the Android runtime
 * boundary and submits the supplied textual content through exactly one
 * AndroidRuntimeGateway.
 *
 * It does not choose constitutional classifications, resolve identity, grant
 * authorization, interpret input, make decisions, plan work, select or execute
 * capabilities, create or persist logical memory, or perform Android actions.
 *
 * It grants no authority and creates no second runtime path.
 */
interface AndroidRuntimeInputCoordinator {

    fun submit(
        schemaVersion: SchemaVersion,
        source: ContextSource,
        trustLevel: ContextTrustLevel,
        securityLevel: ContextSecurityLevel,
        content: String,
    ): RuntimeResult
}
