package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.verification.VerificationRequest

/**
 * Represents the stable operational result of constitutional verification.
 *
 * A verified result preserves one VerificationRequest for which genuine
 * verification evidence was established. Preserving that request does not
 * update world state, report final task success, change task or plan state, or
 * produce a final Outcome.
 *
 * A deferred result contains neither request nor error. A failed result contains
 * one matching error.
 */
@ConsistentCopyVisibility
data class VerificationResult private constructor(
    val traceId: TraceId,
    val status: VerificationStatus,
    val request: VerificationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: VerificationStatus,
            request: VerificationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): VerificationResult {
            when (status) {
                VerificationStatus.VERIFIED -> {
                    require(request != null && error == null) {
                        "Verified results require a request and must not contain an error."
                    }
                }

                VerificationStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred verification results must not contain a request or error."
                    }
                }

                VerificationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed verification results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.observation
                        .execution
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "Verification result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Verification result and error must use the same trace identity."
            }

            return VerificationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
