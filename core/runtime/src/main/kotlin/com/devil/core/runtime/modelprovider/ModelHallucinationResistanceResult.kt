package com.devil.core.runtime.modelprovider

/**
 * Stage 243A bounded Hallucination Resistance result.
 *
 * ASSESSED preserves:
 *
 * - one exact VERIFIED_FOR_REVIEW Stage 242 Model Output Verification result;
 * - therefore the exact Stage 241 Model Output Interpretation result transitively;
 * - the exact Stage 240 Model Context Assembly result transitively;
 * - the exact Stage 239 Structured Reasoning result transitively;
 * - the exact Stage 238 Tool-Using Intelligence context transitively;
 * - the exact Stage 235 Model Routing result transitively;
 * - the exact Stage 234 provider architecture and provider provenance transitively;
 * - the exact existing Devil CapabilityContract transitively;
 * - one normalized explicitly supplied hallucination-resistance basis;
 * - one normalized explicitly supplied hallucination-resistance assessment.
 *
 * DEFERRED preserves the exact upstream Stage 242 result and contains no
 * Stage 243A hallucination-resistance metadata.
 *
 * Stage 243A does not:
 *
 * - establish factual correctness;
 * - establish verified truth or verified reality;
 * - invoke, replace, bypass, or imitate constitutional VerificationAuthority;
 * - create VerificationRequest or VerificationEvidence;
 * - establish constitutional Verification;
 * - reinterpret or replace constitutional Understanding;
 * - create or select a Brain Decision;
 * - create a Task or Plan;
 * - grant authorization;
 * - create an ExecutionRequest;
 * - select, authorize, activate, or execute a capability;
 * - invoke tools;
 * - invoke providers or models;
 * - perform inference;
 * - create Observation or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 243B AI Failure Recovery;
 * - implement Stage 243C Model Independence Validation;
 * - implement Stage 244 Personality Foundation V2.
 *
 * HALLUCINATION_RESISTANCE_ASSESSED != VERIFIED_TRUTH.
 * HALLUCINATION_RESISTANCE_ASSESSED != CONSTITUTIONAL_VERIFICATION.
 * HALLUCINATION_RESISTANCE_ASSESSED != VERIFICATION_AUTHORITY_RESULT.
 * HALLUCINATION_RESISTANCE_ASSESSED != VERIFICATION_EVIDENCE.
 * HALLUCINATION_RESISTANCE_ASSESSED != CONSTITUTIONAL_UNDERSTANDING.
 * HALLUCINATION_RESISTANCE_ASSESSED != BRAIN_DECISION.
 * HALLUCINATION_RESISTANCE_ASSESSED != AUTHORIZATION.
 * HALLUCINATION_RESISTANCE_ASSESSED != OBSERVATION.
 * HALLUCINATION_RESISTANCE_ASSESSED != OUTCOME.
 * HALLUCINATION_RESISTANCE_ASSESSED != WORLD_MODEL_UPDATE.
 * HALLUCINATION_RESISTANCE_ASSESSED != LEARNING.
 * HALLUCINATION_RESISTANCE_ASSESSED != MEMORY.
 * MODEL_OUTPUT_VERIFICATION != VERIFIED_TRUTH.
 * MODEL_OUTPUT_INTERPRETED != VERIFIED_TRUTH.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 */
@ConsistentCopyVisibility
data class ModelHallucinationResistanceResult private constructor(
    val status: ModelHallucinationResistanceStatus,
    val modelOutputVerification: ModelOutputVerificationResult,
    val resistanceBasisDescription: String?,
    val resistanceAssessmentDescription: String?,
) {
    companion object {

        fun create(
            status: ModelHallucinationResistanceStatus,
            modelOutputVerification: ModelOutputVerificationResult,
            resistanceBasisDescription: String? = null,
            resistanceAssessmentDescription: String? = null,
        ): ModelHallucinationResistanceResult {
            return when (status) {
                ModelHallucinationResistanceStatus.ASSESSED -> {
                    require(
                        modelOutputVerification.status ==
                            ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW,
                    ) {
                        "Stage 243A Hallucination Resistance requires VERIFIED_FOR_REVIEW Stage 242 Model Output Verification."
                    }

                    val normalizedBasis =
                        requireNotNull(resistanceBasisDescription)
                            .trim()

                    val normalizedAssessment =
                        requireNotNull(resistanceAssessmentDescription)
                            .trim()

                    require(normalizedBasis.isNotEmpty()) {
                        "Stage 243A hallucination-resistance basis must not be blank."
                    }

                    require(normalizedAssessment.isNotEmpty()) {
                        "Stage 243A hallucination-resistance assessment must not be blank."
                    }

                    ModelHallucinationResistanceResult(
                        status = status,
                        modelOutputVerification = modelOutputVerification,
                        resistanceBasisDescription = normalizedBasis,
                        resistanceAssessmentDescription = normalizedAssessment,
                    )
                }

                ModelHallucinationResistanceStatus.DEFERRED -> {
                    require(resistanceBasisDescription == null) {
                        "Deferred Stage 243A Hallucination Resistance must not contain resistance-basis metadata."
                    }

                    require(resistanceAssessmentDescription == null) {
                        "Deferred Stage 243A Hallucination Resistance must not contain resistance-assessment metadata."
                    }

                    ModelHallucinationResistanceResult(
                        status = status,
                        modelOutputVerification = modelOutputVerification,
                        resistanceBasisDescription = null,
                        resistanceAssessmentDescription = null,
                    )
                }
            }
        }
    }
}
