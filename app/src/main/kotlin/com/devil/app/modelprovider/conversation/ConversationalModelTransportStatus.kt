package com.devil.app.modelprovider.conversation

/**
 * Stage 313 bounded transport result classification.
 *
 * GENERATED means a transport implementation returned generated text.
 * UNAVAILABLE means no bounded transport result could be obtained.
 *
 * GENERATED != VERIFIED.
 * GENERATED != OUTCOME.
 * GENERATED != AUTHORIZATION.
 */
enum class ConversationalModelTransportStatus {
    GENERATED,
    UNAVAILABLE,
}
