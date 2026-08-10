package com.devil.app.internet

import android.Manifest
import com.devil.app.permission.DefaultAndroidCapabilityPermissionRequirementSource
import kotlin.test.Test
import kotlin.test.assertEquals

class Stage42InternetKnowledgePermissionRequirementTest {

    @Test
    fun `Internet Knowledge explicitly maps to Android INTERNET permission`() {
        val permissions =
            DefaultAndroidCapabilityPermissionRequirementSource()
                .requiredPermissions(
                    AndroidInternetKnowledgeCapability.contract,
                )

        assertEquals(
            listOf(
                Manifest.permission.INTERNET,
            ),
            permissions,
        )
    }
}
