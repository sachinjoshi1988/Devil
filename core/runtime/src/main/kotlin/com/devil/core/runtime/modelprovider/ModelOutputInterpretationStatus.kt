package com.devil.core.runtime.modelprovider

/**
 * Stage 241 bounded Model Output Interpretation status.
 *
 * INTERPRETED means one exact ASSEMBLED Stage 240 Model Context Assembly result
 * has been associated with explicitly supplied bounded model-output and
 * interpretation metadata.
 *
 * DEFERRED means Stage 241 cannot truthfully establish bounded model-output
 * interpretation.
 *
 * Stage 241 represents supplied model output structurally only.
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
enum class ModelOutputInterpretationStatus {
    INTERPRETED,
    DEFERRED,
}
