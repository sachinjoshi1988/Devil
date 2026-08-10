package com.devil.core.model.child

/**
 * Immutable Stage 44 result of one explicit guardian-approval evaluation.
 *
 * The complete GuardianApprovalRequest remains attached so the exact approval
 * scope is preserved.
 *
 * APPROVED applies only to this exact request.
 *
 * It is not reusable as blanket approval for another child-policy request.
 *
 * Guardian approval
 * != Devil authorization
 * != Owner Mode
 * != Android permission
 * != Executive readiness
 * != Execution APPROVED
 * != verified Outcome.
 */
@ConsistentCopyVisibility
data class GuardianApprovalDecision private constructor(
    val status: GuardianApprovalStatus,
    val request: GuardianApprovalRequest,
    val rationale: String,
) {
    companion object {

        fun create(
            status: GuardianApprovalStatus,
            request: GuardianApprovalRequest,
            rationale: String,
        ): GuardianApprovalDecision {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Guardian approval decision rationale must not be blank."
            }

            return GuardianApprovalDecision(
                status = status,
                request = request,
                rationale = normalizedRationale,
            )
        }
    }
}
