package com.devil.core.runtime.modelprovider

import com.devil.core.model.reliability.RecoveryAttemptBudget
import com.devil.core.model.reliability.RecoveryStrategy

/**
 * Stage 243B bounded AI Failure Recovery result.
 *
 * PREPARED preserves:
 *
 * - one exact ASSESSED Stage 243A Hallucination Resistance result;
 * - therefore the exact Stage 242 Model Output Verification result transitively;
 * - the exact Stage 241 Model Output Interpretation result transitively;
 * - the exact Stage 240 Model Context Assembly result transitively;
 * - the exact Stage 239 Structured Reasoning result transitively;
 * - the exact Stage 238 Tool-Using Intelligence context transitively;
 * - the exact Stage 235 Model Routing result transitively;
 * - the exact Stage 234 provider architecture and provider provenance transitively;
 * - the exact existing Devil CapabilityContract transitively;
 * - one explicitly supplied bounded RecoveryStrategy;
 * - one explicitly supplied finite RecoveryAttemptBudget;
 * - one normalized explicitly supplied recovery rationale.
 *
 * DEFERRED preserves the exact upstream Stage 243A result and contains no
 * Stage 243B recovery metadata.
 *
 * Stage 243B reuses the existing Stage 45 recovery vocabulary without becoming
 * another reliability authority or recovery executor.
 *
 * Stage 243B does not:
 *
 * - establish that a model failure actually occurred;
 * - fabricate RecoveryEvidence or ReliabilityAssessment;
 * - grant RecoveryDisposition.RECOVERY_ELIGIBLE;
 * - create or replace constitutional recovery authority;
 * - create a RecoveryRequest;
 * - consume a recovery attempt;
 * - retry an operation;
 * - restart or reinitialize a model;
 * - reconnect a provider;
 * - invoke a local or cloud model or provider;
 * - perform inference;
 * - generate replacement model output;
 * - establish that recovery succeeded;
 * - establish factual correctness or verified truth;
 * - invoke, replace, bypass, or imitate constitutional VerificationAuthority;
 * - create VerificationRequest or VerificationEvidence;
 * - reinterpret constitutional Understanding;
 * - create or select a Brain Decision;
 * - create a Task or Plan;
 * - grant authorization;
 * - create an ExecutionRequest;
 * - select, authorize, activate, or execute a capability;
 * - create Observation or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create, commit, persist, recall, or expose Memory;
 * - implement Stage 243C Model Independence Validation;
 * - implement Stage 244 Personality Foundation V2.
 *
 * MODEL_FAILURE_RECOVERY_PREPARED != RECOVERY_REQUEST.
 * MODEL_FAILURE_RECOVERY_PREPARED != RECOVERY_EXECUTED.
 * MODEL_FAILURE_RECOVERY_PREPARED != RETRY_STARTED.
 * MODEL_FAILURE_RECOVERY_PREPARED != MODEL_INVOKED.
 * MODEL_FAILURE_RECOVERY_PREPARED != PROVIDER_INVOKED.
 * MODEL_FAILURE_RECOVERY_PREPARED != INFERENCE_PERFORMED.
 * MODEL_FAILURE_RECOVERY_PREPARED != RECOVERY_SUCCESS.
 * MODEL_FAILURE_RECOVERY_PREPARED != VERIFIED_TRUTH.
 * MODEL_FAILURE_RECOVERY_PREPARED != CONSTITUTIONAL_VERIFICATION.
 * MODEL_FAILURE_RECOVERY_PREPARED != VERIFICATION_AUTHORITY_RESULT.
 * MODEL_FAILURE_RECOVERY_PREPARED != BRAIN_DECISION.
 * MODEL_FAILURE_RECOVERY_PREPARED != AUTHORIZATION.
 * MODEL_FAILURE_RECOVERY_PREPARED != EXECUTION.
 * MODEL_FAILURE_RECOVERY_PREPARED != OBSERVATION.
 * MODEL_FAILURE_RECOVERY_PREPARED != OUTCOME.
 * MODEL_FAILURE_RECOVERY_PREPARED != WORLD_MODEL_UPDATE.
 * MODEL_FAILURE_RECOVERY_PREPARED != LEARNING.
 * MODEL_FAILURE_RECOVERY_PREPARED != MEMORY.
 */
@ConsistentCopyVisibility
data class ModelFailureRecoveryResult private constructor(
    val status: ModelFailureRecoveryStatus,
    val hallucinationResistance: ModelHallucinationResistanceResult,
    val recoveryStrategy: RecoveryStrategy?,
    val attemptBudget: RecoveryAttemptBudget?,
    val recoveryRationale: String?,
) {
    companion object {

        fun create(
            status: ModelFailureRecoveryStatus,
            hallucinationResistance: ModelHallucinationResistanceResult,
            recoveryStrategy: RecoveryStrategy? = null,
            attemptBudget: RecoveryAttemptBudget? = null,
            recoveryRationale: String? = null,
        ): ModelFailureRecoveryResult {
            return when (status) {
                ModelFailureRecoveryStatus.PREPARED -> {
                    require(
                        hallucinationResistance.status ==
                            ModelHallucinationResistanceStatus.ASSESSED,
                    ) {
                        "Stage 243B AI Failure Recovery requires ASSESSED Stage 243A Hallucination Resistance."
                    }

                    val requiredStrategy =
                        requireNotNull(recoveryStrategy) {
                            "Stage 243B AI Failure Recovery requires a recovery strategy."
                        }

                    val requiredBudget =
                        requireNotNull(attemptBudget) {
                            "Stage 243B AI Failure Recovery requires a finite recovery-attempt budget."
                        }

                    require(!requiredBudget.exhausted) {
                        "Stage 243B AI Failure Recovery requires at least one remaining recovery attempt."
                    }

                    val normalizedRationale =
                        requireNotNull(recoveryRationale)
                            .trim()

                    require(normalizedRationale.isNotEmpty()) {
                        "Stage 243B AI-failure recovery rationale must not be blank."
                    }

                    ModelFailureRecoveryResult(
                        status = status,
                        hallucinationResistance = hallucinationResistance,
                        recoveryStrategy = requiredStrategy,
                        attemptBudget = requiredBudget,
                        recoveryRationale = normalizedRationale,
                    )
                }

                ModelFailureRecoveryStatus.DEFERRED -> {
                    require(recoveryStrategy == null) {
                        "Deferred Stage 243B AI Failure Recovery must not contain a recovery strategy."
                    }

                    require(attemptBudget == null) {
                        "Deferred Stage 243B AI Failure Recovery must not contain a recovery-attempt budget."
                    }

                    require(recoveryRationale == null) {
                        "Deferred Stage 243B AI Failure Recovery must not contain recovery-rationale metadata."
                    }

                    ModelFailureRecoveryResult(
                        status = status,
                        hallucinationResistance = hallucinationResistance,
                        recoveryStrategy = null,
                        attemptBudget = null,
                        recoveryRationale = null,
                    )
                }
            }
        }
    }
}
