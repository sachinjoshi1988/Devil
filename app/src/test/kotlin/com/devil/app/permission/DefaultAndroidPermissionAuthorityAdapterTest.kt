package com.devil.app.permission

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultAndroidPermissionAuthorityAdapterTest {

    @Test
    fun `assess returns unavailable and performs no grant check when requirement mapping is unavailable`() {
        var checks = 0

        val adapter =
            DefaultAndroidPermissionAuthorityAdapter(
                requirementSource =
                    AndroidCapabilityPermissionRequirementSource {
                        null
                    },
                grantChecker =
                    AndroidPermissionGrantChecker {
                        checks += 1
                        true
                    },
            )

        val result = adapter.assess(createCapability())

        assertEquals(
            AndroidPermissionAssessmentStatus.UNAVAILABLE,
            result.status,
        )
        assertEquals(0, checks)
        assertEquals(emptyList(), result.requiredPermissions)
    }

    @Test
    fun `assess returns not required when approved mapping requires no runtime permission`() {
        var checks = 0

        val adapter =
            DefaultAndroidPermissionAuthorityAdapter(
                requirementSource =
                    AndroidCapabilityPermissionRequirementSource {
                        emptyList()
                    },
                grantChecker =
                    AndroidPermissionGrantChecker {
                        checks += 1
                        false
                    },
            )

        val result = adapter.assess(createCapability())

        assertEquals(
            AndroidPermissionAssessmentStatus.NOT_REQUIRED,
            result.status,
        )
        assertEquals(0, checks)
    }

    @Test
    fun `assess returns granted only when every required Android permission is granted`() {
        val checked = mutableListOf<String>()

        val adapter =
            DefaultAndroidPermissionAuthorityAdapter(
                requirementSource =
                    AndroidCapabilityPermissionRequirementSource {
                        listOf(
                            "android.permission.CAMERA",
                            "android.permission.RECORD_AUDIO",
                        )
                    },
                grantChecker =
                    AndroidPermissionGrantChecker { permission ->
                        checked += permission
                        true
                    },
            )

        val result = adapter.assess(createCapability())

        assertEquals(
            AndroidPermissionAssessmentStatus.GRANTED,
            result.status,
        )
        assertEquals(
            listOf(
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
            ),
            result.requiredPermissions,
        )
        assertEquals(
            result.requiredPermissions,
            checked,
        )
    }

    @Test
    fun `assess returns denied when any required Android permission is denied`() {
        val adapter =
            DefaultAndroidPermissionAuthorityAdapter(
                requirementSource =
                    AndroidCapabilityPermissionRequirementSource {
                        listOf(
                            "android.permission.CAMERA",
                            "android.permission.RECORD_AUDIO",
                        )
                    },
                grantChecker =
                    AndroidPermissionGrantChecker { permission ->
                        permission !=
                            "android.permission.RECORD_AUDIO"
                    },
            )

        val result = adapter.assess(createCapability())

        assertEquals(
            AndroidPermissionAssessmentStatus.DENIED,
            result.status,
        )
        assertEquals(
            listOf(
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
            ),
            result.requiredPermissions,
        )
    }

    @Test
    fun `Android permission granted remains distinct from Devil authorization`() {
        val result =
            DefaultAndroidPermissionAuthorityAdapter(
                requirementSource =
                    AndroidCapabilityPermissionRequirementSource {
                        listOf(
                            "android.permission.CAMERA",
                        )
                    },
                grantChecker =
                    AndroidPermissionGrantChecker {
                        true
                    },
            ).assess(
                createCapability(),
            )

        assertEquals(
            AndroidPermissionAssessmentStatus.GRANTED,
            result.status,
        )

        // The Android permission assessment contains no AuthorizationResult
        // and grants no constitutional continuation or execution authority.
        assertEquals(
            "capability-stage-29",
            result.capabilityId.value,
        )
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    "capability-stage-29",
                ),
            category = CapabilityCategory.ACTION,
            name = "Stage 29 Test Capability",
            description =
                "Represents one registered capability for bounded Android permission assessment.",
        )
    }
}
