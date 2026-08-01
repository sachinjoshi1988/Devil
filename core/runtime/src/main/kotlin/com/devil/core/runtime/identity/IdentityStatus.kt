package com.devil.core.runtime.identity

/**
 * Describes the result of identity resolution for supplied context.
 *
 * This status does not imply authentication, trust, ownership, relationship,
 * authorization, or permission to act.
 */
enum class IdentityStatus {
    RESOLVED,
    UNRESOLVED,
    FAILED,
}
