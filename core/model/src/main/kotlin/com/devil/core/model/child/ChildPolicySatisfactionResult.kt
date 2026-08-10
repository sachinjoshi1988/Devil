package com.devil.core.model.child

/**
 * Immutable Stage 44 result describing child/guardian policy satisfaction.
 *
 * The original satisfaction request remains attached so the evidence basis is
 * explicit.
 *
 * This result is a Stage 44 policy result only.
 *
 * SATISFIED does not become constitutional authorization or execution approval.
 */
@ConsistentCopyVisibility
data class ChildPolicySatisfactionResult private constructor(
    val status: ChildPolicySatisfactionStatus,
    val request: ChildPolicySatisfactionRequest,
    val rationale: String,
) {
    companion object {

        fun create(
            status: ChildPolicySatisfactionStatus,
            request: ChildPolicySatisfactionRequest,
            rationale: String,
        ): ChildPolicySatisfactionResult {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Child-policy satisfaction rationale must not be blank."
            }

            return ChildPolicySatisfactionResult(
                status = status,
                request = request,
                rationale = normalizedRationale,
            )
        }
    }
}
