package com.devil.app.vision

import android.Manifest
import com.devil.app.permission.DefaultAndroidCapabilityPermissionRequirementSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage41VisionPermissionRequirementTest {

    @Test
    fun `Vision requires exactly Android CAMERA runtime permission`() {
        val source =
            DefaultAndroidCapabilityPermissionRequirementSource()

        assertEquals(
            listOf(
                Manifest.permission.CAMERA,
            ),
            source.requiredPermissions(
                AndroidVisionCapability.contract,
            ),
        )
    }
}
