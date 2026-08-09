package com.devil.core.runtime.conversation

/**
 * Describes whether one genuine conversation identity is available for bounded
 * conversation continuity.
 *
 * This status does not generate conversation identities, establish persistence,
 * create logical memory, authenticate a subject, grant authorization, execute
 * capabilities, or establish a verified outcome.
 */
enum class ConversationIdentityProvisionStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
