package com.devil.core.runtime.modelprovider

/**
 * Stage 242 bounded Model Output Verification status.
 *
 * VERIFIED_FOR_REVIEW means one exact INTERPRETED Stage 241 Model Output
 * Interpretation result has been associated with explicitly supplied bounded
 * verification metadata.
 *
 * DEFERRED means Stage 242 cannot truthfully establish the bounded
 * model-output verification boundary.
 *
 * Stage 242 verification is structural verification preparation only.
 *
 * MODEL_OUTPUT_VERIFICATION != VERIFIED_TRUTH.
 * MODEL_OUTPUT_VERIFICATION != CONSTITUTIONAL_VERIFICATION.
 * MODEL_OUTPUT_VERIFICATION != VERIFICATION_AUTHORITY_RESULT.
 * MODEL_OUTPUT_VERIFICATION != OBSERVATION.
 * MODEL_OUTPUT_VERIFICATION != OUTCOME.
 * MODEL_OUTPUT_VERIFICATION != WORLD_MODEL_UPDATE.
 * MODEL_OUTPUT_VERIFICATION != LEARNING.
 * MODEL_OUTPUT_VERIFICATION != MEMORY.
 * MODEL_OUTPUT_INTERPRETED != VERIFIED_TRUTH.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
enum class ModelOutputVerificationStatus {
    VERIFIED_FOR_REVIEW,
    DEFERRED,
}
