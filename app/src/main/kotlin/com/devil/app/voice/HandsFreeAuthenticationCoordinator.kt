package com.devil.app.voice

/**
 * Stage 37 coordinator for the bounded authentication-request handoff.
 *
 * This coordinator may be invoked only after the hands-free state has reached
 * AUTHENTICATION_REQUESTED.
 *
 * It does not infer authentication from Code Red and cannot create
 * ACTIVE_SESSION.
 */
class HandsFreeAuthenticationCoordinator(
    private val authenticationHandoff:
        HandsFreeAuthenticationHandoff =
        DefaultHandsFreeAuthenticationHandoff(),
) {

    fun requestAuthentication(
        state: HandsFreeConversationState,
    ): HandsFreeAuthenticationHandoffResult {
        require(
            state ==
                HandsFreeConversationState.AUTHENTICATION_REQUESTED,
        ) {
            "Hands-free authentication may be requested only from AUTHENTICATION_REQUESTED state."
        }

        return authenticationHandoff.requestAuthentication()
    }
}
