package com.devil.app.accessibility

import com.devil.app.permission.DefaultAndroidCapabilityPermissionRequirementSource
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class Stage38AccessibilityPermissionRequirementTest {

    @Test
    fun `accessibility capability requires no Activity runtime permission`() {
        val permissions =
            DefaultAndroidCapabilityPermissionRequirementSource()
                .requiredPermissions(
                    AndroidAccessibilityCapability.contract,
                )

        assertNotNull(permissions)
        assertTrue(permissions.isEmpty())
    }

    @Test
    fun `unknown capability permission mapping remains unavailable`() {
        val unknownCapability =
            CapabilityContract.create(
                capabilityId =
                    CapabilityId.from(
                        "stage-38-unknown-capability",
                    ),
                category =
                    CapabilityCategory.ACTION,
                name =
                    "Unknown Stage 38 Capability",
                description =
                    "A test capability with no approved Android permission mapping.",
            )

        val permissions =
            DefaultAndroidCapabilityPermissionRequirementSource()
                .requiredPermissions(
                    unknownCapability,
                )

        assertNull(permissions)
    }
}
