package com.devil.app.observation

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.app.accessibility.Stage314AndroidAccessibilityChangeReadinessStore
import com.devil.app.execution.Stage314AndroidPostActionExpectationStore
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage314AndroidPostActionObservationSourceTest {

    @Test
    fun `missing Stage 314 expectation fails closed`() {
        val traceId =
            TraceId.from("trace-stage-314-observation-empty")
        val capabilityId =
            CapabilityId.from("stage-314-capability")

        val source =
            Stage314AndroidPostActionObservationSource(
                expectationStore =
                    Stage314AndroidPostActionExpectationStore(),
                observationStore =
                    Stage314AndroidPostActionObservationStore(),
                accessibilityChangeReadinessStore =
                    Stage314AndroidAccessibilityChangeReadinessStore(),
            )

        val result =
            source.observe(
                traceId = traceId,
                capabilityId = capabilityId,
            )

        assertEquals(
            AndroidObservationStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `stable captured snapshot becomes genuine observation`() {
        val traceId =
            TraceId.from("trace-stage-314-observation")
        val capabilityId =
            CapabilityId.from("stage-314-capability")

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()
        val observationStore =
            Stage314AndroidPostActionObservationStore()
        val readinessStore =
            Stage314AndroidAccessibilityChangeReadinessStore()

        expectationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText =
                "Settings, privacy, and permissions presentation",
        )

        readinessStore.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )
        readinessStore.markExecutionAttempted(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        val captured =
            listOf(
                AndroidScreenElementRecord.create(
                    position = 0,
                    text = "DEVIL",
                    contentDescription = null,
                ),
                AndroidScreenElementRecord.create(
                    position = 1,
                    text = "SETTINGS",
                    contentDescription = null,
                ),
            )

        readinessStore.signalAccessibilitySnapshot(captured)
        readinessStore.signalAccessibilitySnapshot(captured)

        val source =
            Stage314AndroidPostActionObservationSource(
                expectationStore = expectationStore,
                observationStore = observationStore,
                accessibilityChangeReadinessStore =
                    readinessStore,
            )

        val result =
            source.observe(
                traceId = traceId,
                capabilityId = capabilityId,
            )

        assertEquals(
            AndroidObservationStatus.OBSERVED,
            result.status,
        )

        assertEquals(
            captured,
            observationStore.current(
                traceId = traceId,
                capabilityId = capabilityId,
            )?.elements,
        )
    }

    @Test
    fun `unstable captured snapshot times out and stores no observation`() {
        val traceId =
            TraceId.from("trace-stage-314-unstable")
        val capabilityId =
            CapabilityId.from("stage-314-capability")

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()
        val observationStore =
            Stage314AndroidPostActionObservationStore()
        val readinessStore =
            Stage314AndroidAccessibilityChangeReadinessStore()

        expectationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText =
                "Settings, privacy, and permissions presentation",
        )

        readinessStore.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )
        readinessStore.markExecutionAttempted(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        readinessStore.signalAccessibilitySnapshot(
            listOf(
                AndroidScreenElementRecord.create(
                    position = 0,
                    text = "MAIN CONVERSATION",
                    contentDescription = null,
                ),
            ),
        )

        val source =
            Stage314AndroidPostActionObservationSource(
                expectationStore = expectationStore,
                observationStore = observationStore,
                accessibilityChangeReadinessStore =
                    readinessStore,
                readinessTimeoutMilliseconds = 10L,
            )

        val result =
            source.observe(
                traceId = traceId,
                capabilityId = capabilityId,
            )

        assertEquals(
            AndroidObservationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            observationStore.current(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )
    }
}
