package com.devil.app.application

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage177ApplicationIntelligenceTest {

    @Test
    fun `descriptor normalizes supplied application metadata`() {
        val descriptor =
            AndroidApplicationDescriptor.create(
                packageName = "  com.example.cartoon  ",
                applicationLabel = "  Cartoon Studio  ",
                launchable = true,
            )

        assertEquals("com.example.cartoon", descriptor.packageName)
        assertEquals("Cartoon Studio", descriptor.applicationLabel)
        assertEquals(true, descriptor.launchable)
    }

    @Test
    fun `descriptor rejects blank required metadata`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidApplicationDescriptor.create(
                packageName = " ",
                applicationLabel = "Cartoon Studio",
                launchable = true,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            AndroidApplicationDescriptor.create(
                packageName = "com.example.cartoon",
                applicationLabel = " ",
                launchable = true,
            )
        }
    }

    @Test
    fun `found result requires application descriptor`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidApplicationInspectionResult.create(
                status = AndroidApplicationInspectionStatus.FOUND,
            )
        }
    }

    @Test
    fun `not found result contains no application descriptor`() {
        val result =
            AndroidApplicationInspectionResult.create(
                status = AndroidApplicationInspectionStatus.NOT_FOUND,
            )

        assertEquals(
            AndroidApplicationInspectionStatus.NOT_FOUND,
            result.status,
        )
        assertNull(result.application)
    }

    @Test
    fun `coordinator normalizes package name and preserves exact source result`() {
        val expected =
            AndroidApplicationInspectionResult.create(
                status = AndroidApplicationInspectionStatus.FOUND,
                application =
                    AndroidApplicationDescriptor.create(
                        packageName = "com.example.cartoon",
                        applicationLabel = "Cartoon Studio",
                        launchable = true,
                    ),
            )

        var suppliedPackageName: String? = null

        val coordinator =
            AndroidApplicationIntelligenceCoordinator(
                source =
                    AndroidApplicationIntelligenceSource { packageName ->
                        suppliedPackageName = packageName
                        expected
                    },
            )

        val result =
            coordinator.inspect("  com.example.cartoon  ")

        assertEquals("com.example.cartoon", suppliedPackageName)
        assertSame(expected, result)
    }

    @Test
    fun `coordinator rejects blank package name without invoking source`() {
        val coordinator =
            AndroidApplicationIntelligenceCoordinator(
                source =
                    AndroidApplicationIntelligenceSource {
                        error("Source must not be invoked.")
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.inspect(" ")
        }
    }
}
