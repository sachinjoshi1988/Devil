package com.devil.core.runtime.modelprovider

/**
 * Stage 242 bounded Model Output Verification result.
 *
 * VERIFIED_FOR_REVIEW preserves:
 *
 * - one exact INTERPRETED Stage 241 Model Output Interpretation result;
 * - therefore the exact Stage 240 Model Context Assembly result transitively;
 * - the exact Stage 239 Structured Reasoning result transitively;
 * - the exact Stage 238 Tool-Using Intelligence context transitively;
 * - the exact Stage 235 Model Routing result transitively;
 * - the exact Stage 234 provider architecture and provider provenance transitively;
 * - the exact existing Devil CapabilityContract transitively;
 * - one normalized explicitly supplied verification-basis description;
 * - one normalized explicitly supplied verification-assessment description.
 *
 * DEFERRED preserves the exact upstream Stage 241 result and contains no
 * Stage 242 verification metadata.
 *
 * Stage 242 does not:
 *
 * - create another Devil intelligence, Brain, Constitution, Executive, Planner,
 *   Unified Devil Runtime, Memory Authority, Security Authority, Decision Authority,
 *   Understanding Authority, Observation Authority, Verification Authority,
 *   Outcome Authority, or Execution Authority;
 * - invoke, replace, bypass, or imitate constitutional VerificationAuthority;
 * - create VerificationRequest or VerificationEvidence;
 * - establish constitutional Verification;
 * - establish that model output is factually correct;
 * - establish verified truth or verified reality;
 * - establish Observation or Outcome;
 * - reinterpret constitutional Understanding;
 * - create or select a Decision;
 * - create a Task or Plan;
 * - grant authorization;
 * - select, authorize, activate, or execute capabilities;
 * - create an ExecutionRequest;
 * - invoke tools;
 * - invoke providers or models;
 * - perform inference;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 243.
 *
 * MODEL_OUTPUT_VERIFICATION != VERIFIED_TRUTH.
 * MODEL_OUTPUT_VERIFICATION != CONSTITUTIONAL_VERIFICATION.
 * MODEL_OUTPUT_VERIFICATION != VERIFICATION_AUTHORITY_RESULT.
 * MODEL_OUTPUT_VERIFICATION != VERIFICATION_EVIDENCE.
 * MODEL_OUTPUT_VERIFICATION != OBSERVATION.
 * MODEL_OUTPUT_VERIFICATION != OUTCOME.
 * MODEL_OUTPUT_VERIFICATION != WORLD_MODEL_UPDATE.
 * MODEL_OUTPUT_VERIFICATION != LEARNING.
 * MODEL_OUTPUT_VERIFICATION != MEMORY.
 * MODEL_OUTPUT_INTERPRETED != VERIFIED_TRUTH.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class ModelOutputVerificationResult private constructor(
    val status: ModelOutputVerificationStatus,
    val interpretation: ModelOutputInterpretationResult,
    val verificationBasisDescription: String?,
    val verificationAssessmentDescription: String?,
) {
    companion object {

        fun create(
            status: ModelOutputVerificationStatus,
            interpretation: ModelOutputInterpretationResult,
            verificationBasisDescription: String? = null,
            verificationAssessmentDescription: String? = null,
        ): ModelOutputVerificationResult {
            return when (status) {
                ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW -> {
                    require(
                        interpretation.status ==
                            ModelOutputInterpretationStatus.INTERPRETED,
                    ) {
                        "Stage 242 Model Output Verification requires interpreted Stage 241 Model Output."
                    }

                    val normalizedBasis =
                        requireNotNull(verificationBasisDescription)
                            .trim()

                    val normalizedAssessment =
                        requireNotNull(verificationAssessmentDescription)
                            .trim()

                    require(normalizedBasis.isNotEmpty()) {
                        "Stage 242 verification-basis description must not be blank."
                    }

                    require(normalizedAssessment.isNotEmpty()) {
                        "Stage 242 verification-assessment description must not be blank."
                    }

                    ModelOutputVerificationResult(
                        status = status,
                        interpretation = interpretation,
                        verificationBasisDescription = normalizedBasis,
                        verificationAssessmentDescription = normalizedAssessment,
                    )
                }

                ModelOutputVerificationStatus.DEFERRED -> {
                    require(verificationBasisDescription == null) {
                        "Deferred Stage 242 Model Output Verification must not contain verification-basis metadata."
                    }

                    require(verificationAssessmentDescription == null) {
                        "Deferred Stage 242 Model Output Verification must not contain verification-assessment metadata."
                    }

                    ModelOutputVerificationResult(
                        status = status,
                        interpretation = interpretation,
                        verificationBasisDescription = null,
                        verificationAssessmentDescription = null,
                    )
                }
            }
        }
    }
}
