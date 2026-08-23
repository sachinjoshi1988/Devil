package com.devil.core.runtime.modelprovider

/**
 * Stage 238 bounded Tool-Using Intelligence status.
 *
 * PREPARED means one exact ROUTED Stage 235 Model Routing result and one exact
 * existing Devil CapabilityContract have been associated with explicitly supplied
 * bounded tool-use intent metadata.
 *
 * DEFERRED means Stage 238 cannot truthfully claim bounded tool-use preparation.
 *
 * Tool-Using Intelligence remains subordinate to Devil's existing constitutional
 * capability-selection, authorization, Executive, and Execution authorities.
 *
 * TOOL_USE_PREPARED != CAPABILITY_SELECTED.
 * TOOL_USE_PREPARED != AUTHORIZATION.
 * TOOL_USE_PREPARED != EXECUTION_REQUEST.
 * TOOL_USE_PREPARED != CAPABILITY_EXECUTED.
 * TOOL_USE_PREPARED != MODEL_INVOKED.
 * TOOL_USE_PREPARED != INFERENCE_PERFORMED.
 * TOOL_USE_PREPARED != TOOL_RESULT.
 * TOOL_USE_PREPARED != VERIFIED_OUTCOME.
 * MODEL != DEVIL.
 * MODEL != EXECUTION_AUTHORITY.
 */
enum class ToolUsingIntelligenceStatus {
    PREPARED,
    DEFERRED,
}
