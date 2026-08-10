package com.devil.core.model.owner

/**
 * Supplies one bounded Stage 43 owner-profile snapshot.
 *
 * A source may expose only information already established by its approved
 * implementation.
 *
 * It must not infer owner identity from profile fields, authenticate a subject,
 * establish trust, grant authorization, enter Owner Mode, create logical
 * memory, persist information, or execute an action.
 */
fun interface OwnerProfileSource {

    fun snapshot(): OwnerProfileSnapshot
}
