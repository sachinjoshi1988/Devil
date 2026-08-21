package com.devil.app.accessibility

/**
 * Stage 178 bounded Android Accessibility Foundation V2 result.
 *
 * This result preserves the exact Android accessibility-service diagnostic
 * from which the bounded foundation status was derived.
 *
 * It does not perform accessibility actions, inspect screen content,
 * resolve targets, execute gestures, observe effects, verify outcomes,
 * or implement Stage 179 Screen Understanding.
 *
 * ACCESSIBILITY_AVAILABLE != DEVIL_AUTHORIZATION.
 * ACCESSIBILITY_CONNECTED != EXECUTION_APPROVAL.
 */
@ConsistentCopyVisibility
data class AndroidAccessibilityFoundationV2Result private constructor(
    val status: AndroidAccessibilityFoundationV2Status,
    val diagnostic: AndroidAccessibilityServiceDiagnostic,
) {
    companion object {

        fun create(
            status: AndroidAccessibilityFoundationV2Status,
            diagnostic: AndroidAccessibilityServiceDiagnostic,
        ): AndroidAccessibilityFoundationV2Result {
            val expectedStatus =
                when (diagnostic.status) {
                    AndroidAccessibilityServiceDiagnosticStatus.CONNECTED ->
                        AndroidAccessibilityFoundationV2Status.AVAILABLE

                    AndroidAccessibilityServiceDiagnosticStatus.ENABLED_BUT_DISCONNECTED ->
                        AndroidAccessibilityFoundationV2Status.DEGRADED

                    AndroidAccessibilityServiceDiagnosticStatus.DISABLED ->
                        AndroidAccessibilityFoundationV2Status.UNAVAILABLE

                    AndroidAccessibilityServiceDiagnosticStatus.UNKNOWN ->
                        AndroidAccessibilityFoundationV2Status.UNKNOWN
                }

            require(status == expectedStatus) {
                "Android Accessibility Foundation V2 status must match the supplied accessibility diagnostic."
            }

            return AndroidAccessibilityFoundationV2Result(
                status = status,
                diagnostic = diagnostic,
            )
        }
    }
}
