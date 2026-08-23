package com.devil.core.runtime.modelprovider

/**
 * Stage 239 bounded Structured Reasoning Integration status.
 *
 * INTEGRATED means one exact PREPARED Stage 238 Tool-Using Intelligence result
 * has been associated with explicitly supplied bounded structured-reasoning metadata.
 *
 * DEFERRED means Stage 239 cannot truthfully establish bounded structured-reasoning
 * integration.
 *
 * Structured reasoning here is model-domain structural context only.
 *
 * STRUCTURED_REASONING_INTEGRATED != BRAIN_DECISION.
 * STRUCTURED_REASONING_INTEGRATED != DECISION_SELECTED.
 * STRUCTURED_REASONING_INTEGRATED != UNDERSTANDING.
 * STRUCTURED_REASONING_INTEGRATED != AUTHORIZATION.
 * STRUCTURED_REASONING_INTEGRATED != MODEL_INVOKED.
 * STRUCTURED_REASONING_INTEGRATED != INFERENCE_PERFORMED.
 * STRUCTURED_REASONING_INTEGRATED != TOOL_EXECUTED.
 * STRUCTURED_REASONING_INTEGRATED != VERIFIED_TRUTH.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 */
enum class StructuredReasoningIntegrationStatus {
    INTEGRATED,
    DEFERRED,
}
