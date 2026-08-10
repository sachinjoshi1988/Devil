package com.devil.app.device

import com.devil.app.permission.DefaultAndroidCapabilityPermissionRequirementSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class Stage40DeviceKnowledgePermissionRequirementTest {

    @Test
    fun `device knowledge requires no Android runtime permission`() {
        val permissions =
            DefaultAndroidCapabilityPermissionRequirementSource()
                .requiredPermissions(
                    AndroidDeviceKnowledgeCapability.contract,
                )

        assertNotNull(permissions)

        assertEquals(
            emptyList(),
            permissions,
        )
    }
}
