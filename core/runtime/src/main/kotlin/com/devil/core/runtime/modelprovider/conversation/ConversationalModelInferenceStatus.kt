package com.devil.core.runtime.modelprovider.conversation

/**
 * Stage 313 bounded conversational model-inference availability status.
 *
 * AVAILABLE means a provider adapter produced explicitly supplied generated
 * model output for the exact bounded inference request.
 *
 * UNAVAILABLE means no generated model output is currently available.
 *
 * FAILED means the bounded inference attempt failed and carries an error
 * description.
 *
 * AVAILABLE != VERIFIED_TRUTH.
 * AVAILABLE != CONSTITUTIONAL_VERIFICATION.
 * AVAILABLE != VERIFIED_OUTCOME.
 * AVAILABLE != DEVIL_AUTHORIZATION.
 * GENERATED_MODEL_OUTPUT != DEVIL_AUTHORITY.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
enum class ConversationalModelInferenceStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
