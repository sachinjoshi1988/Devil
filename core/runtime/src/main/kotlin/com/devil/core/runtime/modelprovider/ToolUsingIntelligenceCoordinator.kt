package com.devil.core.runtime.modelprovider

import com.devil.core.model.capability.CapabilityContract

/**
 * Stage 238 bounded Tool-Using Intelligence coordinator.
 *
 * It associates:
 *
 * - one exact Stage 235 Model Routing result;
 * - one exact already-existing Devil CapabilityContract;
 * - one explicitly supplied bounded tool-use intent description.
 *
 * The existing Devil capability architecture remains authoritative for capability
 * identity, selection, authorization, readiness, and execution.
 *
 * This coordinator does not select or execute the supplied capability and does not
 * grant a model any constitutional authority.
 *
 * It does not:
 *
 * - create another Devil intelligence, Brain, Constitution, Executive, Planner,
 *   Unified Devil Runtime, Memory Authority, Security Authority, or Execution Authority;
 * - register, discover, rank, select, replace, activate, or execute capabilities;
 * - invoke CapabilitySelectionAuthority;
 * - grant authorization;
 * - create an ExecutionRequest;
 * - invoke ExecutionAuthority;
 * - invoke local or cloud models;
 * - perform inference;
 * - create provider-specific function-calling architecture;
 * - establish tool execution or tool results;
 * - establish Observation, Verification, Outcome, or verified reality;
 * - mutate World Model state;
 * - perform Learning;
 * - create or persist Memory;
 * - implement Stage 239 Structured Reasoning Integration;
 * - or implement Stages 240 through 243.
 *
 * CAPABILITY != PROVIDER.
 * MODEL != DEVIL.
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
 */
class ToolUsingIntelligenceCoordinator {

    fun prepare(
        routing: ModelRoutingResult,
        capability: CapabilityContract?,
        toolUseIntentDescription: String?,
    ): ToolUsingIntelligenceResult {
        if (
            routing.status != ModelRoutingStatus.ROUTED ||
            capability == null ||
            toolUseIntentDescription.isNullOrBlank()
        ) {
            return ToolUsingIntelligenceResult.create(
                status = ToolUsingIntelligenceStatus.DEFERRED,
                routing = routing,
            )
        }

        return ToolUsingIntelligenceResult.create(
            status = ToolUsingIntelligenceStatus.PREPARED,
            routing = routing,
            capability = capability,
            toolUseIntentDescription = toolUseIntentDescription,
        )
    }
}
