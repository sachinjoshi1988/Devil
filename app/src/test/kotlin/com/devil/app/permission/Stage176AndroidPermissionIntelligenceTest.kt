package com.devil.app.permission

import com.devil.app.capability.AndroidCapabilityIntegrationResult
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.runtime.capability.CapabilityGovernanceV2Record
import com.devil.core.runtime.capability.CapabilityGovernanceV2Result
import com.devil.core.runtime.capability.CapabilityGovernanceV2Status
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage176AndroidPermissionIntelligenceTest {

    @Test
    fun `satisfied governance assesses Android permission for exact selected capability`() {
        val traceId = TraceId.from("trace-stage-176-satisfied")
        val capability = capability("capability-stage-176-satisfied")

        val integration =
            integration(
                traceId = traceId,
                capability = capability,
                status = CapabilityGovernanceV2Status.SATISFIED,
            )

        val selection =
            CapabilitySelectionResult.create(
                traceId = traceId,
                status = CapabilitySelectionStatus.SELECTED,
                capability = capability,
            )

        val expectedAssessment =
            AndroidPermissionAssessment.create(
                capabilityId = capability.capabilityId,
                status = AndroidPermissionAssessmentStatus.GRANTED,
                requiredPermissions = listOf("android.permission.CAMERA"),
            )

        val coordinator =
            AndroidPermissionIntelligenceCoordinator(
                permissionAuthorityAdapter = AndroidPermissionAuthorityAdapter {
                    expectedAssessment
                },
            )

        val result =
            coordinator.assess(
                traceId = traceId,
                capabilityIntegration = integration,
                capabilitySelection = selection,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(integration, result.capabilityIntegration)
        assertEquals(expectedAssessment, result.permissionAssessment)
    }

    @Test
    fun `deferred governance does not fabricate permission assessment`() {
        val traceId = TraceId.from("trace-stage-176-deferred")
        val capability = capability("capability-stage-176-deferred")

        val integration =
            AndroidCapabilityIntegrationResult.create(
                traceId = traceId,
                governance =
                    CapabilityGovernanceV2Result.create(
                        traceId = traceId,
                        status = CapabilityGovernanceV2Status.DEFERRED,
                    ),
            )

        val selection =
            CapabilitySelectionResult.create(
                traceId = traceId,
                status = CapabilitySelectionStatus.SELECTED,
                capability = capability,
            )

        val coordinator =
            AndroidPermissionIntelligenceCoordinator(
                permissionAuthorityAdapter = AndroidPermissionAuthorityAdapter {
                    error("Permission adapter must not be invoked.")
                },
            )

        val result =
            coordinator.assess(
                traceId = traceId,
                capabilityIntegration = integration,
                capabilitySelection = selection,
            )

        assertNull(result.permissionAssessment)
    }

    @Test
    fun `result preserves exact Stage 175 integration provenance`() {
        val traceId = TraceId.from("trace-stage-176-provenance")
        val capability = capability("capability-stage-176-provenance")

        val integration =
            integration(
                traceId = traceId,
                capability = capability,
                status = CapabilityGovernanceV2Status.SATISFIED,
            )

        val result =
            AndroidPermissionIntelligenceResult.create(
                traceId = traceId,
                capabilityIntegration = integration,
            )

        assertEquals(integration, result.capabilityIntegration)
        assertNull(result.permissionAssessment)
    }

    private fun integration(
        traceId: TraceId,
        capability: CapabilityContract,
        status: CapabilityGovernanceV2Status,
    ): AndroidCapabilityIntegrationResult {
        val governance =
            when (status) {
                CapabilityGovernanceV2Status.SATISFIED ->
                    CapabilityGovernanceV2Result.create(
                        traceId = traceId,
                        status = status,
                        record =
                            CapabilityGovernanceV2Record.create(
                                capability = capability,
                                availability = CapabilityAvailabilityState.AVAILABLE,
                                health = CapabilityHealthState.READY,
                            ),
                    )

                CapabilityGovernanceV2Status.DEFERRED ->
                    CapabilityGovernanceV2Result.create(
                        traceId = traceId,
                        status = status,
                    )

                CapabilityGovernanceV2Status.FAILED ->
                    error("FAILED is not needed by this focused Stage 176 test helper.")
            }

        return AndroidCapabilityIntegrationResult.create(
            traceId = traceId,
            governance = governance,
        )
    }

    private fun capability(
        id: String,
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(id),
            category = CapabilityCategory.ACTION,
            name = "Stage 176 Test Capability",
            description = "Bounded Android permission intelligence test capability.",
        )
    }
}
