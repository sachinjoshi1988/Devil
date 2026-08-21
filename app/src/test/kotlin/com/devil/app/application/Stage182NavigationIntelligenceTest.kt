package com.devil.app.application

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage182NavigationIntelligenceTest {

    @Test
    fun `found launchable application prepares bounded navigation`() {
        val application =
            AndroidApplicationDescriptor.create(
                packageName = "com.example.maps",
                applicationLabel = "Maps",
                launchable = true,
            )

        val inspection =
            AndroidApplicationInspectionResult.create(
                status = AndroidApplicationInspectionStatus.FOUND,
                application = application,
            )

        val result =
            AndroidNavigationIntelligenceCoordinator()
                .prepare(inspection)

        assertEquals(
            AndroidNavigationIntelligenceStatus.READY,
            result.status,
        )
        assertEquals(inspection, result.applicationInspection)
        assertEquals(application.packageName, result.packageName)
    }

    @Test
    fun `found non launchable application remains deferred`() {
        val application =
            AndroidApplicationDescriptor.create(
                packageName = "com.example.background",
                applicationLabel = "Background App",
                launchable = false,
            )

        val inspection =
            AndroidApplicationInspectionResult.create(
                status = AndroidApplicationInspectionStatus.FOUND,
                application = application,
            )

        val result =
            AndroidNavigationIntelligenceCoordinator()
                .prepare(inspection)

        assertEquals(
            AndroidNavigationIntelligenceStatus.DEFERRED,
            result.status,
        )
        assertEquals(inspection, result.applicationInspection)
        assertNull(result.packageName)
    }

    @Test
    fun `not found application remains deferred`() {
        val inspection =
            AndroidApplicationInspectionResult.create(
                status = AndroidApplicationInspectionStatus.NOT_FOUND,
            )

        val result =
            AndroidNavigationIntelligenceCoordinator()
                .prepare(inspection)

        assertEquals(
            AndroidNavigationIntelligenceStatus.DEFERRED,
            result.status,
        )
        assertEquals(inspection, result.applicationInspection)
        assertNull(result.packageName)
    }

    @Test
    fun `ready result requires exact Stage 177 package provenance`() {
        val application =
            AndroidApplicationDescriptor.create(
                packageName = "com.example.maps",
                applicationLabel = "Maps",
                launchable = true,
            )

        val inspection =
            AndroidApplicationInspectionResult.create(
                status = AndroidApplicationInspectionStatus.FOUND,
                application = application,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidNavigationIntelligenceResult.create(
                status = AndroidNavigationIntelligenceStatus.READY,
                applicationInspection = inspection,
                packageName = "com.example.other",
            )
        }
    }

    @Test
    fun `ready result rejects non launchable application`() {
        val application =
            AndroidApplicationDescriptor.create(
                packageName = "com.example.background",
                applicationLabel = "Background App",
                launchable = false,
            )

        val inspection =
            AndroidApplicationInspectionResult.create(
                status = AndroidApplicationInspectionStatus.FOUND,
                application = application,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidNavigationIntelligenceResult.create(
                status = AndroidNavigationIntelligenceStatus.READY,
                applicationInspection = inspection,
                packageName = application.packageName,
            )
        }
    }
}
