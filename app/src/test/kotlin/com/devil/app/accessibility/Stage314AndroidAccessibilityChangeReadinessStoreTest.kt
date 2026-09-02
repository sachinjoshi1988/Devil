package com.devil.app.accessibility

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class Stage314AndroidAccessibilityChangeReadinessStoreTest {

    @Test
    fun `snapshot before execution attempt cannot establish readiness`() {
        val traceId =
            TraceId.from("trace-stage-314-before-attempt")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        val snapshot = snapshot("DEVIL")

        store.signalAccessibilitySnapshot(snapshot)
        store.signalAccessibilitySnapshot(snapshot)

        assertNull(
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )
    }

    @Test
    fun `matching execution attempt allows later stable snapshot readiness`() {
        val traceId =
            TraceId.from("trace-stage-314-stable")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        assertTrue(
            store.markExecutionAttempted(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )

        val snapshot =
            snapshot(
                "DEVIL",
                "SETTINGS",
            )

        store.signalAccessibilitySnapshot(snapshot)
        store.signalAccessibilitySnapshot(snapshot)

        assertEquals(
            snapshot,
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )
    }

    @Test
    fun `first post attempt snapshot alone is not ready`() {
        val traceId =
            TraceId.from("trace-stage-314-first-only")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )
        store.markExecutionAttempted(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        store.signalAccessibilitySnapshot(
            snapshot("DEVIL"),
        )

        assertNull(
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )
    }

    @Test
    fun `different consecutive snapshots do not establish readiness`() {
        val traceId =
            TraceId.from("trace-stage-314-different")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )
        store.markExecutionAttempted(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        store.signalAccessibilitySnapshot(
            snapshot("MAIN CONVERSATION"),
        )
        store.signalAccessibilitySnapshot(
            snapshot(
                "DEVIL",
                "SETTINGS",
            ),
        )

        assertNull(
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )
    }

    @Test
    fun `later equal snapshots establish latest candidate`() {
        val traceId =
            TraceId.from("trace-stage-314-replacement")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )
        store.markExecutionAttempted(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        val oldSnapshot =
            snapshot("MAIN CONVERSATION")
        val newSnapshot =
            snapshot(
                "DEVIL",
                "SETTINGS",
            )

        store.signalAccessibilitySnapshot(oldSnapshot)
        store.signalAccessibilitySnapshot(newSnapshot)
        store.signalAccessibilitySnapshot(newSnapshot)

        assertEquals(
            newSnapshot,
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )
    }

    @Test
    fun `foreign trace cannot mark execution attempted`() {
        val traceId =
            TraceId.from("trace-stage-314-owner")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        assertFalse(
            store.markExecutionAttempted(
                traceId =
                    TraceId.from(
                        "trace-stage-314-foreign",
                    ),
                capabilityId = capabilityId,
            ),
        )

        val snapshot = snapshot("DEVIL")

        store.signalAccessibilitySnapshot(snapshot)
        store.signalAccessibilitySnapshot(snapshot)

        assertNull(
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )
    }

    @Test
    fun `foreign capability cannot mark execution attempted`() {
        val traceId =
            TraceId.from("trace-stage-314-capability")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        assertFalse(
            store.markExecutionAttempted(
                traceId = traceId,
                capabilityId =
                    CapabilityId.from(
                        "stage-314-foreign-capability",
                    ),
            ),
        )
    }

    @Test
    fun `matching readiness times out fail closed without stable snapshot`() {
        val traceId =
            TraceId.from("trace-stage-314-timeout")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )
        store.markExecutionAttempted(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        assertNull(
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )
    }

    @Test
    fun `clear removes only matching pending readiness`() {
        val traceId =
            TraceId.from("trace-stage-314-clear")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )
        store.markExecutionAttempted(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        store.clear(
            traceId =
                TraceId.from(
                    "trace-stage-314-clear-foreign",
                ),
            capabilityId = capabilityId,
        )

        val snapshot = snapshot("DEVIL")

        store.signalAccessibilitySnapshot(snapshot)
        store.signalAccessibilitySnapshot(snapshot)

        assertEquals(
            snapshot,
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )
        store.markExecutionAttempted(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        store.clear(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        store.signalAccessibilitySnapshot(snapshot)
        store.signalAccessibilitySnapshot(snapshot)

        assertNull(
            store.awaitStableAccessibilitySnapshot(
                traceId = traceId,
                capabilityId = capabilityId,
                timeoutMilliseconds = 25L,
            ),
        )
    }

    @Test
    fun `nonpositive readiness timeout is rejected`() {
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        assertFailsWith<IllegalArgumentException> {
            store.awaitStableAccessibilitySnapshot(
                traceId =
                    TraceId.from(
                        "trace-stage-314-invalid-timeout",
                    ),
                capabilityId =
                    CapabilityId.from(
                        "stage-314-capability",
                    ),
                timeoutMilliseconds = 0L,
            )
        }
    }


    @Test
    fun `fresh store does not request accessibility snapshot capture`() {
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        assertFalse(
            store.isAccessibilitySnapshotCapturePending(),
        )
    }

    @Test
    fun `armed readiness requests capture before execution attempt`() {
        val traceId =
            TraceId.from("trace-stage-324-capture-armed")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        assertTrue(
            store.isAccessibilitySnapshotCapturePending(),
        )
    }

    @Test
    fun `stable ready snapshot closes accessibility capture window`() {
        val traceId =
            TraceId.from("trace-stage-324-capture-ready")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        assertTrue(
            store.markExecutionAttempted(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )

        val snapshot =
            snapshot(
                "DEVIL",
                "SETTINGS",
            )

        store.signalAccessibilitySnapshot(snapshot)
        store.signalAccessibilitySnapshot(snapshot)

        assertFalse(
            store.isAccessibilitySnapshotCapturePending(),
        )
    }

    @Test
    fun `matching clear closes accessibility capture window`() {
        val traceId =
            TraceId.from("trace-stage-324-capture-clear")
        val capabilityId =
            CapabilityId.from("stage-314-capability")
        val store =
            Stage314AndroidAccessibilityChangeReadinessStore()

        store.arm(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        assertTrue(
            store.isAccessibilitySnapshotCapturePending(),
        )

        store.clear(
            traceId = traceId,
            capabilityId = capabilityId,
        )

        assertFalse(
            store.isAccessibilitySnapshotCapturePending(),
        )
    }

    private fun snapshot(
        vararg texts: String,
    ): List<AndroidScreenElementRecord> {
        return texts.mapIndexed { index, text ->
            AndroidScreenElementRecord.create(
                position = index,
                text = text,
                contentDescription = null,
            )
        }
    }
}
