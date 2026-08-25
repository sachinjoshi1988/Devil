package com.devil.app.ui.validation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 265 UI Production Validation governance tests.
 *
 * Stage 265 validates already-established Phase-R presentation evidence only.
 *
 * It does not establish application launch readiness, performance readiness,
 * security readiness, constitutional verification, authorization, execution,
 * verified Outcome, World Model update, Learning, or Memory commitment.
 */
class Stage265UiProductionValidationTest {

    @Test
    fun `complete Phase R evidence becomes UI production ready`() {
        val evidence =
            completeEvidence()

        val result =
            DevilUiProductionValidationCoordinator()
                .validate(evidence)

        assertEquals(
            DevilUiProductionValidationStatus.READY,
            result.status,
        )

        assertSame(
            evidence,
            result.evidence,
        )
    }

    @Test
    fun `missing one Phase R requirement remains not ready`() {
        val evidence =
            completeEvidence().copy(
                accessibilityInclusiveDesignPresent = false,
            )

        val result =
            DevilUiProductionValidationCoordinator()
                .validate(evidence)

        assertEquals(
            DevilUiProductionValidationStatus.NOT_READY,
            result.status,
        )

        assertFalse(
            result.evidence.isComplete(),
        )
    }

    @Test
    fun `Phase R evidence requires every completed UI stage and integration requirement`() {
        val source =
            productionValidationSource()

        for (
            expected in
                listOf(
                    "finalDesignSystemPresent",
                    "startupExperiencePresent",
                    "mainConversationExperiencePresent",
                    "voiceInterfacePresent",
                    "memoryInterfacePresent",
                    "taskAutomationInterfacePresent",
                    "educationInterfacePresent",
                    "languageLearningInterfacePresent",
                    "researchInterfacePresent",
                    "financeInterfacePresent",
                    "securityInterfacePresent",
                    "settingsPrivacyPermissionsInterfacePresent",
                    "tabletAdaptiveUiPresent",
                    "accessibilityInclusiveDesignPresent",
                    "phaseRNavigationIntegrated",
                    "adaptivePresentationIntegrated",
                    "inclusivePresentationIntegrated",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 265 Phase-R evidence requirement: $expected",
            )
        }
    }

    @Test
    fun `UI production ready preserves constitutional and production boundaries`() {
        val source =
            productionValidationSource()

        for (
            boundary in
                listOf(
                    "UI_PRODUCTION_READY != APPLICATION_PRODUCTION_LAUNCH.",
                    "UI_PRODUCTION_READY != PERFORMANCE_VALIDATED.",
                    "UI_PRODUCTION_READY != SECURITY_VALIDATED.",
                    "UI_PRODUCTION_READY != CONSTITUTIONALLY_VALIDATED.",
                    "UI_PRODUCTION_READY != AUTHENTICATION.",
                    "UI_PRODUCTION_READY != AUTHORIZATION.",
                    "UI_PRODUCTION_READY != EXECUTION_APPROVAL.",
                    "UI_PRODUCTION_READY != EXECUTION.",
                    "UI_PRODUCTION_READY != CONSTITUTIONAL_OBSERVATION.",
                    "UI_PRODUCTION_READY != CONSTITUTIONAL_VERIFICATION.",
                    "UI_PRODUCTION_READY != VERIFIED_OUTCOME.",
                    "UI_PRODUCTION_READY != WORLD_MODEL_UPDATE.",
                    "UI_PRODUCTION_READY != LEARNING.",
                    "UI_PRODUCTION_READY != MEMORY_COMMITMENT.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 265 boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 265 does not implement Stage 266 performance behavior`() {
        val source =
            productionValidationSource()

        assertTrue(
            source.contains(
                "Stage 265 does not benchmark, profile, optimize, cache, establish render-time",
            ),
        )

        assertTrue(
            source.contains(
                "Stage 266 Performance behavior.",
            ),
        )
    }

    @Test
    fun `Stage 265 contract contains no operational authority wiring`() {
        val source =
            productionValidationSource()

        for (
            forbidden in
                listOf(
                    "UnifiedDevilRuntime",
                    "AuthorizationAuthority",
                    "ExecutionRequest(",
                    "MemoryAuthority",
                    "AndroidAccessibilityActionSource",
                    "AndroidTabletFormFactorCoordinator",
                    "AndroidPermissionIntelligenceCoordinator",
                    "AndroidDeviceSettingsControlCoordinator",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 265 UI validation must not invoke operational authority: $forbidden",
            )
        }
    }

    private fun completeEvidence():
        DevilUiProductionValidationEvidence =
        DevilUiProductionValidationEvidence(
            finalDesignSystemPresent = true,
            startupExperiencePresent = true,
            mainConversationExperiencePresent = true,
            voiceInterfacePresent = true,
            memoryInterfacePresent = true,
            taskAutomationInterfacePresent = true,
            educationInterfacePresent = true,
            languageLearningInterfacePresent = true,
            researchInterfacePresent = true,
            financeInterfacePresent = true,
            securityInterfacePresent = true,
            settingsPrivacyPermissionsInterfacePresent = true,
            tabletAdaptiveUiPresent = true,
            accessibilityInclusiveDesignPresent = true,
            phaseRNavigationIntegrated = true,
            adaptivePresentationIntegrated = true,
            inclusivePresentationIntegrated = true,
        )

    private fun productionValidationSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/validation/DevilUiProductionValidation.kt",
            "src/main/kotlin/com/devil/app/ui/validation/DevilUiProductionValidation.kt",
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
                "Unable to locate Stage 265 source from: ${candidates.joinToString()}",
            )
}
