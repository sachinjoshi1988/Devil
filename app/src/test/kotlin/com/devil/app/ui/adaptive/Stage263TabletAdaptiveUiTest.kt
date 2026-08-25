package com.devil.app.ui.adaptive

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 263 Tablet Adaptive UI governance tests.
 *
 * Stage 263 adapts presentation from current Compose layout constraints only.
 * It does not perform tablet classification, create another Devil/runtime,
 * authenticate, authorize, execute, synchronize Memory, or implement
 * Stage 264 Accessibility & Inclusive Design.
 */
class Stage263TabletAdaptiveUiTest {

    @Test
    fun `adaptive policy preserves bounded compact and expanded layout values`() {
        val source = adaptiveSource()

        for (
            expected in
                listOf(
                    "val tabletBreakpoint: Dp = 600.dp",
                    "val compactConversationCardMaxWidth: Dp = 310.dp",
                    "val expandedConversationCardMaxWidth: Dp = 520.dp",
                    "val expandedContentMaxWidth: Dp = 1120.dp",
                    "val compactHorizontalPadding: Dp = 0.dp",
                    "val expandedHorizontalPadding: Dp = 24.dp",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 263 adaptive policy: $expected",
            )
        }
    }

    @Test
    fun `adaptive presentation is derived from available Compose width`() {
        val source = adaptiveSource()

        assertTrue(
            source.contains(
                "maxWidth >= DevilAdaptiveLayoutPolicy.tabletBreakpoint",
            ),
        )
        assertTrue(
            source.contains(
                "availableWidth = maxWidth",
            ),
        )
        assertTrue(
            source.contains(
                "LocalDevilAdaptivePresentation provides presentation",
            ),
        )
    }

    @Test
    fun `adaptive contract preserves constitutional tablet boundaries`() {
        val source = adaptiveSource()

        for (
            boundary in
                listOf(
                    "ADAPTIVE_LAYOUT != TABLET_FORM_FACTOR_ASSESSMENT.",
                    "ADAPTIVE_LAYOUT != TABLET_EMBODIMENT.",
                    "AVAILABLE_WIDTH != DEVICE_IDENTITY.",
                    "AVAILABLE_WIDTH != DEVICE_TRUST.",
                    "TABLET_PRESENTATION != NEW_DEVIL.",
                    "TABLET_PRESENTATION != NEW_RUNTIME.",
                    "TABLET_PRESENTATION != AUTHENTICATION.",
                    "TABLET_PRESENTATION != AUTHORIZATION.",
                    "TABLET_PRESENTATION != SESSION_CONTINUITY.",
                    "TABLET_PRESENTATION != EXECUTION.",
                    "TABLET_PRESENTATION != MEMORY_SYNC.",
                    "ADAPTIVE_UI != ACCESSIBILITY_AUTHORITY.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 263 boundary: $boundary",
            )
        }
    }

    @Test
    fun `adaptive UI does not invoke tablet authority execution or memory wiring`() {
        val source = adaptiveSource()

        for (
            forbidden in
                listOf(
                    "AndroidTabletFormFactorCoordinator",
                    "AndroidTabletEmbodimentCoordinator",
                    "AndroidEducationTabletExperienceCoordinator",
                    "AndroidUnifiedMultiDeviceValidationCoordinator",
                    "AuthorizationAuthority",
                    "ExecutionRequest",
                    "MemoryAuthority",
                    "UnifiedDevilRuntime",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 263 adaptive UI must not invoke operational authority: $forbidden",
            )
        }
    }

    @Test
    fun `conversation cards consume shared adaptive presentation width`() {
        val source = conversationSource()

        assertTrue(
            source.contains(
                "import com.devil.app.ui.adaptive.DevilAdaptiveContainer",
            ),
        )
        assertTrue(
            source.contains(
                "import com.devil.app.ui.adaptive.LocalDevilAdaptivePresentation",
            ),
        )

        val adaptiveWidth =
            "LocalDevilAdaptivePresentation.current.conversationCardMaxWidth"

        assertTrue(
            source.windowed(adaptiveWidth.length, 1)
                .count { it == adaptiveWidth } >= 2,
            "Both owner and Devil conversation cards must use adaptive width.",
        )

        assertFalse(
            source.contains("max = 310.dp"),
            "Conversation cards must not retain hard-coded compact-only width.",
        )
    }

    @Test
    fun `all intended Phase R surfaces use shared adaptive container`() {
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
                source.contains("DevilAdaptiveContainer"),
                "Stage 263 adaptive container missing from $path",
            )
        }
    }

    @Test
    fun `Stage 263 stops before accessibility and production validation`() {
        val source = adaptiveSource()

        assertTrue(
            source.contains(
                "Stage 263 does not implement Stage 264 Accessibility & Inclusive Design",
            ),
        )
        assertTrue(
            source.contains(
                "or Stage 265 UI Production Validation.",
            ),
        )
    }

    private fun adaptiveSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/adaptive/DevilAdaptiveLayout.kt",
            "src/main/kotlin/com/devil/app/ui/adaptive/DevilAdaptiveLayout.kt",
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
                "Unable to locate Stage 263 source from: ${candidates.joinToString()}",
            )
}
