package com.devil.core.runtime.modelprovider

/**
 * Stage 241 bounded Model Output Interpretation coordinator.
 *
 * It associates:
 *
 * - one exact Stage 240 Model Context Assembly result;
 * - one explicitly supplied raw model-output representation;
 * - one explicitly supplied bounded interpretation description.
 *
 * Stage 240 remains authoritative for exact model-context provenance.
 * Stage 241 preserves that exact upstream object rather than reconstructing it.
 *
 * This coordinator represents and interprets explicitly supplied model output
 * structurally only.
 *
 * It does not:
 *
 * - create or reinterpret constitutional Understanding;
 * - create or select a Decision;
 * - create a Task or Plan;
 * - grant authorization;
 * - select or execute capabilities;
 * - create an ExecutionRequest;
 * - invoke tools;
 * - generate or send prompts;
 * - invoke models or providers;
 * - perform inference;
 * - prove that supplied output originated from a model;
 * - establish provider availability or health;
 * - establish Observation, Verification, Outcome, or verified truth;
 * - mutate World Model state;
 * - perform Learning;
 * - create or persist Memory;
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
class ModelOutputInterpretationCoordinator {

    fun interpret(
        modelContext: ModelContextAssemblyResult,
        rawModelOutput: String?,
        interpretationDescription: String?,
    ): ModelOutputInterpretationResult {
        if (
            modelContext.status != ModelContextAssemblyStatus.ASSEMBLED ||
            rawModelOutput.isNullOrBlank() ||
            interpretationDescription.isNullOrBlank()
        ) {
            return ModelOutputInterpretationResult.create(
                status = ModelOutputInterpretationStatus.DEFERRED,
                modelContext = modelContext,
            )
        }

        return ModelOutputInterpretationResult.create(
            status = ModelOutputInterpretationStatus.INTERPRETED,
            modelContext = modelContext,
            rawModelOutput = rawModelOutput,
            interpretationDescription = interpretationDescription,
        )
    }
}
