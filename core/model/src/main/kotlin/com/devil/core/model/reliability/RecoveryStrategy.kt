package com.devil.core.model.reliability

/**
 * Stage 45 bounded class of a proposed recovery strategy.
 *
 * RETRY_SAME_OPERATION describes a later attempt to repeat the same bounded
 * operation.
 *
 * REINITIALIZE_COMPONENT describes a later attempt to rebuild bounded component
 * state without changing constitutional authority.
 *
 * RECONNECT_SOURCE describes a later attempt to re-establish one bounded
 * external or platform source connection.
 *
 * Strategy
 * != authority
 * != authorization
 * != execution
 * != retry started
 * != recovery success.
 *
 * A strategy is descriptive recovery intent only.
 */
enum class RecoveryStrategy {
    RETRY_SAME_OPERATION,
    REINITIALIZE_COMPONENT,
    RECONNECT_SOURCE,
}
