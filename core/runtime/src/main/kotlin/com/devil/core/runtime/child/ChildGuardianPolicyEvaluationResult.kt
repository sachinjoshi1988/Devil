package com.devil.core.runtime.child

import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.common.TraceId

/**
 * Stable Stage 86 runtime result of one bounded child/guardian-policy evaluation.
 *
 * EVALUATED requires exactly one existing Stage 44 ChildPolicyDecision.
 *
 * DEFERRED must not contain a decision.
 *
 * This result adds no child classification, guardian authority, guardian approval,
 * identity authority, trust, authentication, authorization, security session,
 * capability authority, execution authority, Observation, Verification, Outcome,
 * constitutional Learning, Memory, or persistence authority.
 */
@ConsistentCopyVisibility
data class ChildGuardianPolicyEvaluationResult private constructor(
    val traceId: TraceId,
    val status: ChildGuardianPolicyEvaluationStatus,
    val decision: ChildPolicyDecision?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ChildGuardianPolicyEvaluationStatus,
            decision: ChildPolicyDecision? = null,
        ): ChildGuardianPolicyEvaluationResult {
            when (status) {
                ChildGuardianPolicyEvaluationStatus.EVALUATED -> {
                    require(decision != null) {
                        "Evaluated child/guardian policy results require one Stage 44 decision."
                    }
                }

                ChildGuardianPolicyEvaluationStatus.DEFERRED -> {
                    require(decision == null) {
                        "Deferred child/guardian policy results must not contain a decision."
                    }
                }
            }

            return ChildGuardianPolicyEvaluationResult(
                traceId = traceId,
                status = status,
                decision = decision,
            )
        }
    }
}
