package com.devil.app.ui.settings

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 262 Settings / Privacy / Permissions Interface governance tests.
 *
 * These tests verify bounded presentation and navigation without treating UI
 * state as settings mutation, Android permission grant, Devil authorization,
 * privacy disclosure, execution, constitutional Verification, or Memory.
 */
class Stage262SettingsPrivacyPermissionsInterfaceTest {

    @Test
    fun `settings interface uses locked Devil identity asset`() {
        val source = interfaceSource()

        assertTrue(
            source.contains(
                "R.drawable.devil_primary_logo",
            ),
        )

        assertTrue(
            source.contains(
                "text = \"SETTINGS\"",
            ),
        )
    }

    @Test
    fun `settings interface presents bounded device settings context`() {
        val source = interfaceSource()

        for (
            expected in
                listOf(
                    "\"DEVICE SETTINGS\"",
                    "\"COMMAND\"",
                    "\"STATUS\"",
                    "settingsCommand",
                    "settingsControlStatus",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 262 settings presentation: $expected",
            )
        }
    }

    @Test
    fun `settings interface presents bounded Android permission context`() {
        val source = interfaceSource()

        for (
            expected in
                listOf(
                    "\"ANDROID PERMISSIONS\"",
                    "\"CAPABILITY\"",
                    "\"ASSESSMENT STATUS\"",
                    "\"REQUIRED PERMISSIONS\"",
                    "permissionCapability",
                    "permissionAssessmentStatus",
                    "requiredPermissions",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 262 permission presentation: $expected",
            )
        }
    }

    @Test
    fun `settings interface presents bounded privacy exposure context`() {
        val source = interfaceSource()

        for (
            expected in
                listOf(
                    "\"PRIVACY EXPOSURE\"",
                    "\"RATIONALE\"",
                    "privacyExposureStatus",
                    "privacyExposureRationale",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 262 privacy-exposure presentation: $expected",
            )
        }
    }

    @Test
    fun `settings interface presents bounded privacy disclosure context`() {
        val source = interfaceSource()

        for (
            expected in
                listOf(
                    "\"PRIVACY DISCLOSURE\"",
                    "\"TREATMENT\"",
                    "privacyDisclosureStatus",
                    "privacyDisclosureTreatment",
                    "privacyDisclosureRationale",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 262 privacy-disclosure presentation: $expected",
            )
        }
    }

    @Test
    fun `settings interface presents bounded privacy representation metadata only`() {
        val source = interfaceSource()

        for (
            expected in
                listOf(
                    "\"PRIVACY REPRESENTATION\"",
                    "\"DATA CLASSIFICATION\"",
                    "privacyRepresentationStatus",
                    "privacyDataClassification",
                    "\"Protected representation content is intentionally not displayed by this interface.\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 262 privacy-representation presentation: $expected",
            )
        }
    }

    @Test
    fun `settings interface preserves constitutional settings privacy permission boundaries`() {
        val source = interfaceSource()

        for (
            boundary in
                listOf(
                    "SETTINGS_INTERFACE != SETTINGS_AUTHORITY.",
                    "SETTINGS_PRESENTATION != SETTINGS_CHANGED.",
                    "SETTINGS_READY != SETTINGS_CHANGED.",
                    "PERMISSION_PRESENTATION != ANDROID_PERMISSION_REQUEST.",
                    "PERMISSION_PRESENTATION != ANDROID_PERMISSION_GRANT.",
                    "ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.",
                    "PERMISSION_ASSESSED != EXECUTION_APPROVAL.",
                    "PRIVACY_PRESENTATION != PRIVACY_AUTHORIZATION.",
                    "PRIVACY_EXPOSURE_ALLOWED != DEVIL_AUTHORIZATION.",
                    "DISCLOSURE_AVAILABLE != PERMISSION_TO_TRANSMIT.",
                    "DISCLOSURE_PRESENTATION != DISCLOSURE_PERFORMED.",
                    "PRIVACY_REPRESENTATION_STATUS != VERIFIED_PRIVACY.",
                    "SETTINGS_PRIVACY_PERMISSIONS_INTERFACE != EXECUTION.",
                    "SETTINGS_PRIVACY_PERMISSIONS_INTERFACE != CONSTITUTIONAL_VERIFICATION.",
                    "SETTINGS_PRIVACY_PERMISSIONS_INTERFACE != WORLD_MODEL_UPDATE.",
                    "SETTINGS_PRIVACY_PERMISSIONS_INTERFACE != MEMORY_COMMITMENT.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 262 boundary: $boundary",
            )
        }
    }

    @Test
    fun `missing supplied Stage 262 information remains truthful`() {
        val source = interfaceSource()

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
    fun `conversation exposes bounded settings navigation`() {
        val source = conversationSource()

        assertTrue(
            source.contains(
                "onSettingsOpen: () -> Unit = {}",
            ),
        )

        assertTrue(
            source.contains(
                "onClick = onSettingsOpen",
            ),
        )

        assertTrue(
            source.contains(
                "text = \"SETTINGS\"",
            ),
        )

        assertTrue(
            source.contains(
                "settingsNavigationEnabled",
            ),
        )

        for (
            boundary in
                listOf(
                    "SETTINGS_NAVIGATION != SETTINGS_CHANGE.",
                    "SETTINGS_NAVIGATION != ANDROID_PERMISSION_REQUEST.",
                    "SETTINGS_NAVIGATION != ANDROID_PERMISSION_GRANT.",
                    "SETTINGS_NAVIGATION != DEVIL_AUTHORIZATION.",
                    "SETTINGS_NAVIGATION != PRIVACY_DISCLOSURE.",
                    "SETTINGS_NAVIGATION != EXECUTION.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 262 navigation boundary: $boundary",
            )
        }
    }

    @Test
    fun `activity supplies no fabricated settings privacy or permission state`() {
        val source = activitySource()

        assertTrue(
            source.contains(
                "DevilSettingsPrivacyPermissionsInterface(",
            ),
        )

        for (
            suppliedNull in
                listOf(
                    "settingsCommand = null",
                    "settingsControlStatus = null",
                    "permissionCapability = null",
                    "permissionAssessmentStatus = null",
                    "requiredPermissions = null",
                    "privacyExposureStatus = null",
                    "privacyExposureRationale = null",
                    "privacyDisclosureStatus = null",
                    "privacyDisclosureTreatment = null",
                    "privacyDisclosureRationale = null",
                    "privacyRepresentationStatus = null",
                    "privacyDataClassification = null",
                )
        ) {
            assertTrue(
                source.contains(suppliedNull),
                "Activity must not fabricate Stage 262 state: $suppliedNull",
            )
        }
    }

    @Test
    fun `settings interface contains no operational settings privacy or permission wiring`() {
        val source = interfaceSource()

        for (
            forbidden in
                listOf(
                    "AndroidDeviceSettingsControlCoordinator",
                    "AndroidPermissionIntelligenceCoordinator",
                    "AndroidPermissionAuthorityAdapter",
                    "PrivacyExposureCoordinator",
                    "PrivacyDisclosureCoordinator",
                    "PrivacyRepresentationReducer",
                    "AuthorizationAuthority",
                    "ExecutionRequest",
                    "MemoryAuthority",
                    "WorldModelUpdateRequest",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 262 UI must not invoke operational wiring: $forbidden",
            )
        }
    }

    @Test
    fun `Stage 262 does not implement Stage 263 or later UI work`() {
        val source = interfaceSource()

        assertTrue(
            source.contains(
                "Stage 262 does not implement Stage 263 or later UI work.",
            ),
        )
    }

    private fun interfaceSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/settings/DevilSettingsPrivacyPermissionsInterface.kt",
            "src/main/kotlin/com/devil/app/ui/settings/DevilSettingsPrivacyPermissionsInterface.kt",
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
                "Unable to locate Stage 262 source from: ${candidates.joinToString()}",
            )
    }
}
