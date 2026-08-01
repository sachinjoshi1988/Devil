package com.devil.core.model.capability

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CapabilityContractTest {

    @Test
    fun `create preserves and normalizes capability`() {
        val contract = CapabilityContract.create(
            capabilityId = CapabilityId.from("camera"),
            category = CapabilityCategory.ACTION,
            name = "  Camera  ",
            description = "  Opens the device camera.  ",
        )

        assertEquals("camera", contract.capabilityId.value)
        assertEquals(CapabilityCategory.ACTION, contract.category)
        assertEquals("Camera", contract.name)
        assertEquals("Opens the device camera.", contract.description)
    }

    @Test
    fun `create rejects blank name`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityContract.create(
                capabilityId = CapabilityId.from("camera"),
                category = CapabilityCategory.ACTION,
                name = "   ",
                description = "Valid description",
            )
        }
    }

    @Test
    fun `create rejects blank description`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityContract.create(
                capabilityId = CapabilityId.from("camera"),
                category = CapabilityCategory.ACTION,
                name = "Camera",
                description = "   ",
            )
        }
    }
}
