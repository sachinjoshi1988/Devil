package com.devil.app.security

/**
 * Stage 228 bounded Security Response Governance status.
 *
 * GOVERNED means one exact AVAILABLE Stage 227 Security Alerting result
 * has been bound to one exact PREPARED existing Stage 91 Security Response
 * preparation result while preserving the exact upstream Stage 90
 * surveillance-record provenance.
 *
 * DEFERRED means Stage 228 cannot truthfully claim bounded response governance.
 *
 * SECURITY_RESPONSE_GOVERNED != AUTHORIZED.
 * SECURITY_RESPONSE_GOVERNED != EXECUTION_APPROVED.
 * SECURITY_RESPONSE_PREPARED != EXECUTED.
 * SECURITY_ALERT_AVAILABLE != RESPONSE_AUTHORITY.
 * SECURITY_EVENT_UNDERSTOOD != THREAT.
 */
enum class AndroidSecurityResponseGovernanceStatus {
    GOVERNED,
    DEFERRED,
}
