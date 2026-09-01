package com.devil.app.outcome

import com.devil.app.accessibility.AndroidScreenElementRecord
import com.devil.app.execution.Stage314AndroidPostActionExpectationStore
import com.devil.app.observation.Stage314AndroidPostActionObservationStore
import com.devil.app.verification.AndroidVerificationEvidence
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Stage314AndroidOutcomePresentationBindingTest {

    @Test
    fun `established outcome binds matching presentation fact`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-outcome-presentation",
            )

        val capabilityId =
            CapabilityId.from(
                "android-accessibility-click-visible-text",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        val observationStore =
            Stage314AndroidPostActionObservationStore()

        val presentationStore =
            Stage314VerifiedAndroidOutcomePresentationStore()

        expectationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText =
                "Settings, privacy, and permissions presentation",
        )

        observationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            elements =
                listOf(
                    AndroidScreenElementRecord.create(
                        position = 0,
                        text =
                            "Settings, privacy, and permissions presentation",
                        contentDescription = null,
                    ),
                ),
        )

        val source =
            Stage314AndroidPostActionOutcomeSource(
                expectationStore = expectationStore,
                observationStore = observationStore,
                presentationStore = presentationStore,
            )

        val result =
            source.establish(
                traceId = traceId,
                verificationEvidence =
                    AndroidVerificationEvidence.create(
                        capabilityId = capabilityId,
                        description =
                            "Independently verified Stage 314 Android effect.",
                    ),
            )

        assertEquals(
            AndroidOutcomeStatus.ESTABLISHED,
            result.status,
        )

        assertEquals(
            "Android action verified.",
            presentationStore.consume(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )
    }

    @Test
    fun `deferred outcome does not bind presentation fact`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-outcome-presentation-deferred",
            )

        val capabilityId =
            CapabilityId.from(
                "android-accessibility-click-visible-text",
            )

        val presentationStore =
            Stage314VerifiedAndroidOutcomePresentationStore()

        val source =
            Stage314AndroidPostActionOutcomeSource(
                expectationStore =
                    Stage314AndroidPostActionExpectationStore(),
                observationStore =
                    Stage314AndroidPostActionObservationStore(),
                presentationStore = presentationStore,
            )

        val result =
            source.establish(
                traceId = traceId,
                verificationEvidence =
                    AndroidVerificationEvidence.create(
                        capabilityId = capabilityId,
                        description =
                            "Verification evidence without preserved Stage 314 observation.",
                    ),
            )

        assertEquals(
            AndroidOutcomeStatus.DEFERRED,
            result.status,
        )

        assertNull(
            presentationStore.consume(
                traceId = traceId,
                capabilityId = capabilityId,
            ),
        )
    }
}
