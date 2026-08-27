package com.devil.app.modelprovider.conversation

/**
 * Stage 313 result of resolving Android conversational-model configuration.
 *
 * AVAILABLE contains exactly one explicitly supplied configuration.
 * UNAVAILABLE contains no fabricated or default configuration.
 */
@ConsistentCopyVisibility
data class ConversationalModelConfigurationResult private constructor(
    val status: ConversationalModelConfigurationStatus,
    val configuration: ConversationalModelConfiguration?,
) {
    companion object {

        fun available(
            configuration: ConversationalModelConfiguration,
        ): ConversationalModelConfigurationResult {
            return ConversationalModelConfigurationResult(
                status = ConversationalModelConfigurationStatus.AVAILABLE,
                configuration = configuration,
            )
        }

        fun unavailable(): ConversationalModelConfigurationResult {
            return ConversationalModelConfigurationResult(
                status = ConversationalModelConfigurationStatus.UNAVAILABLE,
                configuration = null,
            )
        }
    }
}
