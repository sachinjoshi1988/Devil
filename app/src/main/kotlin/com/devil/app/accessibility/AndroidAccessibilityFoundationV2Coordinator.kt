package com.devil.app.accessibility

/**
 * Stage 178 bounded Android Accessibility Foundation V2 coordinator.
 *
 * The coordinator consumes one established Android accessibility-service
 * diagnostic and derives only the bounded foundation state.
 *
 * It does not:
 *
 * - enable or disable Android accessibility;
 * - perform accessibility actions;
 * - inspect or interpret the accessibility tree;
 * - resolve screen targets;
 * - execute touch or gestures;
 * - grant Devil authorization;
 * - create an ExecutionRequest;
 * - establish Observation, Verification, or Outcome;
 * - implement Stage 179 Screen Understanding;
 * - implement Stage 180 Reliable Target Resolution.
 *
 * ACCESSIBILITY_CONNECTED != DEVIL_AUTHORIZATION.
 * ACCESSIBILITY_AVAILABLE != EXECUTION_APPROVAL.
 * ACCESSIBILITY_EVENT != OBSERVATION.
 * ACCESSIBILITY_ACTION != VERIFIED_OUTCOME.
 */
class AndroidAccessibilityFoundationV2Coordinator {

    fun assess(
        diagnostic: AndroidAccessibilityServiceDiagnostic,
    ): AndroidAccessibilityFoundationV2Result {
        val status =
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

        return AndroidAccessibilityFoundationV2Result.create(
            status = status,
            diagnostic = diagnostic,
        )
    }
}
