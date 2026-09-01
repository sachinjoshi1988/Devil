package com.devil.app.capability

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Stage 314 Android capability-selection production-composition validation.
 *
 * Stage 314 reuses the single existing constitutional Capability Selection
 * Authority and single Unified Devil Runtime.
 *
 * The Android embodiment supplies:
 *
 * - the already-established Android capability registry;
 * - one bounded Android capability-selection resolver.
 *
 * It does not create another runtime, Capability Selection Authority,
 * authorization authority, Executive, execution path, or Android permission
 * authority.
 *
 * STRUCTURED_INTENT != AUTHORIZATION.
 * CAPABILITY_SELECTED != EXECUTION_APPROVED.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * ATTEMPTED != VERIFIED.
 */
class Stage314AndroidCapabilitySelectionCompositionTest {

    @Test
    fun `production runtime uses Android registry and bounded Stage 314 resolver`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        listOf(
            "capabilitySelectionAuthority =",
            "DefaultCapabilitySelectionAuthority(",
            "registry =",
            "capabilityRegistry",
            "resolver =",
            "DefaultAndroidCapabilitySelectionResolver()",
            "executionAttemptPort = executionAttemptPort",
        ).forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 314 capability-selection composition marker: $marker",
            )
        }
    }

    @Test
    fun `Stage 314 retains one Unified Devil Runtime production construction`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertEquals(
            1,
            Regex(
                """DefaultUnifiedDevilRuntime\s*\(""",
            ).findAll(source).count(),
        )
    }

    @Test
    fun `Stage 314 composition preserves authority boundaries`() {
        val resolverSource =
            File(
                "src/main/kotlin/com/devil/app/capability/DefaultAndroidCapabilitySelectionResolver.kt",
            ).readText()

        listOf(
            "STRUCTURED_INTENT != AUTHORIZATION.",
            "CAPABILITY_SELECTED != EXECUTION_APPROVED.",
            "ANDROID_PERMISSION != DEVIL_AUTHORIZATION.",
            "ATTEMPTED != VERIFIED.",
        ).forEach { boundary ->
            assertTrue(
                resolverSource.contains(boundary),
                "Missing Stage 314 capability-selection boundary: $boundary",
            )
        }
    }
}
