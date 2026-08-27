package com.devil.app.modelprovider.conversation

/**
 * Stage 313 Android conversational-model configuration availability.
 *
 * AVAILABLE means all required configuration values were explicitly supplied.
 *
 * UNAVAILABLE means at least one required value was absent or blank.
 *
 * AVAILABLE != PROVIDER_AVAILABLE.
 * AVAILABLE != CREDENTIAL_VALID.
 * AVAILABLE != MODEL_INVOKED.
 * AVAILABLE != DEVIL_AUTHORIZATION.
 */
enum class ConversationalModelConfigurationStatus {
    AVAILABLE,
    UNAVAILABLE,
}
