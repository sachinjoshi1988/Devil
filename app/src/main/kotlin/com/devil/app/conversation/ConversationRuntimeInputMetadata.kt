package com.devil.app.conversation

import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel

/**
 * Represents the complete bounded metadata required before one Stage 24
 * conversation input may enter AndroidRuntimeInputCoordinator.
 *
 * These values must already have been established by their proper upstream
 * mechanisms. This record only preserves them.
 *
 * In particular:
 *
 * - source describes the bounded input origin,
 * - trustLevel describes supplied-context trust rather than subject trust,
 * - securityLevel describes supplied-context sensitivity rather than
 *   constitutional SecurityStage,
 * - schemaVersion identifies the applicable contract schema.
 *
 * Creating this record does not authenticate a subject, establish trust,
 * establish security stage, grant authorization, create a session, invoke the
 * runtime, or permit execution.
 */
data class ConversationRuntimeInputMetadata(
    val schemaVersion: SchemaVersion,
    val source: ContextSource,
    val trustLevel: ContextTrustLevel,
    val securityLevel: ContextSecurityLevel,
)
