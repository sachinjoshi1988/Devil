package com.devil.app.security

/**
 * Stage 227 bounded Security Alerting result.
 *
 * AVAILABLE preserves:
 *
 * - one exact UNDERSTOOD Stage 226 Security Event Understanding result;
 * - one normalized explicitly supplied bounded alert description.
 *
 * DEFERRED preserves the exact Stage 226 upstream result without claiming
 * that any security alert is available.
 *
 * Stage 227 does not send an Android notification, notify the owner, prepare
 * a Security Response, classify threat/intrusion/emergency state, authorize
 * an action, or execute anything.
 *
 * SECURITY_ALERT_AVAILABLE != ANDROID_NOTIFICATION_POSTED.
 * SECURITY_ALERT_AVAILABLE != OWNER_NOTIFIED.
 * SECURITY_ALERT_AVAILABLE != SECURITY_RESPONSE.
 * SECURITY_ALERT_AVAILABLE != THREAT_DETERMINATION.
 * SECURITY_ALERT_AVAILABLE != INTRUSION_DETERMINATION.
 * SECURITY_ALERT_AVAILABLE != EMERGENCY_DETERMINATION.
 * SECURITY_ALERT_AVAILABLE != AUTHORIZATION.
 * SECURITY_ALERT_AVAILABLE != EXECUTION.
 * SECURITY_EVENT_UNDERSTOOD != VERIFIED_REALITY.
 */
@ConsistentCopyVisibility
data class AndroidSecurityAlertingResult private constructor(
    val status: AndroidSecurityAlertingStatus,
    val eventUnderstanding: AndroidSecurityEventUnderstandingResult,
    val alertDescription: String?,
) {
    companion object {
        fun create(
            status: AndroidSecurityAlertingStatus,
            eventUnderstanding: AndroidSecurityEventUnderstandingResult,
            alertDescription: String? = null,
        ): AndroidSecurityAlertingResult {
            return when (status) {
                AndroidSecurityAlertingStatus.AVAILABLE -> {
                    require(
                        eventUnderstanding.status ==
                            AndroidSecurityEventUnderstandingStatus.UNDERSTOOD,
                    ) {
                        "Available Stage 227 Security Alerting requires understood Stage 226 Security Event context."
                    }

                    val normalizedDescription =
                        requireNotNull(alertDescription)
                            .trim()

                    require(normalizedDescription.isNotEmpty()) {
                        "Stage 227 security alert description must not be blank."
                    }

                    AndroidSecurityAlertingResult(
                        status = status,
                        eventUnderstanding = eventUnderstanding,
                        alertDescription = normalizedDescription,
                    )
                }

                AndroidSecurityAlertingStatus.DEFERRED -> {
                    require(alertDescription == null) {
                        "Deferred Stage 227 Security Alerting must not contain alert metadata."
                    }

                    AndroidSecurityAlertingResult(
                        status = status,
                        eventUnderstanding = eventUnderstanding,
                        alertDescription = null,
                    )
                }
            }
        }
    }
}
