package com.devil.core.runtime.verification

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Neutral platform-independent result of one bounded verification-evidence
 * attempt.
 *
 * VERIFIED preserves the exact capability identity for which genuine
 * verification evidence was produced together with one nonblank bounded
 * description of that evidence.
 *
 * The description represents evidence only. It grants no authority and does not
 * establish a final constitutional Outcome.
 *
 * DEFERRED contains neither capability identity, description, nor error.
 *
 * FAILED contains one matching operational error and no verification evidence.
 */
@ConsistentCopyVisibility
data class VerificationEvidenceResult private constructor(
    val traceId: TraceId,
    val status: VerificationEvidenceStatus,
    val capabilityId: CapabilityId?,
    val description: String?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: VerificationEvidenceStatus,
            capabilityId: CapabilityId? = null,
            description: String? = null,
            error: UniversalErrorRecord? = null,
        ): VerificationEvidenceResult {
            val normalizedDescription =
                description?.trim()

            when (status) {
                VerificationEvidenceStatus.VERIFIED -> {
                    require(
                        capabilityId != null &&
                            !normalizedDescription.isNullOrEmpty() &&
                            error == null,
                    ) {
                        "Verified evidence results require a capability identity and nonblank description and must not contain an error."
                    }
                }

                VerificationEvidenceStatus.DEFERRED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error == null,
                    ) {
                        "Deferred verification-evidence results must not contain capability identity, description, or error."
                    }
                }

                VerificationEvidenceStatus.FAILED -> {
                    require(
                        capabilityId == null &&
                            normalizedDescription == null &&
                            error != null,
                    ) {
                        "Failed verification-evidence results require an error and must not contain verification evidence."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Verification-evidence result and error must use the same trace identity."
            }

            return VerificationEvidenceResult(
                traceId = traceId,
                status = status,
                capabilityId = capabilityId,
                description = normalizedDescription,
                error = error,
            )
        }
    }
}
