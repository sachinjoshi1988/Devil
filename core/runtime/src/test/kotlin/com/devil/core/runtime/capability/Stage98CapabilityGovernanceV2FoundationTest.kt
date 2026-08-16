package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage98CapabilityGovernanceV2FoundationTest {

    @Test
    fun `selected available ready capability satisfies bounded governance`() {
        val traceId =
            TraceId.from(
                "trace-stage98-capability-governance-001",
            )

        val capability =
            capability()

        val result =
            coordinator().assess(
                traceId = traceId,
                capabilitySelection =
                    selected(
                        traceId = traceId,
                        capability = capability,
                    ),
                availability =
                    CapabilityAvailabilityState.AVAILABLE,
                health =
                    CapabilityHealthState.READY,
            )

        assertEquals(
            CapabilityGovernanceV2Status.SATISFIED,
            result.status,
        )

        val record =
            requireNotNull(result.record)

        assertSame(
            capability,
            record.capability,
        )

        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            record.availability,
        )

        assertEquals(
            CapabilityHealthState.READY,
            record.health,
        )

        assertNull(result.error)
    }

    @Test
    fun `selected unavailable capability remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage98-capability-governance-002",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                capabilitySelection =
                    selected(
                        traceId = traceId,
                    ),
                availability =
                    CapabilityAvailabilityState.UNAVAILABLE,
                health =
                    CapabilityHealthState.READY,
            )

        assertEquals(
            CapabilityGovernanceV2Status.DEFERRED,
            result.status,
        )

        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `selected non ready capability remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage98-capability-governance-003",
            )

        val nonReadyStates =
            listOf(
                CapabilityHealthState.INITIALIZING,
                CapabilityHealthState.BUSY,
                CapabilityHealthState.PAUSED,
                CapabilityHealthState.DEGRADED,
                CapabilityHealthState.UNAVAILABLE,
                CapabilityHealthState.RECOVERING,
                CapabilityHealthState.RETIRED,
            )

        nonReadyStates.forEach { health ->
            val result =
                coordinator().assess(
                    traceId = traceId,
                    capabilitySelection =
                        selected(
                            traceId = traceId,
                        ),
                    availability =
                        CapabilityAvailabilityState.AVAILABLE,
                    health = health,
                )

            assertEquals(
                CapabilityGovernanceV2Status.DEFERRED,
                result.status,
            )

            assertNull(result.record)
            assertNull(result.error)
        }
    }

    @Test
    fun `deferred capability selection remains deferred without fabricated governance`() {
        val traceId =
            TraceId.from(
                "trace-stage98-capability-governance-004",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                capabilitySelection =
                    CapabilitySelectionResult.create(
                        traceId = traceId,
                        status =
                            CapabilitySelectionStatus.DEFERRED,
                    ),
                availability =
                    CapabilityAvailabilityState.AVAILABLE,
                health =
                    CapabilityHealthState.READY,
            )

        assertEquals(
            CapabilityGovernanceV2Status.DEFERRED,
            result.status,
        )

        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `failed capability selection preserves exact upstream failure`() {
        val traceId =
            TraceId.from(
                "trace-stage98-capability-governance-005",
            )

        val error =
            UniversalErrorRecord.create(
                errorCode =
                    ErrorCode.from(
                        "STAGE_98_TEST_CAPABILITY_SELECTION_FAILURE",
                    ),
                traceId = traceId,
                occurredAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_198_000L,
                    ),
                summary =
                    "Synthetic bounded capability-selection failure for Stage 98 governance testing.",
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                capabilitySelection =
                    CapabilitySelectionResult.create(
                        traceId = traceId,
                        status =
                            CapabilitySelectionStatus.FAILED,
                        error = error,
                    ),
                availability =
                    CapabilityAvailabilityState.AVAILABLE,
                health =
                    CapabilityHealthState.READY,
            )

        assertEquals(
            CapabilityGovernanceV2Status.FAILED,
            result.status,
        )

        assertNull(result.record)

        assertSame(
            error,
            result.error,
        )
    }

    @Test
    fun `satisfied governance preserves exact selected capability identity`() {
        val traceId =
            TraceId.from(
                "trace-stage98-capability-governance-006",
            )

        val capability =
            capability(
                capabilityId =
                    "capability-stage98-exact-preservation",
            )

        val selection =
            selected(
                traceId = traceId,
                capability = capability,
            )

        val result =
            coordinator().assess(
                traceId = traceId,
                capabilitySelection = selection,
                availability =
                    CapabilityAvailabilityState.AVAILABLE,
                health =
                    CapabilityHealthState.READY,
            )

        val record =
            requireNotNull(result.record)

        assertSame(
            selection.capability,
            record.capability,
        )

        assertSame(
            capability,
            record.capability,
        )

        assertEquals(
            "capability-stage98-exact-preservation",
            record.capability.capabilityId.value,
        )
    }

    @Test
    fun `cross trace capability selection is rejected before governance assessment`() {
        val traceId =
            TraceId.from(
                "trace-stage98-capability-governance-007",
            )

        val otherTraceId =
            TraceId.from(
                "trace-stage98-capability-governance-other",
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator().assess(
                traceId = traceId,
                capabilitySelection =
                    selected(
                        traceId = otherTraceId,
                    ),
                availability =
                    CapabilityAvailabilityState.AVAILABLE,
                health =
                    CapabilityHealthState.READY,
            )
        }
    }

    @Test
    fun `governance record rejects unavailable state`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityGovernanceV2Record.create(
                capability = capability(),
                availability =
                    CapabilityAvailabilityState.UNAVAILABLE,
                health =
                    CapabilityHealthState.READY,
            )
        }
    }

    @Test
    fun `governance record rejects non ready health`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityGovernanceV2Record.create(
                capability = capability(),
                availability =
                    CapabilityAvailabilityState.AVAILABLE,
                health =
                    CapabilityHealthState.DEGRADED,
            )
        }
    }

    private fun coordinator():
        CapabilityGovernanceV2Coordinator {
        return CapabilityGovernanceV2Coordinator()
    }

    private fun selected(
        traceId: TraceId,
        capability: CapabilityContract = capability(),
    ): CapabilitySelectionResult {
        return CapabilitySelectionResult.create(
            traceId = traceId,
            status =
                CapabilitySelectionStatus.SELECTED,
            capability = capability,
        )
    }

    private fun capability(
        capabilityId: String =
            "capability-stage98-governance-test",
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId =
                CapabilityId.from(
                    capabilityId,
                ),
            category =
                CapabilityCategory.ACTION,
            name =
                "Stage 98 Governance Test Capability",
            description =
                "Registered capability used only to verify bounded Capability Governance V2 semantics.",
        )
    }
}
