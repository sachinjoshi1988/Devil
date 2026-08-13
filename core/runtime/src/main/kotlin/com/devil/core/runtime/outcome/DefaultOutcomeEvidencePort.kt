package com.devil.core.runtime.outcome

import com.devil.core.runtime.verification.VerificationEvidenceResult
import com.devil.core.runtime.verification.VerificationEvidenceStatus
import com.devil.core.runtime.verification.VerificationResult
import com.devil.core.runtime.verification.VerificationStatus

/**
 * Default fail-closed core outcome-evidence port.
 *
 * No production outcome embodiment is configured inside core runtime.
 *
 * Therefore:
 *
 * - VERIFIED remains DEFERRED rather than being fabricated as outcome evidence;
 * - DEFERRED remains DEFERRED;
 * - FAILED preserves its matching operational error.
 *
 * The genuine VerificationEvidenceResult is also required so that a future
 * embodiment never needs to reconstruct or invent verification evidence.
 *
 * A platform embodiment may implement OutcomeEvidencePort outside core and be
 * injected through the normal Unified Devil Runtime composition boundary.
 *
 * This default performs no platform outcome determination and invents no
 * evidence.
 */
class DefaultOutcomeEvidencePort : OutcomeEvidencePort {

    override fun establish(
        verification: VerificationResult,
        verificationEvidence: VerificationEvidenceResult,
    ): OutcomeEvidenceResult {
        require(
            verificationEvidence.traceId == verification.traceId,
        ) {
            "Verification result and verification evidence must use the same trace identity."
        }

        return when (verification.status) {
            VerificationStatus.VERIFIED -> {
                require(
                    verificationEvidence.status ==
                        VerificationEvidenceStatus.VERIFIED,
                ) {
                    "Verified constitutional results require genuine verified evidence before outcome evidence may be attempted."
                }

                val request =
                    requireNotNull(verification.request)

                require(
                    verificationEvidence.capabilityId ==
                        request.observation.execution.capability.capabilityId,
                ) {
                    "Verification result and verification evidence must refer to the same capability identity."
                }

                OutcomeEvidenceResult.create(
                    traceId = verification.traceId,
                    status = OutcomeEvidenceStatus.DEFERRED,
                )
            }

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
        }
    }
}
