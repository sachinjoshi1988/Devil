package com.devil.core.runtime.modelprovider

/**
 * Stage 241 bounded Model Output Interpretation result.
 *
 * INTERPRETED preserves:
 *
 * - one exact ASSEMBLED Stage 240 Model Context Assembly result;
 * - therefore the exact Stage 239 Structured Reasoning result transitively;
 * - the exact Stage 238 Tool-Using Intelligence context transitively;
 * - the exact Stage 235 Model Routing result transitively;
 * - the exact Stage 234 provider architecture and provider provenance transitively;
 * - the exact existing Devil CapabilityContract transitively;
 * - one normalized explicitly supplied raw model-output representation;
 * - one normalized explicitly supplied bounded interpretation description.
 *
 * DEFERRED preserves the exact upstream Stage 240 result and contains no
 * Stage 241 model-output or interpretation metadata.
 *
 * Stage 241 does not:
 *
 * - create another Devil intelligence, Brain, Constitution, Executive, Planner,
 *   Unified Devil Runtime, Memory Authority, Security Authority, Decision Authority,
 *   Understanding Authority, Observation Authority, Verification Authority,
 *   Outcome Authority, or Execution Authority;
 * - replace, modify, reinterpret, or become constitutional Understanding;
 * - create or select a Decision;
 * - create a Task or Plan;
 * - grant authorization;
 * - select, authorize, activate, or execute capabilities;
 * - create an ExecutionRequest;
 * - invoke tools;
 * - generate or send prompts;
 * - invoke a local or cloud provider or model;
 * - perform inference;
 * - establish that supplied model output was genuinely produced by a provider;
 * - establish provider availability, health, credentials, trust, or connectivity;
 * - treat model output or its interpretation as evidence or verified truth;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 242;
 * - or implement Stage 243.
 *
 * MODEL_OUTPUT_INTERPRETED != VERIFIED_TRUTH.
 * MODEL_OUTPUT_INTERPRETED != CONSTITUTIONAL_UNDERSTANDING.
 * MODEL_OUTPUT_INTERPRETED != BRAIN_DECISION.
 * MODEL_OUTPUT_INTERPRETED != AUTHORIZATION.
 * MODEL_OUTPUT_INTERPRETED != OBSERVATION.
 * MODEL_OUTPUT_INTERPRETED != VERIFICATION.
 * MODEL_OUTPUT_INTERPRETED != OUTCOME.
 * MODEL_OUTPUT_INTERPRETED != WORLD_MODEL_UPDATE.
 * MODEL_OUTPUT_INTERPRETED != LEARNING.
 * MODEL_OUTPUT_INTERPRETED != MEMORY.
 * MODEL_OUTPUT_INTERPRETED != MODEL_INVOCATION.
 * MODEL_OUTPUT_INTERPRETED != INFERENCE_PERFORMED.
 */
@ConsistentCopyVisibility
data class ModelOutputInterpretationResult private constructor(
    val status: ModelOutputInterpretationStatus,
    val modelContext: ModelContextAssemblyResult,
    val rawModelOutput: String?,
    val interpretationDescription: String?,
) {
    companion object {

        fun create(
            status: ModelOutputInterpretationStatus,
            modelContext: ModelContextAssemblyResult,
            rawModelOutput: String? = null,
            interpretationDescription: String? = null,
        ): ModelOutputInterpretationResult {
            return when (status) {
                ModelOutputInterpretationStatus.INTERPRETED -> {
                    require(
                        modelContext.status ==
                            ModelContextAssemblyStatus.ASSEMBLED,
                    ) {
                        "Interpreted Stage 241 Model Output requires assembled Stage 240 Model Context."
                    }

                    val normalizedOutput =
                        requireNotNull(rawModelOutput)
                            .trim()

                    val normalizedInterpretation =
                        requireNotNull(interpretationDescription)
                            .trim()

                    require(normalizedOutput.isNotEmpty()) {
                        "Stage 241 raw model output must not be blank."
                    }

                    require(normalizedInterpretation.isNotEmpty()) {
                        "Stage 241 model-output interpretation description must not be blank."
                    }

                    ModelOutputInterpretationResult(
                        status = status,
                        modelContext = modelContext,
                        rawModelOutput = normalizedOutput,
                        interpretationDescription = normalizedInterpretation,
                    )
                }

                ModelOutputInterpretationStatus.DEFERRED -> {
                    require(rawModelOutput == null) {
                        "Deferred Stage 241 Model Output Interpretation must not contain model-output metadata."
                    }

                    require(interpretationDescription == null) {
                        "Deferred Stage 241 Model Output Interpretation must not contain interpretation metadata."
                    }

                    ModelOutputInterpretationResult(
                        status = status,
                        modelContext = modelContext,
                        rawModelOutput = null,
                        interpretationDescription = null,
                    )
                }
            }
        }
    }
}
