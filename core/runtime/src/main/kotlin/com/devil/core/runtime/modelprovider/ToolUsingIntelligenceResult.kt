package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityContract

/**
 * Stage 238 bounded Tool-Using Intelligence result.
 *
 * PREPARED preserves:
 *
 * - one exact ROUTED Stage 235 Model Routing result;
 * - therefore the exact Stage 234 provider architecture and provider provenance;
 * - one exact existing Devil CapabilityContract;
 * - one normalized explicitly supplied bounded tool-use intent description.
 *
 * DEFERRED preserves the exact upstream routing result but contains no capability
 * association and no tool-use metadata.
 *
 * This result does not:
 *
 * - create another Devil intelligence, Brain, Constitution, Executive, Planner,
 *   Unified Devil Runtime, Memory Authority, Security Authority, or Execution Authority;
 * - allow a model or provider to become Devil;
 * - allow a model or provider to become an authority;
 * - register, discover, select, replace, activate, or execute capabilities;
 * - invoke CapabilitySelectionAuthority;
 * - invoke AuthorizationAuthority;
 * - invoke Executive readiness;
 * - create an ExecutionRequest;
 * - invoke ExecutionAuthority;
 * - perform local or remote execution;
 * - establish Android permission;
 * - establish constitutional authorization;
 * - invoke a provider or model;
 * - perform inference;
 * - create or send prompts;
 * - define or execute provider-specific function calling;
 * - create JSON tool schemas or provider SDK bindings;
 * - establish a tool result;
 * - establish Observation, Verification, Outcome, or verified reality;
 * - accept model output as truth;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 239 Structured Reasoning Integration;
 * - or implement Stages 240 through 243.
 *
 * CAPABILITY != PROVIDER.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 * MODEL != EXECUTION_AUTHORITY.
 * TOOL_USE_PREPARED != CAPABILITY_SELECTED.
 * TOOL_USE_PREPARED != AUTHORIZATION.
 * TOOL_USE_PREPARED != EXECUTION_REQUEST.
 * TOOL_USE_PREPARED != CAPABILITY_EXECUTED.
 * TOOL_USE_PREPARED != MODEL_INVOKED.
 * TOOL_USE_PREPARED != INFERENCE_PERFORMED.
 * TOOL_USE_PREPARED != TOOL_RESULT.
 * TOOL_USE_PREPARED != VERIFIED_OUTCOME.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class ToolUsingIntelligenceResult private constructor(
    val status: ToolUsingIntelligenceStatus,
    val routing: ModelRoutingResult,
    val capability: CapabilityContract?,
    val toolUseIntentDescription: String?,
) {
    companion object {

        fun create(
            status: ToolUsingIntelligenceStatus,
            routing: ModelRoutingResult,
            capability: CapabilityContract? = null,
            toolUseIntentDescription: String? = null,
        ): ToolUsingIntelligenceResult {
            return when (status) {
                ToolUsingIntelligenceStatus.PREPARED -> {
                    require(routing.status == ModelRoutingStatus.ROUTED) {
                        "Prepared Stage 238 Tool-Using Intelligence requires routed Stage 235 Model Routing."
                    }

                    val preservedCapability =
                        requireNotNull(capability) {
                            "Prepared Stage 238 Tool-Using Intelligence requires one existing Devil capability contract."
                        }

                    val normalizedIntent =
                        requireNotNull(toolUseIntentDescription)
                            .trim()

                    require(normalizedIntent.isNotEmpty()) {
                        "Stage 238 tool-use intent description must not be blank."
                    }

                    ToolUsingIntelligenceResult(
                        status = status,
                        routing = routing,
                        capability = preservedCapability,
                        toolUseIntentDescription = normalizedIntent,
                    )
                }

                ToolUsingIntelligenceStatus.DEFERRED -> {
                    require(capability == null) {
                        "Deferred Stage 238 Tool-Using Intelligence must not contain a capability."
                    }

                    require(toolUseIntentDescription == null) {
                        "Deferred Stage 238 Tool-Using Intelligence must not contain tool-use intent metadata."
                    }

                    ToolUsingIntelligenceResult(
                        status = status,
                        routing = routing,
                        capability = null,
                        toolUseIntentDescription = null,
                    )
                }
            }
        }
    }
}
