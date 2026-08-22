package com.devil.app.voice

/**
 * Stage 204 bounded Voice Production Validation result.
 *
 * VALIDATED preserves one exact available Stage 195 Voice Architecture V2
 * result plus normalized explicit validation focus and evidence description.
 *
 * DEFERRED preserves no voice architecture or validation metadata.
 *
 * VOICE_PRODUCTION_VALIDATED != CONSTITUTIONAL_VERIFICATION.
 * VOICE_PRODUCTION_VALIDATED != SPEECH_EXECUTED.
 * VOICE_PRODUCTION_VALIDATED != OWNER_VOICE_VERIFIED.
 * VOICE_PRODUCTION_VALIDATED != REAL_DEVICE_VALIDATED.
 */
@ConsistentCopyVisibility
data class AndroidVoiceProductionValidationResult private constructor(
    val status: AndroidVoiceProductionValidationStatus,
    val voiceArchitecture: AndroidVoiceArchitectureV2Result?,
    val validationFocus: String?,
    val validationEvidenceDescription: String?,
) {
    companion object {
        fun create(
            status: AndroidVoiceProductionValidationStatus,
            voiceArchitecture: AndroidVoiceArchitectureV2Result? = null,
            validationFocus: String? = null,
            validationEvidenceDescription: String? = null,
        ): AndroidVoiceProductionValidationResult {
            return when (status) {
                AndroidVoiceProductionValidationStatus.VALIDATED -> {
                    val architecture =
                        requireNotNull(voiceArchitecture) {
                            "Validated Stage 204 voice production requires one Stage 195 voice architecture result."
                        }

                    require(
                        architecture.status ==
                            AndroidVoiceArchitectureV2Status.AVAILABLE,
                    ) {
                        "Validated Stage 204 voice production requires available Stage 195 voice architecture."
                    }

                    val normalizedFocus =
                        requireNotNull(validationFocus)
                            .trim()

                    val normalizedEvidence =
                        requireNotNull(validationEvidenceDescription)
                            .trim()

                    require(normalizedFocus.isNotEmpty()) {
                        "Stage 204 validation focus must not be blank."
                    }

                    require(normalizedEvidence.isNotEmpty()) {
                        "Stage 204 validation evidence description must not be blank."
                    }

                    AndroidVoiceProductionValidationResult(
                        status = status,
                        voiceArchitecture = architecture,
                        validationFocus = normalizedFocus,
                        validationEvidenceDescription = normalizedEvidence,
                    )
                }

                AndroidVoiceProductionValidationStatus.DEFERRED -> {
                    require(voiceArchitecture == null) {
                        "Deferred Stage 204 voice production validation must not contain voice architecture."
                    }

                    require(validationFocus == null) {
                        "Deferred Stage 204 voice production validation must not contain validation focus."
                    }

                    require(validationEvidenceDescription == null) {
                        "Deferred Stage 204 voice production validation must not contain validation evidence."
                    }

                    AndroidVoiceProductionValidationResult(
                        status = status,
                        voiceArchitecture = null,
                        validationFocus = null,
                        validationEvidenceDescription = null,
                    )
                }
            }
        }
    }
}
