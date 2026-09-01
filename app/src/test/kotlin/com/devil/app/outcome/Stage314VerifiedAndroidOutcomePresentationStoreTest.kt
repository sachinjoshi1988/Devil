package com.devil.app.outcome

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage314VerifiedAndroidOutcomePresentationStoreTest {

    @Test
    fun `matching trace and capability consume established outcome once`() {
        val store =
            Stage314VerifiedAndroidOutcomePresentationStore()

        val traceId =
            TraceId.from("trace-stage-314-presentation")

        val capabilityId =
            CapabilityId.from(
                "android-accessibility-click-visible-text",
            )

        store.bindEstablished(
            traceId = traceId,
            capabilityId = capabilityId,
            message = "Android action verified.",
        )

        assertEquals(
            "Android action verified.",
            store.consume(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )

        assertNull(
            store.consume(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )
    }

    @Test
    fun `foreign trace cannot consume established outcome`() {
        val store =
            Stage314VerifiedAndroidOutcomePresentationStore()

        val capabilityId =
            CapabilityId.from(
                "android-accessibility-click-visible-text",
            )

        store.bindEstablished(
            traceId =
                TraceId.from(
                    "trace-stage-314-presentation-owner",
                ),
            capabilityId = capabilityId,
            message = "Android action verified.",
        )

        assertNull(
            store.consume(
                traceId =
                    TraceId.from(
                        "trace-stage-314-presentation-foreign",
                    ),
                capabilityId = capabilityId,
            ),
        )
    }

    @Test
    fun `foreign capability cannot consume established outcome`() {
        val store =
            Stage314VerifiedAndroidOutcomePresentationStore()

        val traceId =
            TraceId.from(
                "trace-stage-314-presentation-capability",
            )

        store.bindEstablished(
            traceId = traceId,
            capabilityId =
                CapabilityId.from(
                    "android-accessibility-click-visible-text",
                ),
            message = "Android action verified.",
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
    }
}
