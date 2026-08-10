package com.devil.app.owner

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage43OwnerProfileProductionCompositionTest {

    @Test
    fun `Devil application composes bounded owner profile update coordinator without fabricating owner data`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertTrue(
            source.contains(
                "OwnerProfileUpdateCoordinator",
            ),
        )

        assertTrue(
            source.contains(
                "val ownerProfileUpdateCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "OwnerProfileUpdateCoordinator()",
            ),
        )

        assertFalse(
            source.contains(
                "OwnerProfile.create(",
            ),
        )

        assertFalse(
            source.contains(
                "OwnerRelationship.create(",
            ),
        )

        assertFalse(
            source.contains(
                "OwnerProfileSnapshot.create(",
            ),
        )
    }
}
