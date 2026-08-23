package com.devil.core.runtime.modelprovider

/**
 * Stage 243 bounded Model Phase Integration result.
 *
 * INTEGRATED preserves:
 *
 * - one exact VERIFIED_FOR_REVIEW Stage 242 Model Output Verification result;
 * - therefore the exact Stage 241 Model Output Interpretation result transitively;
 * - the exact Stage 240 Model Context Assembly result transitively;
 * - the exact Stage 239 Structured Reasoning Integration result transitively;
 * - the exact Stage 238 Tool-Using Intelligence result transitively;
 * - the exact Stage 235 Model Routing result transitively;
 * - the exact Stage 234 provider architecture and provider provenance transitively;
 * - the exact existing Devil CapabilityContract transitively;
 * - one normalized explicitly supplied model-phase integration objective;
 * - one normalized explicitly supplied model-phase integration description.
 *
 * DEFERRED preserves the exact upstream Stage 242 result and contains no
 * Stage 243 integration metadata.
 *
 * Stage 243 closes the bounded Stage 234–243 model-provider phase structurally.
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain, Constitution, Executive, Planner, Unified Devil Runtime,
 *   Memory Authority, Security Authority, Decision Authority, Understanding Authority,
 *   Observation Authority, Verification Authority, Outcome Authority, Learning Authority,
 *   World Model Authority, or Execution Authority;
 * - reinterpret or replace constitutional Understanding;
 * - create or select a constitutional Decision;
 * - create a Task or Plan;
 * - grant authorization;
 * - register, select, authorize, activate, or execute capabilities;
 * - create an ExecutionRequest;
 * - invoke tools;
 * - generate or transmit prompts;
 * - establish provider availability, health, credentials, trust, or connectivity;
 * - invoke a local or cloud provider or model;
 * - perform inference;
 * - claim that supplied model output was genuinely provider-produced;
 * - promote Stage 241 interpretation into truth;
 * - promote Stage 242 verification metadata into constitutional Verification;
 * - establish Verification evidence or a Verification Authority result;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement personality, UI, performance, production-security, constitutional-validation,
 *   testing, alpha, beta, launch, or later-roadmap stages.
 *
 * Stage 242 remains authoritative for its own bounded model-output verification.
 * Stage 243 preserves the exact upstream result rather than reconstructing it.
 *
 * MODEL_PHASE_INTEGRATED != VERIFIED_TRUTH.
 * MODEL_PHASE_INTEGRATED != CONSTITUTIONAL_VERIFICATION.
 * MODEL_PHASE_INTEGRATED != VERIFICATION_AUTHORITY_RESULT.
 * MODEL_PHASE_INTEGRATED != VERIFICATION_EVIDENCE.
 * MODEL_PHASE_INTEGRATED != BRAIN_DECISION.
 * MODEL_PHASE_INTEGRATED != AUTHORIZATION.
 * MODEL_PHASE_INTEGRATED != EXECUTION.
 * MODEL_PHASE_INTEGRATED != OBSERVATION.
 * MODEL_PHASE_INTEGRATED != OUTCOME.
 * MODEL_PHASE_INTEGRATED != WORLD_MODEL_UPDATE.
 * MODEL_PHASE_INTEGRATED != LEARNING.
 * MODEL_PHASE_INTEGRATED != MEMORY.
 * MODEL_OUTPUT_VERIFICATION != VERIFIED_TRUTH.
 * MODEL_OUTPUT != VERIFIED_TRUTH.
 * MODEL != DEVIL.
 * MODEL != BRAIN.
 * MODEL != AUTHORITY.
 */
@ConsistentCopyVisibility
data class ModelPhaseIntegrationResult private constructor(
    val status: ModelPhaseIntegrationStatus,
    val modelOutputVerification: ModelOutputVerificationResult,
    val integrationObjective: String?,
    val integrationDescription: String?,
) {
    companion object {

        fun create(
            status: ModelPhaseIntegrationStatus,
            modelOutputVerification: ModelOutputVerificationResult,
            integrationObjective: String? = null,
            integrationDescription: String? = null,
        ): ModelPhaseIntegrationResult {
            return when (status) {
                ModelPhaseIntegrationStatus.INTEGRATED -> {
                    require(
                        modelOutputVerification.status ==
                            ModelOutputVerificationStatus.VERIFIED_FOR_REVIEW,
                    ) {
                        "Integrated Stage 243 Model Phase requires VERIFIED_FOR_REVIEW Stage 242 Model Output Verification."
                    }

                    val normalizedObjective =
                        requireNotNull(integrationObjective)
                            .trim()

                    val normalizedDescription =
                        requireNotNull(integrationDescription)
                            .trim()

                    require(normalizedObjective.isNotEmpty()) {
                        "Stage 243 model-phase integration objective must not be blank."
                    }

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 243 model-phase integration description must not be blank."
                    }

                    ModelPhaseIntegrationResult(
                        status = status,
                        modelOutputVerification = modelOutputVerification,
                        integrationObjective = normalizedObjective,
                        integrationDescription = normalizedDescription,
                    )
                }

                ModelPhaseIntegrationStatus.DEFERRED -> {
                    require(integrationObjective == null) {
                        "Deferred Stage 243 Model Phase Integration must not contain integration-objective metadata."
                    }

                    require(integrationDescription == null) {
                        "Deferred Stage 243 Model Phase Integration must not contain integration-description metadata."
                    }

                    ModelPhaseIntegrationResult(
                        status = status,
                        modelOutputVerification = modelOutputVerification,
                        integrationObjective = null,
                        integrationDescription = null,
                    )
                }
            }
        }
    }
}
