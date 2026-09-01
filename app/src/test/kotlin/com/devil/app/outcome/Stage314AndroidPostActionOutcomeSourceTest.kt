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

class Stage314AndroidPostActionOutcomeSourceTest {

    @Test
    fun `matching preserved expectation observation and verification establish bounded outcome`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-outcome",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        val observationStore =
            Stage314AndroidPostActionObservationStore()

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
            )

        val result =
            source.establish(
                traceId = traceId,
                verificationEvidence =
                    verificationEvidence(
                        capabilityId,
                    ),
            )

        assertEquals(
            AndroidOutcomeStatus.ESTABLISHED,
            result.status,
        )

        assertEquals(
            capabilityId,
            result.evidence?.capabilityId,
        )
    }

    @Test
    fun `verification without preserved expectation remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-outcome-no-expectation",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val source =
            Stage314AndroidPostActionOutcomeSource(
                expectationStore =
                    Stage314AndroidPostActionExpectationStore(),
                observationStore =
                    Stage314AndroidPostActionObservationStore(),
            )

        val result =
            source.establish(
                traceId = traceId,
                verificationEvidence =
                    verificationEvidence(
                        capabilityId,
                    ),
            )

        assertEquals(
            AndroidOutcomeStatus.DEFERRED,
            result.status,
        )

        assertNull(result.evidence)
    }

    @Test
    fun `verification without preserved actual observation remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-outcome-no-observation",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        expectationStore.bind(
            traceId = traceId,
            capabilityId = capabilityId,
            expectedVisibleText =
                "Settings, privacy, and permissions presentation",
        )

        val source =
            Stage314AndroidPostActionOutcomeSource(
                expectationStore = expectationStore,
                observationStore =
                    Stage314AndroidPostActionObservationStore(),
            )

        val result =
            source.establish(
                traceId = traceId,
                verificationEvidence =
                    verificationEvidence(
                        capabilityId,
                    ),
            )

        assertEquals(
            AndroidOutcomeStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `foreign trace cannot establish outcome from preserved evidence`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-outcome-owner",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        val observationStore =
            Stage314AndroidPostActionObservationStore()

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
            )

        val result =
            source.establish(
                traceId =
                    TraceId.from(
                        "trace-stage-314-outcome-foreign",
                    ),
                verificationEvidence =
                    verificationEvidence(
                        capabilityId,
                    ),
            )

        assertEquals(
            AndroidOutcomeStatus.DEFERRED,
            result.status,
        )
    }

    @Test
    fun `foreign capability cannot establish outcome from preserved evidence`() {
        val traceId =
            TraceId.from(
                "trace-stage-314-outcome-capability",
            )

        val capabilityId =
            CapabilityId.from(
                "stage-314-capability",
            )

        val expectationStore =
            Stage314AndroidPostActionExpectationStore()

        val observationStore =
            Stage314AndroidPostActionObservationStore()

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
            )

        val result =
            source.establish(
                traceId = traceId,
                verificationEvidence =
                    verificationEvidence(
                        CapabilityId.from(
                            "stage-314-foreign-capability",
                        ),
                    ),
            )

        assertEquals(
            AndroidOutcomeStatus.DEFERRED,
            result.status,
        )
    }

    private fun verificationEvidence(
        capabilityId: CapabilityId,
    ): AndroidVerificationEvidence {
        return AndroidVerificationEvidence.create(
            capabilityId = capabilityId,
            description =
                "Genuine independently verified Stage 314 Android effect.",
        )
    }
}
