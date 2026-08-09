package com.devil.app.permission

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertNull

class DefaultAndroidCapabilityPermissionRequirementSourceTest {

    @Test
    fun `default source does not fabricate Android permission requirements`() {
        val source:
            AndroidCapabilityPermissionRequirementSource =
            DefaultAndroidCapabilityPermissionRequirementSource()

        assertNull(
            source.requiredPermissions(
                createCapability(),
            ),
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage-29-default",
                ),
            category = CapabilityCategory.ACTION,
            name = "Stage 29 Test Capability",
            description =
                "Represents one registered capability without fabricated Android permission requirements.",
        )
    }
}
