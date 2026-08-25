package com.devil.app.ui.security

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 261 Security Interface governance tests.
 *
 * These tests verify bounded security/surveillance presentation and navigation
 * without treating UI state as authentication, authorization, verified reality,
 * Security Response execution, constitutional Verification, or Memory.
 */
class Stage261SecurityInterfaceTest {

    @Test
    fun `security interface uses locked Devil identity asset`() {
        val source = securityInterfaceSource()

        assertTrue(
            source.contains(
                "R.drawable.devil_primary_logo",
            ),
        )
        assertTrue(
            source.contains(
                "text = \"SECURITY\"",
            ),
        )
    }

    @Test
    fun `security interface presents bounded security and surveillance state`() {
        val source = securityInterfaceSource()

        for (
            expected in
                listOf(
                    "\"SECURITY STATE\"",
                    "\"SECURITY STAGE\"",
                    "\"SURVEILLANCE\"",
                    "\"INTEGRATION STATUS\"",
                    "\"CAMERA ADAPTER STATUS\"",
                    "\"EVENT UNDERSTANDING\"",
                    "\"UNDERSTANDING\"",
                    "\"SECURITY ALERTING\"",
                    "\"ALERT\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 261 security presentation: $expected",
            )
        }
    }

    @Test
    fun `security interface presents response emergency and owner dashboard context`() {
        val source = securityInterfaceSource()

        for (
            expected in
                listOf(
                    "\"RESPONSE & EMERGENCY\"",
                    "\"RESPONSE GOVERNANCE\"",
                    "\"EMERGENCY ESCALATION\"",
                    "\"ESCALATION DESCRIPTION\"",
                    "\"OWNER SECURITY DASHBOARD\"",
                    "\"SUMMARY\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 261 response presentation: $expected",
            )
        }
    }

    @Test
    fun `security interface presents evidence privacy and validation context`() {
        val source = securityInterfaceSource()

        for (
            expected in
                listOf(
                    "\"EVIDENCE RETENTION\"",
                    "\"RETENTION CONTEXT\"",
                    "\"SURVEILLANCE PRIVACY\"",
                    "\"PRIVACY CONTROL CONTEXT\"",
                    "\"SECURITY VALIDATION\"",
                    "\"VALIDATION FOCUS\"",
                    "\"VALIDATION EVIDENCE\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 261 evidence/privacy/validation presentation: $expected",
            )
        }
    }

    @Test
    fun `security interface preserves constitutional security boundaries`() {
        val source = securityInterfaceSource()

        for (
            boundary in
                listOf(
                    "SECURITY_INTERFACE != SECURITY_AUTHORITY.",
                    "SECURITY_PRESENTATION != AUTHENTICATION.",
                    "SECURITY_PRESENTATION != OWNER_MODE.",
                    "SECURITY_PRESENTATION != AUTHORIZATION.",
                    "SECURITY_EVENT_PRESENTATION != VERIFIED_REALITY.",
                    "SECURITY_EVENT_PRESENTATION != THREAT_DETERMINATION.",
                    "SECURITY_ALERT_PRESENTATION != OWNER_NOTIFIED.",
                    "SECURITY_RESPONSE_PRESENTATION != EXECUTION_APPROVED.",
                    "EMERGENCY_PRESENTATION != EMERGENCY_CONFIRMED.",
                    "EMERGENCY_PRESENTATION != EMERGENCY_SERVICE_CONTACTED.",
                    "EVIDENCE_RETENTION_PRESENTATION != PERSISTENCE.",
                    "PRIVACY_CONTROL_PRESENTATION != PRIVACY_DISCLOSURE_PERFORMED.",
                    "SECURITY_VALIDATION_PRESENTATION != CONSTITUTIONAL_VERIFICATION.",
                    "SECURITY_INTERFACE != EXECUTION.",
                    "SECURITY_INTERFACE != WORLD_MODEL_UPDATE.",
                    "SECURITY_INTERFACE != MEMORY_COMMITMENT.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 261 security boundary: $boundary",
            )
        }
    }

    @Test
    fun `missing supplied security information remains truthful`() {
        val source = securityInterfaceSource()

        assertTrue(
            source.contains(
                "\"Unavailable\"",
            ),
        )
        assertTrue(
            source.contains(
                "?.trim()",
            ),
        )
        assertTrue(
            source.contains(
                "?.takeIf(String::isNotEmpty)",
            ),
        )
    }

    @Test
    fun `conversation exposes bounded security navigation`() {
        val source = conversationSource()

        assertTrue(
            source.contains(
                "onSecurityOpen: () -> Unit = {}",
            ),
        )
        assertTrue(
            source.contains(
                "onClick = onSecurityOpen",
            ),
        )
        assertTrue(
            source.contains(
                "text = \"SECURITY\"",
            ),
        )
        assertTrue(
            source.contains(
                "securityNavigationEnabled",
            ),
        )

        for (
            boundary in
                listOf(
                    "SECURITY_NAVIGATION != SECURITY_AUTHORITY.",
                    "SECURITY_NAVIGATION != AUTHENTICATION.",
                    "SECURITY_NAVIGATION != AUTHORIZATION.",
                    "SECURITY_NAVIGATION != SECURITY_RESPONSE_EXECUTION.",
                    "SECURITY_NAVIGATION != CONSTITUTIONAL_VERIFICATION.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 261 security-navigation boundary: $boundary",
            )
        }
    }

    @Test
    fun `activity supplies no fabricated security state`() {
        val source = activitySource()

        assertTrue(
            source.contains(
                "DevilSecurityInterface(",
            ),
        )

        for (
            suppliedNull in
                listOf(
                    "securityStage = null",
                    "securityState = null",
                    "surveillanceIntegrationStatus = null",
                    "cameraAdapterStatus = null",
                    "eventUnderstandingStatus = null",
                    "eventUnderstandingDescription = null",
                    "alertingStatus = null",
                    "alertDescription = null",
                    "responseGovernanceStatus = null",
                    "emergencyEscalationStatus = null",
                    "escalationDescription = null",
                    "ownerDashboardStatus = null",
                    "dashboardSummary = null",
                    "evidenceRetentionStatus = null",
                    "retentionDescription = null",
                    "privacyControlsStatus = null",
                    "privacyControlsDescription = null",
                    "productionValidationStatus = null",
                    "validationFocus = null",
                    "validationEvidenceDescription = null",
                )
        ) {
            assertTrue(
                source.contains(suppliedNull),
                "Activity must not fabricate Stage 261 security state: $suppliedNull",
            )
        }
    }

    @Test
    fun `security interface contains no operational security wiring`() {
        val source = securityInterfaceSource()

        for (
            forbidden in
                listOf(
                    "AndroidSecuritySurveillanceIntegrationCoordinator",
                    "AndroidSecurityCameraAdapterCoordinator",
                    "AndroidSecurityEventUnderstandingCoordinator",
                    "AndroidSecurityAlertingCoordinator",
                    "AndroidSecurityResponseGovernanceCoordinator",
                    "AndroidEmergencyEscalationCoordinator",
                    "AndroidOwnerSecurityDashboardCoordinator",
                    "AndroidSecurityEvidenceRetentionCoordinator",
                    "AndroidSurveillancePrivacyControlsCoordinator",
                    "AndroidSecurityProductionValidationCoordinator",
                    "SecurityTransitionAuthority",
                    "ExecutionRequest",
                    "MemoryAuthority",
                    "WorldModelUpdateRequest",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 261 UI must not invoke operational security wiring: $forbidden",
            )
        }
    }

    @Test
    fun `Stage 261 does not implement Stage 262 or later UI work`() {
        val source = securityInterfaceSource()

        assertTrue(
            source.contains(
                "Stage 261 does not implement Stage 262 or later UI work.",
            ),
        )
    }

    private fun securityInterfaceSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/security/DevilSecurityInterface.kt",
            "src/main/kotlin/com/devil/app/ui/security/DevilSecurityInterface.kt",
        )

    private fun conversationSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
            "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
        )

    private fun activitySource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/DevilActivity.kt",
            "src/main/kotlin/com/devil/app/DevilActivity.kt",
        )

    private fun readSource(
        vararg candidates: String,
    ): String {
        return candidates
            .asSequence()
            .map(::File)
            .firstOrNull(File::isFile)
            ?.readText()
            ?: error(
                "Unable to locate Stage 261 source from: ${candidates.joinToString()}",
            )
    }
}
