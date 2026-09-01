package com.devil.app.execution

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage314AndroidPostActionExpectationStoreTest {

    @Test
    fun `empty expectation store fails closed`() {
        val store =
            Stage314AndroidPostActionExpectationStore()

        assertNull(
            store.current(
                traceId =
                    TraceId.from(
                        "trace-stage-314-expectation-empty",
                    ),
                capabilityId =
                    CapabilityId.from(
                        "stage-314-capability",
                    ),
            ),
        )
    }

    @Test
    fun `expectation preserves genuine trace capability and bounded condition`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-expectation",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val store =
            Stage314AndroidPostActionExpectationStore()

        store.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText = "  Security  ",
        )

        val expectation =
            store.current(
                traceId = traceId,
                capabilityId = capabilityId,
            )

        assertEquals(
            traceId,
            expectation?.traceId,
        )

        assertEquals(
            capabilityId,
            expectation?.capabilityId,
        )

        assertEquals(
            "Security",
            expectation?.expectedVisibleText,
        )
    }

    @Test
    fun `foreign trace cannot read or consume expectation`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-expectation-owner",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val store =
            Stage314AndroidPostActionExpectationStore()

        store.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText = "Security",
        )

        assertNull(
            store.consume(
                traceId =
                    TraceId.from(
                        "trace-stage-314-expectation-foreign",
                    ),
                capabilityId = capabilityId,
            ),
        )

        assertEquals(
            "Security",
            store.current(
                traceId = traceId,
                capabilityId = capabilityId,
            )?.expectedVisibleText,
        )
    }

    @Test
    fun `foreign capability cannot read or consume expectation`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-expectation-capability",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val store =
            Stage314AndroidPostActionExpectationStore()

        store.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText = "Security",
        )

        assertNull(
            store.consume(
                traceId = traceId,
                capabilityId =
                    CapabilityId.from(
                        "stage-314-foreign-capability",
                    ),
            ),
        )

        assertEquals(
            "Security",
            store.current(
                traceId = traceId,
                capabilityId = capabilityId,
            )?.expectedVisibleText,
        )
    }

    @Test
    fun `matching expectation is consumed exactly once`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-expectation-once",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val store =
            Stage314AndroidPostActionExpectationStore()

        store.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText = "Security",
        )

        assertEquals(
            "Security",
            store.consume(
                traceId = traceId,
                capabilityId = capabilityId,
            )?.expectedVisibleText,
        )

        assertNull(
            store.consume(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )
    }

    @Test
    fun `clear removes expectation`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-expectation-clear",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val store =
            Stage314AndroidPostActionExpectationStore()

        store.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText = "Security",
        )

        store.clear()

        assertNull(
            store.current(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )
    }

    @Test
    fun `blank expected condition is rejected`() {
        val store =
            Stage314AndroidPostActionExpectationStore()

        assertFailsWith<IllegalArgumentException> {
            store.bind(
                traceId =
                    TraceId.from(
                        "trace-stage-314-expectation-blank",
                    ),
                capabilityId =
                    CapabilityId.from(
                        "stage-314-capability",
                    ),
                expectedVisibleText = "   ",
            )
        }
    }
}
