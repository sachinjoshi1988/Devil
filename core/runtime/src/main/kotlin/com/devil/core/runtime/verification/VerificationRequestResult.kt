package com.devil.core.runtime.verification

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.verification.VerificationRequest

/**
 * Represents the structured operational result of verification-request
 * preparation.
 *
 * An available result contains one VerificationRequest. An unavailable result
 * contains neither request nor error. A failed result contains one matching
 * error.
 *
 * This result does not establish verification evidence, determine whether an
 * intended outcome was achieved, update world state, report success, or produce
 * a final outcome.
 */
@ConsistentCopyVisibility
data class VerificationRequestResult private constructor(
    val traceId: TraceId,
    val status: VerificationRequestStatus,
    val request: VerificationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: VerificationRequestStatus,
            request: VerificationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): VerificationRequestResult {
            when (status) {
                VerificationRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available verification request results require a request and must not contain an error."
                    }
                }

                VerificationRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable verification request results must not contain a request or error."
                    }
                }

                VerificationRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed verification request results require an error and must not contain a request."
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
                "Verification request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Verification request result and error must use the same trace identity."
            }

            return VerificationRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
