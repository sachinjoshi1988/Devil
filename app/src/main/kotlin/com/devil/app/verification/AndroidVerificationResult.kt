package com.devil.app.verification

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents one bounded Android verification result.
 *
 * VERIFIED preserves genuine AndroidVerificationEvidence.
 *
 * DEFERRED contains neither evidence nor error.
 *
 * FAILED contains one matching error and no evidence.
 *
 * This result does not establish a final constitutional Outcome, claim task or
 * plan completion, update world state, create logical memory, or report success
 * beyond the bounded verification evidence it preserves.
 */
@ConsistentCopyVisibility
data class AndroidVerificationResult private constructor(
    val traceId: TraceId,
    val status: AndroidVerificationStatus,
    val evidence: AndroidVerificationEvidence?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: AndroidVerificationStatus,
            evidence: AndroidVerificationEvidence? = null,
            error: UniversalErrorRecord? = null,
        ): AndroidVerificationResult {
            when (status) {
                AndroidVerificationStatus.VERIFIED -> {
                    require(evidence != null && error == null) {
                        "Verified Android results require evidence and must not contain an error."
                    }
                }

                AndroidVerificationStatus.DEFERRED -> {
                    require(evidence == null && error == null) {
                        "Deferred Android verification results must not contain evidence or error."
                    }
                }

                AndroidVerificationStatus.FAILED -> {
                    require(evidence == null && error != null) {
                        "Failed Android verification results require an error and must not contain evidence."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Android verification result and error must use the same trace identity."
            }

            return AndroidVerificationResult(
                traceId = traceId,
                status = status,
                evidence = evidence,
                error = error,
            )
        }
    }
}
