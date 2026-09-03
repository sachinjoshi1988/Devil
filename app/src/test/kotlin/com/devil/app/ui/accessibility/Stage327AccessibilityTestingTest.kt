package com.devil.app.ui.accessibility

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 327 — Accessibility Testing.
 *
 * This Beta-stage validation protects the already-established Stage 264
 * Accessibility & Inclusive Design presentation contract across the current
 * Devil UI.
 *
 * Stage 327 validates existing presentation architecture. It does not create
 * another accessibility architecture and does not extend Android
 * AccessibilityService authority.
 *
 * ACCESSIBILITY_TESTED != ACCESSIBILITY_SERVICE_AUTHORIZED.
 * UI_SEMANTICS != SCREEN_UNDERSTANDING.
 * UI_SEMANTICS != ACCESSIBILITY_ACTION.
 * TOUCH_TARGET_PRESENT != EXECUTION_APPROVAL.
 * CONTENT_DESCRIPTION_PRESENT != VERIFIED_SCREEN_UNDERSTANDING.
 * LIVE_REGION_PRESENT != CONSTITUTIONAL_OBSERVATION.
 * ACCESSIBILITY_PRESENTATION != AUTHENTICATION.
 * ACCESSIBILITY_PRESENTATION != AUTHORIZATION.
 * ACCESSIBILITY_PRESENTATION != VERIFIED_OUTCOME.
 * SOURCE_ACCESSIBILITY_VALIDATED != REAL_DEVICE_ACCESSIBILITY_VALIDATED.
 * VISUAL_CONTRAST_INTENT != MEASURED_CONTRAST_VERIFICATION.
 * STAGE_327 != STAGE_328_PRIVACY_TESTING.
 */
class Stage327AccessibilityTestingTest {

    private fun source(path: String): String {
        val workingDirectory =
            File(
                requireNotNull(System.getProperty("user.dir")) {
                    "Stage 327 requires a JVM user.dir for source validation."
                },
            )

        val candidates =
            listOf(
                File(workingDirectory, path),
                File(workingDirectory, "app/$path"),
                File(workingDirectory.parentFile ?: workingDirectory, "app/$path"),
            )

        val resolved =
            candidates.firstOrNull { it.isFile }
                ?: error(
                    "Unable to resolve Stage 327 source file: $path " +
                        "from ${workingDirectory.absolutePath}",
                )

        return resolved.readText()
    }

    @Test
    fun `stage 264 inclusive design remains the accessibility authority for Devil UI presentation`() {
        val inclusiveDesign =
            source(
                "src/main/kotlin/com/devil/app/ui/accessibility/DevilInclusiveDesign.kt",
            )

        assertTrue(
            inclusiveDesign.contains(
                "val minimumInteractiveTarget: Dp = 48.dp",
            ),
            "Stage 327 requires the existing 48 dp minimum interactive target.",
        )
        assertTrue(
            inclusiveDesign.contains("fun Modifier.devilInclusiveHeading()"),
            "Stage 327 requires the existing semantic-heading primitive.",
        )
        assertTrue(
            inclusiveDesign.contains("heading()"),
            "The inclusive heading primitive must retain heading semantics.",
        )
        assertTrue(
            inclusiveDesign.contains("fun Modifier.devilMeaningfulImage("),
            "Stage 327 requires the existing meaningful-image accessibility primitive.",
        )
        assertTrue(
            inclusiveDesign.contains("contentDescription = normalizedDescription"),
            "Meaningful imagery must retain explicit accessibility descriptions.",
        )
        assertTrue(
            inclusiveDesign.contains(
                "fun Modifier.devilInclusiveInteractiveTarget()",
            ),
            "Stage 327 requires the existing shared interactive-target primitive.",
        )
        assertTrue(
            inclusiveDesign.contains(
                "minWidth = DevilInclusiveDesignPolicy.minimumInteractiveTarget",
            ),
        )
        assertTrue(
            inclusiveDesign.contains(
                "minHeight = DevilInclusiveDesignPolicy.minimumInteractiveTarget",
            ),
        )
        assertTrue(
            inclusiveDesign.contains("fun Modifier.devilPoliteStatus()"),
            "Stage 327 requires the existing polite live-region primitive.",
        )
        assertTrue(
            inclusiveDesign.contains("LiveRegionMode.Polite"),
            "Changing accessibility presentation must retain polite live-region semantics.",
        )
        assertTrue(
            inclusiveDesign.contains(
                "INCLUSIVE_UI != ACCESSIBILITY_SERVICE",
            ),
            "Inclusive UI must remain distinct from Android AccessibilityService.",
        )
    }

    @Test
    fun `current beta interfaces retain inclusive headings and interactive targets`() {
        val interfacePaths =
            listOf(
                "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
                "src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
                "src/main/kotlin/com/devil/app/ui/education/DevilLanguageLearningInterface.kt",
                "src/main/kotlin/com/devil/app/ui/memory/DevilMemoryInterface.kt",
                "src/main/kotlin/com/devil/app/ui/task/DevilTaskAutomationInterface.kt",
                "src/main/kotlin/com/devil/app/ui/settings/DevilSettingsPrivacyPermissionsInterface.kt",
                "src/main/kotlin/com/devil/app/ui/security/DevilSecurityInterface.kt",
                "src/main/kotlin/com/devil/app/ui/finance/DevilFinanceInterface.kt",
                "src/main/kotlin/com/devil/app/ui/research/DevilResearchInterface.kt",
            )

        interfacePaths.forEach { path ->
            val uiSource = source(path)

            assertTrue(
                uiSource.contains("devilInclusiveHeading"),
                "Stage 327 requires inclusive heading use in $path",
            )
            assertTrue(
                uiSource.contains("devilInclusiveInteractiveTarget"),
                "Stage 327 requires bounded interactive-target support in $path",
            )
        }
    }

    @Test
    fun `current meaningful Devil interface imagery remains accessibility described`() {
        val describedImagePaths =
            listOf(
                "src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
                "src/main/kotlin/com/devil/app/ui/education/DevilLanguageLearningInterface.kt",
                "src/main/kotlin/com/devil/app/ui/memory/DevilMemoryInterface.kt",
                "src/main/kotlin/com/devil/app/ui/task/DevilTaskAutomationInterface.kt",
                "src/main/kotlin/com/devil/app/ui/settings/DevilSettingsPrivacyPermissionsInterface.kt",
                "src/main/kotlin/com/devil/app/ui/security/DevilSecurityInterface.kt",
                "src/main/kotlin/com/devil/app/ui/finance/DevilFinanceInterface.kt",
                "src/main/kotlin/com/devil/app/ui/research/DevilResearchInterface.kt",
                "src/main/kotlin/com/devil/app/ui/voice/DevilVoiceInterface.kt",
            )

        describedImagePaths.forEach { path ->
            val uiSource = source(path)

            assertTrue(
                uiSource.contains("contentDescription"),
                "Stage 327 requires accessibility description metadata in $path",
            )
            assertTrue(
                uiSource.contains("\"Devil\""),
                "The current meaningful Devil identity image must remain described in $path",
            )
        }
    }

    @Test
    fun `conversation and voice changing presentation retain polite live regions`() {
        val conversation =
            source(
                "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
            )
        val voice =
            source(
                "src/main/kotlin/com/devil/app/ui/voice/DevilVoiceInterface.kt",
            )

        assertTrue(
            conversation.contains("LiveRegionMode.Polite"),
            "Conversation changing presentation must retain polite accessibility announcements.",
        )
        assertTrue(
            conversation.contains("liveRegion"),
            "Conversation accessibility live-region semantics are required.",
        )

        assertTrue(
            voice.contains("LiveRegionMode.Polite"),
            "Voice changing presentation must retain polite accessibility announcements.",
        )
        assertTrue(
            voice.contains("liveRegion"),
            "Voice accessibility live-region semantics are required.",
        )
    }

    @Test
    fun `stage 327 preserves scalable typography instead of creating accessibility fixed-font overrides`() {
        val typography =
            source(
                "src/main/kotlin/com/devil/app/ui/theme/DevilTypography.kt",
            )
        val inclusiveDesign =
            source(
                "src/main/kotlin/com/devil/app/ui/accessibility/DevilInclusiveDesign.kt",
            )

        assertTrue(
            typography.contains("Typography("),
            "Devil must continue using the established Material typography architecture.",
        )
        assertTrue(
            typography.contains("FontFamily.Default"),
            "The current system-font-backed scalable typography contract must remain present.",
        )
        assertTrue(
            inclusiveDesign.contains(
                "Stage 264 intentionally preserves Compose/Material text scaling.",
            ),
            "Stage 327 must preserve the existing text-scaling accessibility boundary.",
        )
    }

    @Test
    fun `stage 327 does not merge inclusive UI testing with accessibility execution authority`() {
        val inclusiveDesign =
            source(
                "src/main/kotlin/com/devil/app/ui/accessibility/DevilInclusiveDesign.kt",
            )
        val accessibilityFoundation =
            source(
                "src/main/kotlin/com/devil/app/accessibility/AndroidAccessibilityFoundationV2Coordinator.kt",
            )

        assertTrue(
            inclusiveDesign.contains(
                "INCLUSIVE_UI != ACCESSIBILITY_SERVICE",
            ),
        )
        assertTrue(
            inclusiveDesign.contains(
                "UI_SEMANTICS != ACCESSIBILITY_ACTION",
            ),
        )

        assertTrue(
            accessibilityFoundation.contains(
                "ACCESSIBILITY_CONNECTED != DEVIL_AUTHORIZATION",
            ),
        )
        assertTrue(
            accessibilityFoundation.contains(
                "ACCESSIBILITY_AVAILABLE != EXECUTION_APPROVAL",
            ),
        )

        assertFalse(
            inclusiveDesign.contains("performGlobalAction("),
            "Inclusive-design presentation must not perform accessibility actions.",
        )
        assertFalse(
            inclusiveDesign.contains("AccessibilityNodeInfo"),
            "Inclusive-design presentation must not inspect Android accessibility nodes.",
        )
    }

    @Test
    fun `stage 327 source validation does not claim measured contrast or real device accessibility verification`() {
        val palette =
            source(
                "src/main/kotlin/com/devil/app/ui/theme/DevilColorPalette.kt",
            )

        assertTrue(
            palette.contains(
                "high-contrast futuristic presentation language",
            ),
            "The existing palette may retain its high-contrast presentation intent.",
        )

        /*
         * Source inspection alone cannot establish measured visual contrast,
         * TalkBack behavior, physical touch usability, or real-device
         * accessibility success. Those remain Stage 327 physical validation
         * evidence and are deliberately not manufactured by this unit test.
         */
        assertFalse(
            palette.contains("MEASURED_CONTRAST_VERIFIED"),
            "Palette declarations must not be treated as measured contrast verification.",
        )
    }

    @Test
    fun `stage 327 remains accessibility testing and does not implement stage 328 privacy testing`() {
        val stage326 =
            source(
                "src/main/kotlin/com/devil/app/education/Stage326LanguageCurriculumValidationStatus.kt",
            )

        assertTrue(
            stage326.contains(
                "STAGE_326 != STAGE_327_ACCESSIBILITY_TESTING",
            ),
            "Stage 327 must begin from the explicit frozen Stage326 boundary.",
        )

        /*
         * Stage 328 Privacy Testing remains a later roadmap target.
         * This test intentionally creates no privacy reducer, privacy authority,
         * permission decision, protected-data disclosure, or privacy execution.
         */
    }
}
