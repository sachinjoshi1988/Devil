package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.GuardianEducationPolicyRecord

/**
 * Stable Stage 144 result of bounded Guardian Policy Foundation preparation.
 *
 * PREPARED requires one GuardianEducationPolicyRecord.
 * DEFERRED must not contain one.
 *
 * This result grants no guardian authority, guardian approval, constitutional
 * authorization, execution approval, Observation, Verification, Outcome,
 * Learning result, or Memory commitment.
 */
@ConsistentCopyVisibility
data class GuardianEducationPolicyPreparationResult private constructor(
    val traceId: TraceId,
    val status: GuardianEducationPolicyPreparationStatus,
    val guardianPolicy: GuardianEducationPolicyRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: GuardianEducationPolicyPreparationStatus,
            guardianPolicy: GuardianEducationPolicyRecord? = null,
        ): GuardianEducationPolicyPreparationResult {
            when (status) {
                GuardianEducationPolicyPreparationStatus.PREPARED -> {
                    require(guardianPolicy != null) {
                        "Prepared Guardian Policy Foundation results require one guardian-policy context."
                    }
                }

                GuardianEducationPolicyPreparationStatus.DEFERRED -> {
                    require(guardianPolicy == null) {
                        "Deferred Guardian Policy Foundation results must not contain a guardian-policy context."
                    }
                }
            }

            return GuardianEducationPolicyPreparationResult(
                traceId = traceId,
                status = status,
                guardianPolicy = guardianPolicy,
            )
        }
    }
}
