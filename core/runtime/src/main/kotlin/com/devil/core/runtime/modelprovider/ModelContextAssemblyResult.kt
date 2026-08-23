package com.devil.core.runtime.modelprovider

/**
 * Stage 240 bounded Model Context Assembly result.
 *
 * ASSEMBLED preserves:
 *
 * - one exact INTEGRATED Stage 239 Structured Reasoning result;
 * - therefore the exact Stage 238 Tool-Using Intelligence context transitively;
 * - the exact Stage 235 Model Routing result transitively;
 * - the exact Stage 234 provider architecture and provider provenance transitively;
 * - the exact existing Devil CapabilityContract transitively;
 * - one normalized explicitly supplied model-context objective;
 * - one normalized explicitly supplied assembled-context description.
 *
 * DEFERRED preserves the exact upstream Stage 239 result and contains no
 * Stage 240 model-context metadata.
 *
 * Stage 240 does not:
 *
 * - create another Devil intelligence, Brain, Constitution, Executive, Planner,
 *   Unified Devil Runtime, Memory Authority, Security Authority, Decision Authority,
 *   or Execution Authority;
 * - replace, modify, reinterpret, or become ContextEnvelope;
 * - create constitutional context;
 * - read, recall, commit, persist, or expose Memory;
 * - query or mutate World Model state;
 * - reinterpret Understanding;
 * - create or select a Decision;
 * - create a Task or Plan;
 * - select, authorize, activate, or execute a capability;
 * - create an ExecutionRequest;
 * - invoke tools;
 * - generate or send a provider-specific prompt;
 * - manage token budgets or provider context windows;
 * - invoke local or cloud models;
 * - perform inference;
 * - establish model output;
 * - establish Observation, Verification, Outcome, or verified reality;
 * - implement Stage 241 Model Output Interpretation;
 * - or implement Stages 242 through 243.
 *
 * MODEL_CONTEXT_ASSEMBLED != CONTEXT_ENVELOPE.
 * MODEL_CONTEXT_ASSEMBLED != CONSTITUTIONAL_CONTEXT.
 * MODEL_CONTEXT_ASSEMBLED != MEMORY_RECALL.
 * MODEL_CONTEXT_ASSEMBLED != WORLD_MODEL_QUERY.
 * MODEL_CONTEXT_ASSEMBLED != AUTHORIZATION.
 * MODEL_CONTEXT_ASSEMBLED != BRAIN_DECISION.
 * MODEL_CONTEXT_ASSEMBLED != PROMPT_SENT.
 * MODEL_CONTEXT_ASSEMBLED != MODEL_INVOKED.
 * MODEL_CONTEXT_ASSEMBLED != INFERENCE_PERFORMED.
 * MODEL_CONTEXT_ASSEMBLED != MODEL_OUTPUT.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class ModelContextAssemblyResult private constructor(
    val status: ModelContextAssemblyStatus,
    val structuredReasoning: StructuredReasoningIntegrationResult,
    val modelContextObjective: String?,
    val assembledContextDescription: String?,
) {
    companion object {

        fun create(
            status: ModelContextAssemblyStatus,
            structuredReasoning: StructuredReasoningIntegrationResult,
            modelContextObjective: String? = null,
            assembledContextDescription: String? = null,
        ): ModelContextAssemblyResult {
            return when (status) {
                ModelContextAssemblyStatus.ASSEMBLED -> {
                    require(
                        structuredReasoning.status ==
                            StructuredReasoningIntegrationStatus.INTEGRATED,
                    ) {
                        "Assembled Stage 240 Model Context requires integrated Stage 239 Structured Reasoning."
                    }

                    val normalizedObjective =
                        requireNotNull(modelContextObjective)
                            .trim()

                    val normalizedDescription =
                        requireNotNull(assembledContextDescription)
                            .trim()

                    require(normalizedObjective.isNotEmpty()) {
                        "Stage 240 model-context objective must not be blank."
                    }

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 240 assembled-context description must not be blank."
                    }

                    ModelContextAssemblyResult(
                        status = status,
                        structuredReasoning = structuredReasoning,
                        modelContextObjective = normalizedObjective,
                        assembledContextDescription = normalizedDescription,
                    )
                }

                ModelContextAssemblyStatus.DEFERRED -> {
                    require(modelContextObjective == null) {
                        "Deferred Stage 240 Model Context Assembly must not contain model-context objective metadata."
                    }

                    require(assembledContextDescription == null) {
                        "Deferred Stage 240 Model Context Assembly must not contain assembled-context metadata."
                    }

                    ModelContextAssemblyResult(
                        status = status,
                        structuredReasoning = structuredReasoning,
                        modelContextObjective = null,
                        assembledContextDescription = null,
                    )
                }
            }
        }
    }
}
