package com.devil.app.ui.accessibility

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 264 Accessibility & Inclusive Design governance tests.
 *
 * Stage 264 improves Phase-R presentation semantics and inclusive interaction
 * targets only.
 *
 * It does not connect to Android AccessibilityService, inspect screen content,
 * perform accessibility actions, authenticate, authorize, execute, establish
 * constitutional Observation / Verification / Outcome, mutate World Model state,
 * perform Learning, or commit Memory.
 */
class Stage264AccessibilityInclusiveDesignTest {

    @Test
    fun `inclusive design policy preserves minimum interactive target`() {
        val source = inclusiveDesignSource()

        assertTrue(
            source.contains(
                "val minimumInteractiveTarget: Dp = 48.dp",
            ),
        )

        assertTrue(
            source.contains(
                "minWidth = DevilInclusiveDesignPolicy.minimumInteractiveTarget",
            ),
        )

        assertTrue(
            source.contains(
                "minHeight = DevilInclusiveDesignPolicy.minimumInteractiveTarget",
            ),
        )
    }

    @Test
    fun `inclusive design exposes bounded semantic helpers`() {
        val source = inclusiveDesignSource()

        for (
            expected in
                listOf(
                    "fun Modifier.devilInclusiveHeading()",
                    "heading()",
                    "fun Modifier.devilMeaningfulImage(",
                    "contentDescription = normalizedDescription",
                    "fun Modifier.devilInclusiveInteractiveTarget()",
                    "fun Modifier.devilPoliteStatus()",
                    "liveRegion = LiveRegionMode.Polite",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 264 inclusive presentation helper: $expected",
            )
        }
    }

    @Test
    fun `meaningful image description cannot be blank`() {
        val source = inclusiveDesignSource()

        assertTrue(
            source.contains(
                "val normalizedDescription = description.trim()",
            ),
        )

        assertTrue(
            source.contains(
                "require(normalizedDescription.isNotEmpty())",
            ),
        )
    }

    @Test
    fun `inclusive design preserves constitutional boundaries`() {
        val source = inclusiveDesignSource()

        for (
            boundary in
                listOf(
                    "INCLUSIVE_UI != ACCESSIBILITY_SERVICE.",
                    "UI_SEMANTICS != SCREEN_UNDERSTANDING.",
                    "UI_SEMANTICS != ACCESSIBILITY_ACTION.",
                    "ACCESSIBILITY_PRESENTATION != AUTHENTICATION.",
                    "ACCESSIBILITY_PRESENTATION != OWNER_MODE.",
                    "ACCESSIBILITY_PRESENTATION != AUTHORIZATION.",
                    "ACCESSIBILITY_PRESENTATION != EXECUTION_APPROVAL.",
                    "ACCESSIBILITY_PRESENTATION != CONSTITUTIONAL_OBSERVATION.",
                    "ACCESSIBILITY_PRESENTATION != CONSTITUTIONAL_VERIFICATION.",
                    "ACCESSIBILITY_PRESENTATION != VERIFIED_OUTCOME.",
                    "ACCESSIBILITY_PRESENTATION != WORLD_MODEL_UPDATE.",
                    "ACCESSIBILITY_PRESENTATION != MEMORY_COMMITMENT.",
                    "INCLUSIVE_DESIGN != DEVICE_IDENTITY.",
                    "TOUCH_TARGET != EXECUTION_APPROVAL.",
                    "LIVE_REGION != OBSERVATION.",
                    "LIVE_REGION != VERIFIED_OUTCOME.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 264 constitutional boundary: $boundary",
            )
        }
    }

    @Test
    fun `inclusive design contract contains no operational accessibility wiring`() {
        val source = inclusiveDesignSource()

        for (
            forbidden in
                listOf(
                    "AndroidAccessibilityActionSource",
                    "DefaultAndroidAccessibilityActionSource",
                    "AndroidAccessibilityExecutionPerformer",
                    "AndroidAccessibilityNodeResolver",
                    "DevilAccessibilityServiceRegistry",
                    "AndroidAccessibilityFoundationV2Coordinator",
                    "AndroidScreenUnderstandingCoordinator",
                    "AndroidReliableTargetResolutionCoordinator",
                    "AndroidTouchGestureExecutionCoordinator",
                    "AuthorizationAuthority",
                    "ExecutionRequest(",
                    "MemoryAuthority",
                    "UnifiedDevilRuntime",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 264 presentation contract must not invoke operational accessibility wiring: $forbidden",
            )
        }
    }

    @Test
    fun `conversation integrates inclusive presentation helpers`() {
        val source = conversationSource()

        assertTrue(
            source.contains(
                "import com.devil.app.ui.accessibility.devilInclusiveHeading",
            ),
        )

        assertTrue(
            source.contains(
                "import com.devil.app.ui.accessibility.devilInclusiveInteractiveTarget",
            ),
        )

        assertTrue(
            source.contains(
                ".devilInclusiveInteractiveTarget()",
            ),
        )
    }

    @Test
    fun `Phase R interfaces integrate shared inclusive presentation contract`() {
        for (
            path in
                listOf(
                    "app/src/main/kotlin/com/devil/app/ui/voice/DevilVoiceInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/memory/DevilMemoryInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/task/DevilTaskAutomationInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/education/DevilLanguageLearningInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/research/DevilResearchInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/finance/DevilFinanceInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/security/DevilSecurityInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/settings/DevilSettingsPrivacyPermissionsInterface.kt",
                )
        ) {
            val source = readSource(path, path.removePrefix("app/"))

            assertTrue(
                source.contains(
                    "devilInclusiveInteractiveTarget",
                ),
                "Stage 264 inclusive interaction target missing from $path",
            )
        }
    }

    @Test
    fun `major Phase R interfaces expose inclusive heading semantics`() {
        for (
            path in
                listOf(
                    "app/src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
                    "app/src/main/kotlin/com/devil/app/ui/memory/DevilMemoryInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/task/DevilTaskAutomationInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/education/DevilLanguageLearningInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/research/DevilResearchInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/finance/DevilFinanceInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/security/DevilSecurityInterface.kt",
                    "app/src/main/kotlin/com/devil/app/ui/settings/DevilSettingsPrivacyPermissionsInterface.kt",
                )
        ) {
            val source = readSource(path, path.removePrefix("app/"))

            assertTrue(
                source.contains(
                    "devilInclusiveHeading",
                ),
                "Stage 264 heading semantics missing from $path",
            )
        }
    }

    @Test
    fun `Stage 264 preserves existing scalable Material typography`() {
        val source = inclusiveDesignSource()

        assertTrue(
            source.contains(
                "Stage 264 intentionally preserves Compose/Material text scaling.",
            ),
        )

        assertTrue(
            source.contains(
                "MaterialTheme typography",
            ),
        )
    }

    @Test
    fun `Stage 264 stops before UI production validation`() {
        val source = inclusiveDesignSource()

        assertTrue(
            source.contains(
                "Stage 264 does not implement Stage 265 UI Production Validation.",
            ),
        )
    }

    private fun inclusiveDesignSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/accessibility/DevilInclusiveDesign.kt",
            "src/main/kotlin/com/devil/app/ui/accessibility/DevilInclusiveDesign.kt",
        )

    private fun conversationSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
            "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
        )

    private fun readSource(
        vararg candidates: String,
    ): String =
        candidates
            .asSequence()
            .map(::File)
            .firstOrNull(File::isFile)
            ?.readText()
            ?: error(
                "Unable to locate Stage 264 source from: ${candidates.joinToString()}",
            )
}
