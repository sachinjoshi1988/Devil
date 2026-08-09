package com.devil.app.outcome

import com.devil.app.verification.AndroidVerificationResult
import com.devil.app.verification.AndroidVerificationStatus

/**
 * Default Stage 33 Android Outcome adapter.
 *
 * This adapter approaches AndroidOutcomeSource only after Stage 32 produced
 * one genuine VERIFIED result.
 *
 * It does not reinterpret VERIFIED as ESTABLISHED.
 *
 * The source must independently produce genuine bounded outcome evidence.
 *
 * The default source remains DEFERRED because no approved production Android
 * outcome-determination mechanism exists yet.
 */
class DefaultAndroidOutcomeAdapter(
    private val outcomeSource: AndroidOutcomeSource =
        DefaultAndroidOutcomeSource(),
) : AndroidOutcomeAdapter {

    override fun establish(
        verification: AndroidVerificationResult,
    ): AndroidOutcomeResult {
        return when (verification.status) {
            AndroidVerificationStatus.DEFERRED ->
                AndroidOutcomeResult.create(
                    traceId = verification.traceId,
                    status = AndroidOutcomeStatus.DEFERRED,
                )

            AndroidVerificationStatus.FAILED ->
                AndroidOutcomeResult.create(
                    traceId = verification.traceId,
                    status = AndroidOutcomeStatus.FAILED,
                    error = requireNotNull(verification.error),
                )

            AndroidVerificationStatus.VERIFIED -> {
                val verificationEvidence =
                    requireNotNull(verification.evidence)

                val result =
                    outcomeSource.establish(
                        traceId = verification.traceId,
                        verificationEvidence = verificationEvidence,
                    )

                require(
                    result.traceId == verification.traceId,
                ) {
                    "Android verification and outcome result must use the same trace identity."
                }

                require(
                    result.evidence == null ||
                        result.evidence.capabilityId ==
                        verificationEvidence.capabilityId,
                ) {
                    "Android verification and outcome evidence must refer to the same capability identity."
                }

                result
            }
        }
    }
}
