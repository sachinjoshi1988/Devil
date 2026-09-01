package com.devil.app.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 314 production execution-composition validation.
 *
 * Stage 314 must reuse the single existing governed Android execution path.
 *
 * The process-local directive store supplies only explicitly armed embodiment
 * data. It does not create authorization, execution approval, permission,
 * Observation, Verification, Outcome, Learning, Memory, or another runtime.
 *
 * Empty store == no execution directive.
 * No execution directive == existing performer remains DEFERRED.
 *
 * ARMED != AUTHORIZED.
 * DIRECTIVE_AVAILABLE != EXECUTION_APPROVED.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * ATTEMPTED != VERIFIED.
 */
class Stage314ProductionExecutionCompositionTest {

    @Test
    fun `production composition wires Stage 314 store into existing execution path`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        listOf(
            "AndroidRealExecutionDirectiveStore",
            "realExecutionDirectiveStore",
            "DefaultAndroidExecutionPerformer",
            "directiveProvider =",
            "realExecutionDirectiveStore",
            "DefaultAndroidExecutionAdapter",
            "androidExecutionPerformer",
            "DefaultAndroidExecutionAttemptPort",
        ).forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 314 production execution marker: $marker",
            )
        }
    }

    @Test
    fun `Stage 314 production composition preserves one execution attempt port`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertEquals(
            1,
            Regex(
                """DefaultAndroidExecutionAttemptPort\s*\(""",
            ).findAll(source).count(),
        )
    }

    @Test
    fun `Stage 314 composition documents fail closed authority boundaries`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        listOf(
            "ARMED != AUTHORIZED.",
            "DIRECTIVE_AVAILABLE != EXECUTION_APPROVED.",
            "ANDROID_PERMISSION != DEVIL_AUTHORIZATION.",
            "ATTEMPTED != VERIFIED.",
        ).forEach { boundary ->
            assertTrue(
                source.contains(boundary),
                "Missing Stage 314 authority boundary: $boundary",
            )
        }
    }
}
