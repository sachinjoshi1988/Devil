package com.devil.core.runtime.modelprovider

/**
 * Stage 239 bounded Structured Reasoning Integration result.
 *
 * INTEGRATED preserves:
 *
 * - one exact PREPARED Stage 238 Tool-Using Intelligence result;
 * - therefore the exact Stage 235 routing result;
 * - the exact Stage 234 provider architecture and provider provenance transitively;
 * - the exact existing Devil CapabilityContract transitively;
 * - one normalized explicitly supplied reasoning objective;
 * - one normalized explicitly supplied structured-reasoning description.
 *
 * DEFERRED preserves the exact upstream Stage 238 result but contains no Stage 239
 * reasoning metadata.
 *
 * Stage 239 does not:
 *
 * - create another Devil intelligence;
 * - create another Brain, Constitution, Executive, Planner, Unified Devil Runtime,
 *   Memory Authority, Security Authority, Decision Authority, or Execution Authority;
 * - replace or invoke constitutional DecisionAuthority;
 * - create or select a DecisionRecord;
 * - reinterpret constitutional Understanding;
 * - implement Devil's Brain reasoning pipeline;
 * - claim Situation Assessment, Hypothesis Generation, Evidence Collection,
 *   Conflict Detection, Reasoning Evaluation, Decision Selection, Confidence Review,
 *   or Brain Decision has constitutionally occurred;
 * - grant authorization;
 * - register, select, activate, or execute capabilities;
 * - create an ExecutionRequest;
 * - invoke tools;
 * - invoke a provider or model;
 * - perform inference;
 * - create or expose hidden chain-of-thought;
 * - establish model output;
 * - establish Observation, Verification, Outcome, or verified reality;
 * - treat supplied reasoning metadata as evidence or verified truth;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 240 Context Assembly;
 * - or implement Stages 241 through 243.
 *
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 * STRUCTURED_REASONING_INTEGRATED != BRAIN_DECISION.
 * STRUCTURED_REASONING_INTEGRATED != DECISION_SELECTED.
 * STRUCTURED_REASONING_INTEGRATED != UNDERSTANDING.
 * STRUCTURED_REASONING_INTEGRATED != AUTHORIZATION.
 * STRUCTURED_REASONING_INTEGRATED != MODEL_INVOKED.
 * STRUCTURED_REASONING_INTEGRATED != INFERENCE_PERFORMED.
 * STRUCTURED_REASONING_INTEGRATED != TOOL_EXECUTED.
 * STRUCTURED_REASONING_INTEGRATED != VERIFICATION.
 * STRUCTURED_REASONING_INTEGRATED != VERIFIED_TRUTH.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class StructuredReasoningIntegrationResult private constructor(
    val status: StructuredReasoningIntegrationStatus,
    val toolUsingIntelligence: ToolUsingIntelligenceResult,
    val reasoningObjective: String?,
    val structuredReasoningDescription: String?,
) {
    companion object {

        fun create(
            status: StructuredReasoningIntegrationStatus,
            toolUsingIntelligence: ToolUsingIntelligenceResult,
            reasoningObjective: String? = null,
            structuredReasoningDescription: String? = null,
        ): StructuredReasoningIntegrationResult {
            return when (status) {
                StructuredReasoningIntegrationStatus.INTEGRATED -> {
                    require(
                        toolUsingIntelligence.status ==
                            ToolUsingIntelligenceStatus.PREPARED,
                    ) {
                        "Integrated Stage 239 Structured Reasoning requires prepared Stage 238 Tool-Using Intelligence."
                    }

                    requireNotNull(toolUsingIntelligence.capability) {
                        "Integrated Stage 239 Structured Reasoning requires the exact capability preserved by Stage 238."
                    }

                    val normalizedObjective =
                        requireNotNull(reasoningObjective)
                            .trim()

                    val normalizedDescription =
                        requireNotNull(structuredReasoningDescription)
                            .trim()

                    require(normalizedObjective.isNotEmpty()) {
                        "Stage 239 reasoning objective must not be blank."
                    }

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 239 structured-reasoning description must not be blank."
                    }

                    StructuredReasoningIntegrationResult(
                        status = status,
                        toolUsingIntelligence = toolUsingIntelligence,
                        reasoningObjective = normalizedObjective,
                        structuredReasoningDescription = normalizedDescription,
                    )
                }

                StructuredReasoningIntegrationStatus.DEFERRED -> {
                    require(reasoningObjective == null) {
                        "Deferred Stage 239 Structured Reasoning must not contain reasoning objective metadata."
                    }

                    require(structuredReasoningDescription == null) {
                        "Deferred Stage 239 Structured Reasoning must not contain structured-reasoning metadata."
                    }

                    StructuredReasoningIntegrationResult(
                        status = status,
                        toolUsingIntelligence = toolUsingIntelligence,
                        reasoningObjective = null,
                        structuredReasoningDescription = null,
                    )
                }
            }
        }
    }
}
