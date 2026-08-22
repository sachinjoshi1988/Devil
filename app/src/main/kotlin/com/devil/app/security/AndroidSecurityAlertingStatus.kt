package com.devil.app.security

/**
 * Stage 227 bounded Security Alerting status.
 *
 * AVAILABLE means one exact UNDERSTOOD Stage 226 Security Event result
 * has been associated with one explicitly supplied bounded alert description.
 *
 * DEFERRED means Stage 227 cannot truthfully claim a security alert is available.
 *
 * SECURITY_ALERT_AVAILABLE != ANDROID_NOTIFICATION_POSTED.
 * SECURITY_ALERT_AVAILABLE != OWNER_NOTIFIED.
 * SECURITY_ALERT_AVAILABLE != SECURITY_RESPONSE.
 * SECURITY_ALERT_AVAILABLE != THREAT_DETERMINATION.
 * SECURITY_ALERT_AVAILABLE != INTRUSION_DETERMINATION.
 * SECURITY_ALERT_AVAILABLE != EMERGENCY_DETERMINATION.
 * SECURITY_ALERT_AVAILABLE != AUTHORIZATION.
 * SECURITY_ALERT_AVAILABLE != EXECUTION.
 */
enum class AndroidSecurityAlertingStatus {
    AVAILABLE,
    DEFERRED,
}
