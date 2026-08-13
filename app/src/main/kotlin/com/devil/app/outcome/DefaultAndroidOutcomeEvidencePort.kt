package com.devil.app.outcome

import com.devil.app.verification.AndroidVerificationEvidence
import com.devil.app.verification.AndroidVerificationResult
import com.devil.app.verification.AndroidVerificationStatus
import com.devil.core.runtime.outcome.OutcomeEvidencePort
import com.devil.core.runtime.outcome.OutcomeEvidenceResult
import com.devil.core.runtime.outcome.OutcomeEvidenceStatus
import com.devil.core.runtime.verification.VerificationEvidenceResult
import com.devil.core.runtime.verification.VerificationEvidenceStatus
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.verification.VerificationStatus

/**
 * Android implementation of the neutral constitutional OutcomeEvidencePort.
 *
 * This boundary is reached only after the single Unified Devil Runtime has
 * produced both one constitutional VerificationResult and its genuine
 * VerificationEvidenceResult.
 *
 * VERIFIED is necessary but remains insufficient for outcome evidence.
 *
 * A genuine constitutional VERIFIED result is translated into the existing
 * bounded AndroidVerificationResult shape using only the preserved trace,
 * capability identity, and genuine verification-evidence description.
 *
 * The existing AndroidOutcomeAdapter is then allowed to approach its bounded
 * Android outcome source.
 *
 * Android outcome evidence becomes neutral constitutional outcome evidence only
 * when trace identity and capability identity remain consistent.
 *
 * This port grants no authority, establishes no task or plan completion,
 * updates no World Model state, performs no Learning, commits no Memory, and
 * creates no alternate runtime, Brain, Executive, Planner, Security Authority,
 * Verification Authority, or Outcome Authority.
 *
 * VERIFIED != OUTCOME_EVIDENCE.
 * OUTCOME_EVIDENCE != OUTCOME.
 * OUTCOME != COMPLETED.
 */
class DefaultAndroidOutcomeEvidencePort(
    private val outcomeAdapter: AndroidOutcomeAdapter,
) : OutcomeEvidencePort {

    override fun establish(
        verification: VerificationResult,
        verificationEvidence: VerificationEvidenceResult,
    ): OutcomeEvidenceResult {
        require(
            verificationEvidence.traceId ==
                verification.traceId,
        ) {
            "Constitutional verification and verification evidence must use the same trace identity."
        }

        return when (verification.status) {
            VerificationStatus.DEFERRED -> {
                require(
                    verificationEvidence.status ==
                        VerificationEvidenceStatus.DEFERRED,
                ) {
                    "Deferred constitutional verification must preserve deferred verification-evidence state."
                }

                OutcomeEvidenceResult.create(
                    traceId = verification.traceId,
                    status = OutcomeEvidenceStatus.DEFERRED,
                )
            }

            VerificationStatus.FAILED -> {
                require(
                    verificationEvidence.status ==
                        VerificationEvidenceStatus.FAILED,
                ) {
                    "Failed constitutional verification must preserve failed verification-evidence state."
                }

                OutcomeEvidenceResult.create(
                    traceId = verification.traceId,
                    status = OutcomeEvidenceStatus.FAILED,
                    error = requireNotNull(verification.error),
                )
            }

            VerificationStatus.VERIFIED -> {
                require(
                    verificationEvidence.status ==
                        VerificationEvidenceStatus.VERIFIED,
                ) {
                    "Verified constitutional results require genuine verified evidence before Android outcome evidence may be attempted."
                }

                val request =
                    requireNotNull(verification.request)

                val capabilityId =
                    request.observation
                        .execution
                        .capability
                        .capabilityId

                require(
                    verificationEvidence.capabilityId ==
                        capabilityId,
                ) {
                    "Constitutional verification and verification evidence must refer to the same capability identity."
                }

                val androidVerification =
                    AndroidVerificationResult.create(
                        traceId = verification.traceId,
                        status = AndroidVerificationStatus.VERIFIED,
                        evidence =
                            AndroidVerificationEvidence.create(
                                capabilityId =
                                    requireNotNull(
                                        verificationEvidence.capabilityId,
                                    ),
                                description =
                                    requireNotNull(
                                        verificationEvidence.description,
                                    ),
                            ),
                    )

                val androidOutcome =
                    outcomeAdapter.establish(
                        verification = androidVerification,
                    )

                require(
                    androidOutcome.traceId ==
                        verification.traceId,
                ) {
                    "Android outcome and constitutional verification must use the same trace identity."
                }

                require(
                    androidOutcome.evidence == null ||
                        androidOutcome.evidence.capabilityId ==
                        capabilityId,
                ) {
                    "Android outcome evidence and constitutional verification must refer to the same capability identity."
                }

                when (androidOutcome.status) {
                    AndroidOutcomeStatus.ESTABLISHED -> {
                        val evidence =
                            requireNotNull(
                                androidOutcome.evidence,
                            )

                        OutcomeEvidenceResult.create(
                            traceId = verification.traceId,
                            status =
                                OutcomeEvidenceStatus.ESTABLISHED,
                            capabilityId =
                                evidence.capabilityId,
                            description =
                                evidence.description,
                        )
                    }

                    AndroidOutcomeStatus.DEFERRED ->
                        OutcomeEvidenceResult.create(
                            traceId = verification.traceId,
                            status =
                                OutcomeEvidenceStatus.DEFERRED,
                        )

                    AndroidOutcomeStatus.FAILED ->
                        OutcomeEvidenceResult.create(
                            traceId = verification.traceId,
                            status =
                                OutcomeEvidenceStatus.FAILED,
                            error =
                                requireNotNull(
                                    androidOutcome.error,
                                ),
                        )
                }
            }
        }
    }
}
