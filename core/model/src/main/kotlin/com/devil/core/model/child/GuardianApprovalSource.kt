package com.devil.core.model.child

/**
 * Supplies one explicit Stage 44 guardian decision for exactly one bounded
 * GuardianApprovalRequest.
 *
 * An implementation must not:
 *
 * - infer guardian approval from guardian authority;
 * - infer approval from FAMILY relationships;
 * - authenticate a guardian;
 * - establish trust;
 * - grant Devil authorization;
 * - enter Owner Mode;
 * - grant Android permission;
 * - invoke UnifiedDevilRuntime;
 * - persist logical memory;
 * - or execute an action.
 */
fun interface GuardianApprovalSource {

    fun decide(
        request: GuardianApprovalRequest,
    ): GuardianApprovalDecision
}
