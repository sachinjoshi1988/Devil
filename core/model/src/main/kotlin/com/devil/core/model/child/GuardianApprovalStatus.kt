package com.devil.core.model.child

/**
 * Stage 44 bounded status for one explicit guardian decision.
 *
 * APPROVED means one authorized guardian-approval source explicitly approved
 * the exact bounded request represented by the associated decision.
 *
 * DENIED means that exact request was explicitly denied.
 *
 * UNAVAILABLE means no justified guardian decision is available.
 *
 * Approval status
 * != guardian authority
 * != identity resolution
 * != authentication
 * != subject trust
 * != Devil authorization
 * != Owner Mode
 * != Android permission
 * != Execution APPROVED.
 */
enum class GuardianApprovalStatus {
    APPROVED,
    DENIED,
    UNAVAILABLE,
}
