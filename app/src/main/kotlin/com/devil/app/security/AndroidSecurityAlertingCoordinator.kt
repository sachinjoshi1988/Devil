package com.devil.app.security

/**
 * Stage 227 bounded Security Alerting coordinator.
 *
 * It associates one exact Stage 226 Security Event Understanding result with
 * one explicitly supplied bounded security-alert description.
 *
 * It preserves Stage 226 provenance and does not independently reinterpret
 * the underlying surveillance signal.
 *
 * It does not:
 *
 * - post an Android notification;
 * - use NotificationManager;
 * - notify or contact the owner;
 * - speak an alert;
 * - trigger an alarm;
 * - operate a lock;
 * - contact emergency services;
 * - prepare or execute a Stage 91 Security Response;
 * - determine threat status;
 * - determine intrusion status;
 * - determine emergency status;
 * - establish constitutional Observation;
 * - establish constitutional Verification;
 * - establish Outcome;
 * - authenticate a subject or device;
 * - grant constitutional authorization;
 * - create a Decision, Task, Plan, or ExecutionRequest;
 * - execute local or remote capabilities;
 * - mutate World Model state;
 * - create, commit, or persist Memory;
 * - implement Stage 228 Security Response Governance.
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
class AndroidSecurityAlertingCoordinator {

    fun prepare(
        eventUnderstanding: AndroidSecurityEventUnderstandingResult,
        alertDescription: String?,
    ): AndroidSecurityAlertingResult {
        if (
            eventUnderstanding.status !=
                AndroidSecurityEventUnderstandingStatus.UNDERSTOOD ||
            alertDescription.isNullOrBlank()
        ) {
            return AndroidSecurityAlertingResult.create(
                status = AndroidSecurityAlertingStatus.DEFERRED,
                eventUnderstanding = eventUnderstanding,
            )
        }

        return AndroidSecurityAlertingResult.create(
            status = AndroidSecurityAlertingStatus.AVAILABLE,
            eventUnderstanding = eventUnderstanding,
            alertDescription = alertDescription,
        )
    }
}
