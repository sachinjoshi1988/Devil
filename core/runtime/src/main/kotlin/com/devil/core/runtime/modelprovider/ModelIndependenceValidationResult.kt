package com.devil.core.runtime.modelprovider

/**
 * Stage 243C bounded Model Independence Validation result.
 *
 * VALIDATED preserves:
 *
 * - one exact PREPARED Stage 243B AI Failure Recovery result;
 * - therefore the exact Stage 243A Hallucination Resistance result transitively;
 * - therefore the exact Stage 242 Model Output Verification result transitively;
 * - the exact prior model-phase provenance transitively;
 * - the original provider architecture and provider identity transitively;
 * - one exact AVAILABLE alternate provider architecture;
 * - one normalized explicitly supplied validation basis;
 * - one normalized explicitly supplied validation assessment.
 *
 * The alternate provider must have a provider identity different from the
 * provider already preserved by the Stage 243B provenance chain.
 *
 * DEFERRED preserves the exact Stage 243B result and alternate provider input
 * but contains no Stage 243C validation metadata.
 *
 * Stage 243C establishes structural provider independence only.
 *
 * It does not:
 *
 * - switch providers;
 * - modify routing;
 * - invoke any model or provider;
 * - perform inference;
 * - execute recovery;
 * - consume a recovery attempt;
 * - establish recovery success;
 * - establish behavioral or output equivalence between providers;
 * - establish factual correctness or verified truth;
 * - invoke or replace constitutional VerificationAuthority;
 * - reinterpret constitutional Understanding;
 * - create or select a Brain Decision;
 * - grant authorization;
 * - create a Task, Plan, or ExecutionRequest;
 * - execute a capability;
 * - create Observation or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 244 Personality Foundation V2.
 *
 * MODEL_INDEPENDENCE_VALIDATED != PROVIDER_SWITCHED.
 * MODEL_INDEPENDENCE_VALIDATED != MODEL_ROUTED.
 * MODEL_INDEPENDENCE_VALIDATED != MODEL_INVOKED.
 * MODEL_INDEPENDENCE_VALIDATED != PROVIDER_INVOKED.
 * MODEL_INDEPENDENCE_VALIDATED != INFERENCE_PERFORMED.
 * MODEL_INDEPENDENCE_VALIDATED != RECOVERY_SUCCESS.
 * MODEL_INDEPENDENCE_VALIDATED != VERIFIED_TRUTH.
 * MODEL_INDEPENDENCE_VALIDATED != CONSTITUTIONAL_VERIFICATION.
 * MODEL_INDEPENDENCE_VALIDATED != BRAIN_DECISION.
 * MODEL_INDEPENDENCE_VALIDATED != AUTHORIZATION.
 * MODEL_INDEPENDENCE_VALIDATED != EXECUTION.
 * MODEL_INDEPENDENCE_VALIDATED != OUTCOME.
 * MODEL_INDEPENDENCE_VALIDATED != WORLD_MODEL_UPDATE.
 * MODEL_INDEPENDENCE_VALIDATED != LEARNING.
 * MODEL_INDEPENDENCE_VALIDATED != MEMORY.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
@ConsistentCopyVisibility
data class ModelIndependenceValidationResult private constructor(
    val status: ModelIndependenceValidationStatus,
    val modelFailureRecovery: ModelFailureRecoveryResult,
    val alternateProviderArchitecture: ModelProviderArchitectureResult,
    val validationBasisDescription: String?,
    val validationAssessmentDescription: String?,
) {
    companion object {

        fun create(
            status: ModelIndependenceValidationStatus,
            modelFailureRecovery: ModelFailureRecoveryResult,
            alternateProviderArchitecture: ModelProviderArchitectureResult,
            validationBasisDescription: String? = null,
            validationAssessmentDescription: String? = null,
        ): ModelIndependenceValidationResult {
            return when (status) {
                ModelIndependenceValidationStatus.VALIDATED -> {
                    require(
                        modelFailureRecovery.status ==
                            ModelFailureRecoveryStatus.PREPARED,
                    ) {
                        "Stage 243C Model Independence Validation requires PREPARED Stage 243B AI Failure Recovery."
                    }

                    require(
                        alternateProviderArchitecture.status ==
                            ModelProviderArchitectureStatus.AVAILABLE,
                    ) {
                        "Stage 243C Model Independence Validation requires an AVAILABLE alternate provider architecture."
                    }

                    val originalProvider =
                        requireNotNull(
                            modelFailureRecovery
                                .hallucinationResistance
                                .modelOutputVerification
                                .interpretation
                                .modelContext
                                .structuredReasoning
                                .toolUsingIntelligence
                                .routing
                                .providerArchitecture
                                .provider,
                        ) {
                            "Stage 243C requires original provider provenance from the Stage 243B chain."
                        }

                    val alternateProvider =
                        requireNotNull(alternateProviderArchitecture.provider) {
                            "Stage 243C requires an alternate provider record."
                        }

                    require(
                        originalProvider.providerId != alternateProvider.providerId,
                    ) {
                        "Stage 243C alternate provider identity must differ from the original provider identity."
                    }

                    val normalizedBasis =
                        requireNotNull(validationBasisDescription).trim()

                    require(normalizedBasis.isNotEmpty()) {
                        "Stage 243C model-independence validation basis must not be blank."
                    }

                    val normalizedAssessment =
                        requireNotNull(validationAssessmentDescription).trim()

                    require(normalizedAssessment.isNotEmpty()) {
                        "Stage 243C model-independence validation assessment must not be blank."
                    }

                    ModelIndependenceValidationResult(
                        status = status,
                        modelFailureRecovery = modelFailureRecovery,
                        alternateProviderArchitecture =
                            alternateProviderArchitecture,
                        validationBasisDescription = normalizedBasis,
                        validationAssessmentDescription =
                            normalizedAssessment,
                    )
                }

                ModelIndependenceValidationStatus.DEFERRED -> {
                    require(validationBasisDescription == null) {
                        "Deferred Stage 243C Model Independence Validation must not contain validation-basis metadata."
                    }

                    require(validationAssessmentDescription == null) {
                        "Deferred Stage 243C Model Independence Validation must not contain validation-assessment metadata."
                    }

                    ModelIndependenceValidationResult(
                        status = status,
                        modelFailureRecovery = modelFailureRecovery,
                        alternateProviderArchitecture =
                            alternateProviderArchitecture,
                        validationBasisDescription = null,
                        validationAssessmentDescription = null,
                    )
                }
            }
        }
    }
}
