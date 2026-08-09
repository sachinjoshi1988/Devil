package com.devil.app.permission

import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AndroidPermissionAssessmentTest {

    @Test
    fun `create preserves granted assessment with explicit permissions`() {
        val assessment =
            AndroidPermissionAssessment.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-permission-test",
                    ),
                status =
                    AndroidPermissionAssessmentStatus.GRANTED,
                requiredPermissions =
                    listOf(
                        "android.permission.CAMERA",
                    ),
            )

        assertEquals(
            AndroidPermissionAssessmentStatus.GRANTED,
            assessment.status,
        )
        assertEquals(
            listOf("android.permission.CAMERA"),
            assessment.requiredPermissions,
        )
    }

    @Test
    fun `create preserves denied assessment with explicit permissions`() {
        val assessment =
            AndroidPermissionAssessment.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-permission-test",
                    ),
                status =
                    AndroidPermissionAssessmentStatus.DENIED,
                requiredPermissions =
                    listOf(
                        "android.permission.RECORD_AUDIO",
                    ),
            )

        assertEquals(
            AndroidPermissionAssessmentStatus.DENIED,
            assessment.status,
        )
    }

    @Test
    fun `create preserves not required assessment without permissions`() {
        val assessment =
            AndroidPermissionAssessment.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-permission-test",
                    ),
                status =
                    AndroidPermissionAssessmentStatus.NOT_REQUIRED,
            )

        assertEquals(emptyList(), assessment.requiredPermissions)
    }

    @Test
    fun `create preserves unavailable assessment without permissions`() {
        val assessment =
            AndroidPermissionAssessment.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-permission-test",
                    ),
                status =
                    AndroidPermissionAssessmentStatus.UNAVAILABLE,
            )

        assertEquals(emptyList(), assessment.requiredPermissions)
    }

    @Test
    fun `create rejects granted assessment without explicit permissions`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidPermissionAssessment.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-permission-test",
                    ),
                status =
                    AndroidPermissionAssessmentStatus.GRANTED,
            )
        }
    }

    @Test
    fun `create rejects unavailable assessment containing permissions`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidPermissionAssessment.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-permission-test",
                    ),
                status =
                    AndroidPermissionAssessmentStatus.UNAVAILABLE,
                requiredPermissions =
                    listOf(
                        "android.permission.CAMERA",
                    ),
            )
        }
    }

    @Test
    fun `create normalizes and deduplicates explicit permissions`() {
        val assessment =
            AndroidPermissionAssessment.create(
                capabilityId =
                    CapabilityId.from(
                        "capability-permission-test",
                    ),
                status =
                    AndroidPermissionAssessmentStatus.GRANTED,
                requiredPermissions =
                    listOf(
                        " android.permission.CAMERA ",
                        "android.permission.CAMERA",
                    ),
            )

        assertEquals(
            listOf("android.permission.CAMERA"),
            assessment.requiredPermissions,
        )
    }
}
