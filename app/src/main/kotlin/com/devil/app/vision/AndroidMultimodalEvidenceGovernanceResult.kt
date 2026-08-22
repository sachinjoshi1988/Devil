package com.devil.app.vision

/**
 * Stage 212 bounded Multimodal Evidence Governance result.
 *
 * GOVERNED preserves the exact available Stage 210 Voice + Vision Interaction
 * result and the exact available Stage 211 Educational Vision result.
 *
 * DEFERRED preserves both exact upstream results without promoting either into
 * constitutional Observation, Verification, trusted reality, or Outcome.
 *
 * MULTIMODAL_EVIDENCE_GOVERNED != CONSTITUTIONAL_OBSERVATION.
 * MULTIMODAL_EVIDENCE_GOVERNED != CONSTITUTIONAL_VERIFICATION.
 * MULTIMODAL_CONTEXT != VERIFIED_REALITY.
 * VOICE_TRANSCRIPT != VISUAL_EVIDENCE.
 * VISUAL_DESCRIPTION != VERIFIED_PIXELS.
 * ACCESSIBILITY_METADATA != VERIFIED_PIXELS.
 * EDUCATIONAL_CONTEXT != VERIFIED_CORRECTNESS.
 * GOVERNED_PROVENANCE != SOURCE_TRUST.
 * GOVERNED_MULTIMODAL_CONTEXT != OUTCOME.
 */
@ConsistentCopyVisibility
data class AndroidMultimodalEvidenceGovernanceResult private constructor(
    val status: AndroidMultimodalEvidenceGovernanceStatus,
    val voiceVisionInteraction: AndroidVoiceVisionInteractionResult,
    val educationalVision: AndroidEducationalVisionResult,
) {
    companion object {
        fun create(
            status: AndroidMultimodalEvidenceGovernanceStatus,
            voiceVisionInteraction: AndroidVoiceVisionInteractionResult,
            educationalVision: AndroidEducationalVisionResult,
        ): AndroidMultimodalEvidenceGovernanceResult {
            when (status) {
                AndroidMultimodalEvidenceGovernanceStatus.GOVERNED -> {
                    require(
                        voiceVisionInteraction.status ==
                            AndroidVoiceVisionInteractionStatus.AVAILABLE,
                    ) {
                        "Governed Stage 212 multimodal evidence requires available Stage 210 Voice + Vision Interaction."
                    }

                    require(
                        educationalVision.status ==
                            AndroidEducationalVisionStatus.AVAILABLE,
                    ) {
                        "Governed Stage 212 multimodal evidence requires available Stage 211 Educational Vision."
                    }
                }

                AndroidMultimodalEvidenceGovernanceStatus.DEFERRED -> Unit
            }

            return AndroidMultimodalEvidenceGovernanceResult(
                status = status,
                voiceVisionInteraction = voiceVisionInteraction,
                educationalVision = educationalVision,
            )
        }
    }
}
