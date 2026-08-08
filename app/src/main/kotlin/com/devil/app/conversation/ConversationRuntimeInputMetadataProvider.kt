package com.devil.app.conversation

/**
 * Supplies complete bounded metadata required before one typed conversation
 * submission may enter the Android runtime-input boundary.
 *
 * Implementations must not infer subject trust from ContextTrustLevel, derive
 * ContextSecurityLevel from SecurityStage, invent schema version, authenticate
 * a subject, grant authorization, or invoke the Unified Devil Runtime.
 */
interface ConversationRuntimeInputMetadataProvider {

    fun provide(): ConversationRuntimeInputMetadataResult
}
